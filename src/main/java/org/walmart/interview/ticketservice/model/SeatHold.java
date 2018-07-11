package org.walmart.interview.ticketservice.model;

import java.util.Date;
import java.util.List;

public class SeatHold {

	private int id;
	
	private List<Seat> selectedSeats;
	
	private String customerEmail;
	
	private Date creationTimeStamp;
}
