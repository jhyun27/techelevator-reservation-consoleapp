package com.techelevator.excelsior.model.dao;

import java.util.List;

import com.techelevator.excelsior.model.Category;
import com.techelevator.excelsior.model.Venue;

public interface VenueDAO {
	
	List<Venue> listAllVenues();
	Venue getVenueByVenueId(int venueId);

}
