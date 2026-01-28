package foodie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 从 CSV/TSV 读取菜单数据并创建 Menu（会自动更新 Ingredient → foodlist）。
 * - 自动去除 UTF-8 BOM
 * - 自动检测分隔符：逗号(,), 分号(;), 制表符(\t)
 * - 表头大小写不敏感：Menu_name, URL, Ingredients
 * - Ingredients：英文逗号分隔
 * - 清理括号注释 "(...)"；短语转下划线；全小写
 */
public class DataLoader {

    /** 读取 CSV/TSV，返回创建好的 Menu 列表 */
    public static List<Menu> loadMenusFromCSV(String csvPath) throws IOException {
        List<Menu> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String headerLine = br.readLine();
            if (headerLine == null) return result;

            headerLine = stripBOM(headerLine);

            char delimiter = detectDelimiter(headerLine);
            List<String> headers = parseDelimitedLine(headerLine, delimiter);
            Map<String, Integer> idx = headerIndex(headers);

            if (!idx.containsKey("menu_name") || !idx.containsKey("url") || !idx.containsKey("ingredients")) {
                throw new IllegalArgumentException("CSV header must contain Menu_name, URL, Ingredients");
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                line = stripBOM(line); 
                List<String> cols = parseDelimitedLine(line, delimiter);

                String rawName = getSafe(cols, idx.get("menu_name"));
                String rawUrl  = getSafe(cols, idx.get("url"));
                String rawIngs = getSafe(cols, idx.get("ingredients"));

                if (isBlank(rawName) && isBlank(rawUrl) && isBlank(rawIngs)) continue;

                String menuName = (rawName == null) ? "" : rawName.trim();
                String url = (rawUrl == null) ? "" : rawUrl.trim();

                List<String> ingList = new ArrayList<>();
                if (!isBlank(rawIngs)) {
                    String cleaned = rawIngs.replaceAll("\\(.*?\\)", ""); 
                    String[] parts = cleaned.split(",");
                    for (String p : parts) {
                        String v = normalizeIngredient(p);
                        if (!v.isEmpty()) ingList.add(v);
                    }
                }

                Menu m = new Menu(menuName, url, new ArrayList<>(ingList));
                result.add(m);
            }
        }
        return result;
    }

    // ======= 工具方法 =======

    private static String stripBOM(String s) {
        if (s == null) return null;
        if (!s.isEmpty() && s.charAt(0) == '\uFEFF') {
            return s.substring(1);
        }
        return s;
    }

    private static char detectDelimiter(String headerLine) {
        int commas = count(headerLine, ',');
        int semis  = count(headerLine, ';');
        int tabs   = count(headerLine, '\t');
        if (commas >= semis && commas >= tabs && commas > 0) return ',';
        if (semis  >= commas && semis  >= tabs && semis  > 0) return ';';
        if (tabs   >= commas && tabs   >= semis && tabs   > 0) return '\t';
        return ','; 
    }

    private static int count(String s, char c) {
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) n++;
        }
        return n;
    }

    private static List<String> parseDelimitedLine(String line, char delimiter) {
        List<String> out = new ArrayList<>();
        if (line == null) return out;

        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (inQuotes) {
                if (ch == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"'); 
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(ch);
                }
            } else {
                if (ch == '"') {
                    inQuotes = true;
                } else if (ch == delimiter) {
                    out.add(cur.toString());
                    cur.setLength(0);
                } else {
                    cur.append(ch);
                }
            }
        }
        out.add(cur.toString());
        return out;
    }

    private static Map<String, Integer> headerIndex(List<String> headers) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            String h = headers.get(i);
            if (h == null) continue;
            String k = stripBOM(h).trim().toLowerCase();
            map.put(k, i);
        }
        return map;
    }

    private static String getSafe(List<String> cols, int i) {
        if (i < 0 || i >= cols.size()) return "";
        return cols.get(i);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String normalizeIngredient(String s) {
        if (s == null) return "";
        String t = s.trim().toLowerCase();
        if (t.isEmpty()) return "";
        t = t.replaceAll("\\s+", "_");
        return t;
    }

    /**
     * Save a new recipe to the CSV file
     * @param csvPath path to the CSV file
     * @param name recipe name
     * @param url recipe URL
     * @param ingredients comma-separated ingredient list
     * @throws IOException if file operation fails
     */
    public static void saveRecipeToCSV(String csvPath, String name, String url, String ingredients) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvPath, true))) {
            // Normalize ingredients
            String[] parts = ingredients.split(",");
            StringBuilder normalizedIngs = new StringBuilder();
            for (int i = 0; i < parts.length; i++) {
                String ing = normalizeIngredient(parts[i]);
                if (!ing.isEmpty()) {
                    if (i > 0 && !normalizedIngs.toString().isEmpty()) {
                        normalizedIngs.append(",");
                    }
                    normalizedIngs.append(ing);
                }
            }
            
            // Escape quotes in fields
            String escapedName = name.replace("\"", "\"\"");
            String escapedUrl = url.replace("\"", "\"\"");
            String escapedIngs = normalizedIngs.toString().replace("\"", "\"\"");
            
            // Write CSV line
            String line = String.format("\"%s\",\"%s\",\"%s\"%n", escapedName, escapedUrl, escapedIngs);
            bw.write(line);
            bw.flush();
        }
    }
}
