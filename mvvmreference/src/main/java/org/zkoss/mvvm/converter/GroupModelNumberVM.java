package org.zkoss.mvvm.converter;

import org.zkoss.bind.*;
import org.zkoss.mvvm.data.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.*;

public class GroupModelNumberVM {

    GroupsModelArray model = new FoodGroupsModel(Food.foodList);

    public GroupsModelArray getModel() {
        return model;
    }

    /* convert a data object (Food) to its index in its group */
    public Converter getGroupItemNumberConverter() {
        return new Converter() {
            @Override
            public Object coerceToUi(Object o, Component component, BindContext bindContext) {
                FoodGroupsModel model = (FoodGroupsModel) bindContext.getConverterArg("model");
                Food food = (Food) o;
                return model.getIndexInGroup(food);
            }

            @Override
            public Object coerceToBean(Object o, Component component, BindContext bindContext) {
                return null;
            }
        };
    }
}
