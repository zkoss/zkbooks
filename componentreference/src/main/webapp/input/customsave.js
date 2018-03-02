zk.afterLoad('ckez', function() {
    CKEDITOR.plugins.registered['customsave']= {
        init : function( editor ) {
            var command = editor.addCommand( 'customsave', {
                    modes : { wysiwyg: 1, source: 1 },
                    exec : function( editor ) {
                        editor.updateElement();
                        var val = editor.getData();
                        var wgt = zk.$(editor.element.$);
                        wgt.fire('onChange', { value: val }, { sendAhead: true });
                        wgt.fire('onSave', { value: val }, { sendAhead: true });
                        console.log('own save handler is called');
                    }
                }
            );
//            editor.ui.addButton( 'CustomSave',{ /*does not replace original button, if needed*/
            editor.ui.addButton( 'Save',{
                label: 'Save',
                icon: 'Save',
                command: 'customsave',
                toolbar: 'document,10'
            });
        }
    }
});//zk.afterLoad
