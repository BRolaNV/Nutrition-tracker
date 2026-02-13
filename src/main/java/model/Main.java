package model;

import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        HashMap<String, Double> reference = new HashMap<>();

        Scanner scanner = new Scanner(System.in);

        User Rolan = new User("Rolan");

        Rolan.setProtein(scanner.nextDouble());
        Rolan.setFat(scanner.nextDouble());
        Rolan.setCarbohydrates(scanner.nextDouble());
        Rolan.setFiber(scanner.nextDouble());

        reference.put("Protein", Rolan.getProtein());
        reference.put("Fat", Rolan.getFat());
        reference.put("Carbohydrates", Rolan.getCarbohydrates());
        reference.put("Fiber", Rolan.getFiber());
        reference.put("Calories", Rolan.getCalories());
    }
}
