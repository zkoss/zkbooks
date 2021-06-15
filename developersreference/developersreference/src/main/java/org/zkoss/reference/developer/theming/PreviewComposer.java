package org.zkoss.reference.developer.theming;

import org.zkoss.zk.au.out.AuInvoke;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;
import org.zkoss.zul.theme.Themes;

public class PreviewComposer extends SelectorComposer<Component> {
    static String[] themeNames = { "iceblue", "breeze", "atlantic" };
    private ListModelList model = new ListModelList(themeNames);

    @Wire
    private Selectbox themeBox;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        themeBox.setModel(model);
        model.addToSelection(Themes.getCurrentTheme());
    }

    @Listen(Events.ON_SELECT + "= selectbox")
    public void switchStyle(SelectEvent e){
        String themeName = e.getSelectedObjects().iterator().next().toString();
        Clients.response(new AuInvoke("switchTheme", themeName));
        Themes.setTheme(Executions.getCurrent(), themeName);
    }
}
