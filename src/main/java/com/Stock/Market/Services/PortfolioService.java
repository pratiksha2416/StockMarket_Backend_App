package com.Stock.Market.Services;

import com.Stock.Market.Models.DTO.PortfolioDTO;
import com.Stock.Market.Models.DTO.StockData;
import com.Stock.Market.Models.Portfolio;
import com.Stock.Market.Models.User;
import com.Stock.Market.Repositories.PortfolioRepo;
import com.Stock.Market.Repositories.UserRepo;
import com.Stock.Market.Services.Utility.APICallingMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class PortfolioService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    PortfolioRepo portfolioRepo;
    public PortfolioDTO getPortfolio(String email) {
        User user = userRepo.findByUserEmail(email);
        PortfolioDTO p = new PortfolioDTO();
        if(user.getStatus().equals("Logged in")){
            List<Portfolio> list = new ArrayList<>();
            if(!portfolioRepo.existsByEmail(email)){
                list.add(null);
                p.setProfitAndLoss(0);
                p.setTotalAmount(0);
                p.setInvestedAmount(0);
                p.setStockList(list);
                return p;
            }
            list  = portfolioRepo.findByEmail(email);
            p.setStockList(list);
            double totalAmount =0;
            double investedAmount = 0;

            for(Portfolio portfolio : list){
                totalAmount += portfolio.getCurrentAmount();
                investedAmount += portfolio.getInvestedAmount();
            }
            p.setTotalAmount(totalAmount);
            p.setInvestedAmount(investedAmount);
            p.setProfitAndLoss(totalAmount-investedAmount);
            return p;
        }else{
            p.setMessage("Login First");
            return p;
        }

    }

    @Scheduled(fixedDelay = 60000) // 60000 milliseconds = 1 minute
    public void updateStockPrices() {
        List<Portfolio> portfolios = portfolioRepo.findAll();

        for (Portfolio portfolio : portfolios) {
            try {
                StockData stockData = APICallingMethod.apiCallingMethodByStock(portfolio.getSymbol());

                // Update the current price in the portfolio
                portfolio.setCurrentPrice(Double.parseDouble(stockData.getCurrentPrice()));

                // Calculate the current amount based on the updated price
                double currentAmount = portfolio.getCurrentPrice() * portfolio.getQuantity();
                portfolio.setCurrentAmount(currentAmount);

                // Save the updated portfolio
                portfolioRepo.save(portfolio);
            } catch (IOException e) {
                // Handle any exceptions that occur during the API call
                e.printStackTrace();
            }
        }
    }
}
