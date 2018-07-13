package org.walmart.interview.ticketservice;

import static org.junit.Assert.assertEquals;

import java.time.Instant;

import org.junit.Test;
import org.walmart.interview.ticketservice.impl.TicketServiceImpl;
import org.walmart.interview.ticketservice.model.SeatHold;

public class TicketServiceImplTest {
	
	/**
     * Test for {@link TicketService#numSeatsAvailable()} in following conditions:
     * 1. Check # of seats before any seat is HELD/RESERVED
     * 2. Hold 5 seats and check there are only 20 more available seats
     * 3. Reserve the held seats and check that there is no change in num of available seats
     * 4. Wait for the expiry time and check that 5 seats that were held(expired) are made available in numSeats count.
     * 
     * @throws Exception
     */
	@Test
    public void testNumSeatsAvailableInVariousScenarios() throws Exception
    {
    	//Create a Venue of 25 seats.
        TicketServiceImpl ticketService = new TicketServiceImpl(5, 5);
        
        //Check # of seats before any seat is HELD/RESERVED
        assertEquals(25, ticketService.numSeatsAvailable());
        
        //Hold 5 seats and check there are only 20 more available seats
        SeatHold seatHold = ticketService.findAndHoldSeats(5, "email1");
        assertEquals(20, ticketService.numSeatsAvailable());
        
        //Now reserve the held seats and check that there is no change in num of available seats
        ticketService.reserveSeats(seatHold.getId(), "email1");
        assertEquals(20, ticketService.numSeatsAvailable());
        
        //Hold 5 more seats and check that # of seats available after hold is 15
        ticketService.findAndHoldSeats(5, "email1");
        assertEquals(15, ticketService.numSeatsAvailable());
        
        //Wait for the expiry time and check that 5 seats that were held above should be released,
        //Which means # of seats available should be increased back to 20 as 5 of them are released due to HOLD time expiry.
        Thread.sleep(12000);
        assertEquals(20, ticketService.numSeatsAvailable());
        
        
    }
    
    /**
     * Test to verify if exception is thrown when num of requested seats in more that available seats.
     * 
     * @throws Exception
     */
	@Test
    public void testExceptionsForRequestedSeatsGreaterThanAvailableSeats() throws Exception {
    	//Create a Venue of 25 seats.
        TicketServiceImpl ticketService = new TicketServiceImpl(5, 5);
        
        //Check # of seats before any seat is HELD/RESERVED
        assertEquals(25, ticketService.numSeatsAvailable());
        
        //Hold 26 seats and check an exception is threw
        try {
        	SeatHold seatHold = ticketService.findAndHoldSeats(26, "email1");
        }catch (Exception e) {
        	assertEquals("Not enough seats available", e.getMessage());
        }
        
    }
    
    
    /**
     * Test to verify if exception is thrown when requested to reserve held seats when they are expired.
     * 
     * @throws Exception
     */
	@Test
    public void testHeldSeatsShouldNotBeAllowedToBeReservedAfterHoldExpiry() throws Exception {
    	//Create a Venue of 25 seats.
        TicketServiceImpl ticketService = new TicketServiceImpl(5, 5);
        
        //Hold 5 seats and check an exception is threw
        SeatHold seatHold = ticketService.findAndHoldSeats(5, "email1");
        
        //Update the held time (decreate the time by 15 sec) to make SeatHold expired.  
        seatHold.setCreationTimeStamp(Instant.now().minusSeconds(15));
        
        //Call reserveSeats() and verify that it doesn't allow to reserve seats.
        try {
        	ticketService.reserveSeats(seatHold.getId(), "email1");
        } catch (Exception e) {
			assertEquals("Can't reserve Seats as SeatHold expired", e.getMessage());
		}
        
        
    }
    
    /**
     * Test to verify if exception is thrown when requested to reserve held seats using SeatHoldId
     * that doesn't exists.
     * 
     * @throws Exception
     */
	@Test
    public void testExceptionWhenReserveSeatsIsCalledWithIncorrectSeatHoldId() throws Exception {
    	//Create a Venue of 25 seats.
        TicketServiceImpl ticketService = new TicketServiceImpl(5, 5);
        
        //Call reserveSeats() and verify that it doesn't allow to reserve seats.
        try {
        	ticketService.reserveSeats(1, "email1");
        } catch (Exception e) {
			assertEquals("No seats held with seatHoldId: 1", e.getMessage());
		}
        
        
    }

}
