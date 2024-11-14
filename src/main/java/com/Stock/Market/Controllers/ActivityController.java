package com.Stock.Market.Controllers;

import com.Stock.Market.Models.DTO.StockActivity;
import com.Stock.Market.Models.DTO.StockOrderRequest;
import com.Stock.Market.Services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @PostMapping("/BuyStock")
    private StockActivity buyStock(@RequestBody StockOrderRequest order,@RequestParam String email) throws IOException {
        return activityService.buyStock(order,email);
    }

    @PostMapping("SellStock")
    private StockActivity sellStock(@RequestBody StockOrderRequest order, @RequestParam String email) throws IOException {
        return activityService.sellStock(order,email);
    }
}
