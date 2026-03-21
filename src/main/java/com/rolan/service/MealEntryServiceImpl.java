package com.rolan.service;

import com.rolan.model.MealEntry;
import com.rolan.model.User;
import com.rolan.repository.MealEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealEntryServiceImpl implements MealEntryService{

    private final MealEntryRepository mealEntryRepository;

    @Override
    public void createMealEntry(User user, double protein, double fat, double carbohydrates, double fiber) {
        mealEntryRepository.save(MealEntry.builder()
                .userId(user.getId())
                .protein(protein)
                .fat(fat)
                .carbohydrates(carbohydrates)
                .fiber(fiber)
                .date(LocalDate.now())
                .build());
    }

    @Override
    public List<MealEntry> getMealEntry(User user) {
        return mealEntryRepository.findByUserId(user.getId());
    }
}
