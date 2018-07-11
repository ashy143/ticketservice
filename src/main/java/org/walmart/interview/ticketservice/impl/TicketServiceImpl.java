package org.walmart.interview.ticketservice.impl;

import org.walmart.interview.ticketservice.TicketService;
import org.walmart.interview.ticketservice.model.SeatHold;
import org.walmart.interview.ticketservice.model.Venue;

public class TicketServiceImpl implements TicketService {
	
	private Venue venue;
	
	public TicketServiceImpl(int rows, int cols) {
		this.venue = new Venue(rows, cols);
	}

	public int numSeatsAvailable() {
		return 0;
	}

	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		return null;
	}

	public String reserveSeats(int seatHoldId, String customerEmail) {
		return null;
	}

}
