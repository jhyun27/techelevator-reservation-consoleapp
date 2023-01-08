package com.techelevator.excelsior.model.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.excelsior.model.Space;
import com.techelevator.excelsior.model.Venue;

public class JDBCSpaceDAO implements SpaceDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCSpaceDAO(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public List<Space> listAllSpaces(int venueId) {
		List<Space> spaces = new ArrayList<Space>();
		
		String selectSql = "SELECT id, venue_id, name, is_accessible, open_from, "
				+ "open_to, daily_rate::decimal AS decimal_daily_rate, max_occupancy FROM space WHERE venue_id = ?";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(selectSql, venueId);
		while (rows.next()) {
			Space space = mapRowToSpace(rows);
			spaces.add(space);
		}
		
		return spaces;
	}
	
	@Override
	public List<Space> listAvailableSpaces(int venueId, LocalDate startDate, LocalDate endDate, int numberOfAttendees) {
		List<Space> availableSpaces = new ArrayList<Space>();
		String selectSql = "SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal AS decimal_daily_rate, max_occupancy FROM space "
				+ "WHERE venue_id = ? "
				+ "AND max_occupancy >= ? "
				+ "AND id NOT IN "
				+ "(SELECT space_id FROM reservation "
				+ "WHERE venue_id = ? "
				+ "AND (? BETWEEN start_date AND end_date "
				+ "OR ? BETWEEN start_date AND end_date))";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(selectSql, venueId, numberOfAttendees, venueId, startDate, endDate);
		while (rows.next()) {
			Space space = mapRowToSpace(rows);
			availableSpaces.add(space);
		}
		
		return availableSpaces;
	}

	@Override
	public String getNameBySpaceId(int spaceId) {
		String selectSql = "SELECT name FROM space WHERE id = ?";
		SqlRowSet rows = jdbcTemplate.queryForRowSet(selectSql, spaceId);
		rows.next();
		
		String name = rows.getString("name");
		return name;
	}
	
	private Space mapRowToSpace(SqlRowSet row) {
		Space space = new Space();
		
		space.setSpaceId(row.getInt("id"));
		space.setVenueId(row.getInt("venue_id"));
		space.setName(row.getString("name"));
		space.setAccessible(row.getBoolean("is_accessible"));
		space.setOpenFrom(row.getInt("open_from"));
		space.setOpenTo(row.getInt("open_to"));
		space.setRate(row.getBigDecimal("decimal_daily_rate"));
		space.setMaxOccupancy(row.getInt("max_occupancy"));

		return space;
	}
}
