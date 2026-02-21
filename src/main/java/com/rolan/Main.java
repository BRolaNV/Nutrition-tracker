package com.rolan;

import dao.Database;
import dao.MealEntryDAO;
import dao.UserDAO;
import dao.UserTargetDAO;
import model.MealEntry;
import model.User;
import model.UserTargets;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {

        Database.createTables();

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Your name: ");
        String name = scanner.nextLine();
        User user;
        UserTargets userTargets;

        if (!UserDAO.existsByName(name)) {
            user = new User(name, UserDAO.saveUser(name));
            System.out.print("Enter Your target Protein: ");
            double protein = scanner.nextDouble();
            System.out.print("Enter Your target Fat: ");
            double fat = scanner.nextDouble();
            System.out.print("Enter Your target Carbohydrates: ");
            double carbohydrates = scanner.nextDouble();
            System.out.print("Enter Your target Fiber: ");
            double fiber = scanner.nextDouble();
            scanner.nextLine();
            userTargets = new UserTargets(user.getId(), protein, fat, carbohydrates, fiber);
            UserTargetDAO.saveTargets(userTargets);
        } else {
            user = UserDAO.findByName(name);
            if (!UserTargetDAO.existsByUserId(user)){
                System.out.print("Enter Your target Protein: ");
                double protein = scanner.nextDouble();
                System.out.print("Enter Your target Fat: ");
                double fat = scanner.nextDouble();
                System.out.print("Enter Your target Carbohydrates: ");
                double carbohydrates = scanner.nextDouble();
                System.out.print("Enter Your target Fiber: ");
                double fiber = scanner.nextDouble();
                scanner.nextLine();
                userTargets = new UserTargets(user.getId(), protein, fat, carbohydrates, fiber);
                UserTargetDAO.saveTargets(userTargets);
            } else {
                userTargets = UserTargetDAO.findTargetsByUserId(user);
            }

        }

        double proteinTotal = 0;
        double fatTotal = 0;
        double carbohydratesTotal = 0;
        double fiberTotal = 0;
        double caloriesTotal = 0;

        System.out.println();
        System.out.println("--- Adding meals ---");

        while (true) {
            System.out.print("Name of meal (or 'stop'): ");
            String nameOfMeal = scanner.nextLine();

            if (nameOfMeal.equals("stop")) {

                List<MealEntry> mealEntriesTotal = MealEntryDAO.findMealEntriesByUserId(user);

                proteinTotal = 0;
                fatTotal = 0;
                carbohydratesTotal = 0;
                fiberTotal = 0;
                caloriesTotal = 0;

                for (MealEntry meal : mealEntriesTotal) {
                    proteinTotal += meal.getProtein();
                    fatTotal += meal.getFat();
                    carbohydratesTotal += meal.getCarbohydrates();
                    fiberTotal += meal.getFiber();
                    caloriesTotal += meal.getCalories();
                }

                UserTargets ut = UserTargetDAO.findTargetsByUserId(user);

                double proteinTarget = ut.getProtein();
                double fatTarget = ut.getFat();
                double carbohydratesTarget = ut.getCarbohydrates();
                double fiberTarget = ut.getFiber();
                double caloriesTarget = ut.getCalories();

                System.out.println("--- Comparison with the target ---");
                System.out.println();
                System.out.printf("%-15s Цель: %12.1fg    | Факт: %10.1fg    | Разница: %+10.1fg    (%+6.1f%%)%n",
                        "Protein:",
                        proteinTarget,
                        proteinTotal,
                        proteinTotal - proteinTarget,
                        (proteinTotal - proteinTarget) / proteinTarget * 100
                );
                System.out.printf("%-15s Цель: %12.1fg    | Факт: %10.1fg    | Разница: %+10.1fg    (%+6.1f%%)%n",
                        "Fat:",
                        fatTarget,
                        fatTotal,
                        fatTotal - fatTarget,
                        (fatTotal - fatTarget) / fatTarget * 100
                );
                System.out.printf("%-15s Цель: %12.1fg    | Факт: %10.1fg    | Разница: %+10.1fg    (%+6.1f%%)%n",
                        "Carbohydrates:",
                        carbohydratesTarget,
                        carbohydratesTotal,
                        carbohydratesTotal - carbohydratesTarget,
                        (carbohydratesTotal - carbohydratesTarget) / carbohydratesTarget * 100
                );
                System.out.printf("%-15s Цель: %12.1fg    | Факт: %10.1fg    | Разница: %+10.1fg    (%+6.1f%%)%n",
                        "Fiber:",
                        fiberTarget,
                        fiberTotal,
                        fiberTotal - fiberTarget,
                        (fiberTotal - fiberTarget) / fiberTarget * 100
                );
                System.out.printf("%-15s Цель: %12.1fkcal | Факт: %10.1fkcal | Разница: %+10.1fkcal (%+6.1f%%)%n",
                        "Calories:",
                        caloriesTarget,
                        caloriesTotal,
                        caloriesTotal - caloriesTarget,
                        (caloriesTotal - caloriesTarget) / caloriesTarget * 100
                );

                break;
            }

            System.out.print("Protein: ");
            double protein = scanner.nextDouble();
            System.out.print("Fat: ");
            double fat = scanner.nextDouble();
            System.out.print("Carbohydrates: ");
            double carbohydrates = scanner.nextDouble();
            System.out.print("Fiber: ");
            double fiber = scanner.nextDouble();
            scanner.nextLine();

            MealEntry mealEntry = new MealEntry(user.getId(), nameOfMeal, protein, fat, carbohydrates, fiber);
            MealEntryDAO.saveMealEntry(mealEntry);

            List<MealEntry> mealEntriesTotal = MealEntryDAO.findMealEntriesByUserId(user);

            proteinTotal = 0;
            fatTotal = 0;
            carbohydratesTotal = 0;
            fiberTotal = 0;
            caloriesTotal = 0;

            for (MealEntry meal : mealEntriesTotal) {
                proteinTotal += meal.getProtein();
                fatTotal += meal.getFat();
                carbohydratesTotal += meal.getCarbohydrates();
                fiberTotal += meal.getFiber();
                caloriesTotal += meal.getCalories();
            }

            System.out.println("\nYou've already eaten today: ");

            System.out.print("\nProtein: " + String.format("%.2f", proteinTotal));
            System.out.print("\nFat: " + String.format("%.2f", fatTotal));
            System.out.print("\nCarbohydrates: " + String.format("%.2f", carbohydratesTotal));
            System.out.print("\nFiber: " + String.format("%.2f", fiberTotal));
            System.out.print("\nCalories: " + String.format("%.2f", caloriesTotal) + "\n\n");

        }

    }
}
