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

public class TicketServiceImpl implements TicketService {

	private Venue venue;

	public Map<Integer, SeatHold> holdsMap;

	private AtomicInteger uniqueId;

	public static final int EXPIRY_IN_SECONDS = 10;

	public TicketServiceImpl(int rows, int cols) {
		this.venue = new Venue(rows, cols);
		this.holdsMap = new HashMap<>();
		this.uniqueId = new AtomicInteger(Integer.MIN_VALUE);
	}

	public int numSeatsAvailable() {
		int numberOfAvailableSeats = 0;
		synchronized (venue) {
			Seat[][] seats = venue.getSeats();
			releaseHeldSeatsOnExpiry();
			for (int i = 0; i < seats.length; i++) {
				for (int j = 0; j < seats[i].length; j++) {
					if (seats[i][j].getState().equals(SeatState.AVAILABLE)) {
						numberOfAvailableSeats++;
					}
				}
			}
		}

		return numberOfAvailableSeats;
	}

	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) throws Exception {
		int numberOfAvaialbleSeats = numSeatsAvailable();
		if (numSeats > numberOfAvaialbleSeats) {
			throw new Exception("Not enough seats available");
		} else {
			System.out.println("Holding seats: ");
			SeatHold hold = holdSeats(numSeats, customerEmail);
			this.holdsMap.put(hold.getId(), hold);
			return hold;
		}
	}

	public String reserveSeats(int seatHoldId, String customerEmail) throws Exception {
		SeatHold seatHold = holdsMap.get(seatHoldId);
		
		if(Objects.isNull(seatHold)) {
			throw new Exception("No seats held with seatHOldId: " + seatHoldId);
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
		SeatHold seatHold = new SeatHold(this.uniqueId.getAndIncrement(), customerEmail);
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
					System.out.println("no here");
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
