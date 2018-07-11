package org.walmart.interview.ticketservice.model;

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

	private void initSeats() {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				seats[i][j] = new Seat(i, j, SeatState.AVAILABLE);
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
