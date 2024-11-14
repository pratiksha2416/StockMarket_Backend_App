package com.Stock.Market.Controllers;

import com.Stock.Market.Models.DTO.ResetDTO;
import com.Stock.Market.Models.User;
import com.Stock.Market.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    private String registerUser(@RequestBody User user) throws NoSuchAlgorithmException{
        return userService.registerUser(user);
    }

    @GetMapping("/login")
    private String loginUser(@RequestParam String email, @RequestParam String Password) throws NoSuchAlgorithmException{
        return userService.userLogin(email, Password);
    }

    @GetMapping("/logout")
    private String signoutUser(@RequestParam String email){
        return userService.signoutUser(email);
    }

    @PostMapping("/addFund")
    private String addFund(@RequestParam String email, double amount) throws NoSuchAlgorithmException{
        return userService.addFund(email,amount);
    }

    @GetMapping("/GetFund")
    private String getFund(@RequestParam String email){
        return userService.getFund(email);
    }

    @PostMapping("/resetPassword")
    private String resetPassword(@RequestParam String email){
        return userService.resetPassword(email);
    }

    @PostMapping("/verifyOTP")
    private String verifyOTP(@RequestBody ResetDTO user) throws NoSuchAlgorithmException{
        return userService.verifyOTP(user);
    }

}
