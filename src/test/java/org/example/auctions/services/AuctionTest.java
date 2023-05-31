package org.example.auctions.services;

import org.example.auctions.services.types.AuctionDuration;
import org.example.auctions.services.types.AuctionStatus;
import org.example.auctions.services.types.AuctionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuctionTest {
    @Test
    public void verifyCreateAuction() {
        Auction auction = new Auction("Android", AuctionType.INC, 600, AuctionDuration.H2, "Eldor");
        assertNotNull(auction.getId());
        assertEquals("Android", auction.getSubject());
        assertEquals(AuctionType.INC, auction.getType());
        assertEquals(600, auction.getPrice());
        assertEquals(AuctionDuration.H2, auction.getDuration());
        assertEquals("Eldor", auction.getOwnerName());
        assertEquals(0, auction.getBids().size());
        assertNull(auction.getWinnerBidId());
        assertEquals(AuctionStatus.UNPUBLISHED, auction.getStatus());
    }
}