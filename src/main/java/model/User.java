package model;

import java.util.HashMap;

public class User {
    private String UserName;
    private int id;

    public User(String name, int id){
        this.UserName = name;
        this.id = id;
    }

    public String getUserName(){
        return UserName;
    }

    private double protein;
    private double fat;
    private double carbohydrates;
    private double fiber;
    private double calories;

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
