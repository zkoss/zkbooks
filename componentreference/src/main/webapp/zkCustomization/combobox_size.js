/**
 * Purpose: increase a combobox width as its popup's when opening the popup
 * Based on version: 8.5.2.1
 */
zk.afterLoad('zul.inp', function() {
    var exWidget = {};
    zk.override(zul.inp.ComboWidget.prototype, exWidget, {
		open: function(opts){
		    exWidget.open.apply(this, arguments);
            jq(this.$n()).css('width', this.$n('pp').offsetWidth+'px');
		}
    });
});