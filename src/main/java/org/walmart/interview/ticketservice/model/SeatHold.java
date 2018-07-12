package org.walmart.interview.ticketservice.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SeatHold {

	private int id;
	
	private List<Seat> selectedSeats;
	
	private String customerEmail;
	
	private Instant creationTimeStamp;
	
	public SeatHold(int id, String email) {
		this.id = id;
		this.selectedSeats = new ArrayList<Seat>();
		this.creationTimeStamp = Instant.now();
		this.customerEmail = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Seat> getSelectedSeats() {
		return selectedSeats;
	}

	public void setSelectedSeats(List<Seat> selectedSeats) {
		this.selectedSeats = selectedSeats;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Instant getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(Instant creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}
	
	
}
