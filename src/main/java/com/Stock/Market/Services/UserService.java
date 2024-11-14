package com.Stock.Market.Services;

import com.Stock.Market.Models.DTO.ResetDTO;
import com.Stock.Market.Models.User;
import com.Stock.Market.Repositories.UserRepo;
import com.Stock.Market.Services.Utility.OTPGenerator;
import com.Stock.Market.Services.Utility.PasswordEncrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    EmailService emailService;

    public String registerUser(User user) throws NoSuchAlgorithmException {
        if(userRepo.existsByuserEmail(user.getUserEmail())){
            return "User Already Registerd";
        }
        String hashPass = PasswordEncrypter.hashPasswordWithStaticSecret(user.getUserPassword());
        user.setUserPassword(hashPass);
        userRepo.save(user);
        return "Regidter Successfully";
    }

    public String userLogin(String email, String password) throws NoSuchAlgorithmException {
        if(!userRepo.existsByuserEmail(email)){
            return "Please Register";
        }

        String hashPass = PasswordEncrypter.hashPasswordWithStaticSecret(password);
        User user =userRepo.findByUserEmail(email);

        if(hashPass.equals(user.getUserPassword())){
            user.setStatus("Logged in");
            userRepo.save(user);
            return "Login Succesfully";
        }else{
            return "InValid Credentials";
        }
    }

    public String signoutUser(String email) {
        User user =userRepo.findByUserEmail(email);

        if(user.getStatus().equals("Logged in")){
            user.setStatus(("Log Out"));
            userRepo.save(user);
            return "Logout Suceessfully";
        }else {
            return "Already LogOut";
        }
    }

    public String addFund(String email, double amount) {
        User user =userRepo.findByUserEmail(email);
        if(user.getStatus().equals("Logged in")){
            user.setFund(user.getFund() + amount);
            userRepo.save(user);
            return amount + "added Suceessfully";
        }else {
            return "Please Login First";
        }
    }

    public String resetPassword(String email) {
        if(!userRepo.existsByuserEmail((email))){
            return "User Not Register";
        }
        User user = userRepo.findByUserEmail(email);
        String otp = OTPGenerator.generateOTP();

        user.setOtp(otp);
        userRepo.save(user);
        emailService.sendOtpEmail(email,otp);
        return "OTP Send Successfully";
    }

    public String verifyOTP(ResetDTO user) throws NoSuchAlgorithmException {
        User existingUser =userRepo.findByUserEmail(user.getEmail());
        if(existingUser != null && existingUser.getOtp().equals(user.getOtp())){
            String newHashPass = PasswordEncrypter.hashPasswordWithStaticSecret(user.getNewPass());
            existingUser.setUserPassword(newHashPass);
            userRepo.save(existingUser);
            return "Password Changed";
        }else{
            return "Invalid OTP";
        }

    }

    public String getFund(String email) {
        User user =  userRepo.findByUserEmail(email);
        if(user.getStatus().equals("Logged in")){
            return Double.toString(user.getFund());
        }else{
            return "LogIn Required";
        }
    }
}
