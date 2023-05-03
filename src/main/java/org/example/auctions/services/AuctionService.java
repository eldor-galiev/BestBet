package org.example.auctions.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuctionService {
    private final Map<String, Auction> auctionsCache;

    public AuctionService() {
        this.auctionsCache = new HashMap<>();
    }

    public Auction createAuction(String subject, type type, Integer price, duration duration, String ownerName) {
        if (price <= 0) {
            throw new RuntimeException("Price is less than zero.");
        }
        Auction auction = new Auction(subject, type, price, duration, ownerName);
        this.auctionsCache.put(auction.getId(), auction);
        return auction;
    }

    public void publishAuction(String id) {
        this.auctionsCache.get(id).publish();
    }

    public void deleteAuction(String id) {
        this.auctionsCache.get(id).delete();
    }

    public void updateAuction(String id, String newSubject, type newType, Integer newPrice, duration newDuration) {
        if (this.auctionsCache.get(id) == null) {
            throw new RuntimeException("There is no such Auction Id.");
        }
        if (this.auctionsCache.get(id).getStatus() == status.PUBLISHED) {
            throw new RuntimeException("The auction is published, it cannot be updated.");
        }
        if (this.auctionsCache.get(id).getStatus() == status.DELETED) {
            throw new RuntimeException("The auction is deleted, it cannot be updated.");
        }
        this.auctionsCache.get(id).setSubject(newSubject);
        this.auctionsCache.get(id).setType(newType);
        this.auctionsCache.get(id).setPrice(newPrice);
        this.auctionsCache.get(id).setDuration(newDuration);
    }

    public Bid createBid(String ownerName, String auctionId, Integer amount) {
        if (this.auctionsCache.get(auctionId) == null) {
            throw new RuntimeException("There is no such Auction Id.");
        }
        Auction auction = this.auctionsCache.get(auctionId);
        if (auction.getBids().size() == 0) {
            if (auction.getType() == type.INC && auction.getPrice() > amount) {
                throw new RuntimeException("The bid amount must be greater than the specified price.");
            }
            if (auction.getType() == type.DEC && auction.getPrice() < amount) {
                throw new RuntimeException("The bid amount must be less than the specified price.");
            }
        }
        else {
            if (auction.getType() == type.INC && auction.getBids().get(auction.getBids().size() - 1).getAmount() > amount) {
                throw new RuntimeException("The bid amount must be greater than the previous bid.");
            }
            if (auction.getType() == type.DEC && auction.getBids().get(auction.getBids().size() - 1).getAmount() < amount) {
                throw new RuntimeException("The bid amount must be less than the previous bid.");
            }
        }
        Bid bid = new Bid(ownerName, auctionId, amount);
        this.auctionsCache.get(auctionId).getBids().add(bid);
        return bid;
    }

    public void cancelBid(String auctionId, String bidId) {
        if (this.auctionsCache.get(auctionId) == null) {
            throw new RuntimeException("There is no such Auction Id.");
        }
        ArrayList<Bid> bids = auctionsCache.get(auctionId).getBids();
        int bidIndex = -1;
        for (int i = 0; i < bids.size(); i++) {
            if (Objects.equals(bids.get(i).getId(), bidId)) {
                bidIndex = i;
                break;
            }
        }
        if (bidIndex >= 0) {
            auctionsCache.get(auctionId).getBids().remove(bidIndex);
        }
        else {
            throw new RuntimeException("There is no such Bid Id.");
        }
    }
}
