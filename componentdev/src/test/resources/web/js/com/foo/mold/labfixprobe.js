/* labfixprobe.js

	TEST FIXTURE ONLY - not part of the component, not linted by
	MoldFileFormPropertyTest/StylingContractPropertyTest (both scan only mold
	files under src/main/resources/web/js, not src/test/resources).

	Exists solely to give the CSS-URI-only fixture molds in the test-scoped
	componentdev-testfixtures addon (dupcss/plaincss/lessprobe/elprobe in
	src/test/resources/metainfo/zk/lang-addon.xml) a <mold-uri> that is NOT
	"mold/simple-label.js" or "mold/fancy.js".

	Why this matters: WpdExtendlet keeps ONE moldInfos map keyed by <mold-uri>
	STRING, shared across every widget in a single com.foo.wpd response
	(WpdExtendlet.java:344-421). The FIRST mold name it encounters for a given
	URI keeps its inlined content; every later mold name sharing that same URI
	is rewritten to an ALIAS - zk._m['x']=[com.foo.SimpleLabel,'y'];  - instead
	of its own bytes, and which one is "first" depends on getMoldNames()
	iteration order, not declaration order. Pointing all four fixture molds at
	THIS file instead keeps them clustered with each other (harmless - nothing
	ever selects these mold names) and off the shipped "default"/"fancy" URIs,
	so MoldFormIT.theWpdResponseInlinesEachMoldFileVerbatim keeps seeing the
	real simpleLabel$mold$/fancy$mold$ bytes under those keys.

	None of these four mold names is ever set with mold="..." on any page, so
	this file's actual DOM output is never rendered; it only needs to be a
	syntactically valid mold expression.
*/
function labfixProbe$mold$(out) {
	out.push('<span', this.domAttrs_(), '>',
		'<span id="', this.uuid, '-inner" class="', this.$s('inner'), '">',
		zUtl.encodeXML(this.getValue()),
		'</span>',
		'</span>');
}
