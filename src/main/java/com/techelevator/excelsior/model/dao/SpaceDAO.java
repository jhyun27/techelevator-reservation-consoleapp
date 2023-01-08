package com.techelevator.excelsior.model.dao;

import java.time.LocalDate;
import java.util.List;

import com.techelevator.excelsior.model.Space;

public interface SpaceDAO {
	
	List<Space> listAllSpaces(int venueId);
	String getNameBySpaceId(int spaceId);
	List<Space> listAvailableSpaces(int venueId, LocalDate startDate, LocalDate endDate, int numberOfAttendees);

}
