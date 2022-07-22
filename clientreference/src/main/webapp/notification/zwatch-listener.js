var sizeListener = {
    onSize: function(ctrl) {
        zk.log(arguments[0].name);
    },
    onAfterSize: function(ctrl) {
        zk.log(arguments[0].name);
    },
    onFitSize: function(ctrl) {
        zk.log(arguments[0].name);
    },
    isWatchable_: function() { //required for a size event listener
        return true;
    }
};

zWatch.listen({onAfterSize: sizeListener,
               onSize: sizeListener,
               onFitSize: sizeListener,
               onShow: sizeListener,
               });

var listener = {
    onCommandReady: function(ctrl) {
        zk.log(arguments[0].name);
    },
    onResponse: function(ctrl) {
        zk.log(arguments[0].name);
    },
    onFloatUp: function(ctrl) {
        zk.log(arguments[0].name);
    },
}

zWatch.listen({onCommandReady: listener,
               onResponse: listener,
               onFloatUp: listener,
               });

