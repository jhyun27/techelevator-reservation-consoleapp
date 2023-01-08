package com.techelevator.excelsior.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.excelsior.model.Category;
import com.techelevator.excelsior.model.Venue;
import com.techelevator.excelsior.model.dao.JDBCVenueDAO;
import com.techelevator.excelsior.model.dao.VenueDAO;

public class JDBCVenueDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private VenueDAO dao;
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
		dao = new JDBCVenueDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Test
	public void list_all_venues() {
		// Arrange
		Venue venueOne = makeNewVenue();
		Venue venueTwo = makeNewVenue();
		venueTwo.setName("testVenueName2");

		String selectSql = "SELECT COUNT(*) AS numberOfRows FROM venue";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(selectSql);
		rows.next();
		int originalRowCount = rows.getInt("numberOfRows");

		save(venueOne);
		save(venueTwo);

		// Act
		List<Venue> venuesReturned = dao.listAllVenues();

		// Assert
		Assert.assertNotNull(venuesReturned);
		Assert.assertEquals(originalRowCount + 2, venuesReturned.size());
	}

	@Test
	public void get_venue_by_venue_id() {
		// Arrange
		Venue venue = makeNewVenue();
		save(venue);
		
		// Act
		Venue returnedVenue = dao.getVenueByVenueId(venue.getVenueId());

		// Assert
		Assert.assertEquals(venue.getVenueId(), returnedVenue.getVenueId());
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
