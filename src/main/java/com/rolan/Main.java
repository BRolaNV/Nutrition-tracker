package com.rolan;

import model.User;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        User Rolan = new User("Rolan");

        System.out.println("Enter Your target Protein: ");
        Rolan.setProtein(scanner.nextDouble());
        System.out.println("Enter Your target Fat: ");
        Rolan.setFat(scanner.nextDouble());
        System.out.println("Enter Your target Carbohydrates: ");
        Rolan.setCarbohydrates(scanner.nextDouble());
        System.out.println("Enter Your target Fiber: ");
        Rolan.setFiber(scanner.nextDouble());

        System.out.println("Protein: " + Rolan.getProtein());
        System.out.println("Fat: " + Rolan.getFat());
        System.out.println("Carbohydrates: " + Rolan.getCarbohydrates());
        System.out.println("Fiber: " + Rolan.getFiber());
        System.out.println("Calories: " + Rolan.getCalories());
    }
}
