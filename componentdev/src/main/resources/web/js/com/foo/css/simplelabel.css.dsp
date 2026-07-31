<%-- simplelabel.css.dsp

	Styling for com.foo.SimpleLabel.

	A .css.dsp file is a stylesheet run through ZK's DSP interpreter, so it can use
	EL and taglibs. That is why it can resolve a classpath resource URL below
	instead of hard-coding one. A plain .css file cannot.

	Served by the dspLoader servlet - see src/test/webapp/WEB-INF/web.xml. Without
	that *.dsp mapping this file 404s and the component renders unstyled.
--%>
<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>

/* The class names below are what this.$s('inner') produces on the client:
   getZclass() returns "z-simplelabel", and $s('inner') appends "-inner". */
.z-simplelabel {
	display: inline-block;
	font-family: ${fontFamilyC};
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
