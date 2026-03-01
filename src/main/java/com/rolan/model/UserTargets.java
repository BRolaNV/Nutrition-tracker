package com.rolan.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UserTargets {

    private int userId;
    private double protein;
    private double fat;
    private double carbohydrates;
    private double fiber;
    private double calories;
    private LocalDate date;

    public UserTargets(int userId, double protein, double fat, double carbohydrates, double fiber) {
        this.userId = userId;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
    }

    public int getUserId() {
        return userId;
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

    public void setDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        this.date = localDate;
    }

    public LocalDate getDate() {
        return date;
    }
    public String toString(){
        return "\nProtein - " + getProtein() +
                "\nFat - " + getFat() +
                "\nCarbohydrates - " + getCarbohydrates() +
                "\nFiber - " + getFiber() +
                "\nCalories - " + getCalories();
    }
}
