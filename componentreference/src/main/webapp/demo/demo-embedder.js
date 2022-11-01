/* embed demo on document */

var DEMO_APP_URL = 'https://www.zkoss.org/component/';

/* embed demo for each div like:
 * <div id="demoID" class="component-demo" data-path="relative-zul-path"/>
 * since embed API requires ID, we need to give unique ID on each div.
 */
function embedDemo(){
    document.querySelectorAll('.component-demo').forEach(function(demoBox){
        var demoPath = demoBox.dataset.path;
        zEmbedded.load(demoBox.id, DEMO_APP_URL + 'demo.zul' + '?path='+ demoPath);
    });
}

//https://doc.wikimedia.org/mediawiki-core/master/js/#!/api/mw.loader-method-getScript
mw.loader.getScript(DEMO_APP_URL + 'zkau/web/js/zkmax/embedded/embedded.js').then(
    embedDemo,
    function(){
        console.error('failed to load zk embedded API');
    }
);
