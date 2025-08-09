package com.SmartSaudiStockAdvisor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;

public class SignUpDTO {

//    private Long user_id;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

//    private float investAmount; Not specified during registering

//    private Timestamp joinDate;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 24, message = "Name should be between 3 and 24")
    @Pattern(
            regexp = "^[a-zA-Z\\s'-]+$",
            message = "Name can only contain letters, spaces, hyphens, and apostrophes"
    )
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 12, message = "Password must be between 8 and 12")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, and one special character"
    )
    private String password;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 24, message = "Username should be between 3 and 24")
    @Pattern(
            regexp = "^[a-zA-Z0-9_-]+$",
            message = "Username can only contain letters, numbers, underscores, and hyphens"
    )
    private String username;

    public SignUpDTO() {
    }

    public SignUpDTO(Long user_id, String email, float investAmount, Timestamp joinDate, String name, String password, String username) {
//        this.user_id = user_id;
        this.email = email;
//        this.investAmount = investAmount;
//        this.joinDate = new Timestamp(System.currentTimeMillis());
        this.name = name;
        this.password = password;
        this.username = username;
    }
//
//    public Long getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(Long user_id) {
//        this.user_id = user_id;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public float getInvestAmount() {
//        return investAmount;
//    }
//
//    public void setInvestAmount(float investAmount) {
//        this.investAmount = investAmount;
//    }

//    public Timestamp getJoinDate() {
//        return joinDate;
//    }
//
//    public void setJoinDate(Timestamp joinDate) {
//        this.joinDate = joinDate;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "SignUpDTO{" +
//                "user_id=" + user_id +
                ", email='" + email + '\'' +
//                ", joinDate=" + joinDate +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
