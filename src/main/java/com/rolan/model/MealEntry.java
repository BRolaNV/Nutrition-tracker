package com.rolan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealEntry {

    private int userId;
    private double protein;
    private double fat;
    private double carbohydrates;
    private double fiber;
    private double calories;
    private String nameOfMeal;
    private LocalDate date;


    public void setDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        this.date = localDate;
    }

    public double getCalories(){
        this.calories = protein * 4 + fat * 9 + carbohydrates * 4;
        return calories;
    }
}
