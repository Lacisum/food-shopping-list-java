package com.foodshoppinglist;

import java.util.List;
import java.util.Map;

/**
 * The main class of the program.
 */
public class Main {

    /** The usage of this program. */
    private static final String USAGE = "Usage: java -jar target/food-shopping-list.jar <meals-file>";
    /** The name of the text assets file */
    private static final String TEXT_ASSETS_FILE_NAME = "texts.yaml";

    static void main(String[] args) {
        // check the number of arguments
        if (args.length != 1) {
            System.err.println(USAGE);
            System.exit(1);
        }
        final String mealsFile = args[0];

        // load text assets and meals
        FrontendHandler frontendHandler = null;
        Object mealsFileContent = null;
        try {
            frontendHandler = new FrontendHandler(TEXT_ASSETS_FILE_NAME);
            mealsFileContent = new MealsFileReader(mealsFile).load();
        } catch (FileFormatException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        MealsHandler mealsHandler = new MealsHandler(mealsFileContent);
        List<String> availableMealsNames = mealsHandler.getAvailableMealsNames();
        frontendHandler.printAvailableMeals(availableMealsNames);

        List<String> selectedMealsNames = frontendHandler.getSelectedMealsFromUserInput(availableMealsNames);
        mealsHandler.setSelectedMealsNames(selectedMealsNames);

        if (selectedMealsNames.isEmpty()) {
            frontendHandler.printYouDidntChooseAnyMeal();
            return;
        }

        frontendHandler.printSelectedMeals(selectedMealsNames);

        Map<String, IngredientAmount> ingredientsTotals = mealsHandler.getIngredientsTotals();
        frontendHandler.printIngredientsTotals(ingredientsTotals);
    }
}
