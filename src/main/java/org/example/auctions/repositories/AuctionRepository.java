package org.example.auctions.repositories;

import org.example.auctions.services.Auction;
import org.example.auctions.services.types.AuctionDuration;
import org.example.auctions.services.types.AuctionStatus;
import org.example.auctions.services.types.AuctionType;

import java.sql.*;
import java.util.ArrayList;

public class AuctionRepository {
    private final Connection connection;

    public AuctionRepository(Connection connection) {
        this.connection = connection;
    }

    public Auction getAuctionById(Long auctionId) {
        try {
            connection.setAutoCommit(false);
            String sql = ("SELECT * FROM auctions where id = ?");
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, auctionId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String subject = resultSet.getString("subject");
                AuctionType auctionType = AuctionType.valueOf(resultSet.getString("auction_type"));
                Integer price = resultSet.getInt("price");
                AuctionDuration auctionDuration = AuctionDuration.valueOf(resultSet.getString("auction_duration"));
                String ownerName = resultSet.getString("owner_name");
                String winnerBidId = resultSet.getString("winner_bid_id");
                AuctionStatus auctionStatus = AuctionStatus.valueOf(resultSet.getString("auction_status"));
                Auction auction = new Auction(id, subject, auctionType, price, auctionDuration, ownerName, winnerBidId, auctionStatus);
                return auction;
            } else {
                throw new RuntimeException("There is no auction with such id in the auctions database.");
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
            throw new RuntimeException("Getting the auction by id from database operation failed");
        }
    }

    public ArrayList <Auction> getAllAuctions() {
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            String sql = ("SELECT * FROM auctions");
            ResultSet resultSet = statement.executeQuery(sql);
            ArrayList<Auction> auctions = new ArrayList<>();
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
                auctions.add(auction);
            }
            return auctions;
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

    public Long addAuction(Auction auction) {
        try {
            connection.setAutoCommit(false);
            String sql = "INSERT into auctions" +
                    "(subject, auction_type, price, auction_duration, owner_name, winner_bid_id, auction_status)" +
                    "values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, auction.getSubject());
            statement.setString(2, auction.getType().toString());
            statement.setInt(3, auction.getPrice());
            statement.setString(4, auction.getDuration().toString());
            statement.setString(5, auction.getOwnerName());
            statement.setString(6, auction.getWinnerBidId());
            statement.setString(7, auction.getStatus().toString());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            Long auctionId = keys.getLong(1);
            connection.commit();
            return auctionId;
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

    public void deleteAuction(Long auctionId) {
        try {
            connection.setAutoCommit(false);
            String sql = "UPDATE Auctions set auction_status = ? where id = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, AuctionStatus.DELETED.toString());
            statement.setLong(2, auctionId);
            statement.executeUpdate();
            connection.commit();
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

    public void updateAuction(Long auctionId, Auction auction) {
        try {
            connection.setAutoCommit(false);
            String sql = "UPDATE Auctions set subject = ?, auction_type = ?, price = ?, auction_duration = ?, auction_status = ? where id = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, auction.getSubject());
            statement.setString(2, auction.getType().toString());
            statement.setInt(3, auction.getPrice());
            statement.setString(4, auction.getDuration().toString());
            statement.setString(5, auction.getStatus().toString());
            statement.setLong(6, auctionId);
            statement.executeUpdate();
            connection.commit();
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
}
