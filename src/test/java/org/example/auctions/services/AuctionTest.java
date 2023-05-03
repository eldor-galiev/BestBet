package org.example.auctions.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuctionTest {
    @Test
    public void verifyCreateAuction() {
        Auction auction = new Auction("Android", type.INC, 600, duration.H2, "Eldor");
        assertNotNull(auction.getId());
        assertEquals("Android", auction.getSubject());
        assertEquals(type.INC, auction.getType());
        assertEquals(600, auction.getPrice());
        assertEquals(duration.H2, auction.getDuration());
        assertEquals("Eldor", auction.getOwnerName());
        assertEquals(0, auction.getBids().size());
        assertNull(auction.getWinnerBidId());
        assertEquals(status.UNPUBLISHED, auction.getStatus());
    }
}