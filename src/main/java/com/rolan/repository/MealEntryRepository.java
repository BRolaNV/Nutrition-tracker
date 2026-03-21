package com.rolan.repository;

import com.rolan.model.MealEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealEntryRepository extends JpaRepository<MealEntry, Integer> {
    List<MealEntry> findByUserId(Integer userId);
}
