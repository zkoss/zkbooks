/**
 * Purpose: keep single selection unchanged
 * Based on version: 9.6.1
 */
    zk.afterLoad('zkmax.inp', function() {
        var exWidget = {};
        zk.override(zkmax.inp.Searchbox.prototype, exWidget, {
            _doSelectItem: function(item, bulk) {
                var $item = jq(item),
                selectedClass = this.$s('selected'),
                selected = $item.hasClass(selectedClass);
                if (!this._multiple && selected){
                    return; //select the current selected item, keep selected, do nothing
                }
                exWidget._doSelectItem.apply(this, arguments);
            },
        });
    });