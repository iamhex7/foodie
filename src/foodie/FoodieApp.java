package foodie;

import java.util.*;

/**
 * FoodieApp (简洁版)
 * - 从 CSV 载入菜单
 * - 读取用户输入的食材（空格分隔；短语自动转下划线、小写）
 * - 排序规则：overlap 降序 -> 缺的数量升序 -> exact 优先 -> 名字
 * - partial 输出时显示还缺哪些食材
 */
public class FoodieApp {

    public static void main(String[] args) throws Exception {
        String defaultCsv = "C:\\\\Users\\\\Hex\\\\OneDrive\\\\foodie\\\\menuaug.csv";
        String csvPath = (args.length > 0) ? args[0] : defaultCsv;

        Scanner sc = new Scanner(System.in);
        
        // Menu: Add or Search?
        System.out.println("═══════════════════════════════════");
        System.out.println("       Welcome to Foodie! 🍕");
        System.out.println("═══════════════════════════════════");
        System.out.println("What would you like to do?");
        System.out.println("  (A)dd a new recipe");
        System.out.println("  (S)earch for dishes to cook");
        System.out.print("Enter your choice (A/S): ");
        String choice = sc.nextLine().trim().toUpperCase();
        
        if (choice.equals("A")) {
            addNewRecipe(sc, csvPath);
        } else if (choice.equals("S")) {
            searchRecipes(sc, csvPath);
        } else {
            System.out.println("Invalid choice. Exiting.");
        }
        
        sc.close();
    }
    
    private static void addNewRecipe(Scanner sc, String csvPath) throws Exception {
        System.out.println("\n─ Add New Recipe ─");
        
        System.out.print("Enter recipe name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Recipe name cannot be empty. Aborting.");
            return;
        }
        
        System.out.print("Enter recipe URL: ");
        String url = sc.nextLine().trim();
        
        System.out.print("Enter ingredients (comma-separated, e.g., beef,onion,potato): ");
        String ingredients = sc.nextLine().trim();
        if (ingredients.isEmpty()) {
            System.out.println("Ingredients cannot be empty. Aborting.");
            return;
        }
        
        try {
            DataLoader.saveRecipeToCSV(csvPath, name, url, ingredients);
            System.out.println("\n✓ Recipe '" + name + "' added successfully!");
        } catch (Exception e) {
            System.out.println("\n✗ Error adding recipe: " + e.getMessage());
        }
    }
    
    private static void searchRecipes(Scanner sc, String csvPath) throws Exception {
        List<Menu> menus = DataLoader.loadMenusFromCSV(csvPath);
        
        System.out.println("\nWhat do you have today?");
        String input = sc.nextLine();
        
        // 规范化：小写 + 下划线
        HashSet<String> mySet = new HashSet<>();
        for (String token : input.trim().split("\\s+")) {
            if (!token.isEmpty()) {
                mySet.add(token.trim().toLowerCase().replaceAll("\\s+", "_"));
            }
        }

        // 去重并排序
        List<Menu> sorted = new ArrayList<>(new HashSet<>(menus));
        sorted.sort((a, b) -> {
            int oa = overlap(a, mySet);
            int ob = overlap(b, mySet);
            if (oa != ob) return Integer.compare(ob, oa); // overlap 多者优先

            int missA = missingIngredients(a, mySet).size();
            int missB = missingIngredients(b, mySet).size();
            if (missA != missB) return Integer.compare(missA, missB); // 缺得少者优先

            boolean aExact = oa == mySet.size() && a.getIngredientList().size() == mySet.size();
            boolean bExact = ob == mySet.size() && b.getIngredientList().size() == mySet.size();
            if (aExact != bExact) return aExact ? -1 : 1; // exact 优先

            return a.getName().compareToIgnoreCase(b.getName()); // 名字兜底
        });

        // 输出
        System.out.println("Sounds good!");
        if (mySet.isEmpty()) {
            System.out.println("No ingredients provided.");
            return;
        }

        boolean anyExact = false, anyPartial = false;

        // exact
        for (Menu m : sorted) {
            int ov = overlap(m, mySet);
            boolean exact = ov == mySet.size() && m.getIngredientList().size() == mySet.size();
            if (exact) {
                if (!anyExact) {
                    System.out.println("With exactly " + mySet + ":");
                    anyExact = true;
                }
                System.out.println("  - " + m.getName() + " (URL: " + m.getUrl() + ")");
            }
        }

        // partial（显示缺什么）
        for (Menu m : sorted) {
            int ov = overlap(m, mySet);
            boolean exact = ov == mySet.size() && m.getIngredientList().size() == mySet.size();
            if (ov > 0 && !exact) {
                if (!anyPartial) {
                    System.out.println("With " + mySet + " (partial overlap):");
                    anyPartial = true;
                }
                List<String> miss = missingIngredients(m, mySet);
                System.out.println("  - " + m.getName() + " (URL: " + m.getUrl() + ")  missing: " + miss);
            }
        }

        if (!anyExact && !anyPartial) {
            System.out.println("With " + mySet + " only, I'm sorry there is no menu there for lookups.");
        }
    }

    // ===== 辅助函数 =====

    private static int overlap(Menu menu, Set<String> have) {
        int c = 0;
        for (String ing : menu.getIngredientList()) {
            if (have.contains(ing)) c++;
        }
        return c;
    }

    private static List<String> missingIngredients(Menu menu, Set<String> have) {
        List<String> miss = new ArrayList<>();
        for (String ing : menu.getIngredientList()) {
            if (!have.contains(ing)) miss.add(ing);
        }
        return miss;
    }
}
