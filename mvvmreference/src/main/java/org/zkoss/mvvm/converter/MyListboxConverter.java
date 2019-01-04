package org.zkoss.mvvm.converter;

import org.zkoss.bind.converter.sys.ListboxModelConverter;

/**
 * a dummy converter example
 */
public class MyListboxConverter extends ListboxModelConverter {
    public MyListboxConverter(){
        System.out.println(MyListboxConverter.class);
    }
}
