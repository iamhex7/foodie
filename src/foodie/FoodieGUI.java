package foodie;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * FoodieGUI - Main application window with orange theme
 */
public class FoodieGUI extends JFrame {
    private static final String CSV_PATH = "C:\\Users\\Hex\\OneDrive\\Archived\\foodie\\menuaug.csv";
    
    // Orange theme colors
    private static final Color ORANGE_PRIMARY = new Color(255, 140, 0);      // Dark orange
    private static final Color ORANGE_LIGHT = new Color(255, 165, 0);        // Light orange
    private static final Color ORANGE_VERY_LIGHT = new Color(255, 200, 100); // Very light orange
    private static final Color DARK_BG = new Color(245, 245, 245);           // Light gray background
    private static final Color TEXT_COLOR = new Color(51, 51, 51);           // Dark gray text
    
    private List<Menu> menus;
    private JTextField ingredientInput;
    private JPanel resultsPanel;
    private JScrollPane resultsScrollPane;
    private JLabel statusLabel;
    
    public FoodieGUI() {
        setTitle("Foodie - Recipe Finder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Load menus from CSV
        try {
            menus = DataLoader.loadMenusFromCSV(CSV_PATH);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading CSV: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            menus = new ArrayList<>();
        }
        
        // Build UI
        buildUI();
    }
    
    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Top section - Title and info
        JPanel headerPanel = createHeaderPanel();
        
        // Middle section - Input
        JPanel inputPanel = createInputPanel();
        
        // Bottom section - Results
        JPanel resultsContainer = createResultsPanel();
        
        // Status bar
        JPanel statusPanel = createStatusPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(resultsContainer, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ORANGE_PRIMARY);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("🍕 Foodie");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Find recipes based on ingredients you have");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.WHITE);
        
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(ORANGE_PRIMARY);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        panel.add(textPanel, BorderLayout.WEST);
        return panel;
    }
    
    private JPanel createInputPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JPanel inputContainer = new JPanel(new BorderLayout(10, 0));
        inputContainer.setBackground(Color.WHITE);
        inputContainer.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel label = new JLabel("Enter ingredients (space-separated):");
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(TEXT_COLOR);
        
        ingredientInput = new JTextField();
        ingredientInput.setFont(new Font("Arial", Font.PLAIN, 14));
        ingredientInput.setBorder(BorderFactory.createLineBorder(ORANGE_LIGHT, 2));
        ingredientInput.setPreferredSize(new Dimension(400, 40));
        
        // Add action listener for Enter key
        ingredientInput.addActionListener(e -> performSearch());
        
        JButton searchButton = createOrangeButton("🔍 Search");
        searchButton.addActionListener(e -> performSearch());
        
        JButton clearButton = createOrangeButton("Clear");
        clearButton.addActionListener(e -> {
            ingredientInput.setText("");
            resultsPanel.removeAll();
            resultsPanel.revalidate();
            resultsPanel.repaint();
            statusLabel.setText("Enter ingredients and click Search");
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);
        
        JPanel inputContent = new JPanel(new BorderLayout(10, 0));
        inputContent.setBackground(Color.WHITE);
        inputContent.add(label, BorderLayout.NORTH);
        inputContent.add(ingredientInput, BorderLayout.CENTER);
        inputContent.add(buttonPanel, BorderLayout.SOUTH);
        
        inputContainer.add(inputContent, BorderLayout.CENTER);
        
        mainPanel.add(inputContainer, BorderLayout.NORTH);
        return mainPanel;
    }
    
    private JPanel createResultsPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(DARK_BG);
        container.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JLabel resultTitle = new JLabel("Results:");
        resultTitle.setFont(new Font("Arial", Font.BOLD, 14));
        resultTitle.setForeground(TEXT_COLOR);
        resultTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(DARK_BG);
        
        resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setBackground(DARK_BG);
        resultsScrollPane.getViewport().setBackground(DARK_BG);
        resultsScrollPane.setBorder(BorderFactory.createLineBorder(ORANGE_LIGHT, 1));
        
        container.add(resultTitle, BorderLayout.NORTH);
        container.add(resultsScrollPane, BorderLayout.CENTER);
        
