package org.zkoss.reference.component.supplementary;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;
import org.zkoss.zul.event.PagingEvent;

import java.util.*;

public class PagingComposer extends SelectorComposer<Component> {

    @Wire("listbox")
    private Listbox listbox;
    @Wire("paging")
    private Paging paging;

    private List dataSource = new ArrayList<Locale>(Arrays.asList(Locale.getAvailableLocales()));
    private ListModelList model = new ListModelList();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        model.addAll(getPageData(0));
        listbox.setModel(model);
        paging.setTotalSize(dataSource.size());
    }

    @Listen("onPaging = paging")
    public void onPaging(PagingEvent event){
        model.clear();
        model.addAll(getPageData(event.getActivePage()));
    }

    public List<Locale> getPageData(int activePage){
        if ((activePage+1) * paging.getPageSize() > (dataSource.size()-1)){
            return dataSource.subList(activePage * paging.getPageSize(), dataSource.size()-1);
        }else{
            return dataSource.subList(activePage * paging.getPageSize(), (activePage + 1) * paging.getPageSize());
        }
    }
}
