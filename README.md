# Food shopping list


- [Presentation](#presentation)
- [Requirements](#requirements)
- [Run the program](#run-the-program)
- [Configure the meals file](#configure-the-meals-file)
- [Run the tests](#run-the-tests)


<a name="Presentation"></a>
## Presentation

Do you know in advance what meals you want to cook this week? Then just feed the program with the names of those meals. You will be given the list of ingredients (with their quantities) you need to buy.




<a name="Requirements"></a>
## Requirements

Make sure that you have Java 25.




<a name="RunTheProgram"></a>
## Run the program

The program takes a "meals file" in argument.
A meals file is a file that lists meals with their required ingredients.
The section [Configure the meals file](#configure-the-meals-file) explains how to write a meals file.

For convenience, a default meals file called [`meals.yaml`](meals.yaml) is provided to you.
To run the program with it, type:

```bash
mvn package
java -jar target/food-shopping-list.jar meals.yaml
```




<a name="ConfigureTheMealsFile"></a>
## Configure the meals file

The program takes in argument the name of the file that contains the selectable meals with their ingredients.
This file must be a YAML file and follow a specific syntax. An example file ([`meals.yaml`](meals.yaml)) is provided. The syntax is the following :

```yaml
the name of a meal:
  an ingredient:
    quantity: 3
    unit: kg
  another ingredient:
    quantity: 1
    unit: unit
the name of another meal:
  an ingredient:
    quantity: 2
    unit: kg
  yet another ingredient:
    quantity: 5
    unit: pinch
```

If some ingredient doesn't have a unit, then you can just type `unit` in the `unit` field (as shown in the example above).  

Throughout the file, make sure that:
- a given ingredient or unit is always written the exact same way
    - example 1: `potatoe` & `potatoes` (with an 's') will be considered as two different ingredients
    - example 2: `teaspoon` & `teaspoons` (with an 's') too
- the quantity for an ingredient is always given in the exact same unit
    - example: using `kg` then `g` for `flour` won't work




<a name="RunTheTests"></a>
## Run the tests

To run the tests, type the following command:

```bash
mvn test
```
