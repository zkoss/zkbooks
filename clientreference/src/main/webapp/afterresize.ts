/**
* Adds a function that will be executed after all onSize events are fired.
* ZK invokes the added function once and removes it.
* It's suitable for doing things after all widgets render completely.
*/
zk.afterResize(function() {
    print();
});