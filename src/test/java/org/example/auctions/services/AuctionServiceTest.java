package org.example.auctions.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import org.example.auctions.services.types.AuctionDuration;
import org.example.auctions.services.types.AuctionStatus;
import org.example.auctions.services.types.AuctionType;

public class AuctionServiceTest {
    private AuctionService auctionService;

    @BeforeEach
    public void beforeEach() {
        auctionService = new AuctionService(null);
    }

    @Test
    public void verifyCreateAuction() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        assertNotNull(auction);
    }

    @Test
    public void verifyPublishAuction() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        auctionService.publishAuction(auction.getId());
        assertEquals(AuctionStatus.PUBLISHED, auction.getStatus());
    }

    @Test
    public void validatePublishAuctionThrowsExceptionWhenAuctionIdNotFound() {
        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
            auctionService.publishAuction("non-existent Id");
        });
        assertEquals("There is no such Auction Id.", expectedException.getMessage());
    }

    @Test
    public void validatePublishAuctionThrowsExceptionWhenAuctionPublished() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        auctionService.publishAuction(auction.getId());
        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
            auctionService.publishAuction(auction.getId());
        });
        assertEquals("The auction has already been published.", expectedException.getMessage());
    }

    @Test
    public void validatePublishAuctionThrowsExceptionWhenAuctionDeleted() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        auctionService.deleteAuction(auction.getId());
        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
            auctionService.publishAuction(auction.getId());
        });
        assertEquals("The auction has already been deleted.", expectedException.getMessage());
    }

    @Test
    public void verifyDeletedAuction() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        auctionService.deleteAuction(auction.getId());
        assertEquals(AuctionStatus.DELETED, auction.getStatus());
    }

    @Test
    public void validateDeleteAuctionThrowsExceptionWhenAuctionIdNotFound() {
        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
            auctionService.deleteAuction("non-existent Id");
        });
        assertEquals("There is no such Auction Id.", expectedException.getMessage());
    }

    @Test
    public void validateDeleteAuctionThrowsExceptionWhenAuctionDeleted() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        auctionService.deleteAuction(auction.getId());
        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
            auctionService.deleteAuction(auction.getId());
        });
        assertEquals("The auction has already been deleted.", expectedException.getMessage());
    }

    @Test
    public void verifyUpdateAuction() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        auctionService.updateAuction(auction.getId(), "Iphone", AuctionType.INC, 200, AuctionDuration.H2);
        assertEquals("Iphone", auction.getSubject());
        assertEquals(AuctionType.INC, auction.getType());
        assertEquals(200, auction.getPrice());
        assertEquals(AuctionDuration.H2, auction.getDuration());
    }

    @Test
    public void validateCreateAuctionThrowsWhenPriceLessZero() {
        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
            Auction auction = auctionService.createAuction("Android", AuctionType.INC, -1, AuctionDuration.H2, "Eldor");
        });
        assertEquals("Price is less than zero.", expectedException.getMessage());
    }

    @Test
    public void validateUpdateAuctionThrowsExceptionWhenAuctionIdNotFound() {
        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
            auctionService.updateAuction("non-existent Id", "Iphone", AuctionType.INC, 200, AuctionDuration.H2);
        });
        assertEquals("There is no such Auction Id.", expectedException.getMessage());
    }

    @Test
    public void validateUpdateAuctionThrowsExceptionWhenAuctionPublished() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        auction.publish();
        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
            auctionService.updateAuction(auction.getId(), "Iphone", AuctionType.INC, 200, AuctionDuration.H2);
        });
        assertEquals("The auction is published, it cannot be updated.", expectedException.getMessage());
    }

    @Test
    public void validateUpdateAuctionThrowsExceptionWhenAuctionDeleted() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        auction.delete();
        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
            auctionService.updateAuction(auction.getId(), "Iphone", AuctionType.INC, 200, AuctionDuration.H2);
        });
        assertEquals("The auction is deleted, it cannot be updated.", expectedException.getMessage());
    }

    @Test
    public void verifyCreateBid() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        Bid bid = auctionService.createBid("Shamil", auction.getId(), 600);
        assertNotNull(bid);
        assertEquals(1, auction.getBids().size());
    }

    @Test
    public void validateCreateBidThrowsExceptionWhenAuctionIdNotFound() {
        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
            Bid bid = auctionService.createBid("Shamil", "non-existent Id", 600);
        });
        assertEquals("There is no such Auction Id.", expectedException.getMessage());
    }

    @Test
    public void validateCreateBidThrowsExceptionWhenAuctionIncFirstBidAmountInvalid() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        RuntimeException expectedException = assertThrows(RuntimeException.class, () ->{
            Bid bid = auctionService.createBid("Shamil", auction.getId(), 400);
        });
        assertEquals("The bid amount must be greater than the specified price.", expectedException.getMessage());
    }

    @Test
    public void validateCreateBidThrowsExceptionWhenAuctionDecFirstBidAmountInvalid() {
        Auction auction = auctionService.createAuction("Android", AuctionType.DEC, 500, AuctionDuration.H2, "Eldor");
        RuntimeException expectedException = assertThrows(RuntimeException.class, () ->{
            Bid bid = auctionService.createBid("Shamil", auction.getId(), 600);
        });
        assertEquals("The bid amount must be less than the specified price.", expectedException.getMessage());
    }

    @Test
    public void validateCreateBidThrowsExceptionWhenAuctionIncNotFirstBidAmountInvalid() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        Bid firstBid = auctionService.createBid("Shamil", auction.getId(), 700);
        RuntimeException expectedException = assertThrows(RuntimeException.class, () ->{
            Bid nextBid = auctionService.createBid("Michael", auction.getId(), 600);
        });
        assertEquals("The bid amount must be greater than the previous bid.", expectedException.getMessage());
    }

    @Test
    public void validateCreateBidThrowsExceptionWhenAuctionDecNotFirstBidAmountInvalid() {
        Auction auction = auctionService.createAuction("Android", AuctionType.DEC, 500, AuctionDuration.H2, "Eldor");
        Bid firstBid = auctionService.createBid("Shamil", auction.getId(), 300);
        RuntimeException expectedException = assertThrows(RuntimeException.class, () ->{
            Bid nextBid = auctionService.createBid("Michael", auction.getId(), 400);
        });
        assertEquals("The bid amount must be less than the previous bid.", expectedException.getMessage());
    }

    @Test
    public void verifyCancelBid() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        Bid bid = auctionService.createBid("Shamil", auction.getId(), 600);
        auctionService.cancelBid(auction.getId(), bid.getId());
        assertEquals(0, auction.getBids().size());
    }

    @Test
    public void validateCancelBidThrowsExceptionWhenAuctionIdNotFound() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        Bid bid = auctionService.createBid("Shamil", auction.getId(), 600);
        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
            auctionService.cancelBid("non-existent Id", bid.getId());
        });
        assertEquals("There is no such Auction Id.", expectedException.getMessage());
    }

    @Test
    public void validateCancelBidThrowsExceptionWhenBidIdNotFound() {
        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
        Bid bid = auctionService.createBid("Shamil", auction.getId(), 600);
        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
            auctionService.cancelBid(auction.getId(), "non-existent Id");
        });
        assertEquals("There is no such Bid Id.", expectedException.getMessage());
    }
}