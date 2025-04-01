/**
 * Purpose: avoid dragging a textbox
 * Based on version: 10.1.0
 */
zk.afterLoad('zul.sel', function() {
    var exWidget = {};
    zk.override(zul.sel.Listitem.prototype, exWidget, {
        getDragOptions_: function () {
            // combine the original options with the new one
            var opts = exWidget.getDragOptions_.apply(this, arguments);

            // return true to avoid dragging a textbox
            opts.ignoredrag = function (draggable, pagexy, event) {
                return event.target.widgetName == 'textbox';
            };

            return opts;
        },
    });
	
});