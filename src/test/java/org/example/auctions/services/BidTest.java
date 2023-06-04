package org.example.auctions.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BidTest {
    @Test
    public void verifyCreateBid() {
        Bid bid = new Bid("Shamil", 1L, 600);
        assertEquals("Shamil", bid.getOwnerName());
        assertEquals("auctionId", bid.getAuctionId());
        assertNotNull(bid.getDuration());
        assertEquals(600, bid.getAmount());
    }
}