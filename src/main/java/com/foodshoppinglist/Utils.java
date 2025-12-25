package com.foodshoppinglist;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.*;
import java.net.URL;

public class Utils {

    /**
     * Loads and returns the content of a YAML file.
     *
     * @param fileName the name of the YAML file
     * @param searchInTheProgramResources if true, the method will search is the resources folder, otherwise it will look in the current directory of the user's current directory
     */
    public static Object readYamlFile(String fileName, boolean searchInTheProgramResources) {
        InputStream inputStream;
        Object content = null;

        // get the file
        if (searchInTheProgramResources) {
            inputStream = getInputStreamInTheProgramResources(fileName);
        } else {
            inputStream = getInputStreamInTheCurrentDirectory(fileName);
        }

        // read the file
        try {
            content = new Yaml().load(inputStream);
        } catch (YAMLException exception) {
            System.err.println("Syntax error in " + fileName + ": " + exception.getMessage());
            System.exit(1);
        }

        return content;
    }

    private static InputStream getInputStreamInTheCurrentDirectory(String fileName) {
        InputStream inputStream = null;
        File file = new File(fileName);
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("No such file: " + fileName);
            System.exit(1);
        }
        return inputStream;
    }

    private static InputStream getInputStreamInTheProgramResources(String fileName) {
        InputStream inputStream = null;
        URL resource = Main.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            System.err.println("No such file: " + fileName);
            System.exit(1);
        }
        try {
            inputStream = resource.openStream();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return inputStream;
    }
}
