package com.foodshoppinglist;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for interfacing with the user.
 */
public class FrontendHandler {

    // The text assets keys
    private static final String INTRODUCE_AVAILABLE_MEALS = "introduce_available_meals";
    private static final String PROMPT_USER = "prompt_user";
    private static final String TRY_AGAIN = "try_again";
    private static final String YOU_DIDNT_CHOOSE_ANY_MEAL = "you_didnt_choose_any_meal";
    private static final String INTRODUCE_SELECTED_MEALS = "introduce_selected_meals";

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
     * Throws an exception if the provided text assets file content is not
     * correctly formatted.
     *
     * @param fileName the name of the file
     * @param content the content of the file
     * @throws FileFormatException if the text assets file content is not
     * correctly formatted
     */
    public static void checkFileFormat(String fileName, Object content) throws FileFormatException {
        String errorPrefix = "Error in " + fileName + ": ";

        if (! (content instanceof Map))
            throw new FileFormatException(errorPrefix + "the root element is not a dictionary");
        for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) content).entrySet()) {
            if (! (entry.getKey() instanceof String))
                throw new FileFormatException(errorPrefix + "the key '" + entry.getKey() + "' is not a string");
            if (! (entry.getValue() instanceof String))
                throw new FileFormatException(
                        errorPrefix + "the value of the key '" + entry.getKey() + "' is not a string"
                );
        }
    }

    /**
     * Parses the provided user input and returns the implied meals names (or no
     * meals names if nothing was entered).
     * <p/>
     * If the input is invalid, throws an exception. An input is valid if it is
     * a blank line or a sequence of valid blank-separated integers (invalid
     * integers being integers that are not associated to any meal).
     *
     * @param userInput the user input
     * @param availableMealsNames the list of the names of available meals
     * @return the list of the names of the user-selected meals
     * @throws InvalidUserInputException if the user input is invalid
     */
    public static List<String> parseUserInput(
            String userInput,
            List<String> availableMealsNames)
            throws InvalidUserInputException {
        // regex for a decimal integer with no leading zero (except if there's just one digit)
        String decimalNumberRegex = "(([1-9][0-9]+)|[0-9])";
        Pattern validInputPattern = Pattern.compile(
                "\\p{Blank}*|(\\p{Blank}*%1$s\\p{Blank}*)|(\\p{Blank}*%1$s(\\p{Blank}+%1$s)+\\p{Blank}*)"
                        .formatted(decimalNumberRegex)
        );
        Matcher validInputMatcher = validInputPattern.matcher(userInput);
        if (! validInputMatcher.matches())
            throw new InvalidUserInputException("The input must be integers separated by spaces.");
        Pattern decimalNumberPattern = Pattern.compile(decimalNumberRegex);
        Matcher decimalNumberMatcher = decimalNumberPattern.matcher(userInput);
        List<Integer> theNumbers = decimalNumberMatcher.results()
                .map(matchResult -> Integer.parseInt(matchResult.group()))
                .distinct()
                .sorted()
                .toList();
        if (theNumbers.stream()
                .anyMatch(integer -> integer < 1 || integer > availableMealsNames.size()))
            throw new InvalidUserInputException("The numbers must be part of those proposed.");
        return theNumbers.stream()
                .map(integer -> availableMealsNames.get(integer - 1))
                .toList();
    }

    /**
     * Prints the provided list of names of available meals.
     *
     * @param availableMealsNames the list of the names of available meals
     */
    public void printAvailableMeals(List<String> availableMealsNames) {
        IO.println();
        IO.println(textAssets.get(INTRODUCE_AVAILABLE_MEALS));
        for (int i = 0; i < availableMealsNames.size(); i++)
            IO.println((i + 1) + ". " + availableMealsNames.get(i));
        IO.println();
    }

    /**
     * Prompts the user to select meals.
     * The user is expected to enter integers separated by spaces, each integer
     * representing a meal. An empty string is also accepted; it indicates that
     * the user selects zero meals.
     * <p/>
     * If the user enters an invalid string, they are informed of it and
     * prompted again.
     *
     * @param availableMealsNames the list of the names of available meals
     * @return the list of the names of the user-selected meals (an empty list
     * may be returned)
     */
    public List<String> getSelectedMealsFromUserInput(List<String> availableMealsNames) {
        String userInput;
        boolean inputIsValidated = false;
        List<String> selectedMeals = null;
        userInput = promptUserInput(textAssets.get(PROMPT_USER));
        while (! inputIsValidated) {
            try {
                selectedMeals = parseUserInput(userInput, availableMealsNames);
                inputIsValidated = true;
            } catch (InvalidUserInputException e) {
                userInput = promptUserInput(e.getMessage() + "\r\n" + textAssets.get(TRY_AGAIN));
            }
        }
        return selectedMeals;
    }

    /**
     * Informs the user that they didn't choose any meal.
     */
    public void printYouDidntChooseAnyMeal() {
        IO.println(textAssets.get(YOU_DIDNT_CHOOSE_ANY_MEAL));
    }

    /**
     * Prints the user-selected meals.
     *
     * @param selectedMealsNames the names of the selected meals
     */
    public void printSelectedMeals(List<String> selectedMealsNames) {
        IO.println(textAssets.get(INTRODUCE_SELECTED_MEALS));
        for (String mealName : selectedMealsNames)
            IO.println("- " + mealName);
        IO.println();
    }

    /**
     * Prompts the user with the given message and returns the user input.
     *
     * @param message the message to prompt the user with
     * @return the user input
     */
    private static String promptUserInput(String message) {
        IO.println(message);
        String userInput = new Scanner(System.in).nextLine();
        IO.println();
        return userInput;
    }
}
