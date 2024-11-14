package com.Stock.Market.Repositories;

import com.Stock.Market.Models.StockOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepo extends JpaRepository<StockOrder,Long> {

    List<StockOrder> findAllByEmail(String email);
}

