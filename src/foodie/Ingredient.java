package foodie;

import java.util.ArrayList;

public class Ingredient {
    private String name;
    private ArrayList<Menu> foodlist;

    public Ingredient(String name) {
        this.name = name;
        this.foodlist = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Menu> getFoodlist() {
        return foodlist;
    }

    public void addMenu(Menu menu) {
        if (!foodlist.contains(menu)) {
            foodlist.add(menu);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
