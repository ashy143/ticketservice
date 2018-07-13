package org.walmart.interview.ticketservice.model;

/**
 * This class represents a Seat of a {@link Venue}. Every seat is identified by
 * a row index and a column index. State of a seat is identified using the
 * property {@link SeatState}. State of a seat is either "AVAILABLE", "HOLD" or
 * "RESERVED"
 */
public class Seat {

	private SeatState state;

	private int row;

	private int col;

	public Seat(int row, int col, SeatState state) {

		this.row = row;
		this.col = col;
		this.state = state;
	}

	public SeatState getState() {
		return state;
	}

	public void setState(SeatState state) {
		this.state = state;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

}
