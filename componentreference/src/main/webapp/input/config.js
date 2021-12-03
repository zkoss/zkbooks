CKEDITOR.editorConfig = function(config) {
    //enable spell checker
    config.disableNativeSpellChecker = false;
    //Automatically enables "Spell Check As You Type" on editor startup
    config.scayt_autoStartup = true;
    //locale
    config.language = 'de';
    // https://ckeditor.com/docs/ckeditor4/latest/api/CKEDITOR_config.html#cfg-removePlugins
    // A plugin required by another plugin cannot be removed and will cause an error to be thrown. So for example if contextmenu is required by tabletools, it can only be removed if tabletools is not loaded
//    config.removePlugins='contextmenu, tabletools, tableselection, liststyle';
    //need to right-click on the space area out of text elements in the content
//    config.enableContextMenu = false;
};
