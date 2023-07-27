
zk.afterLoad('zul.wnd',
    function(){
        const oldPanel = zk.augment(zul.wnd.Panel.prototype, {
            onClose(): void {
                alert('Modified zul.wnd.Panel.prototype.onClose');

                return oldPanel.onClose();
            }
        })

});



