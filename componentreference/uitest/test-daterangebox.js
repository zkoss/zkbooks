// daterangebox.zul - the three enterprise use cases at the bottom of the page.
// Everything here goes through the real widget: preset buttons, the calendar popup,
// and the constraint that has to reject a range before any listener sees it.
const { launch, withPage, check, report, summary, sleep } = require('./cdp');
const BASE = (process.env.ZK_BASE || 'http://localhost:8090/component') + '/input';

// Only one popup is displayed at a time; the others stay in the DOM hidden.
const POPUP = `Array.from(document.querySelectorAll('.z-daterangebox-popup'))`
	+ `.filter(p => getComputedStyle(p).display !== 'none')[0]`;
// Calendar cells carry the date in aria-label: "7 September, 2026".
const MONTHS = ['January', 'February', 'March', 'April', 'May', 'June', 'July',
	'August', 'September', 'October', 'November', 'December'];
const label = d => `${d.getDate()} ${MONTHS[d.getMonth()]}, ${d.getFullYear()}`;
const plusDays = n => { const d = new Date(); d.setDate(d.getDate() + n); return d; };
// A panel also draws the neighbouring months' spill days. They carry the right
// aria-label but are marked z-calendar-outside and are NOT selectable, so clicking one
// is silently ignored. querySelector would hand back exactly that spill cell whenever it
// comes first in document order - even when a live cell for the same date sits in the
// next panel - so always take the first cell that is not an outside day.
const cell = text => `Array.from(${POPUP}.querySelectorAll('[aria-label="${text}"]'))`
	+ `.find(e => !e.classList.contains('z-calendar-outside'))`;
const panelsShown = `Array.from(${POPUP}.querySelectorAll('.z-calendar'))`
	+ `.map(p => p.querySelector('.z-calendar-title').textContent.trim())`;
// What the inputs render and accept: "Aug 31, 2026".
const typed = d => `${MONTHS[d.getMonth()].slice(0, 3)} ${d.getDate()}, ${d.getFullYear()}`;
const openerOf = id => `zkNode('${id}').querySelector('.z-daterangebox-button')`;
const inputsOf = id => `JSON.stringify(Array.from(zkNode('${id}')`
	+ `.querySelectorAll('input')).map(i => i.value))`;

// Pick begin then end in the popup of the given daterangebox.
// The popup opens on the month of the box's current begin value, so both dates have to
// fall inside the months it shows. When they do not, say so instead of clicking a spill
// cell and leaving the popup open - a left-open popup makes the NEXT opener click toggle
// it shut, which used to surface much later as an unrelated timeout.
// Pass {mayReject: true} when the pick is meant to be refused by a constraint.
async function pickRange(page, id, beginDate, endDate, opts = {}) {
	// showTime leaves the popup open after a pick so the times can be adjusted, and the
	// opener toggles - so clicking it again would shut that popup rather than open a fresh
	// one. Reuse whatever is already showing instead. (Esc closes it but wedges the
	// renderer, and clicking the opener to close races the popup's own dismiss-on-blur.)
	// Consecutive picks in this file always target the same box; if that ever stops being
	// true, the selectable-cell check below fails loudly rather than clicking into a
	// stranger's popup.
	if (!(await page.eval(`!!${POPUP}`)))
		await page.click(openerOf(id));
	await page.waitFor(POPUP);
	for (const d of [beginDate, endDate])
		if (!(await page.eval(`!!(${cell(label(d))})`)))
			throw new Error(`${label(d)} is not selectable in ${id}'s popup, which is showing `
				+ `${JSON.stringify(await page.eval(panelsShown))}. Set the box to a range whose `
				+ `begin month brings that date into view before picking it.`);
	await page.click(cell(label(beginDate)));
	await page.click(cell(label(endDate)));
	await sleep(400);
	// A showTime popup stays open for the time row, so the post-condition is that the value
	// committed - not that the popup closed.
	if (opts.mayReject) return;
	const shown = await page.eval(inputsOf(id));
	for (const d of [beginDate, endDate])
		if (shown.indexOf(typed(d)) < 0)
			throw new Error(`${id} did not take ${typed(d)} - its inputs read ${shown}`);
}

