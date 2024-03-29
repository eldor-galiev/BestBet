package org.example.auctions.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.example.auctions.entity.AuctionEntity;
import org.example.auctions.domain.Auction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class AuctionRepository{

    @PersistenceContext
    private EntityManager em;

    public List<Auction> getAllAuctions() {
        TypedQuery<AuctionEntity> list = em.createQuery("select t from " + AuctionEntity.class.getCanonicalName() + " t order by t.id", AuctionEntity.class);
        return list.getResultList().stream().map(this::transformEntityToObject).toList();
    }

    public Auction getAuctionById(Long auctionId) {
        String sql = "select t from " + AuctionEntity.class.getCanonicalName() + " t where t.id = :id";
        TypedQuery<AuctionEntity> list = em.createQuery(sql, AuctionEntity.class)
                .setParameter("id", auctionId);
        return list.getResultList().stream().map(this::transformEntityToObject).toList().get(0);
    }

    public void addAuction(Auction auction) {
        try {
            em.persist(transformObjectToEntity(auction));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAuction(Auction auction) {
        AuctionEntity auctionEntity = transformObjectToEntity(auction);
        em.merge(auctionEntity);
    }

    private Auction transformEntityToObject(AuctionEntity auctionEntity) {
        return new Auction(auctionEntity.getId(), auctionEntity.getSubject(), auctionEntity.getAuctionType(), auctionEntity.getPrice(),
                auctionEntity.getAuctionDuration(), auctionEntity.getOwnerName(), auctionEntity.getWinnerBidId(), auctionEntity.getAuctionStatus());
    }

    private AuctionEntity transformObjectToEntity(Auction auction) {
        AuctionEntity auctionEntity = new AuctionEntity();

        auctionEntity.setId(auction.getId());
        auctionEntity.setSubject(auction.getSubject());
        auctionEntity.setAuctionType(auction.getAuctionType());
        auctionEntity.setPrice(auction.getPrice());
        auctionEntity.setAuctionDuration(auction.getAuctionDuration());
        auctionEntity.setOwnerName(auction.getOwnerName());
        auctionEntity.setWinnerBidId(auction.getWinnerBidId());
        auctionEntity.setAuctionStatus(auction.getAuctionStatus());

        return auctionEntity;
    }
}
