package org.zkoss.reference.developer.mvc.view;

import org.zkoss.reference.developer.mvc.model.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zul.*;

import java.util.LinkedList;

public class FoodComposer extends SelectorComposer<Component> {
    static public LinkedList<Food> foodList = new LinkedList<Food>();
    static{
        foodList.add(new Food("Vegetables", "Asparagus", "Vitamin K", 115, 43, 3));
        foodList.add(new Food("Vegetables", "Beets", "Folate", 33, 74, 4));
        foodList.add(new Food("Vegetables", "Bell peppers", "Vitamin C", 291, 24, 12));
        foodList.add(new Food("Vegetables", "Cauliflower", "Vitamin C", 92, 28, 21));
        foodList.add(new Food("Vegetables", "Eggplant", "Dietary Fiber", 10, 27, 12));
        foodList.add(new Food("Vegetables", "Onions", "Chromium", 21, 60, 4));
        foodList.add(new Food("Vegetables", "Potatoes", "Vitamin C", 26, 132, 6));
        foodList.add(new Food("Vegetables", "Spinach", "Vitamin K", 1110, 41, 12));
        foodList.add(new Food("Vegetables", "Tomatoes", "Vitamin C", 57, 37, 32));
        foodList.add(new Food("Seafood", "Salmon", "Tryptophan", 103, 261, 7));
        foodList.add(new Food("Seafood", "Shrimp", "Tryptophan", 103, 112, 6));
        foodList.add(new Food("Seafood", "Scallops", "Tryptophan", 81, 151, 6));
        foodList.add(new Food("Seafood", "Cod", "Tryptophan", 90, 119, 9));
        foodList.add(new Food("Fruits", "Apples", "Manganese", 33, 61, 4));
        foodList.add(new Food("Fruits", "Cantaloupe", "Vitamin C", 112, 56, 5));
        foodList.add(new Food("Fruits", "Grapes", "Manganese", 33, 61, 9));
        foodList.add(new Food("Fruits", "Pineapple", "Manganese", 128, 75, 4));
        foodList.add(new Food("Fruits", "Strawberries", "Vitamin C", 24, 48, 7));
        foodList.add(new Food("Fruits", "Watermelon", "Vitamin C", 24, 48, 3));
        foodList.add(new Food("Poultry & Lean Meats", "Beef, lean organic", "Tryptophan", 112, 240, 4));
        foodList.add(new Food("Poultry & Lean Meats", "Lamb", "Tryptophan", 109, 229, 3));
        foodList.add(new Food("Poultry & Lean Meats", "Chicken", "Tryptophan", 121, 223, 7));
        foodList.add(new Food("Poultry & Lean Meats", "Venison ", "Protein", 69, 179, 4));
        foodList.add(new Food("Grains", "Corn ", "Vatamin B1", 24, 177, 3));
        foodList.add(new Food("Grains", "Oats ", "Manganese", 69, 147, 6));
        foodList.add(new Food("Grains", "Barley ", "Dietary Fiber", 54, 270, 12));
    }

    static public LinkedList<Food> getFoodList() {
        return foodList;
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        Listbox listbox = (Listbox)comp;
        FoodGroupsModel model = new FoodGroupsModel(foodList);
        listbox.setModel(model);
    }
}
