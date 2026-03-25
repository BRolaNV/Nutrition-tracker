package com.rolan.service;

import com.rolan.model.User;
import com.rolan.model.UserTargets;
import com.rolan.repository.UserTargetRepository;
import com.rolan.service.interfaces.UserTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTargetServiceImpl implements UserTargetService {

    private final UserTargetRepository userTargetRepository;

    @Override
    public UserTargets createUserTargets(User user, double protein, double fat, double carbohydrates, double fiber) {
        return userTargetRepository.save(UserTargets.builder()
                .userId(user.getId())
                .protein(protein)
                .fat(fat)
                .carbohydrates(carbohydrates)
                .fiber(fiber)
                .date(LocalDate.now())
                .build());
    }

    @Override
    public boolean existsByUserId(User user) {
        return userTargetRepository.existsByUserId(user.getId());
    }

    @Override
    public List<UserTargets> findTargetsByUserId(User user) {
        return userTargetRepository.findByUserId(user.getId());
    }
}
