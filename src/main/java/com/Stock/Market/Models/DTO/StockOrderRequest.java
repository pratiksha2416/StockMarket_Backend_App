package com.Stock.Market.Models.DTO;

import lombok.Data;

@Data
public class StockOrderRequest {
    private String stockName;
    private int quantity;
}
