package com.SmartSaudiStockAdvisor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignUpDTO {

    @NotBlank(message = "{validation.sign-up.email.not-blank}")
    @Email(message = "{validation.sign-up.email.email}")
    private String email;

    @NotBlank(message = "{validation.sign-up.name.not-blank}")
    @Size(min = 3, max = 24, message = "{validation.sign-up.name.size}")
    @Pattern(
            regexp = "^[a-zA-Z\\s'\\u0600-\\u06FF-]+$",
            message = "{validation.sign-up.name.pattern}"
    )
    private String name;

    @NotBlank(message = "{validation.sign-up.password.not-blank}")
    @Size(min = 8, max = 16, message = "{validation.sign-up.password.size}")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "{validation.sign-up.password.pattern}"
    )
    private String password;

    @NotBlank(message = "{validation.sign-up.username.not-blank}")
    @Size(min = 3, max = 24, message = "{validation.sign-up.username.size}")
    @Pattern(
            regexp = "^[a-zA-Z0-9_-]+$",
            message = "{validation.sign-up.username.pattern}"
    )
    private String username;

    public SignUpDTO() {
    }

    public SignUpDTO( String email, String name, String password, String username) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
