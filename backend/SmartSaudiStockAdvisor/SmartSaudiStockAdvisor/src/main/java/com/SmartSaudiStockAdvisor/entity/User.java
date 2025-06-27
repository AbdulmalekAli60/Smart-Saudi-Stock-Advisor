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

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "invest_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal investAmount;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WatchList> watchLists;

    public User() {
        this.joinDate = new Timestamp(System.currentTimeMillis());
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
    }
}
