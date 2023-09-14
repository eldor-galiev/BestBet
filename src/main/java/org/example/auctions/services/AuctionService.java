package org.example.auctions.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


import lombok.RequiredArgsConstructor;
import org.example.auctions.domain.Auction;
import org.example.auctions.domain.Bid;
import org.example.auctions.repositories.AuctionRepository;
import org.example.auctions.repositories.BidRepository;
import org.example.auctions.types.AuctionDuration;
import org.example.auctions.types.AuctionStatus;
import org.example.auctions.types.AuctionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;

    private final BidRepository bidRepository;

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

        auction.delete();

        auctionRepository.updateAuction(auction);
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

        determineAuctionWinner(auction);

        auctionRepository.updateAuction(auction);
    }

    @Transactional
    public void determineAuctionWinner(Auction auction) {
        List<Bid> bids = bidRepository.getBidsByAuctionId(auction.getId());
        if (auction.getAuctionType() == AuctionType.INC) {
            bids.stream().
                    max(Comparator.comparingInt(Bid::getAmount)).
                    ifPresent(winnerBid -> auction.setWinnerBidId(winnerBid.getId()));
        }

        if (auction.getAuctionType() == AuctionType.DEC) {
            bids.stream()
                    .min(Comparator.comparingInt(Bid::getAmount)).
                    ifPresent(winnerBid -> auction.setWinnerBidId(winnerBid.getId()));
        }
    }

    @Transactional
    public void createBid(Long auctionId, String ownerName, Integer amount) {
        Auction auction = auctionRepository.getAuctionById(auctionId);
        List<Bid> bids = bidRepository.getBidsByAuctionId(auctionId);

        Bid lastBid = bids.stream().max(Comparator.comparing(Bid::getCreatedAt)).orElse(null);

        if (lastBid != null) {
            if (lastBid.getAmount() >= amount && auction.getAuctionType() == AuctionType.INC) {
                throw new RuntimeException("The bet must be better than the previous one.");
            }

            if (lastBid.getAmount() <= amount && auction.getAuctionType() == AuctionType.DEC) {
                throw new RuntimeException("The bet must be better than the previous one.");
            }
        }

        Bid bid = new Bid(auctionId, ownerName, amount);
        bidRepository.addBid(bid);
    }

    @Transactional
    public List<Bid> getBidsByAuctionId(Long auctionId) {
        return bidRepository.getBidsByAuctionId(auctionId);
    }
}