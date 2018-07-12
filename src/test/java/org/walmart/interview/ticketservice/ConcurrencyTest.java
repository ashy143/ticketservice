package org.walmart.interview.ticketservice;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.walmart.interview.ticketservice.impl.TicketServiceImpl;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;

@RunWith(ConcurrentTestRunner.class)
public class ConcurrencyTest {
	
	private TicketServiceImpl ticketService = new TicketServiceImpl(4,4);
	 
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
