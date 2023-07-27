"use strict";
zk.afterLoad('zul.wnd', function () {
    const oldPanel = zk.augment(zul.wnd.Panel.prototype, {
        onClose() {
            alert('Modified zul.wnd.Panel.prototype.onClose');
            return oldPanel.onClose();
        }
    });
});
