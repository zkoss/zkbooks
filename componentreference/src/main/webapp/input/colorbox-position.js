/**
 * Purpose: override the popup position of a colorbox
 * Based on version: 9.6.1
 */
zk.afterLoad('zkex.inp', function() {
    var exWidget = {};
    zk.override(zkex.inp.Colorbox.prototype, exWidget, {
        _getPosition: function _getPosition() {
          var parent = this.parent;
          if (!parent) return;
          if (parent.$instanceof(zul.wgt.Toolbar)) return 'vertical' == parent.getOrient() ? 'end_before' : 'after_start'; //position in toolbar
          return 'before_start';
        },
    });
});
