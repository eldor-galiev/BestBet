package org.example.auctions.repositories;

import org.example.auctions.services.Auction;
import org.example.auctions.services.Bid;
import org.example.auctions.services.types.AuctionDuration;
import org.example.auctions.services.types.AuctionStatus;
import org.example.auctions.services.types.AuctionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuctionRepository {
    private final Connection connection;
    private final Map<Long, Auction> auctionsCache;
    public AuctionRepository(Connection connection) {
        this.connection = connection;
        this.auctionsCache = new HashMap<>();
        readDB();
    }

    public Map<Long, Auction> getAuctionsCache() {
        return auctionsCache;
    }

    public Auction addAuction(String subject, AuctionType auctionType, Integer price, AuctionDuration auctionDuration, String ownerName) {
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("INSERT into auctions (subject, auction_type, price, auction_duration, owner_name, winner_bid_id, auction_status)" +
                    "values ('" + subject + "', '" + auctionType + "', " + price + ", '" + auctionDuration + "', '" + ownerName + "', " + null + ", '" + AuctionStatus.UNPUBLISHED + "')", Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            Long auctionId = keys.getLong(1);
            connection.commit();
            Auction auction = new Auction(auctionId, subject, auctionType, price, auctionDuration, ownerName);
            auctionsCache.put(auctionId, auction);
            return auction;
        } catch (SQLException exception) {
            System.err.println(exception);
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException exp) {
                    System.err.println(exp);
                }
            }
            throw new RuntimeException("Auction persistence operation failed");
        }
    }

    public void updateAuction(Long auctionId, String subject, AuctionType auctionType, Integer price, AuctionDuration auctionDuration) {
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("UPDATE Auctions set subject = '" + subject + "', " + "auction_type = '" + auctionType + "', "
                    + "price = '" + price + "', " + "auction_duration = '" + auctionDuration + "'" + "where id ='" + auctionId + "';");
            statement.executeUpdate();
            connection.commit();
            getAuctionsCache().get(auctionId).setSubject(subject);
            getAuctionsCache().get(auctionId).setType(auctionType);
            getAuctionsCache().get(auctionId).setPrice(price);
            getAuctionsCache().get(auctionId).setDuration(auctionDuration);
        } catch (SQLException exception) {
            System.err.println(exception);
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException exp) {
                    System.err.println(exp);
                }
            }
            throw new RuntimeException("Auction update operation failed");
        }
    }

    public void deleteAuction(Long auctionId) {
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("UPDATE Auctions set auction_status = '" + AuctionStatus.DELETED + "' where id ='" + auctionId + "';");
            statement.executeUpdate();
            connection.commit();
            getAuctionsCache().get(auctionId).delete();
        } catch (SQLException exception) {
            System.err.println(exception);
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException exp) {
                    System.err.println(exp);
                }
            }
            throw new RuntimeException("Auction deletion operation failed");
        }
    }

    public void readDB() {
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            String sql = ("SELECT * FROM auctions ;");
            ResultSet resultSet = statement.executeQuery(sql);
            auctionsCache.clear();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String subject = resultSet.getString("subject");
                AuctionType auctionType = AuctionType.valueOf(resultSet.getString("auction_type"));
                Integer price = resultSet.getInt("price");
                AuctionDuration auctionDuration = AuctionDuration.valueOf(resultSet.getString("auction_duration"));
                String ownerName = resultSet.getString("owner_name");
                String winnerBidId = resultSet.getString("winner_bid_id");
                AuctionStatus auctionStatus = AuctionStatus.valueOf(resultSet.getString("auction_status"));
                Auction auction = new Auction(id, subject, auctionType, price, auctionDuration, ownerName, winnerBidId, auctionStatus);
                auctionsCache.put(id, auction);
            }
        } catch (SQLException exception) {
            System.err.println(exception);
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch (SQLException exp) {
                    System.err.println(exp);
                }
            }
            throw new RuntimeException("Reading the Auctions database operation failed");
        }

    }
}
