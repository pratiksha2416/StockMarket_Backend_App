package com.Stock.Market.Repositories;

import com.Stock.Market.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository <User, Long> {
    boolean existsByuserEmail(String userEmail);


    User findByUserEmail(String email);
}
