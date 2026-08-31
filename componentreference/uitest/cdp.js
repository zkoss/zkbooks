// Minimal zero-dependency Chrome DevTools Protocol driver (Node 22 global WebSocket).
const { spawn } = require('child_process');

const CHROME = process.env.CHROME_BIN
	|| '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome';
const PORT = Number(process.env.CDP_PORT || 9222);

const sleep = ms => new Promise(r => setTimeout(r, ms));

async function fetchJson(path, method = 'GET') {
	const res = await fetch(`http://127.0.0.1:${PORT}${path}`, { method });
	return res.json();
}

async function launch() {
	const proc = spawn(CHROME, [
		'--headless=new', '--disable-gpu', '--hide-scrollbars',
		`--remote-debugging-port=${PORT}`,
		'--user-data-dir=/tmp/cdp-zk-profile',
		'--window-size=1200,900', 'about:blank',
	], { stdio: 'ignore', detached: false });
	for (let i = 0; i < 60; i++) {
		try { await fetchJson('/json/version'); return proc; } catch { await sleep(250); }
	}
	throw new Error('chrome did not start');
}

class Page {
	constructor(ws) { this.ws = ws; this.id = 0; this.pending = new Map(); this.errors = []; }

	static async open(url) {
		const target = await fetchJson('/json/new?' + encodeURIComponent(url), 'PUT');
		const ws = new WebSocket(target.webSocketDebuggerUrl);
		await new Promise((ok, bad) => { ws.onopen = ok; ws.onerror = bad; });
		const page = new Page(ws);
		page.targetId = target.id;
		ws.onmessage = ev => {
			const msg = JSON.parse(ev.data);
			if (msg.id && page.pending.has(msg.id)) {
				const { ok, bad } = page.pending.get(msg.id);
				page.pending.delete(msg.id);
				msg.error ? bad(new Error(JSON.stringify(msg.error))) : ok(msg.result);
			} else if (msg.method === 'Runtime.exceptionThrown') {
				page.errors.push('JS exception: ' +
					(msg.params.exceptionDetails.exception?.description || msg.params.exceptionDetails.text));
			} else if (msg.method === 'Runtime.consoleAPICalled' && msg.params.type === 'error') {
				page.errors.push('console.error: ' + msg.params.args.map(a => a.value ?? a.description).join(' '));
			}
		};
		await page.send('Runtime.enable');
		await page.send('Page.enable');
		return page;
	}

	send(method, params = {}) {
		const id = ++this.id;
		return new Promise((ok, bad) => {
			this.pending.set(id, { ok, bad });
			this.ws.send(JSON.stringify({ id, method, params }));
			setTimeout(() => { if (this.pending.delete(id)) bad(new Error(method + ' timed out')); }, 20000);
		});
	}

	async eval(expr) {
		const r = await this.send('Runtime.evaluate',
			{ expression: expr, returnByValue: true, awaitPromise: true });
		if (r.exceptionDetails)
			throw new Error('eval failed: ' + (r.exceptionDetails.exception?.description || r.exceptionDetails.text) + '\n  expr: ' + expr);
		return r.result.value;
	}

	// Poll until expr is truthy. Returns its value.
	async waitFor(expr, timeout = 8000) {
		const end = Date.now() + timeout;
		let last;
		while (Date.now() < end) {
			last = await this.eval(expr);
			if (last) return last;
			await sleep(120);
		}
		let diag = '';
		try {
			diag = await this.eval(`JSON.stringify({
				notifs: Array.from(document.querySelectorAll('.z-notification')).map(n => n.textContent.trim()),
				active: document.activeElement ? document.activeElement.className : null,
				popupsOpen: Array.from(document.querySelectorAll('.z-confirmpopup'))
					.filter(c => getComputedStyle(c).display !== 'none')
					.map(c => c.textContent.slice(0, 40))
			})`);
		} catch {}
		throw new Error(`waitFor timed out: ${expr} (last value: ${JSON.stringify(last)})\n  state: ${diag}`);
	}

	async rect(jsElementExpr) {
		const r = await this.eval(`(() => { const e = ${jsElementExpr};
			if (!e) return null; const b = e.getBoundingClientRect();
			return {x: b.x + b.width / 2, y: b.y + b.height / 2, w: b.width, h: b.height}; })()`);
		if (!r) throw new Error('element not found: ' + jsElementExpr);
		if (r.w === 0 && r.h === 0) throw new Error('element has zero size: ' + jsElementExpr);
		return r;
	}

