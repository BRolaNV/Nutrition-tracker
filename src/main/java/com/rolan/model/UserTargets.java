package com.rolan.model;

import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTargets {

    private int userId;
    private double protein;
    private double fat;
    private double carbohydrates;
    private double fiber;
    private double calories;
    private LocalDate date;

    public double getCalories(){
        this.calories = protein * 4 + fat * 9 + carbohydrates * 4;
        return calories;
    }

    public void setDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        this.date = localDate;
    }

    public String toString(){
        return "\nProtein - " + getProtein() +
                "\nFat - " + getFat() +
                "\nCarbohydrates - " + getCarbohydrates() +
                "\nFiber - " + getFiber() +
                "\nCalories - " + getCalories();
    }
}
