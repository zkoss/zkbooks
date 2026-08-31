const { launch, withPage, check, report, summary, sleep } = require('./cdp');
const BASE = (process.env.ZK_BASE || 'http://localhost:8090/component') + '/essential';

(async () => {
	const chrome = await launch();

	await withPage(`${BASE}/carousel.zul`, async page => {
		// --- the two claims that were only read out of bytecode -------------
		const nextArrow = `zkNode('changingCarousel').querySelector('.z-carousel-arrow-next')`;
		await page.click(nextArrow);
		await page.waitFor(`textOf('changingLog') !== 'onChanging: -'`);
		const changing = await page.eval(`textOf('changingLog')`);
		const select = await page.eval(`textOf('selectLog')`);
		check('onChanging InputEvent: getPreviousValue()=leaving index, getValue()=target index',
			changing === 'onChanging: leaving 0, going to 1', `got "${changing}"`);
		check('onSelect: activeIndex already updated and getSelectedItem() is the new slide',
			select === 'onSelect: activeIndex is now 1, getSelectedItem() is Second', `got "${select}"`);

		await page.click(nextArrow);
		await page.waitFor(`textOf('changingLog').indexOf('leaving 1') >= 0`);
		check('onChanging reports the second hop as 1 -> 2',
			(await page.eval(`textOf('changingLog')`)) === 'onChanging: leaving 1, going to 2',
			await page.eval(`textOf('changingLog')`));

		// --- server-side navigation ----------------------------------------
		await page.click(`byText('.z-button', 'Go to Q3')`);
		await page.waitFor(`zkNode('drivenCarousel').querySelectorAll('.z-carouselitem-active').length === 1`);
		const activeQ = await page.eval(
			`zkWidget('drivenCarousel')._activeIndex`);
		check('setActiveIndex(2) moves the carousel to the third slide', activeQ === 2, `activeIndex=${activeQ}`);

		await page.click(`byText('.z-button', 'Which slide is active?')`);
		await page.waitFor(`notifText()`);
		const notif = await page.eval(`notifText()`);
		check('getSelectedItem() returns the active carouselitem', /Q3/.test(notif || ''), `notification: "${notif}"`);
		await page.eval(`document.querySelectorAll('.z-notification').forEach(n => n.remove())`);

		// --- loop=false disables the arrows at the boundary -----------------
		const noLoop = `Array.from(document.querySelectorAll('.z-carousel')).find(c => zk.Widget.$(c)._loop === false)`;
		const prevDisabled = await page.eval(
			`(() => { const p = ${noLoop}.querySelector('.z-carousel-arrow-prev');
			  return p.disabled === true || /disabled/.test(p.className); })()`);
		check('loop="false" leaves the prev arrow visible but disabled at slide 0', prevDisabled === true);

		// --- indicators -----------------------------------------------------
		const indicators = await page.eval(
			`zkNode('changingCarousel').querySelectorAll('.z-carousel-indicators > *').length`);
		check('one indicator per slide', indicators === 3, `found ${indicators}`);

		// --- showArrows / showIndicators ------------------------------------
		const arrowsOff = await page.eval(
			`(() => { const c = Array.from(document.querySelectorAll('.z-carousel'))
				.find(c2 => zk.Widget.$(c2)._showArrows === false);
			  return c ? c.querySelectorAll('.z-carousel-arrow').length : -1; })()`);
		check('showArrows="false" renders no arrows', arrowsOff === 0, `arrow count ${arrowsOff}`);
		const indsOff = await page.eval(
			`(() => { const c = Array.from(document.querySelectorAll('.z-carousel'))
				.find(c2 => zk.Widget.$(c2)._showIndicators === false);
			  return c ? c.querySelectorAll('.z-carousel-indicators > *').length : -1; })()`);
		check('showIndicators="false" renders no indicators', indsOff === 0, `indicator count ${indsOff}`);

		// --- keyboard navigation --------------------------------------------
		await page.eval(`zkNode('changingCarousel').focus()`);
		const before = await page.eval(`zkWidget('changingCarousel')._activeIndex`);
		await page.key('ArrowRight', 'ArrowRight', 39);
		await sleep(400);
		const after = await page.eval(`zkWidget('changingCarousel')._activeIndex`);
		check('arrow key moves to the next slide when keyboard is on', after === (before + 1) % 3,
			`${before} -> ${after}`);

		// --- the gallery use case keeps its caption in sync -------------------
		await page.click(`zkNode('gallery').querySelector('.z-carousel-arrow-next')`);
		await page.waitFor(`textOf('galleryCaption') !== 'Nameplate, 2026-03-11'`);
		check('gallery use case: onSelect updates the caption from getSelectedItem()',
			(await page.eval(`textOf('galleryCaption')`)) === 'Hydraulic unit, 2026-03-11',
			await page.eval(`textOf('galleryCaption')`));

		// --- the KPI board renders the size it asks for ----------------------
		// .z-label carries its own font-size, so a size set on the wrapper alone never
		// reaches the number: it has to sit on the label. Cheap to get wrong again.
		const kpi = await page.eval(`(() => {
			const b = document.querySelector('.kpi-board');
			if (!b) return null;
			const n = b.querySelector('.kpi-number');
			return { number: getComputedStyle(n).fontSize,
				unit: getComputedStyle(b.querySelector('.kpi-unit')).fontSize,
				// count the real slides only: a looping carousel also renders clones
				numbers: b.querySelectorAll('.z-carouselitem .kpi-number').length,
				notes: b.querySelectorAll('.z-carouselitem .kpi-note').length };
		})()`);
		check('KPI board: the value renders at its declared size, larger than its unit',
			kpi && parseFloat(kpi.number) === 58 && parseFloat(kpi.unit) < parseFloat(kpi.number)
				&& kpi.numbers === 3 && kpi.notes === 3, JSON.stringify(kpi));

		// --- the review-queue use case logs the move before it happens --------
		await page.click(`zkNode('reviewQueue').querySelector('.z-carousel-arrow-next')`);
		await page.waitFor(`textOf('reviewLog').indexOf('document 0') >= 0`);
		check('review-queue use case: onChanging logs from -> to',
			(await page.eval(`textOf('reviewLog')`)) === 'moving from document 0 to document 1',
			await page.eval(`textOf('reviewLog')`));
	});
	report('carousel.zul');

	chrome.kill();
	process.exit(summary());
})().catch(e => { console.error('RUN FAILED:', e.message); process.exit(1); });
