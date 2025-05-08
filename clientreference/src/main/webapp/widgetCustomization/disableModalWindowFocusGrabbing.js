/**
 * an example to override a private function from zk 9.
 * purpose: a modal window will grab your focus to its first focusable component by default.
 * This js override a function to disable this behavior.
 * since 10.1.0
 */
zk.afterLoad('zul.wnd', function () {
    var _modals = [], _lastfocus;
    /**
     * private functions defined in zk 9 are moved to its widget static function in zk 10.
     */
    zul.wnd.Window_._markModal = function (wgt) {
        zk.currentModal = wgt;
        var wnd = _modals[0], fc = zk.currentFocus;
        if (wnd) wnd._lastfocus = fc;
        else _lastfocus = fc;
        _modals.unshift(wgt);
    }
});