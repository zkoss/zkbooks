package org.zkoss.reference.developer.mvc.model;

import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.GroupsModelArray;

import java.util.List;

public class FoodGroupsModel extends GroupsModelArray {
    public FoodGroupsModel(Food[][] foods) {
        super(foods, new FieldComparator("category", true));
    }

    public FoodGroupsModel(List<Food> foods) {
        super(foods.toArray(new Food[0]), new FieldComparator("category", true));
    }


    protected Object createGroupHead(Object[] groupdata,int index,int col) {
        return new Object[] {groupdata[0], new Integer(col)};
    }
};