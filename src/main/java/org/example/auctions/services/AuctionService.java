package org.example.auctions.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import org.example.auctions.repositories.AuctionRepository;
import org.example.auctions.services.types.AuctionDuration;
import org.example.auctions.services.types.AuctionStatus;
import org.example.auctions.services.types.AuctionType;

public class AuctionService {
    private final AuctionRepository auctionRepository;

    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    public Auction createAuction(String subject, AuctionType auctionType, Integer price, AuctionDuration auctionDuration, String ownerName) {
        if (price <= 0) {
            throw new RuntimeException("Price is less than zero.");
        }
        return auctionRepository.addAuction(subject, auctionType, price, auctionDuration, ownerName);
    }

    public void publishAuction(String auctionId) {
        if (auctionRepository.getAuctionsCache().get(auctionId) == null) {
            throw new RuntimeException("There is no such Auction Id.");
        }
        Auction auction = auctionRepository.getAuctionsCache().get(auctionId);
        if (auction.getStatus() == AuctionStatus.PUBLISHED) {
            throw new RuntimeException("The auction has already been published.");
        }
        if (auction.getStatus() == AuctionStatus.DELETED) {
            throw new RuntimeException("The auction has already been deleted.");
        }
        if (auction.getStatus() == AuctionStatus.COMPLETED) {
            throw new RuntimeException("The auction has already been completed.");
        }
        auction.publish();
    }

    public void deleteAuction(Long auctionId) {
        if (auctionRepository.getAuctionsCache().get(auctionId) == null) {
            throw new RuntimeException("There is no such Auction Id.");
        }
        Auction auction = auctionRepository.getAuctionsCache().get(auctionId);
        if (auction.getStatus() == AuctionStatus.DELETED) {
            throw new RuntimeException("The auction has already been deleted.");
        }
        if (auction.getStatus() == AuctionStatus.COMPLETED) {
            throw new RuntimeException("The auction has already been completed.");
        }
        auctionRepository.deleteAuction(auctionId);
    }

    public void updateAuction(Long auctionId, String newSubject, AuctionType newType, Integer newPrice, AuctionDuration newDuration) {
        if (auctionRepository.getAuctionsCache().get(auctionId) == null) {
            throw new RuntimeException("There is no such Auction Id.");
        }
        if (auctionRepository.getAuctionsCache().get(auctionId).getStatus() == AuctionStatus.PUBLISHED) {
            throw new RuntimeException("The auction is published, it cannot be updated.");
        }
        if (auctionRepository.getAuctionsCache().get(auctionId).getStatus() == AuctionStatus.DELETED) {
            throw new RuntimeException("The auction is deleted, it cannot be updated.");
        }
        auctionRepository.updateAuction(auctionId, newSubject, newType, newPrice, newDuration);
    }

    public Bid createBid(String ownerName, String auctionId, Integer amount) {
        if (!auctionRepository.getAuctionsCache().containsKey(auctionId)) {
            throw new RuntimeException("There is no such Auction Id.");
        }
        Auction auction = auctionRepository.getAuctionsCache().get(auctionId);
        if (auction.getBids().size() == 0) {
            if (auction.getType() == AuctionType.INC && auction.getPrice() > amount) {
                throw new RuntimeException("The bid amount must be greater than the specified price.");
            }
            if (auction.getType() == AuctionType.DEC && auction.getPrice() < amount) {
                throw new RuntimeException("The bid amount must be less than the specified price.");
            }
        }
        else {
            if (auction.getType() == AuctionType.INC && auction.getBids().get(auction.getBids().size() - 1).getAmount() > amount) {
                throw new RuntimeException("The bid amount must be greater than the previous bid.");
            }
            if (auction.getType() == AuctionType.DEC && auction.getBids().get(auction.getBids().size() - 1).getAmount() < amount) {
                throw new RuntimeException("The bid amount must be less than the previous bid.");
            }
        }
        Bid bid = new Bid(ownerName, auctionId, amount);
        auction.getBids().add(bid);
        return bid;
    }

    public void cancelBid(String auctionId, String bidId) {
        if (auctionRepository.getAuctionsCache().get(auctionId) == null) {
            throw new RuntimeException("There is no such Auction Id.");
        }
        ArrayList<Bid> bids = auctionRepository.getAuctionsCache().get(auctionId).getBids();
        int bidIndex = -1;
        for (int i = 0; i < bids.size(); i++) {
            if (Objects.equals(bids.get(i).getId(), bidId)) {
                bidIndex = i;
                break;
            }
        }
        if (bidIndex >= 0) {
            auctionRepository.getAuctionsCache().get(auctionId).getBids().remove(bidIndex);
        }
        else {
            throw new RuntimeException("There is no such Bid Id.");
        }
    }
}
