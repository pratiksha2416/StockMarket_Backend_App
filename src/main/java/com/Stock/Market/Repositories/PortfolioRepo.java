package com.Stock.Market.Repositories;

import com.Stock.Market.Models.DTO.PortfolioDTO;
import com.Stock.Market.Models.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, Long> {

        boolean existsByEmailAndSymbol(String email, String stockName);

        Portfolio findByEmailAndSymbol(String email, String stockName);

        boolean existsByEmail(String email);

        List<Portfolio> findByEmail(String email);
}
