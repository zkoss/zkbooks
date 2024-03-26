/**
 * Purpose: an example to override the default function of a widget
 * Based on version: 10.0
 */
zk.afterLoad('zul.inp', function() { //specify zk widget package name
    var exWidget:any = {};
    zk.override(zul.inp.ComboWidget.prototype, exWidget, { //specify zk full widget name
		doClick_: function(e: zk.Event){
			exWidget.doClick_.apply(this, arguments); //call the original widget's overridden function
			//implement your custom logic
			zk.log('override onclick');
		},
    });

});