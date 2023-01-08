package com.techelevator.excelsior.model.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.excelsior.model.Reservation;
import com.techelevator.excelsior.model.Space;
import com.techelevator.excelsior.model.Venue;
import com.techelevator.excelsior.model.dao.JDBCSpaceDAO;
import com.techelevator.excelsior.model.dao.SpaceDAO;

public class JDBCSpaceDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private SpaceDAO dao;
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
		dao = new JDBCSpaceDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Test
	public void get_name_by_space_id() {
		Space spaceOne = makeNewSpace();
		save(spaceOne);
		
		String expectedName = spaceOne.getName();
		String returnedName = dao.getNameBySpaceId(spaceOne.getSpaceId());
		
		Assert.assertEquals(expectedName, returnedName);
	}
	
	@Test
	public void list_all_spaces() {
		// Arrange
		Space spaceOne = makeNewSpace();
		Space spaceTwo = makeNewSpace();
		save(spaceOne);
		spaceTwo.setVenueId(spaceOne.getVenueId());
		save(spaceTwo);

		// Act
		List<Space> spaceReturned = dao.listAllSpaces(spaceOne.getVenueId());

		// Assert
		Assert.assertNotNull(spaceReturned);
		Assert.assertEquals(2, spaceReturned.size());
	}
	
	@Test
	public void list_available_spaces_none_available() {
		Space space = makeNewSpace();
		save(space);
		
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = startDate.plusDays(5);
		
		Reservation reservation = makeNewReservation();
		reservation.setSpaceId(space.getSpaceId());
		reservation.setStartDate(startDate);
		reservation.setEndDate(endDate);
		save(reservation);
		
		List<Space> availableSpaces = dao.listAvailableSpaces(space.getVenueId(), startDate, endDate, 1);
		
		Assert.assertEquals(0, availableSpaces.size());
		
	}
	
	@Test
	public void list_available_spaces_one_available() {
		Space space = makeNewSpace();
		save(space);
		
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = startDate.plusDays(5);
		
		List<Space> availableSpaces = dao.listAvailableSpaces(space.getVenueId(), startDate, endDate, 1);
		
		Assert.assertEquals(1, availableSpaces.size());
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
	
	private Reservation makeNewReservation() {
		Reservation reservation = new Reservation();
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
}
