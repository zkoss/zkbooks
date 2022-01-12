window.addEventListener('load', function () {
  const codeReader = new zkmax.barscanner.zxing.BrowserDatamatrixCodeReader()
  console.log('ZXing code reader initialized');

  const decodeFun = (e) => {

    const parent = e.target.parentNode.parentNode;
    var img = parent.getElementsByClassName('img')[0].cloneNode(true);
    loadImage(img);
    const resultEl = parent.getElementsByClassName('result')[0];
    img.onload = function(){
        codeReader.decodeFromImage(img)
          .then(result => {
            console.log(result);
            resultEl.textContent = result.text;
          })
          .catch(err => {
            console.error(err);
            resultEl.textContent = err;
          });
    }
    console.log(`Started decode for image from ${img.src}`)
  };

  for (const element of document.getElementsByClassName('decodeButton')) {
    element.addEventListener('click', decodeFun, false);
  }

})

function loadImage(image){
    image.style = 'display: none';
    document.body.append(image);
}