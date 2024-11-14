package com.Stock.Market.Services;

import com.Stock.Market.Models.DTO.StockActivity;
import com.Stock.Market.Models.DTO.StockData;
import com.Stock.Market.Models.DTO.StockOrderRequest;
import com.Stock.Market.Models.Portfolio;
import com.Stock.Market.Models.StockOrder;
import com.Stock.Market.Models.User;
import com.Stock.Market.Repositories.ActivityRepo;
import com.Stock.Market.Repositories.PortfolioRepo;
import com.Stock.Market.Repositories.UserRepo;
import com.Stock.Market.Services.Utility.APICallingMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ActivityService {

    @Autowired
    ActivityRepo activityRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    PortfolioRepo portfolioRepo;

    public StockActivity buyStock(StockOrderRequest order, String email) throws IOException {
        User user = userRepo.findByUserEmail(email);
        StockData stockData = APICallingMethod.apiCallingMethodByStock(order.getStockName());
        StockOrder stockOrder = new StockOrder();
        StockActivity stockActivity = new StockActivity();
        double amount = Double.parseDouble(stockData.getCurrentPrice())* order.getQuantity();

        if(user.getStatus().equals("Logged in")){
            if(user.getFund() >= amount){
                stockOrder.setUserName(user.getUserName());
                stockOrder.setEmail(email);
                stockOrder.setStockName(order.getStockName());
                stockOrder.setStockPrice(Double.parseDouble(stockData.getCurrentPrice()));
                stockOrder.setQuantity(order.getQuantity());
                stockOrder.setTotalAmount(amount);
                stockOrder.setStatus("BUY");
                activityRepo.save(stockOrder);
                user.setFund(user.getFund()-amount);
                userRepo.save(user);

                stockActivity.setStockName(order.getStockName());
                stockActivity.setStockPrice(Double.parseDouble(stockData.getCurrentPrice()));
                stockActivity.setQuantity(order.getQuantity());
                stockActivity.setTotalAmount(amount);
                stockActivity.setStatus("BUY");
                stockActivity.setMessege("Success");

                if(!portfolioRepo.existsByEmailAndSymbol(email,stockActivity.getStockName())){
                    Portfolio portfolio = new Portfolio();

                    portfolio.setEmail(email);
                    portfolio.setBuyPrice(stockActivity.getStockPrice());
                    portfolio.setSymbol(stockActivity.getStockName());
                    portfolio.setQuantity(stockActivity.getQuantity());
                    portfolio.setInvestedAmount(amount);
                    portfolio.setCurrentPrice(Double.parseDouble(stockData.getCurrentPrice()));
                    portfolio.setCurrentAmount(portfolio.getQuantity()*Double.parseDouble(stockData.getCurrentPrice()));
                    portfolioRepo.save(portfolio);
                }else{
                    Portfolio portfolio = portfolioRepo.findByEmailAndSymbol(email,stockActivity.getStockName());
                    double newSharesCost = stockActivity.getQuantity() * stockActivity.getStockPrice();
                    double oldSharesCost = portfolio.getQuantity() * portfolio.getBuyPrice();

                    portfolio.setId(portfolio.getId());
                    portfolio.setBuyPrice((newSharesCost+oldSharesCost)/(portfolio.getQuantity())+ stockActivity.getQuantity());
                    portfolio.setSymbol(stockActivity.getStockName());
                    portfolio.setQuantity((portfolio.getQuantity())+ stockActivity.getQuantity());
                    portfolio.setInvestedAmount(newSharesCost + oldSharesCost);
                    portfolio.setCurrentAmount(Double.parseDouble(stockData.getCurrentPrice())* portfolio.getQuantity());
                    portfolio.setCurrentPrice(stockActivity.getStockPrice());
                    portfolioRepo.save(portfolio);

                }
                return stockActivity;

            }else{
                stockActivity.setTotalAmount(0.0);
                stockActivity.setStockName(null);
                stockActivity.setQuantity(0);
                stockActivity.setStatus("Error");
                stockActivity.setMessege("Insufficent Balance");
                stockActivity.setStockPrice(0);
                return stockActivity;
            }
        }
        return stockActivity;
    }

    public StockActivity sellStock(StockOrderRequest order, String email) throws IOException {
        StockOrder stockOrder = new StockOrder();
        User user =userRepo.findByUserEmail(email);
        StockData stockData = APICallingMethod.apiCallingMethodByStock(order.getStockName());
        StockActivity stockActivity = new StockActivity();
        if(user.getStatus().equals("Logged in")){
            if(!portfolioRepo.existsByEmailAndSymbol(email, order.getStockName())){
                stockActivity.setTotalAmount(0.0);
                stockActivity.setStockName(null);
                stockActivity.setQuantity(0);
                stockActivity.setStatus("Error");
                stockActivity.setMessege("Insufficent Stock");
                stockActivity.setStockPrice(0);
                return stockActivity;
            }else{
                Portfolio portfolio = portfolioRepo.findByEmailAndSymbol(email, order.getStockName());
                if (portfolio.getQuantity() == order.getQuantity()) {
                    stockActivity.setTotalAmount(Double.parseDouble(stockData.getCurrentPrice())*order.getQuantity());
                    stockActivity.setStockName(portfolio.getSymbol());
                    stockActivity.setQuantity(order.getQuantity());
                    stockActivity.setStatus("SELL");
                    stockActivity.setMessege("Sold Successfully");
                    stockActivity.setStockPrice(Double.parseDouble(stockData.getCurrentPrice()));

                    stockOrder.setEmail(email);
                    stockOrder.setStockName(order.getStockName());
                    stockOrder.setStockPrice(Double.parseDouble(stockData.getCurrentPrice()));
                    stockOrder.setQuantity(order.getQuantity());
                    stockOrder.setUserName(user.getUserName());
                    stockOrder.setTotalAmount(Double.parseDouble(stockData.getCurrentPrice()) * order.getQuantity());
                    stockOrder.setStatus("SELL");

                    activityRepo.save(stockOrder);
                    user.setFund(user.getFund() + stockOrder.getTotalAmount());
                    userRepo.save(user);

                    portfolioRepo.delete(portfolio);
                    return stockActivity;

                }else if(portfolio.getQuantity() < order.getQuantity()){
                    stockActivity.setTotalAmount(0.0);
                    stockActivity.setStockName(null);
                    stockActivity.setQuantity(0);
                    stockActivity.setStatus("Error");
                    stockActivity.setMessege("Insufficient Stock");
                    stockActivity.setStockPrice(0);
                    return stockActivity;
                }else {
                    portfolio.setEmail(email);
                    portfolio.setSymbol(order.getStockName());
                    portfolio.setCurrentPrice(Double.parseDouble(stockData.getCurrentPrice()) * order.getQuantity());
                    portfolio.setInvestedAmount(portfolio.getInvestedAmount() - (portfolio.getBuyPrice() * order.getQuantity()));
                    portfolio.setQuantity(portfolio.getQuantity() - order.getQuantity());
                    portfolio.setCurrentAmount(Double.parseDouble(stockData.getCurrentPrice())* portfolio.getQuantity());
                    portfolio.setBuyPrice(portfolio.getBuyPrice());
                    portfolio.setCurrentAmount(portfolio.getCurrentPrice() - ((Double.parseDouble(stockData.getCurrentPrice())) * order.getQuantity()));
                    portfolioRepo.save(portfolio);


                    stockActivity.setTotalAmount(Double.parseDouble(stockData.getCurrentPrice()) * order.getQuantity());
                    stockActivity.setStockName(portfolio.getSymbol());
                    stockActivity.setQuantity(order.getQuantity());
                    stockActivity.setStatus("SELL");
                    stockActivity.setMessege("Sold Successfully");
                    stockActivity.setStockPrice(Double.parseDouble(stockData.getCurrentPrice()));

                    stockOrder.setEmail(email);
                    stockOrder.setStockName(order.getStockName());
                    stockOrder.setStockPrice(Double.parseDouble(stockData.getCurrentPrice()));
                    stockOrder.setQuantity(order.getQuantity());
                    stockOrder.setUserName(user.getUserName());
                    stockOrder.setTotalAmount(Double.parseDouble(stockData.getCurrentPrice()) * order.getQuantity());
                    stockOrder.setStatus("SELL");
                    activityRepo.save(stockOrder);

                    user.setFund(user.getFund() + stockOrder.getTotalAmount());
                    userRepo.save(user);
                    return stockActivity;
                }
            }
        }else{
            stockActivity.setTotalAmount(0);
            stockActivity.setStockName(null);
            stockActivity.setQuantity(0);
            stockActivity.setStatus("NONE");
            stockActivity.setMessege("Please First Login");
            stockActivity.setStockPrice(0);
            return stockActivity;
        }
    }
}
