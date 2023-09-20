zk.afterLoad('zul.wnd',
    function(){
        //locate the target function in the target class (or parent class)
        const oldPanel = zk.augment(zul.wnd.Panel.prototype, {
            //copy the function signatre
            onClose: function() {
                //implement the custom logic
                zk.log('Modified zul.wnd.Panel.prototype.onClose');
                //call the original function before overidden if it's needed
                oldPanel.onClose.apply(this);

                this.addedMethod();
            },
            addedMethod: function(){
            }
        })
    }
);