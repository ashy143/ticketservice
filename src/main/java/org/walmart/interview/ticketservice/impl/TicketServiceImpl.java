package org.walmart.interview.ticketservice.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.walmart.interview.ticketservice.TicketService;
import org.walmart.interview.ticketservice.model.Seat;
import org.walmart.interview.ticketservice.model.SeatHold;
import org.walmart.interview.ticketservice.model.SeatState;
import org.walmart.interview.ticketservice.model.Venue;

/**
 * All the func
 *
 */
public class TicketServiceImpl implements TicketService {

	private Venue venue;

	// Map of SeatHoldId to SeatHold for easy access to SeatHold using SeatHoldId.
	private Map<Integer, SeatHold> holdsMap;

	private AtomicInteger uniqueIdGenerator;

	// Default expiry time after which the held seats will be released and made
	// available.
	public static final int EXPIRY_IN_SECONDS = 10;

	public TicketServiceImpl(int rows, int cols) {
		this.venue = new Venue(rows, cols);
		this.holdsMap = new HashMap<>();
		this.uniqueIdGenerator = new AtomicInteger(Integer.MIN_VALUE);
	}

	public Map<Integer, SeatHold> getHoldsMap() {
		return holdsMap;
	}

	public void setHoldsMap(Map<Integer, SeatHold> holdsMap) {
		this.holdsMap = holdsMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.walmart.interview.ticketservice.TicketService#numSeatsAvailable()
	 * Straight forward iterate through all the seats in venue and check if seat is
	 * available or not. We don't have to bother about iterating through each seats
	 * everytime this function is called since capacity is very small.
	 */
	public int numSeatsAvailable() {
		int numberOfAvailableSeats = 0;
		synchronized (venue) {
			Seat[][] seats = venue.getSeats();
			releaseHeldSeatsOnExpiry();
			for (int i = 0; i < seats.length; i++) {
				for (int j = 0; j < seats[i].length; j++) {
					if (SeatState.AVAILABLE.equals(seats[i][j].getState())) {
						numberOfAvailableSeats++;
					}
				}
			}
		}

		return numberOfAvailableSeats;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.walmart.interview.ticketservice.TicketService#findAndHoldSeats(int,
	 * java.lang.String)
	 * 
	 * This method finds the best available seats from the venue (although no such
	 * strategy is mentioned for how to find best seats, we pick available seats
	 * while looking row by row for an available seat ).
	 * 
	 */
	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) throws Exception {
		
		if(numSeats < 1) {
			throw new Exception("Number of seats can't be less than 1");
		}
		
		int numberOfAvaialbleSeats = numSeatsAvailable();
		if (numSeats > numberOfAvaialbleSeats) {
			throw new Exception("Not enough seats available");
		} else {
			SeatHold hold = holdSeats(numSeats, customerEmail);
			this.holdsMap.put(hold.getId(), hold);
			return hold;
		}
	}

	/* (non-Javadoc)
	 * @see org.walmart.interview.ticketservice.TicketService#reserveSeats(int, java.lang.String)
	 * 
	 * This method reserves the held seats in a venue. Before reserving a seat it will also check
	 */
	public String reserveSeats(int seatHoldId, String customerEmail) throws Exception {
		SeatHold seatHold = holdsMap.get(seatHoldId);

		if (Objects.isNull(seatHold)) {
			throw new Exception("No seats held with seatHoldId: " + seatHoldId);
		}
		
		if(Instant.now().minusSeconds(10).isAfter(seatHold.getCreationTimeStamp())) {
			throw new Exception("Can't reserve Seats as SeatHold expired");
		}
		
		synchronized (venue) {
			Seat[][] seats = venue.getSeats();
			List<Seat> heldSeats = seatHold.getSelectedSeats();

			heldSeats.forEach(heldSeat -> {
				seats[heldSeat.getRow()][heldSeat.getCol()].setState(SeatState.RESERVED);
			});

			holdsMap.remove(seatHoldId);
		}

		return "reserved_" + seatHoldId + "_" + customerEmail;
	}

	private SeatHold holdSeats(int numSeats, String customerEmail) {
		SeatHold seatHold = new SeatHold(this.uniqueIdGenerator.getAndIncrement(), customerEmail);
		synchronized (venue) {
			Seat[][] seats = venue.getSeats();
			List<Seat> selectedSeats = new ArrayList<Seat>();

			for (int i = 0; i < seats.length; i++) {
				for (int j = 0; j < seats[i].length; j++) {
					if (seats[i][j].getState().equals(SeatState.AVAILABLE) && numSeats > 0) {
						seats[i][j].setState(SeatState.HOLD);
						selectedSeats.add(new Seat(i, j, SeatState.HOLD));
						--numSeats;
					}
				}
			}
			seatHold.setSelectedSeats(selectedSeats);
		}
		return seatHold;
	}

	private void releaseHeldSeatsOnExpiry() {
		synchronized (venue) {
			Seat[][] seats = venue.getSeats();
			for (Map.Entry<Integer, SeatHold> entry : holdsMap.entrySet()) {
				SeatHold seatHold = entry.getValue();
				if (Instant.now().isAfter(seatHold.getCreationTimeStamp().plusSeconds(EXPIRY_IN_SECONDS))) {
					seatHold.getSelectedSeats().forEach(seat -> {
						seats[seat.getRow()][seat.getCol()].setState(SeatState.AVAILABLE);
					});
				}
			}

			holdsMap = holdsMap.entrySet().stream()
					.filter(entry -> Instant.now().minusSeconds(EXPIRY_IN_SECONDS)
							.isBefore(entry.getValue().getCreationTimeStamp()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		}

	}

}
