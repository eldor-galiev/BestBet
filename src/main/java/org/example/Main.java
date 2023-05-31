package org.example;

import org.example.auctions.repositories.AuctionRepository;
import org.example.auctions.services.AuctionService;
import org.example.auctions.services.types.AuctionDuration;
import org.example.auctions.services.types.AuctionType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String connectionUrl = "jdbc:mysql://localhost:3306/bestbet";
        try (Connection conn = DriverManager.getConnection(connectionUrl, "root", "eldor1512")) {
            AuctionRepository auctionRepository = new AuctionRepository(conn);
            AuctionService auctionService = new AuctionService(auctionRepository);
//            auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
//            auctionService.updateAuction(17L, "Android", AuctionType.DEC, 200, AuctionDuration.H1);
//            System.out.println(auctionRepository.getAuctionsCache());
//            auctionService.deleteAuction(14L);
            System.out.println("ok");
        } catch (SQLException e) {
            System.err.println(e);
        }

    }

}