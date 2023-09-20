/* an example to compare with zk.augment() */
zk.afterLoad('zul.wnd',
    function(){
        var xWidget = {};
        zk.override(zul.wnd.Panel.prototype, xWidget, {
            onClose: function() :void{
                zk.log('Modified zul.wnd.Panel.prototype.onClose');
                // xWidget.onClose.apply(this); //cannot get function suggestion for xWidget since it has no explicit type
                this.addedMethod()
            },
            addedMethod: function(){
            },
        })
    }
);