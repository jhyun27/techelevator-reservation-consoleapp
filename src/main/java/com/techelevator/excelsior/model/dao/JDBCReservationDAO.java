package com.techelevator.excelsior.model.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.excelsior.model.Category;
import com.techelevator.excelsior.model.Reservation;
import com.techelevator.excelsior.model.Space;

public class JDBCReservationDAO implements ReservationDAO {
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JDBCReservationDAO(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public int bookReservation(Reservation reservation) {
		String insertSql = "INSERT INTO reservation (reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for) VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING reservation_id";
		
		SqlRowSet rows = jdbcTemplate.queryForRowSet(insertSql, reservation.getSpaceId(), reservation.getNumberOfAttendees(), reservation.getStartDate(), reservation.getEndDate(), reservation.getReservationName());
		rows.next();
		int reservationId = rows.getInt("reservation_id");
		
		reservation.setReservationId(reservationId);
		return reservationId;
	}
	
	@Override
	public Reservation getReservationByReservationId(int reservationId) {
		String selectsql = "SELECT reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for FROM Reservation WHERE reservation_id = ?";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(selectsql, reservationId);
		rows.next();
		
		Reservation reservation = mapRowToReservation(rows);
		
		return reservation;
	}

//BONUS	
//	@Override
//	public List<Reservation> searchReservationByReservationName(int spaceId) {
//		List<Reservation> reservations = new ArrayList<Reservation>();
//
//		String searchSql = "SELECT reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for FROM reservation WHERE reserved_for ILIKE ?";
//
//		SqlRowSet rows = jdbcTemplate.queryForRowSet(searchSql, "%" + spaceId + "%");
//
//		while (rows.next()) {
//			Reservation reservation = mapRowToReservation(rows);
//			reservations.add(reservation);
//		}
//		return reservations;
//	}
	

	private Reservation mapRowToReservation(SqlRowSet row) {
		Reservation reservation = new Reservation();
		reservation.setSpaceId(row.getInt("space_id"));
		reservation.setNumberOfAttendees(row.getInt("number_of_attendees"));
		reservation.setStartDate(LocalDate.parse(row.getString("start_date")));
		reservation.setEndDate(LocalDate.parse(row.getString("end_date")));
		reservation.setReservationName(row.getString("reserved_for"));
		return reservation;
	}
}
