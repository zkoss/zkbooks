package org.zkoss.reference.developer.mvc.model;

import org.zkoss.zul.*;

public class FoodGroupRenderer implements RowRenderer {
    public void render(Row row, java.lang.Object obj, int index) {
        if (row instanceof Group) {
            //display the group head
            row.appendChild(new Label(obj.toString()));
        } else {
            //display an element
            Object[] data = (Object[]) obj;
            row.appendChild(new Label(data[0].toString()));
            row.appendChild(new Label(data[1].toString()));
            row.appendChild(new Label(data[2].toString()));
            row.appendChild(new Label(data[3].toString()));
            row.appendChild(new Label(data[4].toString()));
        }
    }
};