/**
 * Purpose: a custom Render to show a year-first title for tw and japan calendar
 * Based on version: 9.5.1.0
 */
zk.afterLoad('zul.db', function() {
    zul.db.Renderer.titleHTML =
    function (wgt, out, localizedSymbols) {
		var uuid = wgt.uuid,
			view = wgt._view,
			val = wgt.getTime(),
			m = val.getMonth(),
			y = val.getFullYear(),
			// to avoid moment using the last day(according to local timezone) of the previous year in ie.
			date = zk.ie ? new Date(y, m) : val._moment.toDate(),
			localeDateTimeFormat = zk.ie < 11 ? null : new Intl.DateTimeFormat(localizedSymbols.LAN_TAG, {year: 'numeric'}),
			displayYear = this._getDisplayYear(date, localizedSymbols, localeDateTimeFormat),
			yofs = y - (y % 10 + 1),
			ydec = zk.parseInt(y / 100),
			text = wgt.$s('text'),
			minyear = wgt._minyear,
			maxyear = wgt._maxyear,
			endYearLength = this._getPadYearLength(wgt, localizedSymbols, localeDateTimeFormat);


		switch (view) {
		case 'day':
		    //render in year first order for title according to national convention
		    if ( wgt._localizedSymbols.LAN_TAG.indexOf("zh-TW") == 0
		        || wgt._localizedSymbols.LAN_TAG.indexOf("ja-JP") == 0) {
		        out.push('<span id="', uuid,'-ty" class="', text, '">', displayYear, '年</span>',
                '<span id="', uuid, '-tm" class="', text, '">', localizedSymbols.SMON[m], '</span>');
		    }else{
                out.push('<span id="', uuid,'-ty" class="', text, '">', displayYear, '</span>',
                '<span id="', uuid, '-tm" class="', text, '">', localizedSymbols.SMON[m], '</span>');
		    }
			break;
		case 'month':
			out.push('<span id="', uuid,
					'-ty" class="', text, '">', displayYear, '</span>');
			break;
		case 'year':
			var yearGap = 11,
				startYear = yofs < minyear ? minyear : yofs,
				startDate = new Date(startYear, m),
				displayStartYear = this._getDisplayYear(startDate, localizedSymbols, localeDateTimeFormat, endYearLength),
				expectedEndYear = yofs + yearGap;
				endYear = expectedEndYear > maxyear ? maxyear : expectedEndYear,
				endDate = new Date(endYear, m),
				displayEndYear = this._getDisplayYear(endDate, localizedSymbols, localeDateTimeFormat, endYearLength);
			out.push('<span id="', uuid, '-tyd" class="', text, '">',
					displayStartYear, ' - ', displayEndYear, '</span>');
			break;
		case 'decade':
			// each start year of cell is ten more than previous one,
			// so the end year of last cell equals the start year of first cell add 10 * 11 + 9.
			var yearGap = 10 * 11 + 9,
				expectedStartYear = ydec * 100 - 10,
				startYear = expectedStartYear < minyear ? minyear : expectedStartYear,
				startDate = new Date(startYear, m),
				displayStartYear = this._getDisplayYear(startDate, localizedSymbols, localeDateTimeFormat, endYearLength),
				expectedEndYear = expectedStartYear + yearGap,
				endYear = expectedEndYear > maxyear ? maxyear : expectedEndYear,
				endDate = new Date(endYear, m),
				displayEndYear = this._getDisplayYear(endDate, localizedSymbols, localeDateTimeFormat, endYearLength);
			out.push('<span id="', uuid, '-tyd" class="', text, '">',
					displayStartYear, ' - ', displayEndYear, '</span>');
			break;
		}
	};

});
