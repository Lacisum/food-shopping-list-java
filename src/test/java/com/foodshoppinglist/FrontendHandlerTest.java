package com.foodshoppinglist;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FrontendHandlerTest {
    static private List<String> availableMealsNames;

    @BeforeAll
    static void initBeforeAll() {
        availableMealsNames = List.of("pâtes", "risotto");
    }

    @Test
    void method_parseSelectedMealsInput_throwsException_whenUserInputIsInvalid() {
        // bad format:
        assertThrows(InvalidUserInputException.class, () ->
                FrontendHandler.parseUserInput("a", availableMealsNames));
        assertThrows(InvalidUserInputException.class, () ->
                FrontendHandler.parseUserInput("1 2 p", availableMealsNames));
        assertThrows(InvalidUserInputException.class, () ->
                FrontendHandler.parseUserInput("1.7", availableMealsNames));
        assertThrows(InvalidUserInputException.class, () ->
                FrontendHandler.parseUserInput("1 2.0 3", availableMealsNames));
        assertThrows(InvalidUserInputException.class, () ->
                FrontendHandler.parseUserInput("01", availableMealsNames));
        assertThrows(InvalidUserInputException.class, () ->
                FrontendHandler.parseUserInput("-1", availableMealsNames));
        // out of bounds:
        assertThrows(InvalidUserInputException.class, () ->
                FrontendHandler.parseUserInput("0", availableMealsNames));
        assertThrows(InvalidUserInputException.class, () ->
                FrontendHandler.parseUserInput("3", availableMealsNames));
        // spaces between integers required:
        assertThrows(InvalidUserInputException.class, () ->
                FrontendHandler.parseUserInput("12", availableMealsNames));
    }

    @Test
    void method_parseSelectedMealsInput_returnsTheCorrectList_whenUserInputIsValid() throws InvalidUserInputException {
        assertEquals(
                new ArrayList<String>(),
                FrontendHandler.parseUserInput("", availableMealsNames));
        assertEquals(
                List.of(availableMealsNames.get(0)),
                FrontendHandler.parseUserInput("1", availableMealsNames));
        assertEquals(
                List.of(availableMealsNames.get(1)),
                FrontendHandler.parseUserInput("2", availableMealsNames));
        assertEquals(
                availableMealsNames,
                FrontendHandler.parseUserInput("1 2", availableMealsNames));
        // blanks are authorized:
        assertEquals(
                new ArrayList<String>(),
                FrontendHandler.parseUserInput(" \t  \t", availableMealsNames));
        assertEquals(
                availableMealsNames,
                FrontendHandler.parseUserInput("\t 1\t 2   ", availableMealsNames));
        // order is restored:
        assertEquals(
                availableMealsNames,
                FrontendHandler.parseUserInput("2 1", availableMealsNames));
    }
}
