# 🍕 Foodie: Smart Recipe Finder

**Foodie** is a Java-based desktop application designed to solve the "what should I cook with what I have?" dilemma. By inputting the ingredients currently in your pantry, Foodie intelligently suggests recipes from your database, prioritizing matches that maximize your current supplies while minimizing extra trips to the grocery store.


## ✨ Key Features

* **Smart Matching Algorithm**: Recipes are ranked using a multi-tier priority system:
    1.  **Highest Overlap**: Recipes using the most of your available ingredients come first.
    2.  **Minimum Missing**: Among similar overlaps, recipes requiring the fewest additional items are prioritized.
    3.  **Exact Matches**: Highlights recipes that perfectly match your ingredient list.
* **Dual Interface**:
    * **GUI Mode**: A modern, orange-themed Java Swing interface for a user-friendly experience.
    * **CLI Mode**: A lightweight terminal version for quick lookups.
* **Robust Data Loading**: Automatically parses CSV/TSV files with support for UTF-8 BOM, different delimiters, and intelligent ingredient normalization (lowercase, phrase-to-underscore conversion).
* **Detailed Insights**: View exactly which ingredients you have and what you are missing for every recipe, with direct links to full instructions.


## 🛠️ Tech Stack

* **Language**: Java
* **Framework**: Java Swing (GUI), AWT
* **Data Format**: CSV / TSV
* **Architecture**: Object-Oriented Design with bi-directional mapping between Recipes and Ingredients.


## 🚀 Getting Started

### Prerequisites
* Java JDK 8 or higher.
* A CSV file containing your recipe data (Columns required: `Menu_name`, `URL`, `Ingredients`).

### Running the GUI
1. Compile the source files.
2. Run `FoodieGUI.java`.
3. Enter your ingredients separated by spaces (e.g., `beef onion potato`) and hit **Search**.

### Running the CLI
1. Run `FoodieApp.java`.
2. Follow the terminal prompt: `Terminal: What do you have today?`.


## 📊 Project Structure

* `DataLoader.java`: The engine that cleans and imports recipe data.
* `FoodieGUI.java` & `RecipeDetailsDialog.java`: The frontend components providing a rich visual experience.
* `Menu.java` & `Ingredient.java`: Core data models handling the relationship between meals and items.
* `FoodieApp.java`: The entry point for the command-line version.


## 🎨 UI Preview

The GUI features a professional **Orange Theme** designed for clarity:
* **Recipe Cards**: Display match ratios (e.g., 3/5 ingredients) and missing items.
* **Interactive Dialogs**: Checklists showing "Have" vs "Missing" status for every ingredient.


## 📜 License
This project is open-source. Feel free to use and modify it for your personal use!
