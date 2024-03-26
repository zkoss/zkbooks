package org.zkoss.mvvm.template;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;

import java.util.ArrayList;
import java.util.List;

public class FoodViewModel{

	private static List<Food> foods = new ArrayList<Food>();
	static {
		foods.add(new Food("Vegetables", "Asparagus", "Vitamin K", 115, 43, "1 cup - 92 grams"));
		foods.add(new Food("Vegetables", "Beets", "Folate", 33, 74, "1 cup - 170 grams"));
		foods.add(new Food("Vegetables", "Bell peppers", "Vitamin C", 291, 24, "1 cup - 92 grams"));
		foods.add(new Food("Vegetables", "Cauliflower", "Vitamin C", 92, 28, "1 cup - 124 grams"));
		foods.add(new Food("Vegetables", "Eggplant", "Dietary Fiber", 10, 27, "1 cup - 99 grams"));
		foods.add(new Food("Vegetables", "Onions", "Chromium", 21, 60, "1 cup - 160 grams"));
		foods.add(new Food("Vegetables", "Potatoes", "Vitamin C", 26, 132, "1 cup - 122 grams"));
		foods.add(new Food("Vegetables", "Spinach", "Vitamin K", 1110, 41, "1 cup - 180 grams"));
		foods.add(new Food("Vegetables", "Tomatoes", "Vitamin C", 57, 37, "1 cup - 180 grams"));
		foods.add(new Food("Seafood", "Salmon", "Tryptophan", 103, 261, "4 oz - 113.4 grams"));
		foods.add(new Food("Seafood", "Shrimp", "Tryptophan", 103, 112, "4 oz - 113.4 grams"));
		foods.add(new Food("Seafood", "Scallops", "Tryptophan", 81, 151, "4 oz - 113.4 grams"));
		foods.add(new Food("Seafood", "Cod", "Tryptophan", 90, 119, "4 oz - 113.4 grams"));
		foods.add(new Food("Fruits", "Apples", "Manganese", 33, 61, "1 cup - 160 grams"));
		foods.add(new Food("Fruits", "Cantaloupe", "Vitamin C", 112, 56, "1 cup - 160 grams"));
		foods.add(new Food("Fruits", "Grapes", "Manganese", 33, 61, "1 cup - 92 grams"));
		foods.add(new Food("Fruits", "Pineapple", "Manganese", 128, 75, "1 cup - 155 grams"));
		foods.add(new Food("Fruits", "Strawberries", "Vitamin C", 24, 48, "1 cup - 150 grams"));
		foods.add(new Food("Fruits", "Watermelon", "Vitamin C", 24, 48, "1 cup - 152 grams"));
		foods.add(new Food("Poultry & Lean Meats", "Beef, lean organic", "Tryptophan", 112, 240, "4 oz - 113.4 grams"));
		foods.add(new Food("Poultry & Lean Meats", "Lamb", "Tryptophan", 109, 229, "4 oz - 113.4 grams"));
		foods.add(new Food("Poultry & Lean Meats", "Chicken", "Tryptophan", 121, 223, "4 oz - 113.4 grams"));
		foods.add(new Food("Poultry & Lean Meats", "Venison ", "Protein", 69, 179, "4 oz - 113.4 grams"));
		foods.add(new Food("Grains", "Corn ", "Vatamin B1", 24, 177, "1 cup - 164 grams"));
		foods.add(new Food("Grains", "Oats ", "Manganese", 69, 147, "1 cup - 234 grams"));
		foods.add(new Food("Grains", "Barley ", "Dietary Fiber", 54, 270, "1 cup - 200 grams"));
	}

	private FoodGroupsModel foodGroupsModel = new FoodGroupsModel(foods);

	public FoodGroupsModel getFoodGroupsModel() {
		return foodGroupsModel;
	}

	/**
	 * demonstrate how to update a GroupsModel
	 */
	@Command
	public void rename(){
		for (Food f : foods){
			if (f.getCategory().equals("Fruits")){
				f.setCategory("Various Fruits");
				BindUtils.postNotifyChange(f, "category");
			}
		}
	}
}
