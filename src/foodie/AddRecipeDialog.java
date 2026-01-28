package foodie;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Dialog for adding a new recipe through GUI
 */
public class AddRecipeDialog extends JDialog {
    private static final Color ORANGE_PRIMARY = new Color(255, 140, 0);
    private static final Color ORANGE_LIGHT = new Color(255, 165, 0);
    private static final Color TEXT_COLOR = new Color(51, 51, 51);
    
    private JTextField nameField;
    private JTextField urlField;
    private JTextField ingredientsField;
    private boolean confirmed = false;

    public AddRecipeDialog(JFrame parent) {
        super(parent, "Add New Recipe", true);
        setSize(500, 350);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        buildUI();
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Add New Recipe");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(ORANGE_PRIMARY);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Recipe Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel nameLabel = new JLabel("Recipe Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setForeground(TEXT_COLOR);
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 12));
        nameField.setBorder(BorderFactory.createLineBorder(ORANGE_LIGHT, 1));
        formPanel.add(nameField, gbc);

        // URL
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel urlLabel = new JLabel("Recipe URL:");
        urlLabel.setFont(new Font("Arial", Font.BOLD, 12));
        urlLabel.setForeground(TEXT_COLOR);
        formPanel.add(urlLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        urlField = new JTextField(20);
        urlField.setFont(new Font("Arial", Font.PLAIN, 12));
        urlField.setBorder(BorderFactory.createLineBorder(ORANGE_LIGHT, 1));
        formPanel.add(urlField, gbc);

        // Ingredients
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel ingredientsLabel = new JLabel("Ingredients (comma-separated):");
        ingredientsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        ingredientsLabel.setForeground(TEXT_COLOR);
        formPanel.add(ingredientsLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        ingredientsField = new JTextField(20);
        ingredientsField.setFont(new Font("Arial", Font.PLAIN, 12));
        ingredientsField.setBorder(BorderFactory.createLineBorder(ORANGE_LIGHT, 1));
        JScrollPane ingredientsScroll = new JScrollPane(ingredientsField);
        formPanel.add(ingredientsScroll, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton addButton = new JButton("Add Recipe");
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        addButton.setBackground(ORANGE_PRIMARY);
        addButton.setForeground(Color.WHITE);
        addButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> onAdd());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setBackground(new Color(200, 200, 200));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> onCancel());

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        // Assemble main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void onAdd() {
        String name = nameField.getText().trim();
        String url = urlField.getText().trim();
        String ingredients = ingredientsField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a recipe name", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (ingredients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter at least one ingredient", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        confirmed = true;
        dispose();
    }

    private void onCancel() {
        confirmed = false;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getRecipeName() {
        return nameField.getText().trim();
    }

    public String getRecipeUrl() {
        return urlField.getText().trim();
    }

    public String getIngredients() {
        return ingredientsField.getText().trim();
    }
}
