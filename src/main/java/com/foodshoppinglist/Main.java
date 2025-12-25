package com.foodshoppinglist;

import java.util.List;

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
        MealsHandler mealsHandler = null;
        try {
            frontendHandler = new FrontendHandler(TEXT_ASSETS_FILE_NAME);
            mealsHandler = new MealsHandler(mealsFile);
        } catch (FileFormatException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        List<String> availableMealsNames = mealsHandler.getAvailableMealsNames();
        frontendHandler.printAvailableMeals(availableMealsNames);
    }
}
