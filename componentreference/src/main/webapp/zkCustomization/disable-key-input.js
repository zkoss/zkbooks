/**
 * Purpose: disable user keyboard input during "processing" message appears.
 * Based on version: 9.0.0
 */
zk.afterLoad('zk', function() {
    zk.startProcessing0 = zk.startProcessing;
    zk.endProcessing0 = zk.endProcessing;
    zk.startProcessing = function (timeout, pid /* internal use only */) {
        zk.startProcessing0(timeout, pid);
        disableKeyboardInput();
    };

    zk.endProcessing = function (pid /* internal use only */) {
        zk.endProcessing0(pid);
        enableKeyboardInput();
    };

    function disableKeyboardInput(){
        jq(document).bind('keydown.zk', function (e) {
            return false;
        })
    }

    function enableKeyboardInput(){
        jq(document).unbind('.zk');
    }
});