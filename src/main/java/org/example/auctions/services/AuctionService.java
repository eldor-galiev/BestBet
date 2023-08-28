package org.example.auctions.services;

import java.util.List;


import lombok.RequiredArgsConstructor;
import org.example.auctions.domain.Auction;
import org.example.auctions.repositories.AuctionRepository;
import org.example.auctions.types.AuctionDuration;
import org.example.auctions.types.AuctionStatus;
import org.example.auctions.types.AuctionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;

    @Transactional
    public List<Auction> getCollection() {
        return auctionRepository.getAllAuctions();
    }

    @Transactional
    public void createAuction(String subject, AuctionType auctionType, Integer price, AuctionDuration auctionDuration, String ownerName) {
        if (price <= 0) {
            throw new RuntimeException("Price is less than zero.");
        }

        Auction auction = new Auction(subject, auctionType, price, auctionDuration, ownerName);

        auctionRepository.addAuction(auction);
    }

    @Transactional
    public void deleteAuction(Long auctionId) {
        Auction auction = auctionRepository.getAuctionById(auctionId);
        if (auction.getAuctionStatus() == AuctionStatus.DELETED) {
            throw new RuntimeException("The auction has already been deleted.");
        }
        if (auction.getAuctionStatus() == AuctionStatus.COMPLETED) {
            throw new RuntimeException("The auction has already been completed.");
        }

        auctionRepository.deleteAuction(auctionId);
    }

    @Transactional
    public void updateAuction(Long auctionId, String newSubject, AuctionType newType, Integer newPrice, AuctionDuration newDuration) {
        Auction auction = auctionRepository.getAuctionById(auctionId);
        if (auction.getAuctionStatus() == AuctionStatus.PUBLISHED) {
            throw new RuntimeException("The auction is published, it cannot be updated.");
        }
        if (auction.getAuctionStatus() == AuctionStatus.DELETED) {
            throw new RuntimeException("The auction is deleted, it cannot be updated.");
        }
        if (auction.getAuctionStatus() == AuctionStatus.COMPLETED) {
            throw new RuntimeException("The auction is completed, it cannot be updated.");
        }

        auction.setSubject(newSubject);
        auction.setAuctionType(newType);
        auction.setPrice(newPrice);
        auction.setAuctionDuration(newDuration);

        auctionRepository.updateAuction(auction);
    }

    @Transactional
    public void publishAuction(Long auctionId) {
        Auction auction = auctionRepository.getAuctionById(auctionId);
        if (auction.getAuctionStatus() == AuctionStatus.PUBLISHED) {
            throw new RuntimeException("The auction has already been published.");
        }
        if (auction.getAuctionStatus() == AuctionStatus.DELETED) {
            throw new RuntimeException("The auction has already been deleted.");
        }
        if (auction.getAuctionStatus() == AuctionStatus.COMPLETED) {
            throw new RuntimeException("The auction has already been completed.");
        }

        auction.publish();

        auctionRepository.updateAuction(auction);
    }

    @Transactional
    public void completeAuction(Long auctionId) {
        Auction auction = auctionRepository.getAuctionById(auctionId);
        if (auction.getAuctionStatus() == AuctionStatus.DELETED) {
            throw new RuntimeException("The auction has already been deleted.");
        }
        if (auction.getAuctionStatus() == AuctionStatus.COMPLETED) {
            throw new RuntimeException("The auction has already been completed.");
        }

        auction.complete();

        auctionRepository.updateAuction(auction);
    }
//
//    @Transactional
//    public Bid createBid(String ownerName, Long auctionId, Integer amount) {
//        Auction auction = auctionRepository.getAuctionById(auctionId);
//        if (auction.getBids().size() == 0) {
//            if (auction.getAuctionType() == AuctionType.INC && auction.getPrice() > amount) {
//                throw new RuntimeException("The bid amount must be greater than the specified price.");
//            }
//            if (auction.getAuctionType() == AuctionType.DEC && auction.getPrice() < amount) {
//                throw new RuntimeException("The bid amount must be less than the specified price.");
//            }
//        }
//        else {
//            if (auction.getAuctionType() == AuctionType.INC && auction.getBids().get(auction.getBids().size() - 1).getAmount() > amount) {
//                throw new RuntimeException("The bid amount must be greater than the previous bid.");
//            }
//            if (auction.getAuctionType() == AuctionType.DEC && auction.getBids().get(auction.getBids().size() - 1).getAmount() < amount) {
//                throw new RuntimeException("The bid amount must be less than the previous bid.");
//            }
//        }
//        Bid bid = new Bid(ownerName, auctionId, amount);
//        auction.getBids().add(bid);
//        return bid;
//    }
//
//    @Transactional
//    public void cancelBid(Long auctionId, String bidId) {
//        Auction auction = auctionRepository.getAuctionById(auctionId);
//        ArrayList<Bid> bids = auction.getBids();
//        int bidIndex = -1;
//        for (int i = 0; i < bids.size(); i++) {
//            if (Objects.equals(bids.get(i).getId(), bidId)) {
//                bidIndex = i;
//                break;
//            }
//        }
//        if (bidIndex >= 0) {
//            bids.remove(bidIndex);
//        }
//        else {
//            throw new RuntimeException("There is no such Bid Id.");
//        }
//    }
}
