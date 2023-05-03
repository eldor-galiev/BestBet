package org.example.auctions.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuctionServiceTest {
    private AuctionService auctionService;

    @BeforeEach
    public void beforeEach() {
        auctionService = new AuctionService();
    }

    @Test
    public void verifyCreateAuction() {
        Auction auction = auctionService.createAuction("Android", type.INC, 500, duration.H2, "Eldor");
        assertNotNull(auction);
    }

    @Test
    public void verifyPublishAuction() {
        Auction auction = auctionService.createAuction("Android", type.INC, 500, duration.H2, "Eldor");;
        auctionService.publishAuction(auction.getId());
        assertEquals(status.PUBLISHED, auction.getStatus());
    }

    @Test
    public void verifyDeletedAuction() {
        Auction auction = auctionService.createAuction("Android", type.INC, 500, duration.H2, "Eldor");
        auctionService.deleteAuction(auction.getId());
        assertEquals(status.DELETED, auction.getStatus());
    }

    @Test
    public void verifyUpdateAuction() {
        Auction auction = auctionService.createAuction("Android", type.INC, 500, duration.H2, "Eldor");
        auctionService.updateAuction(auction.getId(), "Iphone", type.INC, 200, duration.H2);
        assertEquals("Iphone", auction.getSubject());
        assertEquals(type.INC, auction.getType());
        assertEquals(200, auction.getPrice());
        assertEquals(duration.H2, auction.getDuration());
    }

    @Test
    public void validateCreateAuctionThrowsWhenPriceLessZero() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            Auction auction = auctionService.createAuction("Android", type.INC, -1, duration.H2, "Eldor");
        });
        assertEquals("Price is less than zero.", thrown.getMessage());
    }

    @Test
    public void validateUpdateAuctionThrowsExceptionWhenAuctionIdNotFound() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            auctionService.updateAuction("non-existent Id", "Iphone", type.INC, 200, duration.H2);
        });
        assertEquals("There is no such Auction Id.", thrown.getMessage());
    }

    @Test
    public void validateUpdateAuctionThrowsExceptionWhenAuctionPublished() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            Auction auction = auctionService.createAuction("Android", type.INC, 500, duration.H2, "Eldor");
            auction.publish();
            auctionService.updateAuction(auction.getId(), "Iphone", type.INC, 200, duration.H2);
        });
        assertEquals("The auction is published, it cannot be updated.", thrown.getMessage());
    }

    @Test
    public void validateUpdateAuctionThrowsExceptionWhenAuctionDeleted() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            Auction auction = auctionService.createAuction("Android", type.INC, 500, duration.H2, "Eldor");
            auction.delete();
            auctionService.updateAuction(auction.getId(), "Iphone", type.INC, 200, duration.H2);
        });
        assertEquals("The auction is deleted, it cannot be updated.", thrown.getMessage());
    }

    @Test
    public void verifyCreateBid() {
        Auction auction = auctionService.createAuction("Android", type.INC, 500, duration.H2, "Eldor");
        Bid bid = auctionService.createBid("Shamil", auction.getId(), 600);
        assertNotNull(bid);
        assertEquals(1, auction.getBids().size());
    }

    @Test
    public void validateCreateBidThrowsExceptionWhenAuctionIdNotFound() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            Bid bid = auctionService.createBid("Shamil", "non-existent Id", 600);
        });
        assertEquals("There is no such Auction Id.", thrown.getMessage());
    }

    @Test
    public void validateCreateBidThrowsExceptionWhenAuctionIncFirstBidAmountInvalid() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->{
            Auction auction = auctionService.createAuction("Android", type.INC, 500, duration.H2, "Eldor");
            Bid bid = auctionService.createBid("Shamil", auction.getId(), 400);
        });
        assertEquals("The bid amount must be greater than the specified price.", thrown.getMessage());
    }

    @Test
    public void validateCreateBidThrowsExceptionWhenAuctionDecFirstBidAmountInvalid() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->{
            Auction auction = auctionService.createAuction("Android", type.DEC, 500, duration.H2, "Eldor");
            Bid bid = auctionService.createBid("Shamil", auction.getId(), 600);
        });
        assertEquals("The bid amount must be less than the specified price.", thrown.getMessage());
    }

    @Test
    public void validateCreateBidThrowsExceptionWhenAuctionIncNotFirstBidAmountInvalid() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->{
            Auction auction = auctionService.createAuction("Android", type.INC, 500, duration.H2, "Eldor");
            Bid firstBid = auctionService.createBid("Shamil", auction.getId(), 700);
            Bid nextBid = auctionService.createBid("Michael", auction.getId(), 600);
        });
        assertEquals("The bid amount must be greater than the previous bid.", thrown.getMessage());
    }

    @Test
    public void validateCreateBidThrowsExceptionWhenAuctionDecNotFirstBidAmountInvalid() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->{
            Auction auction = auctionService.createAuction("Android", type.DEC, 500, duration.H2, "Eldor");
            Bid firstBid = auctionService.createBid("Shamil", auction.getId(), 300);
            Bid nextBid = auctionService.createBid("Michael", auction.getId(), 400);
        });
        assertEquals("The bid amount must be less than the previous bid.", thrown.getMessage());
    }

    @Test
    public void verifyCancelBid() {
        Auction auction = auctionService.createAuction("Android", type.INC, 500, duration.H2, "Eldor");
        Bid bid = auctionService.createBid("Shamil", auction.getId(), 600);
        auctionService.cancelBid(auction.getId(), bid.getId());
        assertEquals(0, auction.getBids().size());
    }

    @Test
    public void validateCancelBidThrowsExceptionWhenAuctionIdNotFound() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            Auction auction = auctionService.createAuction("Android", type.INC, 500, duration.H2, "Eldor");
            Bid bid = auctionService.createBid("Shamil", auction.getId(), 600);
            auctionService.cancelBid("non-existent Id", bid.getId());
        });
        assertEquals("There is no such Auction Id.", thrown.getMessage());
    }

    @Test
    public void validateCancelBidThrowsExceptionWhenBidIdNotFound() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            Auction auction = auctionService.createAuction("Android", type.INC, 500, duration.H2, "Eldor");
            Bid bid = auctionService.createBid("Shamil", auction.getId(), 600);
            auctionService.cancelBid(auction.getId(), "non-existent Id");
        });
        assertEquals("There is no such Bid Id.", thrown.getMessage());
    }
}