(async () => {
	const chrome = await launch();

	await withPage(`${BASE}/daterangebox.zul`, async page => {
		// --- ERP: posting-date inquiry -----------------------------------
		const footer = () => page.eval(`textOf('glFooter')`);
		const rowCount = () => page.eval(`zkNode('glRows').querySelectorAll('.z-row').length`);
		const thisMonth = await footer();
		check('the page loads with the This month preset applied',
			/^\d+ line\(s\) over \d+ day\(s\)/.test(thisMonth || ''), `footer: "${thisMonth}"`);
		check('the grid holds exactly the lines the footer counts',
			(await rowCount()) === Number(thisMonth.match(/^(\d+) line/)[1]),
			`${await rowCount()} rows vs footer "${thisMonth}"`);

		await page.click(`byText('.z-button', 'Last month')`);
		await page.waitFor(`textOf('glFooter') !== ${JSON.stringify(thisMonth)}`);
		const lastMonth = await footer();
		const firstOfLastMonth = new Date();
		firstOfLastMonth.setDate(1);
		firstOfLastMonth.setMonth(firstOfLastMonth.getMonth() - 1);
		check('setValue(DateRange) from a preset button reaches both inputs',
			(await page.eval(inputsOf('glRange'))).indexOf(
				`${MONTHS[firstOfLastMonth.getMonth()].slice(0, 3)} 1, ${firstOfLastMonth.getFullYear()}`) >= 0,
			await page.eval(inputsOf('glRange')));
		check('the preset re-queries the ledger',
			(await rowCount()) === Number(lastMonth.match(/^(\d+) line/)[1]),
			`${await rowCount()} rows vs footer "${lastMonth}"`);

		await page.click(`byText('.z-button', 'Quarter to date')`);
		await page.waitFor(`textOf('glFooter') !== ${JSON.stringify(lastMonth)}`);
		const qtd = await footer();
		const qtdDays = Number(qtd.match(/over (\d+) day/)[1]);
		const monthDays = Number(thisMonth.match(/over (\d+) day/)[1]);
		check('quarter to date spans at least the current month',
			qtdDays >= monthDays, `qtd=${qtdDays} days, month=${monthDays} days`);

		// A range picked in the calendar has to drive the same grid the buttons do.
		// Quarter to date leaves the begin three months back, so the popup would open on
		// months that do not contain the last week. Last month puts the previous and the
		// current month on screen, which covers today-6 .. today whatever today is.
		await page.click(`byText('.z-button', 'Last month')`);
		await page.waitFor(`textOf('glFooter') !== ${JSON.stringify(qtd)}`);
		await pickRange(page, 'glRange', plusDays(-6), plusDays(0));
		const picked = await footer();
		check('onChange rebuilds the grid from the range picked in the calendar',
			/over 7 day\(s\)/.test(picked || ''), `footer: "${picked}"`);

		// A gap in the ledger has to fall back to the grid's emptyMessage.
		await pickRange(page, 'glRange', plusDays(-3), plusDays(-2));
		await page.waitFor(`textOf('glFooter').indexOf('0 line(s)') === 0`);
		check('an empty range clears the grid and shows the emptyMessage',
			(await rowCount()) === 0 && await page.eval(
				`/No journal lines posted in this range/.test(zkNode('glRows').closest('.z-grid').textContent)`),
			`${await rowCount()} rows, footer "${await footer()}"`);
		const beforeReject = await footer();

		// maxNights=92: the page claims the component refuses a wider range itself.
		// A typed endpoint commits on blur, which is the shortest way to ask for one.
		const beginInput = `zkNode('glRange').querySelector('.z-daterangebox-begin')`;
		await page.click(beginInput);
		await page.eval(`${beginInput}.select()`);
		await page.send('Input.insertText', { text: typed(plusDays(-200)) });
		await page.key('Tab', 'Tab', 9);
		await page.waitFor(`!!document.querySelector('.z-errorbox-open')`);
		check('maxNights rejects a range wider than a quarter with an error box',
			/At most 92 night\(s\)/.test(await page.eval(
				`document.querySelector('.z-errorbox-content').textContent`)),
			await page.eval(`document.querySelector('.z-errorbox-content').textContent`));
		check('the rejected range never reaches the listener - the grid is untouched',
			(await footer()) === beforeReject, `footer moved to "${await footer()}"`);

		// A typed endpoint inside the constraint does commit, through the same onChange.
		await page.click(beginInput);
		await page.eval(`${beginInput}.select()`);
		await page.send('Input.insertText', { text: typed(plusDays(-11)) });
		await page.key('Tab', 'Tab', 9);
		await page.waitFor(`textOf('glFooter') !== ${JSON.stringify('__none__')}`);
		await sleep(400);
		check('a typed begin date re-queries on blur',
			(await footer()) !== beforeReject, `footer: "${await footer()}"`);

		// --- HR: leave request ------------------------------------------
		const balance = () => page.eval(`textOf('leaveBalance')`);
		check('the leave quote is rendered at load',
			/working day\(s\)/.test(await page.eval(`textOf('leaveSpan')`) || ''),
			await page.eval(`textOf('leaveSpan')`));
		check('a request inside the balance is submittable',
			(await page.eval(`zkWidget('leaveSubmit').isDisabled()`)) === false
			&& /left$/.test(await balance()), await balance());

		// 15 calendar days from the coming Monday is 11 working days - over the balance of 8.
		const monday = plusDays(1);
		while (monday.getDay() !== 1) monday.setDate(monday.getDate() + 1);
		const twoWeeksOn = new Date(monday);
		twoWeeksOn.setDate(twoWeeksOn.getDate() + 14);
		await pickRange(page, 'leaveRange', monday, twoWeeksOn);
		await page.waitFor(`textOf('leaveSpan').indexOf('15 calendar day(s)') >= 0`);
		check('working days, not calendar days, are counted against the balance',
			/15 calendar day\(s\), 11 working day\(s\)/.test(await page.eval(`textOf('leaveSpan')`)),
			await page.eval(`textOf('leaveSpan')`));
		check('going over the balance blocks the submit',
			(await page.eval(`zkWidget('leaveSubmit').isDisabled()`)) === true
			&& /Over the entitlement by 3 working day\(s\)/.test(await balance()), await balance());

		// --- Plant maintenance: overlap check ---------------------------
		const status = () => page.eval(`textOf('mwStatus')`);
		check('the initial window is free and priced in hours',
			/^Line 2 is free - \d+ h/.test(await status() || ''), await status());

		// WO-8851 occupies +9 to +11 days.
		await pickRange(page, 'mwRange', plusDays(9), plusDays(10));
		await page.waitFor(`textOf('mwStatus').indexOf('Rejected') === 0`);
		check('DateRange.overlaps() catches the booked work order',
			/Rejected - the window overlaps WO-8851/.test(await status()), await status());
		check('a colliding window cannot be requested',
			(await page.eval(`zkWidget('mwSubmit').isDisabled()`)) === true);

		// The freeze period is just one more range in the same list.
		await pickRange(page, 'mwRange', plusDays(21), plusDays(22));
		await page.waitFor(`textOf('mwStatus').indexOf('freeze') >= 0`);
		check('the quarter-end freeze is checked by the same call',
			/Rejected - the window overlaps Quarter-end freeze/.test(await status()), await status());

		await pickRange(page, 'mwRange', plusDays(15), plusDays(16));
		await page.waitFor(`textOf('mwStatus').indexOf('free') >= 0`);
		check('a window in the gap between bookings is accepted',
			/^Line 2 is free/.test(await status())
			&& (await page.eval(`zkWidget('mwSubmit').isDisabled()`)) === false, await status());

		// The carried-over times would invert a same-day pick (22:00 to 06:00), so the
		// listener would see a negative duration - the component rejects it first.
		const accepted = await status();
		await pickRange(page, 'mwRange', plusDays(15), plusDays(15), { mayReject: true });
		// An error box that opens while an earlier one is still showing does not get the
		// -open class, so match on the text of any error box instead.
		const invertBox = `Array.from(document.querySelectorAll('.z-errorbox'))`
			+ `.some(b => b.textContent.indexOf('Begin date must not be later than end date') >= 0)`;
		await page.waitFor(invertBox);
		check('an inverted window is rejected by the component, not by the listener',
			(await page.eval(invertBox)) && (await status()) === accepted, await status());
	});

	report('daterangebox.zul');
	chrome.kill();
	process.exit(summary());
})();
