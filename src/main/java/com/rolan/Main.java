package com.rolan;

import model.MealEntry;
import model.User;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static ArrayList<MealEntry> mealEntries = new ArrayList<>();

    public static void printDailySummary(ArrayList<MealEntry> meals, User user) {
        double totalProtein = 0;
        double totalFat = 0;
        double totalCarbohydrates = 0;
        double totalFiber = 0;
        double totalCalories;

        for (MealEntry mealEntry : meals){
            totalProtein += mealEntry.getProtein();
            totalFat += mealEntry.getFat();
            totalCarbohydrates += mealEntry.getCarbohydrates();
            totalFiber += mealEntry.getFiber();
        }

        totalCalories = totalProtein * 4 + totalFat * 9 + totalCarbohydrates * 4;

        System.out.println();
        System.out.println("Total today: ");
        System.out.println();

        System.out.println("Protein: " + totalProtein);
        System.out.println("Fat: " + totalFat);
        System.out.println("Carbohydrates: " + totalCarbohydrates);
        System.out.println("Fiber: " + totalFiber);
        System.out.println("Calories: " + totalCalories);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("--- Comparison with the target ---");
        System.out.println();
        System.out.printf("%-15s Цель: %7.1fg | Факт: %7.1fg | Разница: %+7.1fg (%+6.1f%%)%n",
                "Protein:",
                user.getProtein(),
                totalProtein,
                totalProtein - user.getProtein(),
                (totalProtein - user.getProtein()) / user.getProtein() * 100
        );
        System.out.printf("%-15s Цель: %7.1fg | Факт: %7.1fg | Разница: %+7.1fg (%+6.1f%%)%n",
                "Fat:",
                user.getFat(),
                totalFat,
                totalFat - user.getFat(),
                (totalFat - user.getFat()) / user.getFat() * 100
        );
        System.out.printf("%-15s Цель: %7.1fg | Факт: %7.1fg | Разница: %+7.1fg (%+6.1f%%)%n",
                "Carbohydrates:",
                user.getCarbohydrates(),
                totalCarbohydrates,
                totalCarbohydrates - user.getCarbohydrates(),
                (totalCarbohydrates - user.getCarbohydrates()) / user.getCarbohydrates() * 100
        );
        System.out.printf("%-15s Цель: %7.1fg | Факт: %7.1fg | Разница: %+7.1fg (%+6.1f%%)%n",
                "Protein:",
                user.getFiber(),
                totalFiber,
                totalFiber - user.getFiber(),
                (totalFiber - user.getFiber()) / user.getFiber() * 100
        );
        System.out.printf("%-15s Цель: %7.1fkcal | Факт: %7.1fkcal | Разница: %+7.1fkcal (%+6.1f%%)%n",
                "Calories:",
                user.getCalories(),
                totalCalories,
                totalCalories - user.getCalories(),
                (totalCalories - user.getCalories()) / user.getCalories() * 100
        );

    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        User Rolan = new User("Rolan");

        System.out.print("Enter Your target Protein: ");
        Rolan.setProtein(scanner.nextDouble());
        System.out.print("Enter Your target Fat: ");
        Rolan.setFat(scanner.nextDouble());
        System.out.print("Enter Your target Carbohydrates: ");
        Rolan.setCarbohydrates(scanner.nextDouble());
        System.out.print("Enter Your target Fiber: ");
        Rolan.setFiber(scanner.nextDouble());

        System.out.println("Protein: " + Rolan.getProtein());
        System.out.println("Fat: " + Rolan.getFat());
        System.out.println("Carbohydrates: " + Rolan.getCarbohydrates());
        System.out.println("Fiber: " + Rolan.getFiber());
        System.out.println("Calories: " + Rolan.getCalories());
        scanner.nextLine();

        System.out.println();
        System.out.println("--- Adding meals ---");

        while (true) {
            System.out.print("Name of meal (or 'stop'): ");
            String name = scanner.nextLine();

            if (name.equals("stop")) {
                break;
            }

            MealEntry mealEntry = new MealEntry();
            mealEntry.setNameOfMeal(name);

            System.out.print("Protein: ");
            mealEntry.setProtein(scanner.nextDouble());
            System.out.print("Fat: ");
            mealEntry.setFat(scanner.nextDouble());
            System.out.print("Carbohydrates: ");
            mealEntry.setCarbohydrates(scanner.nextDouble());
            System.out.print("Fiber: ");
            mealEntry.setFiber(scanner.nextDouble());
            scanner.nextLine();

            mealEntries.add(mealEntry);
        }

        printDailySummary(mealEntries, Rolan);
    }
}
