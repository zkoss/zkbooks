zk.afterLoad('zul.wnd',
    function(){
        //locate the target function in the target class (or parent class)
        const oldPanel = zk.augment(zul.wnd.Panel.prototype, {
            onClose() {
                //call the original function before override if it's needed
                oldPanel.onClose.apply(this);
                //implement the custom logic
                zk.log('Modified zul.wnd.Panel.prototype.onClose');
                this.addedMethod();
            },
            addedMethod: function(){
            },
        })
    }
);