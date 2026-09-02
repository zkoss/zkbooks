const { launch, withPage, check, report, summary, sleep } = require('./cdp');
const BASE = (process.env.ZK_BASE || 'http://localhost:8090/component') + '/input';

// Every editor on the page, in document order.
const EDITORS = `Array.from(document.querySelectorAll('.z-codeeditor'))`;
// CodeMirror keeps the document in .cm-content; each line is a .cm-line child.
const DOC_OF = el => `${el}.querySelector('.cm-content').innerText`;

// CodeMirror reads its input from the DOM, not from keydown, so a synthetic
// dispatchKeyEvent inserts nothing. Input.insertText goes through the same
// beforeinput/input path a real keystroke does.
const typeInto = async (page, text) => {
	await page.send('Input.insertText', { text });
	await sleep(300);
};

(async () => {
	const chrome = await launch();

	await withPage(`${BASE}/codeeditor.zul`, async page => {
		// ------------------------------------------------- it mounts at all
		const count = await page.eval(`${EDITORS}.length`);
		check('every <codeeditor> on the page mounts a CodeMirror editor',
			count === 15 && await page.eval(`${EDITORS}.every(e => !!e.querySelector('.cm-editor'))`),
			`found ${count} .z-codeeditor roots`);

		// ------------------------------------------------- multi-line value
		const basicDoc = await page.eval(DOC_OF(`${EDITORS}[0]`));
		check('<attribute name="value"> keeps the newlines (5-line Java sample)',
			basicDoc.split('\n').length === 5 && basicDoc.indexOf('public class Greeter') === 0,
			JSON.stringify(basicDoc.slice(0, 40)));

		// ------------------------------------------------- highlighting is real
		// CodeMirror emits generated class names that change between builds, so assert
		// on the painted colour instead of on a class name.
		const javaColours = await page.eval(`(() => {
			const spans = Array.from(${EDITORS}[0].querySelectorAll('.cm-content span'));
			return Array.from(new Set(spans.map(s => getComputedStyle(s).color)));
		})()`);
		check('the java grammar paints more than one token colour (not plain text)',
			javaColours.length > 1, javaColours.join(', ') || 'no styled spans at all');

		// ------------------------------------------------- language switching
		const sampler = `zkNode('sampler')`;
		const before = await page.eval(`${sampler}.querySelector('.cm-content').innerHTML`);
		await page.click(`zkNode('langPicker').querySelector('.z-combobox-button')`);
		await page.waitFor(`!!byText('.z-comboitem-text', 'sql')`);
		await page.click(`byText('.z-comboitem-text', 'sql')`);
		await sleep(600);
		const after = await page.eval(`${sampler}.querySelector('.cm-content').innerHTML`);
		const samplerText = await page.eval(DOC_OF(sampler));
		check('setLanguage re-highlights the live editor in place',
			before !== after, 'markup unchanged after switching to sql');
		check('...and the document itself is untouched by the switch',
			samplerText.indexOf('const limit = 25;') >= 0, JSON.stringify(samplerText.slice(0, 40)));

		// ------------------------------------------------- lineNumbers / tabSize
		check('lineNumbers="false" removes the gutter',
			!(await page.eval(`!!${EDITORS}[2].querySelector('.cm-gutters')`)));
		check('lineNumbers defaults to true (the basic editor has a gutter)',
			await page.eval(`!!${EDITORS}[0].querySelector('.cm-gutters')`));
		const tab8 = await page.eval(`getComputedStyle(${EDITORS}[2].querySelector('.cm-content')).tabSize`);
		const tab4 = await page.eval(`getComputedStyle(${EDITORS}[0].querySelector('.cm-content')).tabSize`);
		check('tabSize="8" widens the rendered tab (default is 4)',
			tab8 === '8' && tab4 === '4', `tabSize=${tab8} vs default ${tab4}`);

		// ------------------------------------------------- theme
		check('theme="dark" adds the z-codeeditor-dark modifier',
			await page.eval(`${EDITORS}[3].classList.contains('z-codeeditor-dark')`));
		const darkBg = await page.eval(
			`getComputedStyle(${EDITORS}[3].querySelector('.cm-editor')).backgroundColor`);
		check('...and the dark surface really paints (not a transparent box)',
			darkBg === 'rgb(30, 30, 30)', darkBg);

		// ------------------------------------------------- readonly vs disabled
		// readonly turns contenteditable off but keeps tabindex="0", so the box is still
		// reachable by click and by Tab - that is what makes it usable as a code viewer.
		await page.click(`${EDITORS}[4].querySelector('.cm-content .cm-line')`);
		check('readonly stays focusable and keyboard-reachable (tabindex 0)',
			await page.eval(`${EDITORS}[4].querySelector('.cm-content').getAttribute('tabindex') === '0'`)
			&& await page.eval(`document.activeElement === ${EDITORS}[4].querySelector('.cm-content')`),
			await page.eval(`${EDITORS}[4].querySelector('.cm-content').getAttribute('tabindex')`));
		const roBefore = await page.eval(DOC_OF(`${EDITORS}[4]`));
		await typeInto(page, 'XXX');
		check('...but refuses the edit',
			await page.eval(DOC_OF(`${EDITORS}[4]`)) === roBefore);
		check('disabled drops out of the tab order (tabindex -1), readonly does not',
			await page.eval(`${EDITORS}[5].querySelector('.cm-content').getAttribute('tabindex') === '-1'`),
			await page.eval(`${EDITORS}[5].querySelector('.cm-content').getAttribute('tabindex')`));
		check('readonly exposes aria-readonly to a screen reader',
			await page.eval(`${EDITORS}[4].querySelector('.cm-content').getAttribute('aria-readonly') === 'true'`),
			await page.eval(`${EDITORS}[4].querySelector('.cm-content').getAttribute('aria-readonly')`));
		check('disabled adds the z-codeeditor-disabled modifier and dims the box',
			await page.eval(`${EDITORS}[5].classList.contains('z-codeeditor-disabled')`)
			&& await page.eval(`parseFloat(getComputedStyle(${EDITORS}[5]).opacity) < 1`));
		check('disabled blocks the pointer',
			await page.eval(`getComputedStyle(${EDITORS}[5].querySelector('.cm-editor')).pointerEvents === 'none'`));

		// ------------------------------------------------- Tab is not a trap
		await page.click(`${EDITORS}[6].querySelector('.cm-content')`);
		check('clicking into the keyboard demo focuses the editor',
			await page.eval(`document.activeElement.classList.contains('cm-content')`),
			await page.eval(`document.activeElement.className`));
		await page.key('Tab', 'Tab', 9);
		await sleep(300);
		check('Tab moves focus out of the editor (no keyboard trap)',
			!(await page.eval(`document.activeElement.classList.contains('cm-content')`)),
			await page.eval(`document.activeElement.className`));

		// ------------------------------------------------- onChanging / onChange
		await page.click(`zkNode('watched').querySelector('.cm-content')`);
		await typeInto(page, '!!!');
		await page.waitFor(`textOf('changingCount').indexOf('onChanging: 0') < 0`);
		check('typing fires onChanging with the current text',
			/onChanging: [1-9]/.test(await page.eval(`textOf('changingCount')`)),
			await page.eval(`textOf('changingCount')`));
		check('onChange has NOT fired yet - it waits for the commit',
			(await page.eval(`textOf('changeCount')`)).indexOf('not committed yet') >= 0,
			await page.eval(`textOf('changeCount')`));
		await page.click(`byText('.z-button', 'the next stop')`);   // blur the editor
		await page.waitFor(`textOf('changeCount').indexOf('committed') >= 0`);
		check('onChange fires when the editor loses focus, carrying the new value',
			/committed \d+ chars/.test(await page.eval(`textOf('changeCount')`)),
			await page.eval(`textOf('changeCount')`));

		// ------------------------------------------------- client-side API
		await page.click(`byText('.z-button', 'dark')`);
		check('client setTheme("dark") repaints without a server round-trip',
			await page.eval(`zkNode('clientSide').classList.contains('z-codeeditor-dark')`));
		await page.click(`byText('.z-button', 'gutter off')`);
		check('client setLineNumbers(false) drops the gutter',
			!(await page.eval(`!!zkNode('clientSide').querySelector('.cm-gutters')`)));
		await page.click(`byText('.z-button', 'gutter on')`);
		check('client setLineNumbers(true) brings it back',
			await page.eval(`!!zkNode('clientSide').querySelector('.cm-gutters')`));
		await page.click(`byText('.z-button', 'light')`);
		check('client setTheme("light") removes the dark modifier',
			!(await page.eval(`zkNode('clientSide').classList.contains('z-codeeditor-dark')`)));

		// ------------------------------------------------- data binding
		const bound = `${EDITORS}[9]`;
		const STATUS = `(byTextContains('.z-label', 'deployed') || byTextContains('.z-label', 'rejected') || {}).textContent`;
		await page.click(`byText('.z-button', 'Deploy')`);        // baseline, nothing edited
		await page.waitFor(`!!byTextContains('.z-label', 'deployed -')`);
		const baseline = +/deployed - (\d+) chars/.exec(await page.eval(STATUS))[1];
		check('@load pushed the VM string into the editor',
			baseline === (await page.eval(DOC_OF(bound))).length, `VM saw ${baseline} chars`);

		// a trailing comment is still well-formed, so this tests the transport, not the parser
		await page.click(`${bound}.querySelector('.cm-content .cm-line:last-child')`);
		await page.key('End', 'End', 35);
		await typeInto(page, '<!-- v2 -->');
		await page.click(`byText('.z-button', 'Deploy')`);        // the click also blurs the editor
		await page.waitFor(`${STATUS}.indexOf('${baseline} chars') < 0`);
		check('@bind saves the edited text on blur - the VM received exactly what was typed',
			+/deployed - (\d+) chars/.exec(await page.eval(STATUS))[1] === baseline + 11,
			await page.eval(STATUS));

		await page.click(`${bound}.querySelector('.cm-content .cm-line:last-child')`);
		await page.key('End', 'End', 35);
		await typeInto(page, '<');                                // now genuinely malformed
		await page.click(`byText('.z-button', 'Deploy')`);
		await page.waitFor(`!!byTextContains('.z-label', 'rejected')`);
		check('...and a broken edit reaches the VM too, which reports the parse failure',
			await page.eval(`!!byTextContains('.z-label', 'rejected')`),
			await page.eval(STATUS));

		// ------------------------------------------------- accessible name
		const ARIA = (i, attr) => `${EDITORS}[${i}].querySelector('.cm-content').getAttribute('${attr}')`;
		check('ca:aria-label reaches CodeMirror\'s editable region',
			await page.eval(ARIA(10, 'aria-label')) === 'Deployment descriptor editor',
			await page.eval(ARIA(10, 'aria-label')));
		check('ca:aria-labelledby reaches it too, pointing at the visible label',
			await page.eval(`(() => {
				const t = document.getElementById(${ARIA(11, 'aria-labelledby')});
				return !!t && window.normText(t.textContent) === 'Startup script';
			})()`),
			await page.eval(ARIA(11, 'aria-labelledby')));

		// ------------------------------------------------- CSS custom properties
		const custom = await page.eval(
			`getComputedStyle(${EDITORS}[12].querySelector('.cm-editor')).backgroundColor`);
		check('--zk-codeeditor-background on style= repaints the dark surface',
			custom === 'rgb(11, 16, 33)', custom);

		// ------------------------------------------------- use case 1: template
		await page.click(`byText('.z-button', 'Preview')`);
		await page.waitFor(`!!byTextContains('.uc-preview', 'Autumn Sale')`);
		check('template use case: Preview renders the edited HTML',
			await page.eval(`!!byTextContains('.uc-preview', 'Hello Dana')`),
			await page.eval(`(document.querySelector('.uc-preview') || {}).textContent`));
		check('...and reports the committed length',
			/rendered \d+ chars/.test(await page.eval(`textOf('tplStatus')`)),
			await page.eval(`textOf('tplStatus')`));

		// ------------------------------------------------- use case 2: snapshots
		check('snapshot use case: the page opens on the newest snapshot, not an empty box',
			(await page.eval(DOC_OF(`zkNode('snapshotView')`))).indexOf('order-api-v3') >= 0
			&& (await page.eval(`textOf('snapshotNote')`)).indexOf('rollout') >= 0,
			await page.eval(`textOf('snapshotNote')`));
		await page.click(`byTextContains('.z-listcell-content', 'hotfix')`);
		await page.waitFor(`textOf('snapshotNote').indexOf('hotfix') >= 0`);
		const snap = await page.eval(DOC_OF(`zkNode('snapshotView')`));
		check('snapshot use case: selecting a row loads that JSON into the viewer',
			snap.indexOf('order-api-v2') >= 0 && snap.indexOf('"timeoutMs": 8000') >= 0,
			JSON.stringify(snap.slice(0, 60)));
		check('...and the viewer stays read-only but focusable',
			await page.eval(`zkNode('snapshotView').querySelector('.cm-content').getAttribute('aria-readonly') === 'true'`));
		await page.click(`byTextContains('.z-listcell-content', 'baseline')`);
		await page.waitFor(`textOf('snapshotNote').indexOf('baseline') >= 0`);
		check('...and switching rows replaces the document',
			(await page.eval(DOC_OF(`zkNode('snapshotView')`))).indexOf('"failureRatio": 0.8') >= 0);
	});

	report('input/codeeditor.zul');
	await chrome.kill();
	process.exit(summary());
})().catch(e => { console.error(e); process.exit(1); });
