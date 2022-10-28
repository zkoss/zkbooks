/* Purpose: add title attribute on buttons to show tooltips
*/
function (out) {
	var uuid = this.uuid;

	out.push('<nav name="', uuid, '"', this.domAttrs_(), '>');
	if (this.getMold() == "os") {
		out.push(this._innerTags(), '</nav>');
		return;
	}
	
	var btn = this.$s('button'),
		showFirstLast = this._showFirstLast ? '' : 'display: none;';

    //add title on all buttons
	out.push('<ul role="none">',
			'<li style="' , showFirstLast, '" role="none">'+
			'<button name="', uuid, '-first" class="', btn, ' ', this.$s('first'), '" aria-label="', msgzul.FIRST,
            '" title="'+ tooltip.first + '">'+
            '<i class="z-paging-icon z-icon-angle-double-left" aria-hidden="true"></i></button></li>',
			'<li role="none">'+
			'<button name="', uuid, '-prev" class="', btn, ' ', this.$s('previous'),
            '" aria-label="', msgzul.PREV, '" title="previous page">'+
            '<i class="z-paging-icon z-icon-angle-left" aria-hidden="true"></i></button></li>',
			'<li role="none"><input name="',
				uuid, '-real" class="', this.$s('input'), '" type="text" value="',
				this.getActivePage() + 1, '" size="3" aria-label="', msgzul.CURRENT, '"></input><span class="',
				this.$s('text'), '" aria-hidden="true"> / ', this.getPageCount(), '</span></li>',
			'<li role="none">'+
			'<button name="', uuid, '-next" class="', btn, ' ', this.$s('next'),
            '" aria-label="', msgzul.NEXT, '" title="next page">'+
            '<i class="z-paging-icon z-icon-angle-right" aria-hidden="true"></i></button></li>',
			'<li role="none" style="' , showFirstLast, '">'+
			'<button name="', uuid, '-last" class="', btn, ' ', this.$s('last'),
            '" aria-label="', msgzul.LAST, '" title="last page">'+
            '<i class="z-paging-icon z-icon-angle-double-right" aria-hidden="true"></i></button></li></ul>');

	if (this.isDetailed())
		this._infoTags(out);
	out.push('</nav>');
}
