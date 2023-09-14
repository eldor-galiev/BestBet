package org.example.auctions.domain;

import lombok.Getter;
import lombok.Setter;
import org.example.auctions.entity.BidEntity;

import java.time.LocalDateTime;

@Getter
@Setter
public class Bid extends BidEntity {
    private final Long id;
    private final Long auctionId;
    private final String ownerName;
    private final Integer amount;
    private final LocalDateTime createdAt;

    public Bid(Long auctionId, String ownerName, Integer amount) {
        this.id = null;
        this.auctionId = auctionId;
        this.ownerName = ownerName;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    public Bid(Long id, Long auctionId, String ownerName, Integer amount, LocalDateTime createdAt) {
        this.id = id;
        this.auctionId = auctionId;
        this.ownerName = ownerName;
        this.amount = amount;
        this.createdAt = createdAt;
    }
}