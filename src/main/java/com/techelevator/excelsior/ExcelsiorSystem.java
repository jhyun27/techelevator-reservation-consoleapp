package com.techelevator.excelsior;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;


import com.techelevator.excelsior.model.Reservation;
import com.techelevator.excelsior.model.Space;
import com.techelevator.excelsior.model.Venue;
import com.techelevator.excelsior.model.dao.JDBCReservationDAO;
import com.techelevator.excelsior.model.dao.JDBCSpaceDAO;
import com.techelevator.excelsior.model.dao.JDBCVenueDAO;
import com.techelevator.excelsior.model.dao.ReservationDAO;
import com.techelevator.excelsior.model.dao.SpaceDAO;
import com.techelevator.excelsior.model.dao.VenueDAO;

public class ExcelsiorSystem {
	
	private VenueDAO venueDao;
	private ReservationDAO reservationDao;
	private SpaceDAO spaceDao;

	public ExcelsiorSystem(DataSource datasource) {
		venueDao = new JDBCVenueDAO(datasource);
		reservationDao = new JDBCReservationDAO(datasource);
		spaceDao = new JDBCSpaceDAO(datasource);
	}
	
	public List<Venue> listAllVenues() {
		return venueDao.listAllVenues();
	}
	
	public Venue getVenueByVenueId(int venueId) {
		return venueDao.getVenueByVenueId(venueId);
	}
	
	public int bookReservation(int spaceId, int numberOfAttendees, LocalDate startDate, int numberOfDays, String reservationName) {
		LocalDate endDate = startDate.plusDays(numberOfDays);
		
		Reservation reservation = new Reservation();
		reservation.setSpaceId(spaceId);
		reservation.setNumberOfAttendees(numberOfAttendees);
		reservation.setStartDate(startDate);
		reservation.setEndDate(endDate);
		reservation.setReservationName(reservationName);
		
		return reservationDao.bookReservation(reservation);
	}
	
	public Reservation getReservationByReservationId(int reservationId) {
		return reservationDao.getReservationByReservationId(reservationId);
	}
	
	public List<Space> listAllSpaces(int venueId) {
		return spaceDao.listAllSpaces(venueId);
	}
	
	public List<Space> listAvailableSpaces(LocalDate startDate, int numberOfDays, int numberOfAttendees, int venueId) {
		LocalDate endDate = startDate.plusDays(numberOfDays);
		
		return spaceDao.listAvailableSpaces(venueId, startDate, endDate, numberOfAttendees);
	}
	
	public String getNameBySpaceId(int spaceId) {
		return spaceDao.getNameBySpaceId(spaceId);
	}
}
