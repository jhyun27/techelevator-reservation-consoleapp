package com.techelevator.excelsior.model;

import java.time.LocalDate;
import java.util.Date;

public class Reservation {
	
	private int reservationId;
	private int spaceId;
	private int numberOfAttendees;
	private LocalDate startDate;
	private LocalDate endDate;
	private String reservationName;


	public int getReservationId() {
		return reservationId;
	}

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	public int getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}

	public int getNumberOfAttendees() {
		return numberOfAttendees;
	}

	public void setNumberOfAttendees(int numberOfAttendees) {
		this.numberOfAttendees = numberOfAttendees;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate2) {
		this.startDate = startDate2;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate2) {
		this.endDate = endDate2;
	}

	public String getReservationName() {
		return reservationName;
	}

	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + numberOfAttendees;
		result = prime * result + reservationId;
		result = prime * result + ((reservationName == null) ? 0 : reservationName.hashCode());
		result = prime * result + spaceId;
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reservation other = (Reservation) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (numberOfAttendees != other.numberOfAttendees)
			return false;
		if (reservationId != other.reservationId)
			return false;
		if (reservationName == null) {
			if (other.reservationName != null)
				return false;
		} else if (!reservationName.equals(other.reservationName))
			return false;
		if (spaceId != other.spaceId)
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}
	
	

	
}