	// Real mouse click at the centre of the element the expression returns.
	async click(jsElementExpr) {
		// Wrap the expression: a bare `a || b` would bind .scrollIntoView to b only.
		await this.eval(`(() => { const e = ${jsElementExpr};
			if (e) e.scrollIntoView({block: 'center'}); return !!e; })()`);
		await sleep(120);
		const r = await this.rect(jsElementExpr);
		const vp = await this.eval(`({w: innerWidth, h: innerHeight})`);
		if (r.x < 0 || r.y < 0 || r.x > vp.w || r.y > vp.h)
			throw new Error(`element is outside the viewport (${r.x},${r.y} vs ${vp.w}x${vp.h}): ${jsElementExpr}`);
		const base = { x: r.x, y: r.y, button: 'left', clickCount: 1, buttons: 1 };
		await this.send('Input.dispatchMouseEvent', { type: 'mouseMoved', ...base, buttons: 0 });
		await this.send('Input.dispatchMouseEvent', { type: 'mousePressed', ...base });
		await this.send('Input.dispatchMouseEvent', { type: 'mouseReleased', ...base });
		await sleep(250);
	}

	// Click at raw viewport coordinates - used for "click somewhere else entirely".
	async clickAt(x, y) {
		const base = { x, y, button: 'left', clickCount: 1, buttons: 1 };
		await this.send('Input.dispatchMouseEvent', { type: 'mouseMoved', ...base, buttons: 0 });
		await this.send('Input.dispatchMouseEvent', { type: 'mousePressed', ...base });
		await this.send('Input.dispatchMouseEvent', { type: 'mouseReleased', ...base });
		await sleep(250);
	}

	async key(key, code, keyCode, text) {
		const p = { key, code, windowsVirtualKeyCode: keyCode, nativeVirtualKeyCode: keyCode };
		if (text) p.text = text;
		await this.send('Input.dispatchKeyEvent', { type: text ? 'keyDown' : 'rawKeyDown', ...p });
		await this.send('Input.dispatchKeyEvent', { type: 'keyUp', ...p });
		await sleep(250);
	}

	async shot(path) {
		const r = await this.send('Page.captureScreenshot', { format: 'png' });
		require('fs').writeFileSync(path, Buffer.from(r.data, 'base64'));
	}

	async close() {
		try { this.ws.close(); } catch {}
		try { await fetch(`http://127.0.0.1:${PORT}/json/close/${this.targetId}`); } catch {}
	}
}

// --- assertion helpers -------------------------------------------------
let pass = 0, fail = 0;
const results = [];
function check(name, condition, detail = '') {
	if (condition) { pass++; results.push(`  PASS  ${name}`); }
	else { fail++; results.push(`  FAIL  ${name}${detail ? ' -- ' + detail : ''}`); }
}
function report(title) {
	console.log(`\n=== ${title} ===`);
	results.splice(0).forEach(l => console.log(l));
}
function summary() {
	console.log(`\n================ ${pass} passed, ${fail} failed ================`);
	return fail;
}

// JS injected into the page: locate ZK widgets by their ZUL id, and elements by text.
const HELPERS = `
window.zkWidget = function (zkId) {
	var els = document.querySelectorAll('[id]');
	for (var i = 0; i < els.length; i++) {
		var w = zk.Widget.$(els[i]);
		if (w && w.id === zkId) return w;
	}
	return null;
};
window.zkNode = function (zkId) { var w = window.zkWidget(zkId); return w ? w.$n() : null; };
// Some widgets emit their label with &nbsp; instead of a space - a comboitem renders
// "Tom Wu" as "Tom&nbsp;Wu" - so matching on a typed space would silently never hit.
window.normText = function (s) { return (s || '').replace(/\u00a0/g, ' ').trim(); };
window.byText = function (sel, text) {
	return Array.from(document.querySelectorAll(sel))
		.find(function (e) { return window.normText(e.textContent) === text; }) || null;
};
window.byTextContains = function (sel, text) {
	return Array.from(document.querySelectorAll(sel))
		.find(function (e) { return window.normText(e.textContent).indexOf(text) >= 0; }) || null;
};
window.textOf = function (zkId) { var n = window.zkNode(zkId); return n ? window.normText(n.textContent) : null; };
// ZK appends each notification as a new node and keeps the old ones around,
// so the newest one is the last in document order - not the first.
window.notifText = function () {
	var ns = Array.from(document.querySelectorAll('.z-notification'))
		.filter(function (n) { return getComputedStyle(n).display !== 'none'; });
	return ns.length ? ns[ns.length - 1].textContent.trim() : null;
};
true;
`;

async function withPage(url, fn) {
	const page = await Page.open(url);
	try {
		await page.waitFor(`typeof zk !== 'undefined' && !!zk.Desktop && document.readyState === 'complete'`);
		await page.eval(HELPERS);
		await sleep(700); // let ZK finish its first render pass
		await fn(page);
		if (page.errors.length) check('no JS errors on the page', false, page.errors.slice(0, 3).join(' | '));
		else check('no JS errors on the page', true);
	} finally { await page.close(); }
}

module.exports = { launch, Page, withPage, check, report, summary, sleep };
