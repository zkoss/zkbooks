/* embed demo on document */

var DEMO_APP_URL = 'https://www.zkoss.org/component/';



//https://doc.wikimedia.org/mediawiki-core/master/js/#!/api/mw.loader-method-getScript
mw.loader.getScript(DEMO_APP_URL + 'zkau/web/js/zkmax/embedded/embedded.js').then(
    embedDemo,
    function(){
        console.error('failed to load zk embedded API');
    }
);

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

/*
 * [ComponentName]-[SectionHeader].zul
 * all in lowercases.
 * e.g. listbox-nonselectabletags.zul
 */
function defaultPath(demoBox){

    //find component name on breadcrumb
    let componentName = getComponentName();
    // find section header
    let sectionHeader = getSectionHeader(demoBox);
}

function getComponentName(){
    return $('.potix-breadcrumb:last').text().toLowerCase();
}

function getSectionHeader(demoBox){
    let sibling = demoBox.previousElementSibling;
    while (sibling != null){
        if (isSectionHeader(sibling)){
            return $('.mw-headline', sibling).text().toLowerCase().trim();
        }
        sibling = demoBox.previousElementSibling;
        console.log(sibling);
    }
}

function isSectionHeader(element){
    return  element.nodeName == 'H1'
            || element.nodeName == 'H2'
            || element.nodeName == 'H3';
}
