package foodie;

import java.awt.*;
import java.net.URI;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * RecipeDetailsDialog - Pop-up window showing detailed recipe information
 */
public class RecipeDetailsDialog extends JDialog {
    private static final Color ORANGE_PRIMARY = new Color(255, 140, 0);
    private static final Color ORANGE_LIGHT = new Color(255, 165, 0);
    private static final Color DARK_BG = new Color(245, 245, 245);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);
    private static final Color GREEN = new Color(76, 175, 80);
    private static final Color RED = new Color(244, 67, 54);
    
    public RecipeDetailsDialog(JFrame parent, Menu menu, Set<String> mySet) {
        super(parent, "Recipe Details", true);
        setSize(600, 500);
        setLocationRelativeTo(parent);
        setResizable(true);
        
        buildUI(menu, mySet);
    }
    
    private void buildUI(Menu menu, Set<String> mySet) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Header
        JPanel headerPanel = createHeaderPanel(menu);
        
        // Content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(DARK_BG);
        
        // Ingredients section
        JPanel ingredientsPanel = createIngredientsPanel(menu, mySet);
        
        // URL section
        JPanel urlPanel = createUrlPanel(menu);
        
        // Buttons
        JPanel buttonPanel = createButtonPanel();
        
        contentPanel.add(ingredientsPanel, BorderLayout.CENTER);
        contentPanel.add(urlPanel, BorderLayout.SOUTH);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel(Menu menu) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ORANGE_PRIMARY);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel(menu.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        panel.add(titleLabel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createIngredientsPanel(Menu menu, Set<String> mySet) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARK_BG);
        panel.setBorder(new EmptyBorder(15, 0, 15, 0));
        
        JLabel titleLabel = new JLabel("Ingredients:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(TEXT_COLOR);
        
        JPanel ingredientsContainer = new JPanel();
        ingredientsContainer.setLayout(new BoxLayout(ingredientsContainer, BoxLayout.Y_AXIS));
        ingredientsContainer.setBackground(Color.WHITE);
        ingredientsContainer.setBorder(BorderFactory.createLineBorder(ORANGE_LIGHT, 1));
        ingredientsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        for (String ing : menu.getIngredientList()) {
            JPanel ingPanel = createIngredientItemPanel(ing, mySet.contains(ing));
            ingredientsContainer.add(ingPanel);
        }
        
        JScrollPane scrollPane = new JScrollPane(ingredientsContainer);
        scrollPane.setPreferredSize(new Dimension(550, 250));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createIngredientItemPanel(String ingredient, boolean have) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(8, 0, 8, 0));
        
        String icon = have ? "✓" : "✗";
        Color color = have ? GREEN : RED;
        
        JLabel statusLabel = new JLabel(icon);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(color);
        statusLabel.setPreferredSize(new Dimension(30, 20));
        
        JLabel ingLabel = new JLabel(ingredient);
        ingLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        ingLabel.setForeground(TEXT_COLOR);
        
        JLabel statusTextLabel = new JLabel(have ? "Have" : "Missing");
        statusTextLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        statusTextLabel.setForeground(color);
        statusTextLabel.setPreferredSize(new Dimension(60, 20));
        
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(Color.WHITE);
        textPanel.add(ingLabel, BorderLayout.CENTER);
        textPanel.add(statusTextLabel, BorderLayout.EAST);
        
        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(textPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createUrlPanel(Menu menu) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ORANGE_LIGHT, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel urlLabel = new JLabel("Recipe URL:");
        urlLabel.setFont(new Font("Arial", Font.BOLD, 12));
        urlLabel.setForeground(TEXT_COLOR);
        
        JTextField urlField = new JTextField(menu.getUrl());
        urlField.setFont(new Font("Arial", Font.PLAIN, 11));
        urlField.setEditable(false);
        urlField.setBackground(DARK_BG);
        urlField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JButton openButton = new JButton("Open in Browser");
        openButton.setFont(new Font("Arial", Font.BOLD, 11));
        openButton.setBackground(ORANGE_PRIMARY);
        openButton.setForeground(Color.WHITE);
        openButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        openButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        openButton.addActionListener(e -> openURL(menu.getUrl()));
        
        panel.add(urlLabel, BorderLayout.WEST);
        panel.add(urlField, BorderLayout.CENTER);
        panel.add(openButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(DARK_BG);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 12));
        closeButton.setBackground(ORANGE_PRIMARY);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        
        panel.add(closeButton);
        
        return panel;
    }
    
    private void openURL(String url) {
        if (url == null || url.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No URL available", 
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not open URL: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
