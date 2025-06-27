package com.SmartSaudiStockAdvisor.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "watch_list")
// bookmark
public class WatchList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "watch_list_id")
    private Long watchListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public WatchList() {
    }

    public WatchList(Long watchListId, User user, Company company) {
        this.watchListId = watchListId;
        this.user = user;
        this.company = company;
    }

    public Long getWatchListId() {
        return watchListId;
    }

    public void setWatchListId(Long watchListId) {
        this.watchListId = watchListId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "WatchList{" +
                "watchListId=" + watchListId +
                ", user=" + user +
                ", company=" + company +
                '}';
    }
}
