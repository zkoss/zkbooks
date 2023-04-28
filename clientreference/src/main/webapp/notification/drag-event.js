//fire custom events to a server
var dragHandler = {
    onStartDrag: function(controller, event) {
        console.log('startDrag', event.target);
        event.target.fire('onStartDrag', null, {toServer:true});
        //change something e.g. highlighting drop target
    },
    onEndDrag: function(controller, evt) {
        console.log('endDrag', event.target);
        event.target.fire('onEndDrag', null, {toServer:true});
        //remove the drop highlights
    }
}

zWatch.listen({
        onStartDrag: dragHandler,
        onEndDrag: dragHandler
});