/**
 * Purpose: increase a combobox width as its popup's when opening the popup
 * Based on version: 8.5.2.1
 */
zk.afterLoad('zkmax.inp', function() {
    var exWidget = {};
    zk.override(zkmax.inp.Chosenbox.prototype, exWidget, {
		_fixEmptyDisplay: function(type, str, found, exist, hliteFirst){
		    exWidget._fixEmptyDisplay.apply(this, arguments);
		    if (!found && !exist){
		        jq(this.$n('pp')).hide();
		    }
		},
    });
});