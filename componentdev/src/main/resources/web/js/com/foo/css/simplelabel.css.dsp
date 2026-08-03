<%-- simplelabel.css.dsp

	Styling for com.foo.SimpleLabel.

	A .css.dsp file is a stylesheet run through ZK's DSP interpreter, so it CAN use
	EL and taglibs - e.g. url(${c:encodeThemeURL("~./path/to/img.png")}) to resolve
	a classpath resource URL without hard-coding one. This file does not need that:
	every rule below is plain CSS, so a plain .css file would behave identically
	here (verified: pointing <css-uri> at a plain .css produces byte-identical
	rules in zk.wcs). Reach for .css.dsp only when a rule genuinely needs
	server-side EL, such as a classpath image URL.

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
}
