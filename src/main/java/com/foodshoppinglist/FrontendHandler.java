package com.foodshoppinglist;

import java.util.List;
import java.util.Map;

/**
 * A class for interfacing with the user.
 */
public class FrontendHandler {

    // The text assets keys
    public static final String INTRODUCE_AVAILABLE_MEALS = "introduce_available_meals";

    /** The text assets. */
    private final Map<String, String> textAssets;




    /**
     * Builds a <code>FrontendHandler</code>
     *
     * @param textAssetsFile the file that contains the text assets
     * @throws FileFormatException if the file is not correctly formatted
     */
    public FrontendHandler(String textAssetsFile) throws FileFormatException {
        Object content = Utils.readYamlFile(textAssetsFile, true);
        checkFileFormat(textAssetsFile, content);
        this.textAssets = (Map<String, String>) content;
    }




    /**
     * Throws an exception if the provided text assets file content is not correctly formatted.
     *
     * @param fileName the name of the file
     * @param content the content of the file
     * @throws FileFormatException if the text assets file content is not correctly formatted
     */
    static void checkFileFormat(String fileName, Object content) throws FileFormatException {
        String errorPrefix = "Error in " + fileName + ": ";

        if (! (content instanceof Map))
            throw new FileFormatException(errorPrefix + "the root element is not a dictionary");
        for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) content).entrySet()) {
            if (! (entry.getKey() instanceof String))
                throw new FileFormatException(errorPrefix + "the key '" + entry.getKey() + "' is not a string");
            if (! (entry.getValue() instanceof String))
                throw new FileFormatException(errorPrefix + "the value of the key '" + entry.getKey() + "' is not a string");
        }
    }

    /**
     * Prints the provided list of names of available meals.
     *
     * @param availableMealsNames the list of names of available meals
     */
    public void printAvailableMeals(List<String> availableMealsNames) {
        IO.println();
        IO.println(textAssets.get(INTRODUCE_AVAILABLE_MEALS));
        for (int i = 0; i < availableMealsNames.size(); i++)
            IO.println((i + 1) + ". " + availableMealsNames.get(i));
        IO.println();
    }
}
