/**
 * Purpose: select the text in an input widget when it gets focused
 * Based on version: 9.0.0
 */
zk.afterLoad('zul.inp', function() {
    var exWidget = {};
    zk.override(zul.inp.InputWidget.prototype, exWidget, {
		doFocus_: function(e){
			exWidget.doFocus_.apply(this, arguments);
			this.select();
		},
    });
});