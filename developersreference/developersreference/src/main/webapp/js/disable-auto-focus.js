/**
 * an example to override a widget private function
 * Purpose: disable window auto-focus
 * Based on version: 10.1.0
 */
zk.afterLoad('zul.wnd', function () {
    var _modals = [], _lastfocus;
    zul.wnd.Window_._markModal = function (wgt) {
        zk.currentModal = wgt;
        var wnd = _modals[0], fc = zk.currentFocus;
        if (wnd) wnd._lastfocus = fc;
        else _lastfocus = fc;
        _modals.unshift(wgt);
    }
});