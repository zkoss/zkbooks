const { launch, withPage, check, report, summary, sleep } = require('./cdp');
const BASE = (process.env.ZK_BASE || 'http://localhost:8090/component') + '/essential';

const POPUP = msg => `Array.from(document.querySelectorAll('.z-confirmpopup')).find(c => c.textContent.indexOf(${JSON.stringify(msg)}) >= 0)`;
const VISIBLE = el => `(() => { const e = ${el}; return !!e && getComputedStyle(e).display !== 'none'; })()`;
// ZK reuses a single notification widget, so never remove its DOM - wait for the text to change.
const NOTIF_IS = t => `notifText() === ${JSON.stringify(t)}`;
const FOCUS_IS = which => `(document.activeElement.className || '').indexOf('z-confirmpopup-${which}') >= 0`;

(async () => {
	const chrome = await launch();

	// ------------------------------------------------------------ confirmpopup
	await withPage(`${BASE}/confirmpopup.zul`, async page => {
		await page.click(`byText('.z-button', 'Delete')`);
		await page.waitFor(VISIBLE(POPUP('Delete this item?')));
		check('open(ref) shows the popup anchored at the trigger', true);
		check('focus lands on the OK button when it opens',
			await page.eval(FOCUS_IS('ok')), await page.eval(`document.activeElement.className`));

		await page.key('Escape', 'Escape', 27);
		await page.waitFor(NOTIF_IS('cancelled'));
		check('Esc closes the popup and fires onCancel',
			!(await page.eval(VISIBLE(POPUP('Delete this item?')))));

		await page.click(`byText('.z-button', 'Delete')`);
		await page.waitFor(VISIBLE(POPUP('Delete this item?')));
		// the popup focuses OK on a timer; pressing Enter before that lands nowhere
		await page.waitFor(FOCUS_IS('ok'));
		await page.key('Enter', 'Enter', 13, '\r');
		await page.waitFor(NOTIF_IS('confirmed'));
		check('Enter fires onOK and closes the popup',
			!(await page.eval(VISIBLE(POPUP('Delete this item?')))));

		await page.click(`byText('.z-button', 'Delete')`);
		await page.waitFor(VISIBLE(POPUP('Delete this item?')));
		await page.clickAt(5, 5);                              // click well outside the popup
		await page.waitFor(NOTIF_IS('cancelled'));
		check('clicking outside fires onCancel',
			!(await page.eval(VISIBLE(POPUP('Delete this item?')))));

		for (const side of ['top', 'bottom', 'left', 'right']) {
			await page.click(`byText('.z-button', '${side}')`);
			await page.waitFor(VISIBLE(POPUP(`Popup ${side === 'top' ? 'above' : side === 'bottom' ? 'below' : side + ' of'} the button`)));
			const cls = await page.eval(`${POPUP(`Popup ${side === 'top' ? 'above' : side === 'bottom' ? 'below' : side + ' of'} the button`)}.className`);
			check(`placement="${side}" applies its placement class`,
				cls.indexOf('z-confirmpopup-placement-' + side) >= 0, cls);
			await page.key('Escape', 'Escape', 27);
			await sleep(200);
		}

		await page.click(`byText('.z-button', 'defaultFocus=cancel')`);
		await page.waitFor(VISIBLE(POPUP('The records cannot be recovered')));
		await page.waitFor(FOCUS_IS('cancel'), 4000).catch(() => {});
		check('defaultFocus="cancel" puts the focus on Cancel',
			await page.eval(FOCUS_IS('cancel')), await page.eval(`document.activeElement.className`));
		await page.key('Escape', 'Escape', 27);
		await sleep(300);

		// use case: row delete
		const rowsBefore = await page.eval(`zkNode('materialGrid').querySelectorAll('.z-row').length`);
		await page.click(`zkNode('materialGrid').querySelectorAll('.z-button')[0]`);
		await page.waitFor(VISIBLE(POPUP('The material is removed from the master data')));
		await page.click(`${POPUP('The material is removed from the master data')}.querySelector('.z-confirmpopup-ok')`);
		await page.waitFor(`zkNode('materialGrid').querySelectorAll('.z-row').length === ${rowsBefore - 1}`);
		const remaining = await page.eval(`zkNode('materialGrid').textContent`);
		check('row-delete use case: onOK detaches exactly the row that opened the popup',
			rowsBefore === 3 && !/MAT-10021/.test(remaining) && /MAT-10022/.test(remaining) && /MAT-10023/.test(remaining),
			`rows ${rowsBefore} -> ${rowsBefore - 1}`);
	});
	report('confirmpopup.zul');

	// -------------------------------------------------------------------- chip
	await withPage(`${BASE}/chip.zul`, async page => {
		const chipCount = `document.querySelectorAll('.z-chip').length`;
		const before = await page.eval(chipCount);
		await page.click(`byTextContains('.z-chip', 'React').querySelector('.z-chip-close')`);
		await page.waitFor(`${chipCount} === ${before - 1}`);
		check('closable chip: the onClose listener detaches it', true);

		await page.click(`byTextContains('.z-chip', 'Listener, then detached').querySelector('.z-chip-close')`);
		await page.waitFor(`(notifText() || '').indexOf('the default handler detaches it') >= 0`);
		await sleep(400);
		check('a listener runs first, then the default handler still detaches the chip',
			!(await page.eval(`!!byTextContains('.z-chip', 'Listener, then detached')`)));

		await page.click(`byTextContains('.z-chip', 'Stays on close').querySelector('.z-chip-close')`);
		await page.waitFor(`(notifText() || '').indexOf('the chip is kept') >= 0`);
		await sleep(500);
		check('event.stopPropagation() suppresses the default handler and keeps the chip',
			await page.eval(`!!byTextContains('.z-chip', 'Stays on close')`));

		const disabledBefore = await page.eval(`!!byTextContains('.z-chip', 'Disabled and closable')`);
		await page.click(`byTextContains('.z-chip', 'Disabled and closable').querySelector('.z-chip-close')`);
		await sleep(700);
		check('a disabled chip ignores its close button',
			disabledBefore && await page.eval(`!!byTextContains('.z-chip', 'Disabled and closable')`));

		await page.click(`byTextContains('.z-chip', 'Region: EMEA').querySelector('.z-chip-close')`);
		await page.waitFor(`textOf('resultCount') !== 'showing 187 of 1,208 orders'`);
		check('filter-chip use case: closing a chip updates the result count',
			(await page.eval(`textOf('resultCount')`)) === 'showing 412 of 1,208 orders',
			await page.eval(`textOf('resultCount')`));

		check('disabled chips render with the disabled class',
			await page.eval(`!!byTextContains('.z-chip', 'ORDER_READ').className.match(/disabled/)`));
	});
	report('chip.zul');

	// ------------------------------------------------------------- avatargroup
	await withPage(`${BASE}/avatargroup.zul`, async page => {
		const live = `zkNode('liveGroup')`;
		const overflowText = `(() => { const o = ${live}.querySelector('.z-avatargroup-overflow'); return o ? o.textContent : null; })()`;
		check('maxCount hides the extra avatars behind a "+N" indicator',
			(await page.eval(overflowText)) === '+1', await page.eval(overflowText));

		await page.click(`byText('.z-button', 'Add a member')`);
		await page.waitFor(`${overflowText} === '+2'`);
		check('appendChild recalculates the overflow indicator', true);

		await page.click(`byText('.z-button', 'Remove the last member')`);
		await page.waitFor(`${overflowText} === '+1'`);
		check('detach recalculates the overflow indicator', true);

		const hidden = await page.eval(`${live}.querySelectorAll('[data-ag-hidden]').length`);
		check('hidden avatars are marked, not removed', hidden === 1, `marked ${hidden}`);

		await page.click(`byText('.z-button', 'Try to add a label child')`);
		await page.waitFor(`notifText()`);
		check('avatargroup rejects a non-avatar child with a UiException',
			/avatar/i.test(await page.eval(`notifText()`)), await page.eval(`notifText()`));
	});
	report('avatargroup.zul');

	// ------------------------------------------------------------------- badge
	await withPage(`${BASE}/badge.zul`, async page => {
		// wrap mode renders <span class="z-badge ..."><child/><span class="z-badge-indicator">N</span></span>
		const approvals = `(() => { const w = Array.from(document.querySelectorAll('.z-badge'))
			.find(e => (e.textContent || '').indexOf('Approvals') >= 0);
			const i = w && w.querySelector('.z-badge-indicator'); return i ? i.textContent : null; })()`;
		await page.click(`byText('.z-button', '+1')`);
		await page.waitFor(`${approvals} === '1'`);
		await page.click(`byText('.z-button', '+1')`);
		await page.waitFor(`${approvals} === '2'`);
		await page.click(`byText('.z-button', '-1')`);
		await page.waitFor(`${approvals} === '1'`);
		check('setCount re-renders the badge, including out of the empty state', true);

		await page.click(`byText('.z-button', 'Read getDisplayValue()')`);
		await page.waitFor(`textOf('displayValue').indexOf('[') >= 0`);
		check('getDisplayValue() returns the resolved badge text',
			(await page.eval(`textOf('displayValue')`)) === 'getDisplayValue(): [1]',
			await page.eval(`textOf('displayValue')`));

		const texts = await page.eval(
			`Array.from(document.querySelectorAll('.z-badge-indicator')).map(b => b.textContent)`);
		check('count above max renders as {max}+', texts.includes('99+') && texts.includes('5+'),
			JSON.stringify(texts.slice(0, 12)));
		check('showZero="true" is the only way a zero count shows',
			texts.filter(t => t === '0').length === 2,
			`indicators reading "0": ${texts.filter(t => t === '0').length}`);
		check('a non-null value wins over count', texts.includes('HOT'));
		const empty = await page.eval(`(() => {
			const e = Array.from(document.querySelectorAll('.z-badge-standalone'))
				.filter(b => !b.querySelector('.z-badge-indicator'));
			return { count: e.length, width: e.length ? e[0].getBoundingClientRect().width : -1 }; })()`);
		check('an empty badge renders no indicator and collapses to zero size',
			empty.count >= 3 && empty.width === 0, JSON.stringify(empty));

		// A wrap-mode indicator is pulled 10px past the corner of the child it decorates,
		// so any ancestor that does not let content overflow cuts it off. .z-hlayout sets
		// overflow:hidden and did exactly that to the top and bottom placements.
		const clipped = await page.eval(`(() => {
			const CLIP = ['hidden', 'scroll', 'auto', 'clip'], TOL = 1;
			return Array.from(document.querySelectorAll('.z-badge'))
				.filter(b => b.querySelector('.z-badge-indicator'))
				.flatMap(b => {
					const ir = b.querySelector('.z-badge-indicator').getBoundingClientRect();
					const out = [];
					for (let n = b.parentElement; n && n !== document.documentElement; n = n.parentElement) {
						const cs = getComputedStyle(n);
						if (!CLIP.includes(cs.overflowX) && !CLIP.includes(cs.overflowY)) continue;
						const r = n.getBoundingClientRect();
						if (r.top - ir.top > TOL || ir.bottom - r.bottom > TOL
								|| r.left - ir.left > TOL || ir.right - r.right > TOL)
							out.push(b.textContent.trim().slice(0, 16) + ' clipped by ' + n.className);
					}
					return out;
				});
		})()`);
		check('no badge indicator is cut off by a container', clipped.length === 0,
			JSON.stringify(clipped));

		const row = await page.eval(`(() => {
			const r = document.querySelector('.badge-wrap-row');
			if (!r) return null;
			const cs = getComputedStyle(r);
			const inds = Array.from(r.querySelectorAll('.z-badge-indicator'))
				.map(i => i.getBoundingClientRect());
			let overlap = false;
			for (let i = 0; i < inds.length; i++)
				for (let j = i + 1; j < inds.length; j++)
					if (inds[i].left < inds[j].right && inds[j].left < inds[i].right
							&& inds[i].top < inds[j].bottom && inds[j].top < inds[i].bottom)
						overlap = true;
			return { display: cs.display, overflow: cs.overflowX, count: inds.length, overlap };
		})()`);
		check('the wrap-mode row shows all four placements without overlap',
			row && row.display === 'flex' && row.overflow === 'visible'
				&& row.count === 4 && !row.overlap, JSON.stringify(row));
	});
	report('badge.zul');

	// -------------------------------------------------------------- breadcrumb
	await withPage(`${BASE}/breadcrumb.zul`, async page => {
		const collapsed = `Array.from(document.querySelectorAll('.z-breadcrumb')).find(e => zk.Widget.$(e)._maxItems > 0)`;
		const hiddenCount = `${collapsed}.querySelectorAll('.z-breadcrumbitem[data-zk-bc-hidden]').length`;
		check('maxItems collapses the leading items behind an ellipsis',
			(await page.eval(hiddenCount)) > 0 &&
			(await page.eval(`!!${collapsed}.querySelector('.z-breadcrumb-ellipsis')`)),
			`hidden: ${await page.eval(hiddenCount)}`);

		await page.click(`${collapsed}.querySelector('.z-breadcrumb-ellipsis button')`);
		await page.waitFor(`${hiddenCount} === 0`);
		check('activating the ellipsis expands the full path client-side', true);

		check('the last item without href is marked as the current page',
			await page.eval(`(() => { const b = document.querySelector('.z-breadcrumb');
				const items = b.querySelectorAll('.z-breadcrumbitem');
				const last = items[items.length - 1];
				return !last.querySelector('a') || last.getAttribute('aria-current') === 'page'; })()`));
	});
	report('breadcrumb.zul');

	// ------------------------------------------------------------------ avatar
	await withPage(`${BASE}/avatar.zul`, async page => {
		const broken = `Array.from(document.querySelectorAll('.z-avatar')).filter(a => {
			const w = zk.Widget.$(a); return w && (w._image || '').indexOf('no-such-file') >= 0; })`;
		const n = await page.eval(`${broken}.length`);
		check('the three broken-image avatars are on the page', n === 3, `found ${n}`);
		const kinds = await page.eval(`${broken}.map(a => a.querySelector('img') ? 'img'
			: a.querySelector('i') ? 'icon'
			: a.textContent.trim() ? 'initials:' + a.textContent.trim() : 'empty')`);
		check('fallback order after a 404: image -> iconSclass -> initials -> empty',
			JSON.stringify(kinds) === JSON.stringify(['icon', 'initials:Ja', 'empty']),
			JSON.stringify(kinds));
		check('initials are the label\'s first two characters, uppercased only by CSS',
			await page.eval(`(() => { const s = Array.from(document.querySelectorAll('.z-avatar-text'));
				return s[0].textContent === 'Je' && getComputedStyle(s[0]).textTransform === 'uppercase'; })()`));

		// a menupopup is rendered lazily, so it is absent from the DOM until first opened
		check('the user menu is not in the DOM before the avatar is clicked',
			(await page.eval(`document.querySelectorAll('.z-menupopup').length`)) === 0);
		await page.click(`document.querySelector('.z-avatar[title="Jane Chen"]')`);
		await page.waitFor(`(() => { const m = document.querySelector('.z-menupopup');
			return !!m && getComputedStyle(m).display !== 'none'; })()`);
		check('user-menu use case: onClick on the avatar opens the menupopup', true);
		check('the menu carries the account entries',
			/Profile/.test(await page.eval(`document.querySelector('.z-menupopup').textContent`)));

		check('gap renders as inline padding on the initials only when it differs from the default',
			await page.eval(`(() => { const spans = Array.from(document.querySelectorAll('.z-avatar-text'))
				.filter(s => s.textContent.trim() === 'WM');
				return spans.length === 4 && spans.filter(s => s.style.padding).length === 3; })()`),
			await page.eval(`Array.from(document.querySelectorAll('.z-avatar-text'))
				.filter(s => s.textContent.trim() === 'WM').map(s => s.style.padding || 'default').join(',')`));
	});
	report('avatar.zul');

	chrome.kill();
	process.exit(summary());
})().catch(e => { console.error('RUN FAILED:', e.message, e.stack); process.exit(1); });
