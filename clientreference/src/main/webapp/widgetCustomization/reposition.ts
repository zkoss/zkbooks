
zk.afterLoad('zul.inp',
    function(){
        const exWidget = zk.augment(zul.inp.Bandbox.prototype, {
            // _doSelect: function(e){
            //     exWidget._doSelect.apply(this, arguments);
            //     this._checkPopupSpaceAndPosition(this.getPopupNode_(), this.$n());
            // },
            // fireOnSelect(ref: zk.Widget | undefined, evt?: zk.Event<zk.EventMetaData>) {
            //     exWidget.fireOnSelect.apply(this, [ref, evt]);
            //     (zk.Widget.$('$bbox') as zul.inp.ComboWidget)._checkPopupPosition(this.getPopupNode_(), this.$n());
            // }
        });
    }
);