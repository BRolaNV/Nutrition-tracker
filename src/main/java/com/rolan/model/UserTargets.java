package com.rolan.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_targets")
@Entity
public class UserTargets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    private int userId;
    private double protein;
    private double fat;
    private double carbohydrates;
    private double fiber;
    private LocalDate date;

    @Transient
    public double getCalories(){
        return protein * 4 + fat * 9 + carbohydrates * 4;
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
