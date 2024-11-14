package com.Stock.Market.Controllers;

import com.Stock.Market.Models.DTO.PortfolioDTO;
import com.Stock.Market.Repositories.PortfolioRepo;
import com.Stock.Market.Services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortfolioController {

    @Autowired
    PortfolioService portfolioService;

    @GetMapping("/get")
    private PortfolioDTO getPortfolio(@RequestParam String email){
        return portfolioService.getPortfolio(email);
    }
}
