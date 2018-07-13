package org.walmart.interview.ticketservice.model;

/**
 * This class represents a Venue which has a capacity of m X n {@link Seat} (m
 * rows and n columns). An available seat is denoted by
 * {@link SeatState#AVAILABLE}, A held seat is denoted by {@link SeatState#HOLD}
 * and A reserved seat is denoted by {@link SeatState#RESERVED}.
 */
public class Venue {

	private Seat[][] seats;

	private int rows;

	private int cols;

	public Venue(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;

		this.seats = new Seat[rows][cols];
		initSeats();
	}

	/*
	 * Whenever a Venue is initialized it creates a 2D array of {@link Seat} and
	 * assigns a row index, column index and default State(AVAILABLE) of the Seat.
	 */
	private void initSeats() {
		for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
			for (int colIndex = 0; colIndex < cols; colIndex++) {
				seats[rowIndex][colIndex] = new Seat(rowIndex, colIndex, SeatState.AVAILABLE);
			}
		}
	}

	public Seat[][] getSeats() {
		return seats;
	}

	public void setSeats(Seat[][] seats) {
		this.seats = seats;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

}
