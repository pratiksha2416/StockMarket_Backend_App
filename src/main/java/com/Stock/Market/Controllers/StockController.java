package com.Stock.Market.Controllers;

import com.Stock.Market.Models.DTO.StockData;
import com.Stock.Market.Models.StockOrder;
import com.Stock.Market.Services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    StockService stockService;

    @GetMapping("getStockInfoByName")
    public StockData stockData(@RequestParam String symbol) throws IOException {
        return stockService.stockData(symbol);
    }

    @GetMapping("getAllStockInfo")
    public List<StockData> getALlStockInfo() throws IOException{
        return stockService.getAllStockInfo();
    }

    @GetMapping("Statement")
    public List<StockOrder> getStatement(@RequestParam String email){
        return stockService.getStatement(email);
    }
}
