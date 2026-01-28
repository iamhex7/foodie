package foodie;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu {
    private String name;
    private String url;
    private ArrayList<String> ingredientList;

    // static，全局共享，用来管理所有食材
    private static HashMap<String, Ingredient> ingredientMap = new HashMap<>();

    public Menu(String name, String url, ArrayList<String> ingredientList) {
        this.name = name;
        this.url = url;
        this.ingredientList = ingredientList;

        // 遍历 ingredientList，把 menu 加到 ingredient 对应的 foodlist
        for (String ingName : ingredientList) {
            Ingredient ing = ingredientMap.getOrDefault(ingName, new Ingredient(ingName));
            ing.addMenu(this);
            ingredientMap.put(ingName, ing);
        }
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<String> getIngredientList() {
        return ingredientList;
    }

    public static HashMap<String, Ingredient> getIngredientMap() {
        return ingredientMap;
    }

    @Override
    public String toString() {
        return name + " (URL: " + url + ")";
    }
}
