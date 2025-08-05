package com.SmartSaudiStockAdvisor.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Column(name = "join_date")
    private Timestamp joinDate; // make the initialization in def constructor

    @Column(name = "password", nullable = false, length = 255, unique = true, columnDefinition = "CITEXT")
    private String password;

    @Column(name = "email", nullable = false, length = 255, unique = true, columnDefinition = "CITEXT")
    private String email;

    @Column(name = "invest_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal investAmount;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WatchList> watchLists;

    public User() {
//        this.joinDate = new Timestamp(System.currentTimeMillis());
    }

    public User(Long userId, String name, String username, String password, String email, BigDecimal investAmount, List<WatchList> watchLists) {
        this();
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.investAmount = investAmount;
        this.watchLists = watchLists;
        this.joinDate = new Timestamp(System.currentTimeMillis());
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public List<WatchList> getWatchLists() {
        return watchLists;
    }

    public void setWatchLists(List<WatchList> watchLists) {
        this.watchLists = watchLists;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", joinDate=" + joinDate +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", investAmount=" + investAmount +
                ", watchLists=" + watchLists +
                '}';
    }
}
