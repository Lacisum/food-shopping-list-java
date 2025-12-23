package com.foodshoppinglist;

import java.util.Map;

public class TextAssetsLoader {

    private final String fileName;

    public TextAssetsLoader(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Loads and returns the text assets.
     *
     * @return the text assets
     */
    public Map<String, String> load() {
        Object content = Utils.readYamlFile(fileName, true);
        try {
            checkContentCorrectness(content);
        } catch (FileFormatException e) {
            Utils.printFileFormatException(fileName, e.getMessage());
        }
        return (Map<String, String>) content;
    }

    /**
     * Throws an exception if the content is not correctly formatted.
     *
     * @throws FileFormatException if the content is not correctly formatted
     */
    private void checkContentCorrectness(Object content) throws FileFormatException {
        if (! (content instanceof Map))
            throw new FileFormatException("the root element is not a dictionary");
        for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) content).entrySet()) {
            if (! (entry.getKey() instanceof String))
                throw new FileFormatException("the key " + entry.getKey() + " is not a string");
            if (! (entry.getValue() instanceof String))
                throw new FileFormatException("the value of the key " + entry.getKey() + " is not a string");
        }
    }
}
