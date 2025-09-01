"use strict";
/**
 * Purpose: provide a visual feedback when dragging a label. See zk.DnD.ghost()
 * Based on version: 9.6.3
 */
zk.afterLoad('zul.wgt', function () {
    var exWidget = zk.augment(zul.wgt.Label.prototype, {
        /** return a string and zk.DnD.ghost() encloses it with an icon (plus or ban).
        return null and zk.DnD.ghost() just clones the widget's DOM elements.
        */
        getDragMessage_: function () {
            return this.getValue();
        }
    });
});
