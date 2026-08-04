<%-- simplelabel.css.dsp

	Styling for com.foo.SimpleLabel.

	A .css.dsp file is a stylesheet run through ZK's DSP interpreter, so it CAN use
	EL and taglibs. The .z-simplelabel-fancy rule below does exactly that: its
	background-image resolves a classpath resource URL with
	url(${c:encodeThemeURL("~./js/com/foo/img/dot.png")}) - the one thing a plain
	.css file cannot do. Every OTHER rule in this file is plain CSS and would behave
	identically as a plain .css file (verified: pointing <css-uri> at a plain .css
	produces byte-identical output for those rules in zk.wcs). Reach for .css.dsp
	only when a rule genuinely needs server-side EL, such as a classpath image URL;
	everything else can stay a plain .css.

	Delivery: this file is never requested by its own URL. <css-uri> in
	lang-addon.xml causes ZK to server-side-include it into the language's single
	~./zul/css/zk.wcs response (WcsExtendlet), which is in turn interpreted
	in-process by the DspExtendlet that every ClassWebResource registers
	unconditionally. No *.dsp <servlet-mapping> is consulted and none is required in
	web.xml - confirmed by removing it (and the zweb-dsp dependency) and observing
	byte-identical output.
--%>
<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>

/* The class names below are what this.$s('inner') produces on the client:
   getZclass() returns "z-simplelabel", and $s('inner') appends "-inner". */
.z-simplelabel {
	display: inline-block;
	font-family: var(--zk-base-content-font-family);
}

.z-simplelabel-inner {
	padding: 0.2em 0.4em;
}

/* The "fancy" mold adds this on top of -inner. */
.z-simplelabel-fancy {
	border: 1px solid #c0c0c0;
	border-radius: 0.25em;
	background-color: #f5f5f5;
	/* The one thing a plain .css cannot do: resolve a URL for a resource shipped
	   inside this jar. c:encodeThemeURL = encodeURL(resolveThemeURL(uri)); with no
	   theme configured resolveThemeURL returns the URI unchanged
	   (ServletFns.java:95-116), so this becomes
	   <ctx>/zkau/web/<cache-segment>/js/com/foo/img/dot.png. */
	background-image: url(${c:encodeThemeURL("~./js/com/foo/img/dot.png")});
	background-repeat: no-repeat;
	background-position: right center;
}
