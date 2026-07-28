zk.afterLoad('zk', function() {
    let oldFetch = zAu._fetch;
    zAu._fetch = function (resource, options) {
        options.redirect = 'error';
        return oldFetch(resource, options);
    }
});