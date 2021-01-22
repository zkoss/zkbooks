function initBackground(){
    var canvas = zk.Widget.$('$sig').$n('canvas');
    var context = canvas.getContext('2d');
    var img = new Image();
    img.src = '/component/multimedia/zklogo3.png';
    img.onload = function(){
        context.drawImage(img, 0, 0);
    }
}