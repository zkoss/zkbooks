/**
 * Purpose: an example to override the default function of a widget
 * Based on version:
 */
zk.afterLoad('zul.inp', function() { //specify zk widget package name
    var exWidget = {};
    zk.override(zul.inp.ComboWidget.prototype, exWidget, { //specify zk full widget name
		doClick_: function(e){
			exWidget.doClick_.apply(this, arguments); //call the original widget's overridden function
			//implement your custom logic
		},
    });

});
if(zk.version != '9.0.0'){
	console.warn('This override script version compatibility was tested for ZK version ' + ''
	    + 'If you are running a different version, please check this script compatibility ')
}