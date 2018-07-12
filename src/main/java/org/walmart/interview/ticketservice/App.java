package org.walmart.interview.ticketservice;

import org.walmart.interview.ticketservice.impl.TicketServiceImpl;
import org.walmart.interview.ticketservice.model.SeatHold;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args) throws InterruptedException
    {
        System.out.println( "Hello World!" );
        TicketServiceImpl impl = new TicketServiceImpl(10, 20);
        System.out.println("# of available seats(Expected 200): " + impl.numSeatsAvailable());
        try {
			impl.findAndHoldSeats(15, "email1");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
        System.out.println("# of available seats: (Expected 185): " + impl.numSeatsAvailable());
        System.out.println("# of holds " + impl.holdsMap.size());
        Thread.sleep(12000);
        System.out.println("# of available seats: (Expected 200): " + impl.numSeatsAvailable());
        System.out.println("# of holds " + impl.holdsMap.size());
        
        try {
			SeatHold seatHold = impl.findAndHoldSeats(20, "ashwin@email.com");
			System.out.println("# of holds (expected 1): " + impl.holdsMap.size());
			
			System.out.println(impl.reserveSeats(seatHold.getId(), "ashwin@email.com"));
			System.out.println("# of holds (expected 0): " + impl.holdsMap.size());
			
	        Thread.sleep(12000);
	        System.out.println("# of available seats: (Expected 180): " + impl.numSeatsAvailable());
	        System.out.println("# of holds (expected 0): " + impl.holdsMap.size());
	        
	        
	        
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
        
        
    }
}
