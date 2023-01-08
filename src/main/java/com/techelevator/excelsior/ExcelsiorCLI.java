package com.techelevator.excelsior;

import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.excelsior.model.Reservation;
import com.techelevator.excelsior.model.Space;
import com.techelevator.excelsior.model.dao.JDBCReservationDAO;
import com.techelevator.excelsior.model.dao.JDBCSpaceDAO;
import com.techelevator.excelsior.model.dao.JDBCVenueDAO;
import com.techelevator.excelsior.model.dao.ReservationDAO;
import com.techelevator.excelsior.model.dao.SpaceDAO;
import com.techelevator.excelsior.model.dao.VenueDAO;
import com.techelevator.excelsior.view.Menu;

public class ExcelsiorCLI {
	
	private static final String VIEW_VENUES = "1";
	private static final String QUIT = "Q";
	private static final String RETURN = "R";
	private static final String YES = "Y";
	private static final int LIST_ALL_SPACES = 1;
	private static final int SEARCH_FOR_RESERVATION = 2;
	private static final String RESERVE_SPACE = "1";


	private Menu menu;
	private ExcelsiorSystem system;

	public ExcelsiorCLI(DataSource datasource) {
		system = new ExcelsiorSystem(datasource);
		menu = new Menu();
	}

	public void run() {
		showMainMenu();
	}

	private void showMainMenu() {
		while (true) {
			String listOrQuit = menu.displayMainMenu();

			if (listOrQuit.equals(VIEW_VENUES)) {
				showViewVenueMenu();
			} else if (listOrQuit.equalsIgnoreCase(QUIT)) {
				break;
			} else {
				menu.displayErrorMessage();
			}
		}
	}

	private void showViewVenueMenu() {
		boolean listVenues = true;
		while (listVenues) {
			String listVenueSelection = menu.listVenues(system.listAllVenues());
			if (listVenueSelection.equalsIgnoreCase(RETURN)) {
				break;
			} else {
				int pickVenue = 0;
				try {
					pickVenue = Integer.parseInt(listVenueSelection);
				} catch (Exception e) {
					menu.displayErrorMessage();
				}
				if (pickVenue > system.listAllVenues().size() || pickVenue < 1) {
					menu.displayErrorMessage();
				} else {
					int venueId = system.listAllVenues().get(pickVenue - 1).getVenueId();
					menu.displayVenueInfo(system.getVenueByVenueId(venueId));
					listVenues = showVenueSelectionMenu(venueId);
				}
			}
		}
	}


	private boolean showVenueSelectionMenu(int venueId) {
		boolean showVenues = true;
		while (showVenues) {
			String venueSelection = menu.displayVenueSelectionMenu();
			if (venueSelection.equalsIgnoreCase(RETURN)) { 
				showVenues = true;
				break;
			} else {
				int venueSelectionChoice = 0;
				try {
					venueSelectionChoice = Integer.parseInt(venueSelection);	
				} catch (Exception e) {
					menu.displayErrorMessage();
				}
				if (venueSelectionChoice == LIST_ALL_SPACES) {
					int spaceId = system.listAllSpaces(venueId).get(venueSelectionChoice - 1).getSpaceId();
					menu.listSpaces(system.listAllSpaces(venueId), system.getVenueByVenueId(venueId));
					showVenues = showReserveMenu(venueId);
					continue;
				}
				if (venueSelectionChoice == SEARCH_FOR_RESERVATION) {
					// ***BONUS MENU***
				} else {
					menu.displayErrorMessage();
				}
			}
		}
		return showVenues;
	}
	
	private boolean showReserveMenu(int venueId) {
		boolean reserve = true;
		while (reserve) {
			String reserveMenuSelection = menu.displayReserveSpaceMenu();

			if (reserveMenuSelection.equalsIgnoreCase(RETURN)) {
				reserve = true;
				break;
			}
			if (reserveMenuSelection.equals(RESERVE_SPACE)) {
				reserve = false;
				spaceSearch(venueId);
				break;
			} else {
				menu.displayErrorMessage();
			}
		}
		return reserve;
	}
	
	private boolean spaceSearch(int venueId) {
		boolean search = true;
		while (search) {
			LocalDate startDate = menu.getStartDateFromUser();
			int numberOfDays = menu.getEndDateFromUser();
			int numberOfAttendees = menu.getNumberOfAttendeesFromUser();
			
			List<Space> availableSpaces = system.listAvailableSpaces(startDate, numberOfDays, numberOfAttendees, venueId);
			
			double totalCost = menu.listAvailableSpaces(availableSpaces, venueId, numberOfDays);
			
			if (availableSpaces.size() < 1) {
				search = noAvailableSpaces();
				continue;
			}
			int spaceIdSelection = menu.reserveSpaceSelection();
			if (spaceIdSelection == 0) {
				search = false;
				break;
			}
			String reservationName = menu.reserveSpaceFor();
			int reservationId = system.bookReservation(spaceIdSelection, numberOfAttendees, startDate, numberOfDays, reservationName);
			Reservation reservation = system.getReservationByReservationId(reservationId);
			menu.reserveSpaceConfirmation(reservation, system.getNameBySpaceId(reservation.getSpaceId()), system.getVenueByVenueId(venueId), totalCost);
			search = false;
			break;
		}
		return search;
	}

	private boolean noAvailableSpaces() {
		boolean redoSearch = true;
		while (true) {
			String yesOrNo = menu.redoSearch();
			if (yesOrNo.equalsIgnoreCase(YES)) {
				redoSearch = true;
				break;
			} else {
				redoSearch = false;
				break;
			}
		}
		return redoSearch;
	}

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/excelsior-venues");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		ExcelsiorCLI application = new ExcelsiorCLI(dataSource);
		application.run();
	}
}
