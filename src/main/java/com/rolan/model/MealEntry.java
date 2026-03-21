package com.rolan.model;

import jakarta.persistence.*;
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
@Table(name = "meal_entries")
@Entity
public class MealEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    private int userId;
    private double protein;
    private double fat;
    private double carbohydrates;
    private double fiber;
    @Column(name = "name_of_meal")
    private String nameOfMeal;
    private LocalDate date;


    public void setDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        this.date = localDate;
    }

    @Transient
    public double getCalories(){
        return protein * 4 + fat * 9 + carbohydrates * 4;
    }
}
