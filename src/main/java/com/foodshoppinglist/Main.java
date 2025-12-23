package com.foodshoppinglist;

import java.util.Map;

/**
 * The main class of the program.
 */
public class Main {

    private static final String USAGE = "Usage: java -jar target/food-shopping-list.jar <file_name>";
    private static final String TEXT_ASSETS_FILE_NAME = "texts.yaml";

    static void main(String[] args) {
        if (args.length != 1) {
            System.err.println(USAGE);
            System.exit(1);
        }

        final String fileName = args[0];

        Map<String, String> textAssets = new TextAssetsLoader(TEXT_ASSETS_FILE_NAME).load();
        IO.println(textAssets);

    }
}
