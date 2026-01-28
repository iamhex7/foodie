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

        List<Menu> menus = DataLoader.loadMenusFromCSV(csvPath);

        // 用户输入
        Scanner sc = new Scanner(System.in);
        System.out.println("Terminal: What do you have today?");
        String input = sc.nextLine();
        sc.close();

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
