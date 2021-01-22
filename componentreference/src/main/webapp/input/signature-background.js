/** draw an image in a canvas of the signature
 * https://developer.mozilla.org/en-US/docs/Web/API/Canvas_API/Tutorial/Using_images
 */
function initBackground(){
    var canvas = zk.Widget.$('$sig') //get zk widget by ID selector
        .$n('canvas'); // get its inner canvas element
    var context = canvas.getContext('2d');
    var img = new Image();
    img.src = '/component/multimedia/zklogo3.png';
    img.onload = function(){
        context.drawImage(img, 0, 0);
    }
}