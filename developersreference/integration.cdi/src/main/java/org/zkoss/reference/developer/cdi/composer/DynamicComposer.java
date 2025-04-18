package org.zkoss.reference.developer.cdi.composer;


import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

import org.zkoss.reference.developer.cdi.domain.OrderService;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

/**
 * @author Hawk
 */
@VariableResolver(org.zkoss.zkplus.cdi.DelegatingVariableResolver.class)
public class DynamicComposer extends SelectorComposer<Window> {

    @Wire
    private Checkbox specialBox;
    @Wire("grid")
    private Grid grid;
    private OrderService orderService;
    private List<String> orderList;


    @Listen("onClick = button")
    public void listOrder() {
        Set<Bean<?>> beans = null;
        BeanManager beanManager = CDI.current().getBeanManager();
        if (specialBox.isChecked()) {
            beans = beanManager.getBeans("specialOrderService");
        } else {
            beans = beanManager.getBeans("normalOrderService");
        }
        if (!beans.isEmpty()) {
            Context context = beanManager.getContext(ApplicationScoped.class);
            Bean bean = beans.iterator().next();
            orderService = (OrderService) context.get(bean, beanManager.createCreationalContext(bean));
            orderList = orderService.findAll();
            grid.setModel(new ListModelList(orderList));
        }
    }
}
