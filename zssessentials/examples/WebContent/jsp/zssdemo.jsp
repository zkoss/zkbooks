<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ZSS JSP Tag Sample</title>
</head>
<body style="height: 100%">
<script type="text/javascript">
	if (typeof jq == 'undefined' && window.parent) {
		var p = window.parent;
		if (p) {
			jq = p.jq;
			zAu = p.zAu;
			zk = p.zk;
		}
	}
	function export() {
		zAu.send(new zk.Event(zk.Widget.$(jq('$myzss')[0]), 'onExport',
				[ '' ]));
	}
	function showCellTextBtnClick() {
		zAu.send(new zk.Event(zk.Widget.$(jq('$myzss')[0]), 'onShowCellText',
				[ jq('#cellRefTxt').val() ]));
	}
	function setCellTextBtnClick() {
		zAu.send(new zk.Event(zk.Widget.$(jq('$myzss')[0]), 'onSetCellText',
				[ jq('#cellRefTxt').val(), jq('#cellTextTxt').val() ]));
	}
</script>
<%@ taglib prefix="zss" uri="http://www.zkoss.org/jsp/zss"%>
<table>
	<tr>
		<td>Cell:</td>
		<td><input type="text" id="cellRefTxt"></input></td>
		<td><input type="button" id="showBtn" onclick="showCellTextBtnClick();"
			value="Show"></input></td>
		<td>Value:</td>
		<td><input type="text" id="cellTextTxt"></input></td>
		<td><input type="button" id="setCellTextBtn" onclick="setCellTextBtnClick();"
			value="Set"></input></td>
		<td><input type="button" onclick="export();" value="Export"></input>
		</td>
	</tr>
</table>
<div width="100%" style="height: 100%;"><zss:spreadsheet
	id="myzss" src="/WEB-INF/excel/jsp/ZSS-demo_sample.xlsx" width="100%"
	height="800px" maxrows="200" maxcolumns="40"
	apply="org.zkoss.zssessentials.jsp.ZSSComposer" /></div>
</body>
</html>