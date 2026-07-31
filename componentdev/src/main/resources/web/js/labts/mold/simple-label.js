/* simple-label.js

	The DEFAULT mold of com.foo.SimpleLabel.

	A mold file is a single JavaScript EXPRESSION that evaluates to a function; ZK
	assigns it into the widget class's molds map. Do NOT end the file with ';'.

	The named form (function simpleLabel$mold$) is what ZK 10's own sources use -
	83 of the 98 molds in the released 10.3.0.1 zul jar are named. The name never
	appears anywhere else; its only job is to make stack traces readable.
*/
function simpleLabel$mold$(out) {
	var uuid = this.uuid;
	out.push('<span', this.domAttrs_(), '>',
		// The sub-element id convention: "<uuid>-<subId>" is what makes
		// this.$n('inner') resolvable from the widget's JS.
		'<span id="', uuid, '-inner" class="', this.$s('inner'), '">',
		// ALWAYS encode text that came from the server. Without this a value
		// containing markup is an XSS hole.
		zUtl.encodeXML(this.getValue()),
		'</span>',
		'</span>');
}
