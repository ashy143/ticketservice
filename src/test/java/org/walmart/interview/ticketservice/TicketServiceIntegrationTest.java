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

    
    /**
     * Test for {@link TicketService#numSeatsAvailable()} in following conditions:
     * 1. Check # of seats before any seat is HELD/RESERVED
     * 2. Hold 5 seats and check there are only 20 more available seats
     * 3. Reserve the held seats and check that there is no change in num of available seats
     * 4. Wait for the expiry time and check that 5 seats that were held(expired) are made available in numSeats count.
     * 
     * @throws Exception
     */
    public void testTicketServiceFlow() throws Exception
    {
    	 TicketServiceImpl impl = new TicketServiceImpl(10, 20);
    	 assertEquals("# of available seats(Expected 200)", 200, impl.numSeatsAvailable());
         try {
 			impl.findAndHoldSeats(15, "email1");
 		} catch (Exception e) {
 			System.out.println(e.getMessage());
 		}
         assertEquals("# of available seats(Expected 185)", 185, impl.numSeatsAvailable());
         assertEquals("# of holds", 1, impl.getHoldsMap().size());
         
         Thread.sleep(12000);
         
         assertEquals("# of available seats: (Expected 200): ", 200, impl.numSeatsAvailable());
         assertEquals("# of holds", 0, impl.getHoldsMap().size());
         
         try {
 			SeatHold seatHold = impl.findAndHoldSeats(20, "ashwin@email.com");
 			assertEquals("# of holds (expected 1): ", 1, impl.getHoldsMap().size());
 			
 			System.out.println(impl.reserveSeats(seatHold.getId(), "ashwin@email.com"));
 			assertEquals("# of holds (expected 0): ", 0, impl.getHoldsMap().size());
 			
 	        Thread.sleep(12000);
 	        
 	        assertEquals("# of available seats: (Expected 180): ", 180, impl.numSeatsAvailable());
 	       assertEquals("# of holds (expected 0): ", 0, impl.getHoldsMap().size());
 	        
 		} catch (Exception e) {
 			
 		}
        
    }
    
   
}
