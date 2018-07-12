package org.walmart.interview.ticketservice;

import org.walmart.interview.ticketservice.impl.TicketServiceImpl;
import org.walmart.interview.ticketservice.model.SeatHold;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class TicketServiceIntegrationTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TicketServiceIntegrationTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( TicketServiceIntegrationTest.class );
    }

    
    public void testTicketService() throws Exception
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
    
   
}
