/* fancy.js

	The "fancy" mold of com.foo.SimpleLabel - selected with <simplelabel mold="fancy"/>
	or setMold("fancy") on the server.

	It emits a different outer element (div rather than span) and an extra scoped
	class, but keeps the SAME sub-element id ("-inner"). That is the contract that
	lets the widget's JS stay mold-agnostic: SimpleLabel.js calls $n('inner') and
	never has to ask which mold rendered it.
*/
function fancy$mold$(out) {
	var uuid = this.uuid;
	out.push('<div', this.domAttrs_(), '>',
		'<span id="', uuid, '-inner" class="', this.$s('inner'), ' ', this.$s('fancy'), '">',
		zUtl.encodeXML(this.getValue()),
		'</span>',
		'</div>');
}
