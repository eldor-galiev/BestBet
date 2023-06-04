package org.example;

import org.example.auctions.repositories.AuctionRepository;
import org.example.auctions.services.AuctionService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String connectionUrl = "jdbc:mysql://localhost:3306/bestbet";
        try (Connection conn = DriverManager.getConnection(connectionUrl, "root", "eldor1512")) {
            AuctionRepository auctionRepository = new AuctionRepository(conn);
            AuctionService auctionService = new AuctionService(auctionRepository);
//            Long newAuctionId = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
//            auctionService.updateAuction(newAuctionId, "Car", AuctionType.DEC, 200, AuctionDuration.H1);
//            auctionService.deleteAuction(newAuctionId);
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

}