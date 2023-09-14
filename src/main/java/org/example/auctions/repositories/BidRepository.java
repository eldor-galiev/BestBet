package org.example.auctions.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.example.auctions.domain.Bid;
import org.example.auctions.entity.BidEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class BidRepository{

    @PersistenceContext
    private EntityManager em;

    public List<Bid> getAllBids() {
        String sql = "select t from " + BidEntity.class.getCanonicalName() + " t order by t.id";
        TypedQuery<BidEntity> list = em.createQuery(sql, BidEntity.class);
        return list.getResultList().stream().map(this::transformEntityToObject).toList();
    }

    public Bid getBidById(Long auctionId) {
        String sql = "select t from " + BidEntity.class.getCanonicalName() + " t where t.id = :id";
        TypedQuery<BidEntity> list = em.createQuery(sql, BidEntity.class)
                .setParameter("id", auctionId);
        return list.getResultList().stream().map(this::transformEntityToObject).toList().get(0);
    }

    public List<Bid> getBidsByAuctionId(Long auctionId) {
        String sql = "select t from " + BidEntity.class.getCanonicalName() + " t where auctionId = :auctionId";
        TypedQuery<BidEntity> list = em.createQuery(sql, BidEntity.class).setParameter("auctionId", auctionId);
        return list.getResultList().stream().map(this::transformEntityToObject).toList();
    }

    public void addBid(Bid bid) {
        try {
            em.persist(transformObjectToEntity(bid));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Bid transformEntityToObject(BidEntity bidEntity) {
        return new Bid(bidEntity.getId(), bidEntity.getAuctionId(), bidEntity.getOwnerName(),
                bidEntity.getAmount(), bidEntity.getCreatedAt());
    }

    private BidEntity transformObjectToEntity(Bid bid) {
        BidEntity bidEntity = new BidEntity();

        bidEntity.setId(bid.getId());
        bidEntity.setOwnerName(bid.getOwnerName());
        bidEntity.setAmount(bid.getAmount());
        bidEntity.setCreatedAt(bid.getCreatedAt());

        return bidEntity;
    }
}
