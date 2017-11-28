package org.zkoss.reference.developer.mvc.model;

public class Food {
    private String category;
    private String name;
    private String topNutrients;
    private Integer dailyPercent;
    private Integer calories;
    private String quantity;

    public Food(String category, String name, String topNutrients,
                Integer dailyPercent, Integer calories, String quantity) {
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}