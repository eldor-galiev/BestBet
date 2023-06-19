package org.example.auctions.services;

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

    public Auction(String subject, AuctionType auctionType, Integer price, AuctionDuration auctionDuration, String ownerName) {
        this.id = null;
        this.subject = subject;
        this.auctionType = auctionType;
        this.price = price;
        this.auctionDuration = auctionDuration;
        this.ownerName = ownerName;
        this.bids = new ArrayList<>();
        this.auctionStatus = AuctionStatus.UNPUBLISHED;
    }

    public Auction(Long id, Auction auction) {
        this.id = id;
        this.subject = auction.getSubject();
        this.auctionType = auction.getType();
        this.price = auction.getPrice();
        this.auctionDuration = auction.getDuration();
        this.ownerName = auction.getOwnerName();
        this.bids = auction.getBids();
        this.winnerBidId = auction.getWinnerBidId();
        this.auctionStatus = getStatus();
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

    public void setSubject(String newSubject) {
        subject = newSubject;
    }

    public void setType(AuctionType newType) {
        auctionType = newType;
    }

    public void setPrice(Integer newPrice) {
        price = newPrice;
    }

    public void setDuration(AuctionDuration newDuration) {
        auctionDuration = newDuration;
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
                Objects.equals(subject, auction.getSubject()) && Objects.equals(auctionType, auction.getType()) &&
                Objects.equals(auctionDuration, auction.getDuration()) && Objects.equals(price, auction.getPrice()) &&
                Objects.equals(ownerName, auction.getOwnerName()) && Objects.equals(auctionStatus, auction.getStatus()) &&
                Objects.equals(winnerBidId, auction.getWinnerBidId()) && Objects.equals(bids, auction.getBids());
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, auctionType, price, auctionDuration, ownerName, winnerBidId, auctionStatus, winnerBidId, bids);
    }
}