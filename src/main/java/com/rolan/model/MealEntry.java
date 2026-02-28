package com.rolan.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MealEntry {

    private int userId;
    private String nameOfMeal;
    private double protein;
    private double fat;
    private double carbohydrates;
    private double fiber;
    private double calories;
    private LocalDate date;

    public MealEntry(int userId, String nameOfMeal, double protein, double fat, double carbohydrates, double fiber) {
        this.userId = userId;
        this.nameOfMeal = nameOfMeal;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
    }

    public int getUserId() {
        return userId;
    }

    public String getNameOfMeal() {
        return nameOfMeal;
    }

    public void setNameOfMeal(String nameOfMeal) {
        this.nameOfMeal = nameOfMeal;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        this.date = localDate;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public double getCalories(){
        this.calories = protein * 4 + fat * 9 + carbohydrates * 4;
        return calories;
    }
}
