package org.example;

import org.example.auctions.repositories.AuctionRepository;
import org.example.auctions.services.Auction;
import org.example.auctions.services.AuctionService;
import org.example.auctions.services.types.AuctionDuration;
import org.example.auctions.services.types.AuctionType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String connectionUrl = "jdbc:mysql://localhost:3306/bestbet";
        try (Connection conn = DriverManager.getConnection(connectionUrl, "root", "eldor1512")) {
            AuctionRepository auctionRepository = new AuctionRepository(conn);
            AuctionService auctionService = new AuctionService(auctionRepository);
            auctionService.publishAuction(26L);
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

}