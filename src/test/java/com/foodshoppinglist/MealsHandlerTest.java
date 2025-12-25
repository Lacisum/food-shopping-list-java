package com.foodshoppinglist;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MealsHandlerTest {

    static String mockFileName = "mockMealsFileName.yaml";

    @Test
    void method_checkFileFormat_throwsException_whenContentIsNotOfTheCorrectType() {
        assertThrows(FileFormatException.class, () -> MealsHandler.checkFileFormat(mockFileName,
                new Object()));
        assertThrows(FileFormatException.class, () -> MealsHandler.checkFileFormat(mockFileName,
                Map.of("risotto", new Object())));
        assertThrows(FileFormatException.class, () -> MealsHandler.checkFileFormat(mockFileName,
                Map.of("risotto", Map.of("riz", new Object()))));
        assertThrows(FileFormatException.class, () -> MealsHandler.checkFileFormat(mockFileName,
                Map.of("risotto", Map.of("riz", new HashMap<String, Object>()))));

        assertThrows(FileFormatException.class, () -> MealsHandler.checkFileFormat(mockFileName,
                Map.of(1, Map.of("riz", Map.of("quantity", 500, "unit", "g")))));
        assertThrows(FileFormatException.class, () -> MealsHandler.checkFileFormat(mockFileName,
                Map.of("risotto", Map.of(1, Map.of("quantity", 500, "unit", "g")))));
        assertThrows(FileFormatException.class, () -> MealsHandler.checkFileFormat(mockFileName,
                Map.of("risotto", Map.of("riz", Map.of(404, 500, 666, "g")))));

        assertThrows(FileFormatException.class, () -> MealsHandler.checkFileFormat(mockFileName,
                Map.of("risotto", Map.of("riz", Map.of("quantity", "notANumber!", "unit", "g")))));
        assertThrows(FileFormatException.class, () -> MealsHandler.checkFileFormat(mockFileName,
                Map.of("risotto", Map.of("riz", Map.of("quantity", 500, "unit", 404)))));
    }

    @Test
    void method_checkFileFormat_throwsException_whenContentHasTooFewOrTooMuchStuffInIt() {
        assertThrows(FileFormatException.class, () -> MealsHandler.checkFileFormat(mockFileName,
                Map.of("risotto", Map.of("riz", Map.of("wrongKey", 404)))));
        assertThrows(FileFormatException.class, () -> MealsHandler.checkFileFormat(mockFileName,
                Map.of("risotto", Map.of("riz", Map.of("quantity", 500, "unit", "g", "wrongKey", 404)))));
        assertThrows(FileFormatException.class, () -> MealsHandler.checkFileFormat(mockFileName,
                Map.of("risotto", Map.of("riz", Map.of("quantity", 500)))));
        assertThrows(FileFormatException.class, () -> MealsHandler.checkFileFormat(mockFileName,
                Map.of("risotto", Map.of("riz", Map.of("unit", "g")))));
    }

    @Test
    void method_checkFileFormat_throwsException_whenAnIngredientsUsesTwoDifferentUnits() {
        assertThrows(FileFormatException.class, () -> MealsHandler.checkFileFormat(mockFileName,
                Map.of(
                    "risotto", Map.of("riz", Map.of("quantity", 1, "unit", "g")),
                    "riz cantonnais", Map.of("riz", Map.of("quantity", 1, "unit", "kg"))
                )
        ));
    }

    @Test
    void method_checkFileFormat_doesNotThrow_whenContentIsCorrect() {
        assertDoesNotThrow(() -> MealsHandler.checkFileFormat(mockFileName, Map.of("risotto", Map.of("riz", Map.of("quantity", 1, "unit", "g")))));
        assertDoesNotThrow(() -> MealsHandler.checkFileFormat(mockFileName, Map.of("risotto", Map.of("riz", Map.of("quantity", 1.5, "unit", "g")))));
    }

}
