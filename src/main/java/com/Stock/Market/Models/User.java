package com.Stock.Market.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userid;

    private String userName;

    @Column(unique = true)
    private String userEmail;
    private String userPassword;
    private double fund = 0;

    private String status;
    private String otp;

}
