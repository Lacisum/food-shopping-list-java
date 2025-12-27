package com.foodshoppinglist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for reading meals files and checking their correctness.
 */
public class MealsFileReader {

    private final String mealsFileName;

    /**
     * Builds a <code>MealsFileReader</code> parameterized with the given meals
     * file name.
     * @param mealsFileName the name of the meals file
     */
    public MealsFileReader(String mealsFileName) {
        this.mealsFileName = mealsFileName;
    }

    /**
     * Loads the file and check its format.
     *
     * @return the content of the file
     * @throws FileFormatException if the file is not correctly formatted
     */
    public Object load() throws FileFormatException {
        Object content = Utils.readYamlFile(mealsFileName, false);
        checkFileFormat(mealsFileName, content);
        return content;
    }

    /**
     * Throws an exception if the content is not correctly formatted.
     *
     * @param fileName the name of the file
     * @param content the content of the file
     * @throws FileFormatException if the content is not correctly formatted
     */
    public static void checkFileFormat(String fileName, Object content) throws FileFormatException {
        String errorPrefix = "Error in " + fileName + ": ";

        // Associates each ingredient with a map that associates units used for
        // this ingredient with one meal (among potentially many) that the unit
        // is used in.
        // This is used to check that each ingredient uses exactly one unit
        // across the whole file.
        Map<String, Map<String, List<String>>> ingredientsUnits = new HashMap<>();

        if (! (content instanceof Map<?, ?>))
            throw new FileFormatException(errorPrefix + "the root element is not a dictionary");
        Map<Object, Object> meals = (Map<Object, Object>) content;
        for (Map.Entry<Object, Object> mealEntry : meals.entrySet()) {

            if (! (mealEntry.getKey() instanceof String mealName))
                throw new FileFormatException(errorPrefix + "the key '" + mealEntry.getKey() + "' is not a string");
            if (! (mealEntry.getValue() instanceof Map<?, ?>))
                throw new FileFormatException(errorPrefix + "the value of the key '" + mealName + "' is not a dictionary");
            Map<Object, Object> ingredients = (Map<Object, Object>) mealEntry.getValue();
            for (Map.Entry<Object, Object> ingredientEntry : ingredients.entrySet()) {

                if (! (ingredientEntry.getKey() instanceof String ingredientName))
                    throw new FileFormatException(
                            errorPrefix + "ingredient '" + ingredientEntry.getKey() + "' in meal '" + mealName + "' is not a string"
                    );
                if (! (ingredientEntry.getValue() instanceof Map<?, ?>))
                    throw new FileFormatException(
                            errorPrefix + "value of ingredient '" + ingredientName + "' in meal '" + mealName + "' is not a dictionary"
                    );
                Map<Object, Object> ingredientAmount = (Map<Object, Object>) ingredientEntry.getValue();
                if (! ingredientAmount.containsKey("quantity"))
                    throw new FileFormatException(
                            errorPrefix + "ingredient '" + ingredientName + "' in meal '" + mealName + "' misses the key 'quantity'"
                    );
                if (! (ingredientAmount.get("quantity") instanceof Integer) && ! (ingredientAmount.get("quantity") instanceof Double))
                    throw new FileFormatException(
                            errorPrefix + "key 'quantity' of ingredient '" + ingredientName + "' in meal '" + mealName + "' is not a number")
                            ;
                if (! ingredientAmount.containsKey("unit"))
                    throw new FileFormatException(
                            errorPrefix + "ingredient '" + ingredientName + "' in meal '" + mealName + "' misses the key 'unit'"
                    );
                if (! (ingredientAmount.get("unit") instanceof String ingredientUnit))
                    throw new FileFormatException(
                            errorPrefix + "key 'unit' of ingredient '" + ingredientName + "' in meal '" + mealName + "' is not a string"
                    );
                if (ingredientAmount.size() != 2)
                    throw new FileFormatException(
                            errorPrefix + "ingredient '" + ingredientName + "' in meal '" + mealName + "' has one or several keys that are not 'quantity' or 'unit'"
                    );

                // this is used to check that each ingredient uses exactly one unit across the whole file
                if (! ingredientsUnits.containsKey(ingredientName))
                    ingredientsUnits.put(ingredientName, new HashMap<String, List<String>>());
                if (! ingredientsUnits.get(ingredientName).containsKey(ingredientUnit))
                    ingredientsUnits.get(ingredientName).put(ingredientUnit, new ArrayList<String>());
                ingredientsUnits.get(ingredientName).get(ingredientUnit).add(mealName);

            }
        }

        // check that each ingredient uses exactly one unit across the whole file
        for (Map.Entry<String, Map<String, List<String>>> ingredientsUnitEntry : ingredientsUnits.entrySet()) {
            String ingredientName = ingredientsUnitEntry.getKey();
            Map<String, List<String>> ingredientUnits = ingredientsUnitEntry.getValue();
            if (ingredientUnits.size() > 1) {
                String exceptionMessage = "ingredient '" + ingredientName + "' uses several units: ";
                exceptionMessage += String.join(
                        ", ",
                        ingredientUnits.entrySet()
                                .stream()
                                .map(unitEntry -> "'" + unitEntry.getKey() + "' in " + unitEntry.getValue())
                                .toList()
                );
                throw new FileFormatException(errorPrefix + exceptionMessage);
            }
        }
    }
}
