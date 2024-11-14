package com.Stock.Market.Services;

import com.Stock.Market.Models.DTO.StockData;
import com.Stock.Market.Models.StockOrder;
import com.Stock.Market.Models.User;
import com.Stock.Market.Repositories.ActivityRepo;
import com.Stock.Market.Repositories.UserRepo;
import com.Stock.Market.Services.Utility.APICallingMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ActivityRepo activityRepo;


    public StockData stockData(String symbol) throws IOException {
        return APICallingMethod.apiCallingMethodByStock(symbol);
    }

    public List<StockData> getAllStockInfo() throws IOException {
        return APICallingMethod.getAllStockData();
    }

    public List<StockOrder> getStatement(String email) {
        User user = userRepo.findByUserEmail(email);
        if(user.getStatus().equals("Logged in")){
            return activityRepo.findAllByEmail(email);
        }else{
            return new ArrayList<>();
        }
    }


}
