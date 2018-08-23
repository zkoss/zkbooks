package org.zkoss.reference.developer.uipattern;

import java.util.*;

public class CustomerManager {
    static String[] customers = {"Matthew", "Macus", "Lucas", "John"};
    public static List<String> findAll() {
        return Arrays.asList(customers);
    }
}
