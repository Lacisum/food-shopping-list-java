package com.foodshoppinglist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealsHandler {

    private final Map<String, Map<String, IngredientAmount>> meals;
    private List<String> selectedMealsNames;




    /**
     * Builds a <code>MealsHandler</code>
     *
     * @param mealsFileContent the content of the meals file
     */
    public MealsHandler(Object mealsFileContent) {
        this.meals = convertToStructuredObject(mealsFileContent);
    }




    /**
     * Converts the content to a structured object, with a robust and practical
     * type.
     * The returned object is a new object.
     *
     * @param content the content of the meals file as it was delivered by the
     *                file reader
     * @return a map associating each meal name to a map associating each
     * ingredient to its amount
     */
    private static Map<String, Map<String, IngredientAmount>> convertToStructuredObject(Object content) {
        Map<String, Map<String, IngredientAmount>> meals = new HashMap<>();
        Map<String, Map<String, Map<String, Object>>> contentWithAType = (Map<String, Map<String, Map<String, Object>>>) content;

        for (Map.Entry<String, Map<String, Map<String, Object>>> mealEntry : contentWithAType.entrySet()) {
            String mealName = mealEntry.getKey();
            Map<String, Map<String, Object>> mealIngredients = mealEntry.getValue();
            // fill the result
            meals.put(mealName, new HashMap<>());

            for (Map.Entry<String, Map<String, Object>> ingredientEntry : mealIngredients.entrySet()) {
                String ingredientName = ingredientEntry.getKey();
                Map<String, Object> ingredientQuantity = ingredientEntry.getValue();
                float quantity = ((Number) ingredientQuantity.get("quantity")).floatValue();
                String unit = (String) ingredientQuantity.get("unit");
                // fill the result
                meals.get(mealName).put(ingredientName, new IngredientAmount(quantity, unit));
            }
        }
        return meals;
    }

    /**
     * Returns the list of the names of the available meals, in lexicographic
     * order.
     *
     * @return the list of the names of the available meals, in lexicographic
     * order.
     */
    public List<String> getAvailableMealsNames() {
        return meals.keySet().stream().sorted().toList();
    }

    /**
     * Sets the <code>selectedMealsNames</code> attribute to the given value.
     *
     * @param selectedMealsNames the value to affect to the
     *                           <code>selectedMealsNames</code> attribute
     */
    public void setSelectedMealsNames(List<String> selectedMealsNames) {
        this.selectedMealsNames = selectedMealsNames;
    }
}
