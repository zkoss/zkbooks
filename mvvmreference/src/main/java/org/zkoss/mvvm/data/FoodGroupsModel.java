package org.zkoss.mvvm.data;

import org.zkoss.zul.*;

import java.util.*;

public class FoodGroupsModel extends GroupsModelArray {
    private List<String> categories = new LinkedList();


    public FoodGroupsModel(Food[] foods) {
        super(foods, new FieldComparator("category", true));
    }

    public FoodGroupsModel(List<Food> foods) {
        super(foods.toArray(new Food[0]), new FieldComparator("category", true));
        for (int i = 0; i < _heads.length ; i++) {
            categories.add(((Food)(((Object[])_heads[i])[0])).getCategory());
        }
    }

    public int getIndexInGroup(Food food) {
        int groupIndex = categories.indexOf(food.getCategory());
        for (int i = 0 ; i < _data[groupIndex].length ; i++){
            if (((Food)_data[groupIndex][i]).getName().equals(food.getName())){
                return i;
            }
        }
        throw new IllegalArgumentException(food + " should exist in the GroupsModel");
    }

    protected Object createGroupHead(Object[] groupdata,int index,int col) {
        return new Object[] {groupdata[0], new Integer(col)};
    }

    @Override
    protected Object createGroupFoot(Object[] groupdata, int index, int col) {
        return "Total " + groupdata.length + " items";
    }
};