//package org.example.auctions.services;
//
//import org.example.auctions.repositories.AuctionRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//import org.example.auctions.services.types.AuctionDuration;
//import org.example.auctions.services.types.AuctionStatus;
//import org.example.auctions.services.types.AuctionType;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class AuctionServiceTest {
//    private AuctionService auctionService;
//
//    @Mock AuctionRepository auctionRepository;
//
//    @BeforeEach
//    public void beforeEach() {
//        auctionService = new AuctionService(auctionRepository);
//    }
//
//    @Test
//    public void verifyCreateAuction() {
//        Auction newAuction = new Auction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
//        Mockito.when(auctionRepository.addAuction(newAuction)).thenReturn(10L);
//
//        Long auctionId = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
//
//        Mockito.verify(auctionRepository).addAuction(newAuction);
//        assertEquals(10L, auctionId);
//    }
//
//    @Test
//    public void validateCreateAuctionThrowsExceptionWhenPriceLessZero() {
//        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
//            auctionService.createAuction("Android", AuctionType.INC, -1, AuctionDuration.H2, "Eldor");
//        });
//
//        Mockito.verify(auctionRepository, Mockito.never()).addAuction(Mockito.any(Auction.class));
//        assertEquals("Price is less than zero.", expectedException.getMessage());
//    }
//
//    @Test
//    public void verifyDeletedAuction() {
//        Long auctionId = 10L;
//        var existingAuction = new Auction(auctionId, "Android", AuctionType.INC, 200, AuctionDuration.H2, "eldor", null, null);
//        Mockito.when(auctionRepository.getAuctionById(auctionId)).thenReturn(existingAuction);
//        Mockito.doNothing().when(auctionRepository).deleteAuction(auctionId);
//
//        auctionService.deleteAuction(auctionId);
//
//        Mockito.verify(auctionRepository).getAuctionById(auctionId);
//        Mockito.verify(auctionRepository).deleteAuction(auctionId);
//    }
//
//    @Test
//    public void validateDeleteAuctionThrowsExceptionWhenAuctionIdNotFound() {
//        Long auctionId = 0L;
//        Mockito.doThrow(new RuntimeException("There is no auction with such id in the auctions database.")).when(auctionRepository).getAuctionById(auctionId);
//
//        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
//            auctionService.deleteAuction(auctionId);
//        });
//
//        Mockito.verify(auctionRepository).getAuctionById(auctionId);
//        Mockito.verify(auctionRepository, Mockito.never()).deleteAuction(auctionId);
//        assertEquals("There is no auction with such id in the auctions database.", expectedException.getMessage());
//    }
//
//    @Test
//    public void validateDeleteAuctionThrowsExceptionWhenAuctionDeleted() {
//        Long auctionId = 10L;
//        var existingAuction = new Auction(auctionId, "Android", AuctionType.INC, 200, AuctionDuration.H2, "eldor", null, null);
//        existingAuction.delete();
//        Mockito.when(auctionRepository.getAuctionById(auctionId)).thenReturn(existingAuction);
//
//        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
//            auctionService.deleteAuction(auctionId);
//        });
//
//        Mockito.verify(auctionRepository).getAuctionById(auctionId);
//        Mockito.verify(auctionRepository, Mockito.never()).deleteAuction(auctionId);
//        assertEquals("The auction has already been deleted.", expectedException.getMessage());
//    }
//
//    @Test
//    public void validateDeleteAuctionThrowsExceptionWhenAuctionCompleted() {
//        Long auctionId = 10L;
//        var existingAuction = new Auction(auctionId, "Android", AuctionType.INC, 200, AuctionDuration.H2, "eldor", null, null);
//        existingAuction.complete();
//        Mockito.when(auctionRepository.getAuctionById(auctionId)).thenReturn(existingAuction);
//
//        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
//            auctionService.deleteAuction(auctionId);
//        });
//
//        Mockito.verify(auctionRepository).getAuctionById(auctionId);
//        Mockito.verify(auctionRepository, Mockito.never()).deleteAuction(auctionId);
//        assertEquals("The auction has already been completed.", expectedException.getMessage());
//    }
//
//    @Test
//    public void verifyUpdateAuction() {
//        Long auctionId = 10L;
//        var existingAuction = new Auction(auctionId, "Android", AuctionType.INC, 200, AuctionDuration.H2, "eldor", null, null);
//        Mockito.when(auctionRepository.getAuctionById(auctionId)).thenReturn(existingAuction);
//
//        auctionService.updateAuction(auctionId, "Iphone", AuctionType.INC, 200, AuctionDuration.H2);
//
//        Mockito.verify(auctionRepository).getAuctionById(auctionId);
//        Mockito.verify(auctionRepository).updateAuction(auctionId, existingAuction);
//        assertEquals("Iphone", existingAuction.getSubject());
//    }
//
//    @Test
//    public void validateUpdateAuctionThrowsExceptionWhenAuctionIdNotFound() {
//        Long auctionId = 10L;
//        Mockito.doThrow(new RuntimeException("There is no auction with such id in the auctions database.")).when(auctionRepository).getAuctionById(auctionId);
//
//        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
//            auctionService.updateAuction(auctionId, "Iphone", AuctionType.INC, 200, AuctionDuration.H2);
//        });
//
//        Mockito.verify(auctionRepository).getAuctionById(auctionId);
//        Mockito.verify(auctionRepository, Mockito.never()).updateAuction(Mockito.eq(auctionId), Mockito.any(Auction.class));
//        assertEquals("There is no auction with such id in the auctions database.", expectedException.getMessage());
//    }
//
//    @Test
//    public void validateUpdateAuctionThrowsExceptionWhenAuctionPublished() {
//        Long auctionId = 10L;
//        var existingAuction = new Auction(auctionId, "Android", AuctionType.INC, 200, AuctionDuration.H2, "eldor", null, null);
//        existingAuction.publish();
//        Mockito.when(auctionRepository.getAuctionById(auctionId)).thenReturn(existingAuction);
//
//        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
//            auctionService.updateAuction(auctionId, "Iphone", AuctionType.INC, 200, AuctionDuration.H2);
//        });
//
//        Mockito.verify(auctionRepository).getAuctionById(auctionId);
//        Mockito.verify(auctionRepository, Mockito.never()).updateAuction(auctionId, existingAuction);
//        assertEquals("The auction is published, it cannot be updated.", expectedException.getMessage());
//    }
//
//    @Test
//    public void validateUpdateAuctionThrowsExceptionWhenAuctionDeleted() {
//        Long auctionId = 10L;
//        var existingAuction = new Auction(auctionId, "Android", AuctionType.INC, 200, AuctionDuration.H2, "eldor", null, null);
//        existingAuction.delete();
//        Mockito.when(auctionRepository.getAuctionById(auctionId)).thenReturn(existingAuction);
//
//        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
//            auctionService.updateAuction(auctionId, "Iphone", AuctionType.INC, 200, AuctionDuration.H2);
//        });
//
//        Mockito.verify(auctionRepository).getAuctionById(auctionId);
//        Mockito.verify(auctionRepository, Mockito.never()).updateAuction(auctionId, existingAuction);
//        assertEquals("The auction is deleted, it cannot be updated.", expectedException.getMessage());
//    }
//
//    @Test
//    public void validateUpdateAuctionThrowsExceptionWhenAuctionCompleted() {
//        Long auctionId = 10L;
//        var existingAuction = new Auction(auctionId, "Android", AuctionType.INC, 200, AuctionDuration.H2, "eldor", null, null);
//        existingAuction.complete();
//        Mockito.when(auctionRepository.getAuctionById(auctionId)).thenReturn(existingAuction);
//
//        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
//            auctionService.updateAuction(auctionId, "Iphone", AuctionType.INC, 200, AuctionDuration.H2);
//        });
//
//        Mockito.verify(auctionRepository).getAuctionById(auctionId);
//        Mockito.verify(auctionRepository, Mockito.never()).updateAuction(auctionId, existingAuction);
//        assertEquals("The auction is completed, it cannot be updated.", expectedException.getMessage());
//    }
//
//    @Test
//    public void verifyPublishAuction() {
//        Long auctionId = 10L;
//        var existingAuction = new Auction(auctionId, "Android", AuctionType.INC, 200, AuctionDuration.H2, "eldor", null, null);
//        Mockito.when(auctionRepository.getAuctionById(auctionId)).thenReturn(existingAuction);
//
//        auctionService.publishAuction(auctionId);
//
//        Mockito.verify(auctionRepository).getAuctionById(auctionId);
//        Mockito.verify(auctionRepository).updateAuction(auctionId, existingAuction);
//    }
//
//    @Test
//    public void validatePublishAuctionThrowsExceptionWhenAuctionIdNotFound() {
//        Long auctionId = 10L;
//        Mockito.doThrow(new RuntimeException("There is no auction with such id in the auctions database.")).when(auctionRepository).getAuctionById(auctionId);
//
//        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
//            auctionService.publishAuction(auctionId);
//        });
//
//        Mockito.verify(auctionRepository).getAuctionById(auctionId);
//        Mockito.verify(auctionRepository, Mockito.never()).updateAuction(Mockito.eq(auctionId), Mockito.any(Auction.class));
//        assertEquals("There is no auction with such id in the auctions database.", expectedException.getMessage());
//    }
//
//    @Test
//    public void validatePublishAuctionThrowsExceptionWhenAuctionPublished() {
//        Long auctionId = 10L;
//        var existingAuction = new Auction(auctionId, "Android", AuctionType.INC, 200, AuctionDuration.H2, "eldor", null, null);
//        existingAuction.publish();
//        Mockito.when(auctionRepository.getAuctionById(auctionId)).thenReturn(existingAuction);
//
//        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
//            auctionService.publishAuction(auctionId);
//        });
//
//        Mockito.verify(auctionRepository).getAuctionById(auctionId);
//        Mockito.verify(auctionRepository, Mockito.never()).updateAuction(auctionId, existingAuction);
//        assertEquals("The auction has already been published.", expectedException.getMessage());
//    }
//
//    @Test
//    public void validatePublishAuctionThrowsExceptionWhenAuctionDeleted() {
//        Long auctionId = 10L;
//        var existingAuction = new Auction(auctionId, "Android", AuctionType.INC, 200, AuctionDuration.H2, "eldor", null, null);
//        existingAuction.delete();
//        Mockito.when(auctionRepository.getAuctionById(auctionId)).thenReturn(existingAuction);
//
//        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
//            auctionService.publishAuction(auctionId);
//        });
//
//        Mockito.verify(auctionRepository).getAuctionById(auctionId);
//        Mockito.verify(auctionRepository, Mockito.never()).updateAuction(auctionId, existingAuction);
//        assertEquals("The auction has already been deleted.", expectedException.getMessage());
//    }
//
//    @Test
//    public void validatePublishAuctionThrowsExceptionWhenAuctionCompleted() {
//        Long auctionId = 10L;
//        var existingAuction = new Auction(auctionId, "Android", AuctionType.INC, 200, AuctionDuration.H2, "eldor", null, null);
//        existingAuction.complete();
//        Mockito.when(auctionRepository.getAuctionById(auctionId)).thenReturn(existingAuction);
//
//        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
//            auctionService.publishAuction(auctionId);
//        });
//
//        Mockito.verify(auctionRepository).getAuctionById(auctionId);
//        Mockito.verify(auctionRepository, Mockito.never()).updateAuction(auctionId, existingAuction);
//        assertEquals("The auction has already been completed.", expectedException.getMessage());
//    }
//
//
////    @Test
////    public void verifyCreateBid() {
////        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
////        Bid bid = auctionService.createBid("Shamil", auctionId, 600);
////        assertNotNull(bid);
////        assertEquals(1, auction.getBids().size());
////    }
////
////    @Test
////    public void validateCreateBidThrowsExceptionWhenAuctionIdNotFound() {
////        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
////            Bid bid = auctionService.createBid("Shamil", "non-existent Id", 600);
////        });
////        assertEquals("There is no such Auction Id.", expectedException.getMessage());
////    }
////
////    @Test
////    public void validateCreateBidThrowsExceptionWhenAuctionIncFirstBidAmountInvalid() {
////        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
////        RuntimeException expectedException = assertThrows(RuntimeException.class, () ->{
////            Bid bid = auctionService.createBid("Shamil", auctionId, 400);
////        });
////        assertEquals("The bid amount must be greater than the specified price.", expectedException.getMessage());
////    }
////
////    @Test
////    public void validateCreateBidThrowsExceptionWhenAuctionDecFirstBidAmountInvalid() {
////        Auction auction = auctionService.createAuction("Android", AuctionType.DEC, 500, AuctionDuration.H2, "Eldor");
////        RuntimeException expectedException = assertThrows(RuntimeException.class, () ->{
////            Bid bid = auctionService.createBid("Shamil", auctionId, 600);
////        });
////        assertEquals("The bid amount must be less than the specified price.", expectedException.getMessage());
////    }
////
////    @Test
////    public void validateCreateBidThrowsExceptionWhenAuctionIncNotFirstBidAmountInvalid() {
////        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
////        Bid firstBid = auctionService.createBid("Shamil", auctionId, 700);
////        RuntimeException expectedException = assertThrows(RuntimeException.class, () ->{
////            Bid nextBid = auctionService.createBid("Michael", auctionId, 600);
////        });
////        assertEquals("The bid amount must be greater than the previous bid.", expectedException.getMessage());
////    }
////
////    @Test
////    public void validateCreateBidThrowsExceptionWhenAuctionDecNotFirstBidAmountInvalid() {
////        Auction auction = auctionService.createAuction("Android", AuctionType.DEC, 500, AuctionDuration.H2, "Eldor");
////        Bid firstBid = auctionService.createBid("Shamil", auctionId, 300);
////        RuntimeException expectedException = assertThrows(RuntimeException.class, () ->{
////            Bid nextBid = auctionService.createBid("Michael", auctionId, 400);
////        });
////        assertEquals("The bid amount must be less than the previous bid.", expectedException.getMessage());
////    }
////
////    @Test
////    public void verifyCancelBid() {
////        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
////        Bid bid = auctionService.createBid("Shamil", auctionId, 600);
////        auctionService.cancelBid(auctionId, bid.getId());
////        assertEquals(0, auction.getBids().size());
////    }
////
////    @Test
////    public void validateCancelBidThrowsExceptionWhenAuctionIdNotFound() {
////        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
////        Bid bid = auctionService.createBid("Shamil", auctionId, 600);
////        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
////            auctionService.cancelBid("non-existent Id", bid.getId());
////        });
////        assertEquals("There is no such Auction Id.", expectedException.getMessage());
////    }
////
////    @Test
////    public void validateCancelBidThrowsExceptionWhenBidIdNotFound() {
////        Auction auction = auctionService.createAuction("Android", AuctionType.INC, 500, AuctionDuration.H2, "Eldor");
////        Bid bid = auctionService.createBid("Shamil", auctionId, 600);
////        RuntimeException expectedException = assertThrows(RuntimeException.class, () -> {
////            auctionService.cancelBid(auctionId, "non-existent Id");
////        });
////        assertEquals("There is no such Bid Id.", expectedException.getMessage());
////    }
//}