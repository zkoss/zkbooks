/**
 * Purpose: support DataMatrix
 * Based on version: 9.6.0.1
 */
zk.afterLoad('zkmax.barscanner', function () {

    zkmax.barscanner.Barcodescanner.dataMatrix = {
        create: function () {
            return new zkmax.barscanner.zxing.BrowserDatamatrixCodeReader();
        },
        //The zxing library decode method.
        decode: function (wgt, reader) {
            reader.processing = true;
            new Promise(function (resolve, reject) {
                reader.decodeOnce(resolve, reject);
            }).then(function (result) {
                reader.processing = false;
                if (reader.enable) {
                    wgt._doDetect('datamatrix', result.text);
                } else {
                    reader.lastProcessing = false;
                }
            }).catch(function (err) {
                zk.error(err);
            });
        },
    };
    zkmax.barscanner.Barcodescanner.registerLibrary({
        create: zkmax.barscanner.Barcodescanner.dataMatrix.create,
        destroy: zkmax.barscanner.Barcodescanner._zxingDestroy,
        name: 'ZXING',
        init: zkmax.barscanner.Barcodescanner._zxingInit,
        enable: zkmax.barscanner.Barcodescanner._zxingEnable,
        disable: zkmax.barscanner.Barcodescanner._zxingDisable,
        decode: zkmax.barscanner.Barcodescanner.dataMatrix.decode,
        setType: null,
        other: null
    }, 'ZXING', ['DataMatrix']);
});