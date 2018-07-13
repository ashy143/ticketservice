package org.walmart.interview.ticketservice;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.walmart.interview.ticketservice.impl.TicketServiceImpl;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;


/**
 * @author ashwi
 *
 */
@RunWith(ConcurrentTestRunner.class)
public class ConcurrencyTest {
	
	private TicketServiceImpl ticketService = new TicketServiceImpl(4,4);
	 
	 
    /**
     * Concurrently make 3 calls to findAndHoldSeats and assert in @After block for # of seats.
     * This will help to test if tickets hold/reserve works as expected in concurrency. 
     * @throws Exception
     */
    @Test
    @ThreadCount(3)
    public void holdSeatsConcurrentTest() throws Exception {
        ticketService.findAndHoldSeats(4, "email");
    }
    
    @After
    public void testVenueStateAfterConcurrentCallsToHoldSeats() throws InterruptedException {
        assertEquals("Value should be 0", 4, ticketService.numSeatsAvailable());
        assertEquals("No of holds should be 3", 3, ticketService.getHoldsMap().size());
        
        Thread.sleep(12000);
        assertEquals("Value should be 16", 16, ticketService.numSeatsAvailable());
        assertEquals("No of holds should be 0", 0, ticketService.getHoldsMap().size());
        
    }


}
