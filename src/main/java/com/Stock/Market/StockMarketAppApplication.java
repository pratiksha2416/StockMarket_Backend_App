package com.Stock.Market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockMarketAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockMarketAppApplication.class, args);
	}

}
