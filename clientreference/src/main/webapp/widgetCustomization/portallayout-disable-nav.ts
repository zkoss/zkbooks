/**
 * Purpose: Avoid opening a popup when pressing a spacebar.
 * Since this feature is provided by za11y, we should load this js after za11y is loaded.
 */
zk.afterLoad('za11y, zkmax.layout', function() {
    const exWidget = zk.augment(zkmax.layout.Portallayout.prototype, {
        doKeyDown_: function (...args: any[]) {
            // the function below is overridden by za11y which shows the portal popup
            // exWidget.doKeyDown_.apply(this, arguments);
            // to skip the function in za11y, call the function before being overridden by za11y
            this.$supers(zkmax.layout.Portallayout, 'doKeyDown_', args);
        },
    });
});
