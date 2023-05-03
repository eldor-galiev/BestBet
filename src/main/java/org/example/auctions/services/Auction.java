package org.example.auctions.services;

import java.util.*;

public class Auction {
    private final String id;
    private String subject;
    private type type;
    private Integer price;
    private duration duration;
    private final String ownerName;
    private final ArrayList<Bid> bids;
    private String winnerBidId;
    private status status;


    public Auction(String subject, type type, Integer price, duration duration, String ownerName) {
        this.id = UUID.randomUUID().toString();
        this.subject = subject;
        this.type = type;
        this.price = price;
        this.duration = duration;
        this.ownerName = ownerName;
        this.bids = new ArrayList<>();
        this.status = org.example.auctions.services.status.UNPUBLISHED;
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public type getType() {
        return type;
    }

    public Integer getPrice() {
        return price;
    }

    public duration getDuration() {
        return duration;
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

    public status getStatus() {
        return status;
    }

    public void publish() {
        this.status = org.example.auctions.services.status.PUBLISHED;
    }

    public void delete() {
        this.status = org.example.auctions.services.status.DELETED;
    }

    public void setSubject(String newSubject) {
        this.subject = newSubject;
    }

    public void setType(org.example.auctions.services.type newType) {
        this.type = newType;
    }

    public void setPrice(Integer newPrice) {
        this.price = newPrice;
    }

    public void setDuration(org.example.auctions.services.duration newDuration) {
        this.duration = newDuration;
    }
}

enum type {
    INC,
    DEC
}

enum duration {
    H1,
    H2,
    H3
}

enum status {
    UNPUBLISHED,
    PUBLISHED,
    DELETED,
    COMPLETED
}