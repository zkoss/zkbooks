package org.zkoss.reference.developer.spring.composer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;
import org.zkoss.reference.developer.spring.domain.*;
import org.zkoss.reference.developer.spring.scope.PrototypeBean;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;

/**
 * "prototype" is the best-fit scope for ZK in most cases. Since when reloading a page, detach/re-attach a zul snippet, you usually want a fresh new state instead of keeping old state.
 */
@Controller
@Scope("prototype")
public class OrderBeanComposer extends SelectorComposer {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PrototypeBean prototypeBean;

    @Wire("grid")
    private Grid grid;
    @Wire
    private Label prototypeValue;


    private ListModelList<Order> model;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        model = new ListModelList(orderService.list());
        grid.setModel(model);
        prototypeValue.setValue(prototypeBean.getValue());
    }

    @Listen("onClick = button")
    public void newOrder(){
       model.add(orderService.newOrder());
    }
}
