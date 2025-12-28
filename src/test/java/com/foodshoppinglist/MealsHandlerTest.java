package com.foodshoppinglist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MealsHandlerTest {

    // a MealsHandler that contains several meals
    private MealsHandler mealsHandlerWithSeveralMeals;




    @BeforeEach
    void initBeforeEach() {
        mealsHandlerWithSeveralMeals = new MealsHandler(
                Map.of(
                        "risotto",
                        Map.of(
                                "riz", Map.of(
                                        "quantity", 500,
                                        "unit", "g"),
                                "sauce provençale", Map.of(
                                        "quantity", 1,
                                        "unit", "L")
                        ),
                        "tofu basquaise",
                        Map.of(
                                "riz", Map.of(
                                        "quantity", 500,
                                        "unit", "g"),
                                "tofu", Map.of(
                                        "quantity", 400,
                                        "unit", "g")
                        ),
                        "patates sautées",
                        Map.of(
                                "patates", Map.of(
                                        "quantity", 1.5f,
                                        "unit", "kg"),
                                "tofu", Map.of(
                                        "quantity", 400,
                                        "unit", "g")
                        )
                )
        );
    }




    @Test
    void method_getIngredientsTotals_returnsNoIngredientAmount_whenNoMealWasSelected() {
        // test with meals handler with no meals
        MealsHandler mealsHandlerWithNoMeals = new MealsHandler(Map.of());
        mealsHandlerWithNoMeals.setSelectedMealsNames(List.of());
        assertEquals(Map.of(), mealsHandlerWithNoMeals.getIngredientsTotals());

        // test with meals handler with several meals
        mealsHandlerWithSeveralMeals.setSelectedMealsNames(List.of());
        assertEquals(Map.of(), mealsHandlerWithSeveralMeals.getIngredientsTotals());
    }




    @Test
    void method_getIngredientsTotals_returnsTheCorrectIngredients_whenOneMealWasSelected() {
        mealsHandlerWithSeveralMeals.setSelectedMealsNames(List.of("risotto"));
        assertEquals(
                Map.of(
                        "riz",
                        new IngredientAmount(500, "g"),
                        "sauce provençale",
                        new IngredientAmount(1, "L")
                ),
                mealsHandlerWithSeveralMeals.getIngredientsTotals()
        );

        mealsHandlerWithSeveralMeals.setSelectedMealsNames(List.of("tofu basquaise"));
        assertEquals(
                Map.of(
                        "riz",
                        new IngredientAmount(500, "g"),
                        "tofu",
                        new IngredientAmount(400, "g")
                ),
                mealsHandlerWithSeveralMeals.getIngredientsTotals()
        );

        mealsHandlerWithSeveralMeals.setSelectedMealsNames(List.of("patates sautées"));
        assertEquals(
                Map.of(
                        "tofu",
                        new IngredientAmount(400, "g"),
                        "patates",
                        new IngredientAmount(1.5f, "kg")
                ),
                mealsHandlerWithSeveralMeals.getIngredientsTotals()
        );
    }




    @Test
    void method_getIngredientsTotals_returnsTheCorrectIngredients_whenSeveralMealsWereSelected() {
        mealsHandlerWithSeveralMeals.setSelectedMealsNames(List.of("risotto", "tofu basquaise"));
        assertEquals(
                Map.of(
                        "riz",
                        new IngredientAmount(1000, "g"),
                        "sauce provençale",
                        new IngredientAmount(1, "L"),
                        "tofu",
                        new IngredientAmount(400, "g")
                ),
                mealsHandlerWithSeveralMeals.getIngredientsTotals()
        );

        mealsHandlerWithSeveralMeals.setSelectedMealsNames(List.of("tofu basquaise", "patates sautées"));
        assertEquals(
                Map.of(
                        "riz",
                        new IngredientAmount(500, "g"),
                        "tofu",
                        new IngredientAmount(800, "g"),
                        "patates",
                        new IngredientAmount(1.5f, "kg")
                ),
                mealsHandlerWithSeveralMeals.getIngredientsTotals()
        );

        mealsHandlerWithSeveralMeals.setSelectedMealsNames(List.of("risotto", "patates sautées"));
        assertEquals(
                Map.of(
                        "riz",
                        new IngredientAmount(500, "g"),
                        "sauce provençale",
                        new IngredientAmount(1, "L"),
                        "tofu",
                        new IngredientAmount(400, "g"),
                        "patates",
                        new IngredientAmount(1.5f, "kg")
                ),
                mealsHandlerWithSeveralMeals.getIngredientsTotals()
        );

        mealsHandlerWithSeveralMeals.setSelectedMealsNames(List.of("risotto", "tofu basquaise", "patates sautées"));
        assertEquals(
                Map.of(
                        "riz",
                        new IngredientAmount(1000, "g"),
                        "sauce provençale",
                        new IngredientAmount(1, "L"),
                        "tofu",
                        new IngredientAmount(800, "g"),
                        "patates",
                        new IngredientAmount(1.5f, "kg")
                ),
                mealsHandlerWithSeveralMeals.getIngredientsTotals()
        );
    }
}
