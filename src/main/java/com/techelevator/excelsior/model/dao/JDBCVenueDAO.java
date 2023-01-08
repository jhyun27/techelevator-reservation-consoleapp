package com.techelevator.excelsior.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.excelsior.model.Category;
import com.techelevator.excelsior.model.Venue;

public class JDBCVenueDAO implements VenueDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public JDBCVenueDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Venue> listAllVenues() {
		
		List<Venue> venues = new ArrayList<Venue>();
		
		String selectSql = "SELECT id, name, city_id, description FROM venue ORDER BY name ASC";
		
		SqlRowSet rows = jdbcTemplate.queryForRowSet(selectSql);
		
		while (rows.next()) {
			Venue venue = mapRowToVenue(rows);
			venues.add(venue);
		}
		return venues;
	}
	
	@Override
	public Venue getVenueByVenueId(int venueId) {
		String selectSql = "SELECT id, name, city_id, description FROM venue "
				+ "WHERE venue.id = ?";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(selectSql, venueId);
		rows.next();
		
		Venue venue = mapRowToVenue(rows);
		
		String locationSql = "SELECT (city.name || ', ' || state.name) AS location FROM venue "
				+ "JOIN city ON city.id = venue.city_id "
				+ "JOIN state ON state.abbreviation = city.state_abbreviation "
				+ "WHERE venue.id = ?";
		SqlRowSet locationRows = jdbcTemplate.queryForRowSet(locationSql, venueId);
		locationRows.next();
		String location = locationRows.getString("location");
		venue.setLocation(location);
		
		String categorySql = "SELECT string_agg(category.name, ', ') AS categories FROM venue "
				+ "JOIN category_venue ON category_venue.venue_id = venue.id "
				+ "JOIN category ON category.id = category_venue.category_id "
				+ "WHERE venue.id = ?";
		SqlRowSet categoryRows = jdbcTemplate.queryForRowSet(categorySql, venueId);
		categoryRows.next();
		
		venue.setCategories(categoryRows.getString("categories"));
		
		return venue;
	}
	
	
	private Venue mapRowToVenue(SqlRowSet row) {
		Venue venue = new Venue();
		
		venue.setVenueId(row.getInt("id"));
		venue.setName(row.getString("name"));
		venue.setCityId(row.getInt("city_id"));
		venue.setDescription(row.getString("description"));

		return venue;
	}
	
}
