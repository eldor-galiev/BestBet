package org.example.auctions.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.auctions.types.AuctionDuration;
import org.example.auctions.types.AuctionStatus;
import org.example.auctions.types.AuctionType;

@Getter
@Setter

@Entity
@Table(name="auctions")
public class AuctionEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "auction_type")
    private AuctionType auctionType;

    @Column(name = "price")
    private Integer price;

    @Column(name = "auction_duration")
    private AuctionDuration auctionDuration;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "winner_bid_id")
    private Long winnerBidId;

    @Column(name = "auction_status")
    private AuctionStatus auctionStatus;
}