        return container;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(ORANGE_LIGHT, 1));
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        statusLabel = new JLabel("Enter ingredients and click Search");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(TEXT_COLOR);
        
        panel.add(statusLabel, BorderLayout.WEST);
        return panel;
    }
    
    private JButton createOrangeButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(ORANGE_PRIMARY);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ORANGE_LIGHT);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ORANGE_PRIMARY);
            }
        });
        
        return button;
    }
    
    private void performSearch() {
        String input = ingredientInput.getText().trim();
        
        if (input.isEmpty()) {
            statusLabel.setText("Please enter at least one ingredient");
            return;
        }
        
        // Normalize ingredients
        Set<String> mySet = new HashSet<>();
        for (String token : input.split("\\s+")) {
            if (!token.isEmpty()) {
                mySet.add(token.toLowerCase().replaceAll("\\s+", "_"));
            }
        }
        
        // Sort menus
        List<Menu> sorted = new ArrayList<>(new HashSet<>(menus));
        sorted.sort((a, b) -> {
            int oa = overlap(a, mySet);
            int ob = overlap(b, mySet);
            if (oa != ob) return Integer.compare(ob, oa);
            
            int missA = missingIngredients(a, mySet).size();
            int missB = missingIngredients(b, mySet).size();
            if (missA != missB) return Integer.compare(missA, missB);
            
            boolean aExact = oa == mySet.size() && a.getIngredientList().size() == mySet.size();
            boolean bExact = ob == mySet.size() && b.getIngredientList().size() == mySet.size();
            if (aExact != bExact) return aExact ? -1 : 1;
            
            return a.getName().compareToIgnoreCase(b.getName());
        });
        
        // Display results
        displayResults(sorted, mySet);
    }
    
    private void displayResults(List<Menu> sorted, Set<String> mySet) {
        resultsPanel.removeAll();
        
        if (sorted.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No recipes found");
            noResultsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            noResultsLabel.setForeground(TEXT_COLOR);
            resultsPanel.add(noResultsLabel);
            statusLabel.setText("No recipes found for the given ingredients");
            resultsPanel.revalidate();
            resultsPanel.repaint();
            return;
        }
        
        int exactCount = 0, partialCount = 0;
        
        // Exact matches section
        for (Menu m : sorted) {
            int ov = overlap(m, mySet);
            boolean exact = ov == mySet.size() && m.getIngredientList().size() == mySet.size();
            if (exact) {
                if (exactCount == 0) {
                    JLabel sectionLabel = createSectionLabel("✓ Exact Matches");
                    resultsPanel.add(sectionLabel);
                }
                resultsPanel.add(createRecipeCard(m, mySet, true));
                exactCount++;
            }
        }
        
        // Partial matches section
        for (Menu m : sorted) {
            int ov = overlap(m, mySet);
            boolean exact = ov == mySet.size() && m.getIngredientList().size() == mySet.size();
            if (ov > 0 && !exact) {
                if (partialCount == 0) {
                    JLabel sectionLabel = createSectionLabel("~ Partial Matches");
                    resultsPanel.add(sectionLabel);
                }
                resultsPanel.add(createRecipeCard(m, mySet, false));
                partialCount++;
            }
        }
        
        if (exactCount == 0 && partialCount == 0) {
            JLabel noMatchLabel = new JLabel("No recipes found with given ingredients");
            noMatchLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            noMatchLabel.setForeground(TEXT_COLOR);
            resultsPanel.add(noMatchLabel);
            statusLabel.setText("No matching recipes found");
        } else {
            statusLabel.setText("Found " + exactCount + " exact and " + partialCount + " partial matches");
        }
        
        resultsPanel.add(Box.createVerticalGlue());
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
    
    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(ORANGE_PRIMARY);
        label.setBorder(new EmptyBorder(15, 0, 10, 0));
        return label;
    }
    
    private JPanel createRecipeCard(Menu menu, Set<String> mySet, boolean isExact) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ORANGE_VERY_LIGHT, 2),
                new EmptyBorder(12, 12, 12, 12)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setPreferredSize(new Dimension(900, 100));
        
        // Left side - Recipe info
        JPanel infoPanel = new JPanel(new BorderLayout(0, 5));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(menu.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(TEXT_COLOR);
        
        int overlap = overlap(menu, mySet);
        List<String> missing = missingIngredients(menu, mySet);
        
        JLabel matchLabel = new JLabel(
                String.format("Matching: %d/%d ingredients | Missing: %d", 
                        overlap, menu.getIngredientList().size(), missing.size())
        );
        matchLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        matchLabel.setForeground(ORANGE_PRIMARY);
        
        String missingText = missing.isEmpty() ? "No missing ingredients" : 
                "Missing: " + String.join(", ", missing);
        JLabel missingLabel = new JLabel(missingText);
        missingLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        missingLabel.setForeground(new Color(100, 100, 100));
        
        infoPanel.add(nameLabel, BorderLayout.NORTH);
        infoPanel.add(matchLabel, BorderLayout.CENTER);
        infoPanel.add(missingLabel, BorderLayout.SOUTH);
        
        // Right side - Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton viewButton = new JButton("View Recipe");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 11));
        viewButton.setBackground(ORANGE_PRIMARY);
        viewButton.setForeground(Color.WHITE);
        viewButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        viewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewButton.addActionListener(e -> showRecipeDetails(menu, mySet));
        
        buttonPanel.add(viewButton);
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        
        return card;
    }
    
    private void showRecipeDetails(Menu menu, Set<String> mySet) {
        RecipeDetailsDialog dialog = new RecipeDetailsDialog(this, menu, mySet);
        dialog.setVisible(true);
    }
    
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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FoodieGUI frame = new FoodieGUI();
            frame.setVisible(true);
        });
    }
}
