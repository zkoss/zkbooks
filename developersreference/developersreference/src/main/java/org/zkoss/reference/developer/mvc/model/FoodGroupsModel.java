package org.zkoss.reference.developer.mvc.model;

import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.GroupsModelArray;

public class FoodGroupsModel extends GroupsModelArray {
    public FoodGroupsModel(Food[][] foods) {
        super(foods, new FieldComparator("category", true));
    }
    protected Object createGroupHead(Object[] groupdata,int index,int col) {
        return new Object[] {groupdata[0], new Integer(col)};
    }
};