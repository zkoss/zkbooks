/** draw an image in a canvas of the signature
 * https://developer.mozilla.org/en-US/docs/Web/API/Canvas_API/Tutorial/Using_images
 */
zk.afterLoad('zkmax.wgt', function() {
    var exWidget = {};
    zk.override(zkmax.wgt.Signature.prototype, exWidget, {
		bind_: function(){
			exWidget.bind_.apply(this, arguments);
            this.drawBackgroundImage();
		},
		undo: function(){
		    exWidget.undo.apply(this, arguments);
            this.drawBackgroundImage();
		},
		clear: function(){
		    exWidget.clear.apply(this, arguments);
            this.drawBackgroundImage();
		},
        drawBackgroundImage: function(){
            var signatureWidget = this;
            var img = new Image();
            img.src = '/component/multimedia/zklogo3.png';
            img.onload = function(){
                var context = signatureWidget.$n('canvas').getContext('2d');
                context.drawImage(img, 0, 0);
            }
        }
    });

});