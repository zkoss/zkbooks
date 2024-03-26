package org.zkoss.reference.developer.mvc.model;

import java.util.LinkedList;

public class Food {
    //Data
    static public Object[][] foods = new Object[][] { //Note: the order does not matter
        new Object[] { "Vegetables", "Asparagus", "Vitamin K", 115, 43},
        new Object[] { "Vegetables", "Beets", "Folate", 33, 74},
        new Object[] { "Vegetables", "Bell peppers", "Vitamin C", 291, 24},
        new Object[] { "Vegetables", "Cauliflower", "Vitamin C", 92, 28},
        new Object[] { "Vegetables", "Eggplant", "Dietary Fiber", 10, 27},
        new Object[] { "Vegetables", "Onions", "Chromium", 21, 60},
        new Object[] { "Vegetables", "Potatoes", "Vitamin C", 26, 132},
        new Object[] { "Vegetables", "Spinach", "Vitamin K", 1110, 41},
        new Object[] { "Vegetables", "Tomatoes", "Vitamin C", 57, 37},
        new Object[] { "Seafood", "Salmon", "Tryptophan", 103, 261},
        new Object[] { "Seafood", "Shrimp", "Tryptophan", 103, 112},
        new Object[] { "Seafood", "Scallops", "Tryptophan", 81, 151},
        new Object[] { "Seafood", "Cod", "Tryptophan", 90, 119},
        new Object[] { "Fruits", "Apples", "Manganese", 33, 61},
        new Object[] { "Fruits", "Cantaloupe", "Vitamin C", 112, 56},
        new Object[] { "Fruits", "Grapes", "Manganese", 33, 61},
        new Object[] { "Fruits", "Pineapple", "Manganese", 128, 75},
        new Object[] { "Fruits", "Strawberries", "Vitamin C", 24, 48},
        new Object[] { "Fruits", "Watermelon", "Vitamin C", 24, 48},
        new Object[] { "Poultry & Lean Meats", "Beef, lean organic", "Tryptophan", 112, 240},
        new Object[] { "Poultry & Lean Meats", "Lamb", "Tryptophan", 109, 229},
        new Object[] { "Poultry & Lean Meats", "Chicken", "Tryptophan", 121, 223},
        new Object[] { "Poultry & Lean Meats", "Venison ", "Protein", 69, 179},
        new Object[] { "Grains", "Corn ", "Vatamin B1", 24, 177},
        new Object[] { "Grains", "Oats ", "Manganese", 69, 147},
        new Object[] { "Grains", "Barley ", "Dietary Fiber", 54, 270}
    };
    static public LinkedList<Food> foodList = new LinkedList<Food>();
    static{
        Food.foodList.add(new Food("Vegetables", "Asparagus", "Vitamin K", 115, 43, 3));
        Food.foodList.add(new Food("Vegetables", "Beets", "Folate", 33, 74, 4));
        Food.foodList.add(new Food("Vegetables", "Bell peppers", "Vitamin C", 291, 24, 12));
        Food.foodList.add(new Food("Vegetables", "Cauliflower", "Vitamin C", 92, 28, 21));
        Food.foodList.add(new Food("Vegetables", "Eggplant", "Dietary Fiber", 10, 27, 12));
        Food.foodList.add(new Food("Vegetables", "Onions", "Chromium", 21, 60, 4));
        Food.foodList.add(new Food("Vegetables", "Potatoes", "Vitamin C", 26, 132, 6));
        Food.foodList.add(new Food("Vegetables", "Spinach", "Vitamin K", 1110, 41, 12));
        Food.foodList.add(new Food("Vegetables", "Tomatoes", "Vitamin C", 57, 37, 32));
        Food.foodList.add(new Food("Seafood", "Salmon", "Tryptophan", 103, 261, 7));
        Food.foodList.add(new Food("Seafood", "Shrimp", "Tryptophan", 103, 112, 6));
        Food.foodList.add(new Food("Seafood", "Scallops", "Tryptophan", 81, 151, 6));
        Food.foodList.add(new Food("Seafood", "Cod", "Tryptophan", 90, 119, 9));
        Food.foodList.add(new Food("Fruits", "Apples", "Manganese", 33, 61, 4));
        Food.foodList.add(new Food("Fruits", "Cantaloupe", "Vitamin C", 112, 56, 5));
        Food.foodList.add(new Food("Fruits", "Grapes", "Manganese", 33, 61, 9));
        Food.foodList.add(new Food("Fruits", "Pineapple", "Manganese", 128, 75, 4));
        Food.foodList.add(new Food("Fruits", "Strawberries", "Vitamin C", 24, 48, 7));
        Food.foodList.add(new Food("Fruits", "Watermelon", "Vitamin C", 24, 48, 3));
        Food.foodList.add(new Food("Poultry & Lean Meats", "Beef, lean organic", "Tryptophan", 112, 240, 4));
        Food.foodList.add(new Food("Poultry & Lean Meats", "Lamb", "Tryptophan", 109, 229, 3));
        Food.foodList.add(new Food("Poultry & Lean Meats", "Chicken", "Tryptophan", 121, 223, 7));
        Food.foodList.add(new Food("Poultry & Lean Meats", "Venison ", "Protein", 69, 179, 4));
        Food.foodList.add(new Food("Grains", "Corn ", "Vatamin B1", 24, 177, 3));
        Food.foodList.add(new Food("Grains", "Oats ", "Manganese", 69, 147, 6));
        Food.foodList.add(new Food("Grains", "Barley ", "Dietary Fiber", 54, 270, 12));
    }
    private String category;
    private String name;
    private String topNutrients;
    private Integer dailyPercent;
    private Integer calories;
    private Integer quantity;

    public Food(String category, String name, String topNutrients,
                Integer dailyPercent, Integer calories, Integer quantity) {
        this.category = category;
        this.name = name;
        this.topNutrients = topNutrients;
        this.dailyPercent = dailyPercent;
        this.calories = calories;
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopNutrients() {
        return topNutrients;
    }

    public void setTopNutrients(String topNutrients) {
        this.topNutrients = topNutrients;
    }

    public Integer getDailyPercent() {
        return dailyPercent;
    }

    public void setDailyPercent(Integer dailyPercent) {
        this.dailyPercent = dailyPercent;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}