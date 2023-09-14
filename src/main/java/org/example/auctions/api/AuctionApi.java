package org.example.auctions.api;

import lombok.RequiredArgsConstructor;
import org.example.auctions.domain.Auction;
import org.example.auctions.domain.Bid;
import org.example.auctions.services.AuctionService;
import org.example.auctions.types.AuctionDuration;
import org.example.auctions.types.AuctionType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auction")
@RequiredArgsConstructor
public class AuctionApi {

    private final AuctionService service;

    @PostMapping("/create")
    public void create(@RequestParam(value = "subject", required = false) String subject,
                       @RequestParam(value = "auctionType", required = false) AuctionType auctionType,
                       @RequestParam(value = "price", required = false) Integer price,
                       @RequestParam(value = "auctionDuration", required = false) AuctionDuration auctionDuration,
                       @RequestParam(value = "ownerName", required = false) String ownerName) {
        service.createAuction(subject, auctionType, price, auctionDuration, ownerName);
    }

    @GetMapping("/read")
    public List<Auction> getCollection() {
        return service.getCollection();
    }

    @PostMapping("/delete")
    public void delete(@RequestParam(value = "id", required = false) Long id) {
        service.deleteAuction(id);
    }

    @PostMapping("/update")
    public void update(@RequestParam(value = "id", required = false) Long auctionId,
                       @RequestParam(value = "subject", required = false) String newSubject,
                       @RequestParam(value = "auctionType", required = false) AuctionType newType,
                       @RequestParam(value = "price", required = false) Integer newPrice,
                       @RequestParam(value = "auctionDuration", required = false) AuctionDuration newDuration) {
        service.updateAuction(auctionId, newSubject, newType, newPrice, newDuration);
    }

    @PostMapping("/publish")
    public void publish(@RequestParam(value = "id", required = false) Long auctionId) {
        service.publishAuction(auctionId);
    }

    @PostMapping("/complete")
    public void complete(@RequestParam(value = "id", required = false) Long auctionId) {
        service.completeAuction(auctionId);
    }

    @PostMapping("/bet")
    public void makeBet(@RequestParam(value = "auctionId", required = false) Long auctionId,
                        @RequestParam(value = "ownerName", required = false) String ownerName,
                        @RequestParam(value = "amount", required = false) Integer amount) {
        service.createBid(auctionId, ownerName, amount);
    }

    @GetMapping("/bids")
    public List<Bid> getBids(@RequestParam(value = "auctionId", required = false) Long auctionId) {
        return service.getBidsByAuctionId(auctionId);
    }

}
