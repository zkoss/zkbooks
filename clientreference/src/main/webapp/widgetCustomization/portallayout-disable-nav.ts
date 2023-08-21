/**
 * Purpose: Avoid opening a popup when pressing a spacebar.
 * Since this feature is provided by za11y, we should load this js after za11y is loaded.
 */
/* wait for https://tracker.zkoss.org/browse/ZK-5534
zk.afterLoad('za11y, zkmax.layout', function() {
    const exWidget = zk.augment(zkmax.layout.Portallayout.prototype, {
        doKeyDown_: function () {
            // to skip the function in za11y, call its parent's parent's method
            exWidget.doKeyDown_.apply(arguments);
        },
    });
});
*/