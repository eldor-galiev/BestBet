package org.example.auctions.services;

import java.time.LocalDateTime;
import java.util.UUID;

public class Bid {
    private final String id;
    private final Long auctionId;
    private final String ownerName;
    private final Integer amount;
    private final LocalDateTime createdAt;

    public Bid(String ownerName, Long auctionId, Integer amount) {
        this.id = UUID.randomUUID().toString();
        this.auctionId = auctionId;
        this.ownerName = ownerName;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public LocalDateTime getDuration() {
        return createdAt;
    }

    public Integer getAmount() {
        return amount;
    }
}
