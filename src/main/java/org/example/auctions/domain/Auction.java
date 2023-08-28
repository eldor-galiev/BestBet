package org.example.auctions.domain;

import java.util.*;

import lombok.Getter;
import lombok.Setter;
import org.example.auctions.domain.entity.AuctionEntity;
import org.example.auctions.types.AuctionDuration;
import org.example.auctions.types.AuctionStatus;
import org.example.auctions.types.AuctionType;

@Getter
@Setter
public class Auction extends AuctionEntity {
    private final Long id;
    private String subject;
    private AuctionType auctionType;
    private Integer price;
    private AuctionDuration auctionDuration;
    private final String ownerName;
    private Long winnerBidId;
    private AuctionStatus auctionStatus;

    public Auction(String subject, AuctionType auctionType, Integer price, AuctionDuration auctionDuration, String ownerName) {
        this.id = null;
        this.subject = subject;
        this.auctionType = auctionType;
        this.price = price;
        this.auctionDuration = auctionDuration;
        this.ownerName = ownerName;
        this.auctionStatus = AuctionStatus.UNPUBLISHED;
    }

    public Auction(Long id, Auction auction) {
        this.id = id;
        this.subject = auction.getSubject();
        this.auctionType = auction.getAuctionType();
        this.price = auction.getPrice();
        this.auctionDuration = auction.getAuctionDuration();
        this.ownerName = auction.getOwnerName();
        this.winnerBidId = auction.getWinnerBidId();
        this.auctionStatus = getAuctionStatus();
    }

    public Auction(Long id, String subject, AuctionType auctionType, Integer price, AuctionDuration auctionDuration, String ownerName, Long winnerBidId, AuctionStatus auctionStatus) {
        this.id = id;
        this.subject = subject;
        this.auctionType = auctionType;
        this.price = price;
        this.auctionDuration = auctionDuration;
        this.ownerName = ownerName;
        this.winnerBidId = winnerBidId;
        this.auctionStatus = auctionStatus;
    }

    public void publish() {
        auctionStatus = AuctionStatus.PUBLISHED;
    }

    public void delete() {
        auctionStatus = AuctionStatus.DELETED;
    }

    public void complete() {
        auctionStatus = AuctionStatus.COMPLETED;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Auction auction = (Auction) obj;
        return id == auction.id &&
                Objects.equals(subject, auction.getSubject()) && Objects.equals(auctionType, auction.getAuctionType()) &&
                Objects.equals(auctionDuration, auction.getAuctionDuration()) && Objects.equals(price, auction.getPrice()) &&
                Objects.equals(ownerName, auction.getOwnerName()) && Objects.equals(auctionStatus, auction.getAuctionStatus()) &&
                Objects.equals(winnerBidId, auction.getWinnerBidId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, auctionType, price, auctionDuration, ownerName, winnerBidId, auctionStatus, winnerBidId);
    }
}