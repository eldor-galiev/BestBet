package org.example.auctions.services;

import java.util.ArrayList;
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

    public Long createAuction(String subject, AuctionType auctionType, Integer price, AuctionDuration auctionDuration, String ownerName) {
        if (price <= 0) {
            throw new RuntimeException("Price is less than zero.");
        }

        Auction auction = new Auction(subject, auctionType, price, auctionDuration, ownerName);

        return auctionRepository.addAuction(auction);
    }

    public void deleteAuction(Long auctionId) {
        Auction auction = auctionRepository.getAuctionById(auctionId);
        if (auction.getStatus() == AuctionStatus.DELETED) {
            throw new RuntimeException("The auction has already been deleted.");
        }
        if (auction.getStatus() == AuctionStatus.COMPLETED) {
            throw new RuntimeException("The auction has already been completed.");
        }

        auctionRepository.deleteAuction(auctionId);
    }

    public void updateAuction(Long auctionId, String newSubject, AuctionType newType, Integer newPrice, AuctionDuration newDuration) {
        Auction auction = auctionRepository.getAuctionById(auctionId);
        if (auction.getStatus() == AuctionStatus.PUBLISHED) {
            throw new RuntimeException("The auction is published, it cannot be updated.");
        }
        if (auction.getStatus() == AuctionStatus.DELETED) {
            throw new RuntimeException("The auction is deleted, it cannot be updated.");
        }
        if (auction.getStatus() == AuctionStatus.COMPLETED) {
            throw new RuntimeException("The auction is completed, it cannot be updated.");
        }

        auction.setSubject(newSubject);
        auction.setType(newType);
        auction.setPrice(newPrice);
        auction.setDuration(newDuration);

        auctionRepository.updateAuction(auctionId, auction);
    }

    public void publishAuction(Long auctionId) {
        Auction auction = auctionRepository.getAuctionById(auctionId);
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

        auctionRepository.updateAuction(auctionId, auction);
    }

    public Bid createBid(String ownerName, Long auctionId, Integer amount) {
        Auction auction = auctionRepository.getAuctionById(auctionId);
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

    public void cancelBid(Long auctionId, String bidId) {
        Auction auction = auctionRepository.getAuctionById(auctionId);
        ArrayList<Bid> bids = auction.getBids();
        int bidIndex = -1;
        for (int i = 0; i < bids.size(); i++) {
            if (Objects.equals(bids.get(i).getId(), bidId)) {
                bidIndex = i;
                break;
            }
        }
        if (bidIndex >= 0) {
            bids.remove(bidIndex);
        }
        else {
            throw new RuntimeException("There is no such Bid Id.");
        }
    }
}
