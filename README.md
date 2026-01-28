# 🍕 Foodie: Smart Recipe Finder

[![Java Version](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.oracle.com/java/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**Foodie** is an intelligent recipe recommendation engine designed to bridge the gap between your pantry and your plate. By inputting the ingredients you have on hand, Foodie helps you reduce food waste and discover new meals by ranking recipes based on availability and convenience.

## 🌟 Key Features

* **Smart Matching Logic**: Uses a multi-tier priority algorithm to ensure you find the most "cookable" meals first.
<img width="600" height="700" alt="image" src="https://github.com/user-attachments/assets/d9d40461-d8f1-4ec4-9f5e-2b6bdd194cb5" />
<img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/dafec884-1660-44c0-9115-9529fb753668" />


* **Dual Interface Support**:
    * **GUI**: A modern, custom-themed Java Swing interface for visual users.
    * **CLI**: A lightning-fast command-line interface for quick terminal lookups.

<img width="500" height="350" alt="image" src="https://github.com/user-attachments/assets/296f2854-886b-44fd-b565-a50e1022c3d4" />

* **Dynamic Recipe Management**: Add new recipes directly through the application interface; they are automatically persisted to the database.
* **Robust CSV Engine**: Handles complex data imports including UTF-8 BOM, multiple delimiters (`,`, `;`, `\t`), and quoted strings.
* **Deep Ingredient Mapping**: Features bi-directional mapping between ingredients and menus for optimized searching.


## 🧠 How It Works: The Algorithm

Foodie doesn't just look for keywords; it ranks results using a **Three-Tier Priority System**:

1.  **Maximum Overlap**: Recipes that utilize the highest number of your current ingredients are prioritized.
2.  **Minimum Missing**: If overlap is equal, the algorithm favors recipes requiring the fewest additional purchases.
3.  **Exact Matches**: Perfect matches (where you have every ingredient required and nothing more) are highlighted as "Exact Matches".


## 🛠️ Tech Stack

* **Core Language**: Java
* **GUI Framework**: Java Swing & AWT (featuring a custom Orange Theme)
* **Data Structures**: 
    * `HashSet`: For $O(1)$ ingredient matching and uniqueness.
    * `HashMap`: To maintain global ingredient-to-menu associations.
* **Storage**: CSV-based flat-file database.


## 🚀 Getting Started

### Prerequisites
* **Java Development Kit (JDK) 8** or higher.
* A CSV file named `menuaug.csv` in the project path.

### Installation
1.  Clone the repository:
    ```bash
    git clone [https://github.com/yourusername/foodie.git](https://github.com/yourusername/foodie.git)
    ```
2.  Navigate to the directory:
    ```bash
    cd foodie
    ```

### Running the Application
* **For GUI Mode**: Run `FoodieGUI.java`.
* **For CLI Mode**: Run `FoodieApp.java`.


## 📊 Project Structure

* `FoodieGUI.java`: The main visual window and event controller.
* `FoodieApp.java`: Terminal-based entry point.
* `DataLoader.java`: The I/O engine handling CSV parsing and recipe persistence.
* `Menu.java` & `Ingredient.java`: Core data models establishing the relationship between meals and items.
* `RecipeDetailsDialog.java`: Detailed view showing "Have" vs "Missing" status for every ingredient.
* `AddRecipeDialog.java`: Interface for extending the recipe database via GUI.

## 📝 Recipe Data Format

The application expects a CSV format with three specific columns: `Menu_name`, `URL`, and `Ingredients`.

**Example Entry:**
```csv
Tomato_Egg_Fried_Rice, [https://example.com/recipe](https://example.com/recipe), tomato,egg,rice
