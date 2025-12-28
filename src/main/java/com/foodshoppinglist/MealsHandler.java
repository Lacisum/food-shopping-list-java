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

    /**
     * Computes the totals of each ingredient needed for cooking the selected
     * meals.
     *
     * @return a map that associates the names of the ingredients needed for the
     * selected meals to their needed amount (quantity and unit).
     */
    public Map<String, IngredientAmount> getIngredientsTotals() {
        // do the computation with a map of map (i.e. without IngredientAmount, hence why the suffix "WithOnlyMap")
        Map<String, Map<String, Object>> ingredientsTotalsWithOnlyMaps = new HashMap<>();
        for (String selectedMealName : selectedMealsNames) {
            for (Map.Entry<String, IngredientAmount> ingredientEntry : meals.get(selectedMealName).entrySet()) {
                String ingredientName = ingredientEntry.getKey();
                IngredientAmount ingredientAmount = ingredientEntry.getValue();
                ingredientsTotalsWithOnlyMaps.merge(
                        ingredientName,
                        Map.of(
                                "quantity", ingredientAmount.quantity(),
                                "unit", ingredientAmount.unit()
                        ),
                        (oldAmount, newAmount) -> Map.of(
                                "quantity", (Float) oldAmount.get("quantity") + (Float) newAmount.get("quantity"),
                                "unit", oldAmount.get("unit")
                        )
                );
            }
        }

        // convert Map<String, Object> to IngredientAmount
        Map<String, IngredientAmount> ingredientsTotals = new HashMap<>();
        for (Map.Entry<String, Map<String, Object>> ingredientEntry : ingredientsTotalsWithOnlyMaps.entrySet()) {
            String ingredientName = ingredientEntry.getKey();
            Map<String, Object> ingredientAmount = ingredientEntry.getValue();
            ingredientsTotals.put(
                    ingredientName,
                    new IngredientAmount(
                            (float) ingredientAmount.get("quantity"),
                            (String) ingredientAmount.get("unit")
                    )
            );
        }
        return ingredientsTotals;
    }
}
