package org.example.auctions.services;

import java.sql.*;
import java.util.*;
import org.example.auctions.services.types.AuctionDuration;
import org.example.auctions.services.types.AuctionStatus;
import org.example.auctions.services.types.AuctionType;

public class Auction {
    private final Long id;
    private String subject;
    private AuctionType auctionType;
    private Integer price;
    private AuctionDuration auctionDuration;
    private final String ownerName;
    private final ArrayList<Bid> bids;
    private String winnerBidId;
    private AuctionStatus auctionStatus;


    public Auction(Long id, String subject, AuctionType auctionType, Integer price, AuctionDuration auctionDuration, String ownerName) {
        this.id = id;
        this.subject = subject;
        this.auctionType = auctionType;
        this.price = price;
        this.auctionDuration = auctionDuration;
        this.ownerName = ownerName;
        this.bids = new ArrayList<>();
        this.auctionStatus = AuctionStatus.UNPUBLISHED;
    }

    public Auction(Long id, String subject, AuctionType auctionType, Integer price, AuctionDuration auctionDuration, String ownerName, String winnerBidId, AuctionStatus auctionStatus) {
        this.id = id;
        this.subject = subject;
        this.auctionType = auctionType;
        this.price = price;
        this.auctionDuration = auctionDuration;
        this.ownerName = ownerName;
        this.bids = new ArrayList<>();
        this.winnerBidId = winnerBidId;
        this.auctionStatus = auctionStatus;
    }

    public Long getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public AuctionType getType() {
        return auctionType;
    }

    public Integer getPrice() {
        return price;
    }

    public AuctionDuration getDuration() {
        return auctionDuration;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public ArrayList<Bid> getBids() {
        return bids;
    }

    public String getWinnerBidId() {
        return winnerBidId;
    }

    public AuctionStatus getStatus() {
        return auctionStatus;
    }

    public void publish() {
        this.auctionStatus = AuctionStatus.PUBLISHED;
    }

    public void delete() {
        this.auctionStatus = AuctionStatus.DELETED;
    }

    public void setSubject(String newSubject) {
        this.subject = newSubject;
    }

    public void setType(AuctionType newType) {
        this.auctionType = newType;
    }

    public void setPrice(Integer newPrice) {
        this.price = newPrice;
    }

    public void setDuration(AuctionDuration newDuration) {
        this.auctionDuration = newDuration;
    }

}