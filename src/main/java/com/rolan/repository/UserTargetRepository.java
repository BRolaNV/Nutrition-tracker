package com.rolan.repository;

import com.rolan.model.UserTargets;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTargetRepository extends JpaRepository<UserTargets, Integer> {
    List<UserTargets> findByUserId(Integer userId);
    Boolean existsByUserId(Integer userId);
}
