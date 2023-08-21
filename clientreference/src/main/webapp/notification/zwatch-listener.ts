var sizeListener = {
    onSize: function(controller: zk.ZWatchController) {
        zk.log(arguments[0].name);
        //ctrl.origin is null
    },
    onFitSize: function(controller: zk.ZWatchController) {
        zk.log(arguments[0].name);
        //ctrl.origin is null
    },
    beforeSize: function(controller: zk.ZWatchController) {
        zk.log(arguments[0].name);
        //ctrl.origin is null
    },
    afterSize: function(controller: zk.ZWatchController) {
        zk.log(arguments[0].name);
        //ctrl.origin is null
    },
    isWatchable_: function() { //required for a size event listener
        return true;
    }
};

zWatch.listen({onSize: sizeListener,
               onFitSize: sizeListener,
               beforeSize: sizeListener,
               afterSize: sizeListener,
               });

var listener = {
    onCommandReady: function(controller: zk.ZWatchController) {
        zk.log(arguments[0].name);
    },
    onResponse: function(controller: zk.ZWatchController) {
        zk.log(arguments[0].name);
    },
    onFloatUp: function(controller: zk.ZWatchController) {
        zk.log(`focus on ${(controller.origin as zk.Widget).widgetName}`)
    },
}

zWatch.listen({onCommandReady: listener,
               onResponse: listener,
               onFloatUp: listener,
               });

