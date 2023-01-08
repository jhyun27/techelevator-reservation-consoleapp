package com.techelevator.excelsior.model.dao;

import java.util.Date;
import java.util.List;

import com.techelevator.excelsior.model.Reservation;

public interface ReservationDAO {
	
	int bookReservation(Reservation reservation);
	Reservation getReservationByReservationId(int reservationId);
	//List<Reservation> searchReservationByReservationName(int spaceId); BONUS
}
