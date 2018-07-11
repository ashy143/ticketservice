package org.walmart.interview.ticketservice.model;

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
