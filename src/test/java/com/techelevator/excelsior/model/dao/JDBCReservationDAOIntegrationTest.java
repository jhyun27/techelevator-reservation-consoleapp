package com.techelevator.excelsior.model.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.excelsior.model.Reservation;
import com.techelevator.excelsior.model.Space;
import com.techelevator.excelsior.model.Venue;
import com.techelevator.excelsior.model.dao.JDBCReservationDAO;
import com.techelevator.excelsior.model.dao.ReservationDAO;

public class JDBCReservationDAOIntegrationTest {

		private static SingleConnectionDataSource dataSource;
		private ReservationDAO dao;
		private JdbcTemplate jdbcTemplate;

		@BeforeClass
		public static void setupDataSource() {
			dataSource = new SingleConnectionDataSource();
			dataSource.setUrl("jdbc:postgresql://localhost:5432/excelsior-venues");
			dataSource.setUsername("postgres");
			dataSource.setPassword("postgres1");
			dataSource.setAutoCommit(false);
		}

		@AfterClass
		public static void destroyDataSource() {
			dataSource.destroy();
		}

		@After
		public void rollback() throws SQLException {
			dataSource.getConnection().rollback();
		}

		@Before
		public void setup() {
			dao = new JDBCReservationDAO(dataSource);
			jdbcTemplate = new JdbcTemplate(dataSource);
		}
		
		@Test
		public void get_reservation_by_reservation_id() {
			Reservation reservation = makeNewReservation();
			save(reservation);
			String selectSql = "SELECT reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for FROM reservation WHERE reservation_id = ?";
			SqlRowSet rows = jdbcTemplate.queryForRowSet(selectSql, reservation.getReservationId());
			rows.next();

			Reservation expected = mapRowToReservation(rows);
			Reservation returnedExpected = dao.getReservationByReservationId(reservation.getReservationId());
			
			Assert.assertEquals(expected, returnedExpected);
		}
		
		@Test
		public void book_reservation() {
			//Arrange
			Reservation reservation = makeNewReservation();
			String selectSql = "SELECT COUNT(*) AS numberOfRows FROM reservation WHERE reservation_id = ?";
			SqlRowSet rows = jdbcTemplate.queryForRowSet(selectSql, reservation.getReservationId());
			rows.next();
			int originalRowCount = rows.getInt("numberOfRows");
			
			//Act
			dao.bookReservation(reservation);
			String selectSql2 = "SELECT COUNT(*) AS numberOfRows FROM reservation WHERE reservation_id = ?";
			SqlRowSet rows2 = jdbcTemplate.queryForRowSet(selectSql2, reservation.getReservationId());
			rows2.next();
			int rowCountAfterBook = rows2.getInt("numberOfRows");
			
			Assert.assertEquals(originalRowCount + 1, rowCountAfterBook);
		}
		
		private Reservation mapRowToReservation(SqlRowSet row) {
			Reservation reservation = new Reservation();
			reservation.setSpaceId(row.getInt("space_id"));
			reservation.setNumberOfAttendees(row.getInt("number_of_attendees"));
			reservation.setStartDate(LocalDate.parse(row.getString("start_date")));
			reservation.setEndDate(LocalDate.parse(row.getString("end_date")));
			reservation.setReservationName(row.getString("reserved_for"));
			return reservation;
		}
		
		private Reservation makeNewReservation() {
			Reservation reservation = new Reservation();
			Space space = makeNewSpace();
			save(space);
			reservation.setSpaceId(space.getSpaceId());
			reservation.setNumberOfAttendees(10);
			reservation.setStartDate(LocalDate.now());
			reservation.setEndDate(LocalDate.now());
			reservation.setReservationName("testName");
			return reservation;
		}
	
		private Reservation save(Reservation reservation) {
			String insertSql = "INSERT INTO reservation (reservation_id, space_id, number_of_attendees, start_date, end_date, reserved_for) VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING reservation_id";

			SqlRowSet rows = jdbcTemplate.queryForRowSet(insertSql, reservation.getSpaceId(), reservation.getNumberOfAttendees(), reservation.getStartDate(), reservation.getEndDate(), reservation.getReservationId());
			rows.next();
			int reservationId = rows.getInt("reservation_id");

			reservation.setReservationId(reservationId);
			return reservation;
		}
	
		private Space makeNewSpace() {
			Space space = new Space();
			Venue venue = makeNewVenue();
			save(venue);
			space.setName("testSpaceName");
			space.setVenueId(venue.getVenueId());
			space.setAccessible(true);
			space.setOpenFrom(2);
			space.setOpenTo(8);
			space.setRate(new BigDecimal(10));
			space.setMaxOccupancy(100);
			return space;
		}

		private Space save(Space space) {
			String insertSql = "INSERT INTO space (id, venue_id, name, is_accessible, open_from, open_to, daily_rate, max_occupancy) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

			SqlRowSet rows = jdbcTemplate.queryForRowSet(insertSql, space.getVenueId(), space.getName(), space.isAccessible(), space.getOpenFrom(), space.getOpenTo(), space.getRate(), space.getMaxOccupancy());
			rows.next();
			int spaceId = rows.getInt("id");

			space.setSpaceId(spaceId);
			return space;
		}
	
		private Venue makeNewVenue() {
			Venue venue = new Venue();
			venue.setName("testVenueName");
			venue.setCityId(1);
			venue.setDescription("testDescription");
			return venue;
		}
	
		private Venue save(Venue venue) {
			String insertSql = "INSERT INTO venue (id, name, city_id, description) VALUES (DEFAULT, ?, ?, ?) RETURNING id";

			SqlRowSet rows = jdbcTemplate.queryForRowSet(insertSql, venue.getName(), venue.getCityId(),
					venue.getDescription());
			rows.next();
			int venueId = rows.getInt("id");

			venue.setVenueId(venueId);
			return venue;
		}
		
	
}
