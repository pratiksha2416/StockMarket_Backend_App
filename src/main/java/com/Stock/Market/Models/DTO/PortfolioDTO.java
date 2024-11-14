package com.Stock.Market.Models.DTO;

import com.Stock.Market.Models.Portfolio;
import lombok.Data;

import java.util.List;

@Data

public class PortfolioDTO {
    private String message;
    private double totalAmount;
    private double investedAmount;
    private double profitAndLoss;
    private List<Portfolio> StockList;

    public PortfolioDTO() {
        this.message = "None";
    }
}
