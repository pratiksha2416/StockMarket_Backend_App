package com.Stock.Market.Models.DTO;

import lombok.Data;

@Data
public class ResetDTO {
    String email;
    String otp;
    String newPass;
}
