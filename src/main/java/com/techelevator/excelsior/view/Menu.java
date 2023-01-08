package com.techelevator.excelsior.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.techelevator.excelsior.model.Category;
import com.techelevator.excelsior.model.Reservation;
import com.techelevator.excelsior.model.Space;
import com.techelevator.excelsior.model.Venue;
import com.techelevator.excelsior.model.dao.ReservationDAO;
import com.techelevator.excelsior.model.dao.SpaceDAO;
import com.techelevator.excelsior.model.dao.VenueDAO;

public class Menu {
	
	Scanner in = new Scanner(System.in);

	public Menu() {
		
	}
	
	public void displayErrorMessage() {
		displayUserMessage("Invalid selection, please try again");
	}

	public void displayUserMessage(String message) {
		System.out.println(message);
	}

	public String displayMainMenu() {
		displayUserMessage("What would you like to do?");
		displayUserMessage("1) List Venues");
		displayUserMessage("Q) Quit");
		return in.nextLine();
	}
	
	public String displayVenueSelectionMenu() {
		displayUserMessage("What would you like to do next?");
		displayUserMessage("1) View Spaces");
		displayUserMessage("2) Search for Reservation ***BONUS***");
		displayUserMessage("R) Return to Previous Screen");
		return in.nextLine();
	}
	
	public String displayReserveSpaceMenu() {
		displayUserMessage("What would you like to do next?");
		displayUserMessage("1) Reserve a Space");
		displayUserMessage("R) Return to Previous Screen");
		return in.nextLine();
	}
	
	public int reserveSpaceSelection() {
		displayUserMessage("Which space would you like to reserve (enter 0 to cancel)?");
		int result = in.nextInt();
		in.nextLine();
		return result;
	}
	
	public String reserveSpaceFor() {
		displayUserMessage("Who is this reservation for?");
		return in.nextLine();
	}
	
	public void reserveSpaceConfirmation(Reservation reservation, String spaceName, Venue venue, double totalCost) {
		displayUserMessage("Thank you for submitting your reservation! The details for your event are listed below:");
		System.out.println();
		displayUserMessage("Confirmation #: " + reservation.getReservationId());
		displayUserMessage("Venue: " + venue.getName());
		displayUserMessage("Space: " + spaceName);
		displayUserMessage("Reserved for: " + reservation.getReservationName());
		displayUserMessage("Attendees: " + reservation.getNumberOfAttendees());
		displayUserMessage("Arrival Date: " + reservation.getStartDate());
		displayUserMessage("Depart Date:" + reservation.getEndDate());
		displayUserMessage("Total Cost: " + totalCost);
	}

	public LocalDate getStartDateFromUser() {
		displayUserMessage("When do you need the space? MM/DD/YYYY");
		String input = in.nextLine();
		DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate startDate = LocalDate.parse(input, formatter);
		return startDate;
	}
	
	public int getEndDateFromUser() {
		displayUserMessage("How many days will you need the space?");
		int input = in.nextInt();
		in.nextLine();
		return input;
	}
	
	public int getNumberOfAttendeesFromUser() {
		displayUserMessage("How many people will be in attendance?");
		int input = in.nextInt();
		in.nextLine();
		return input;
	}

	public String listVenues(List<Venue> venues) {
		System.out.println();
		displayUserMessage("Which venue would you like to view?");
		if (venues.size() > 0) {
			for (int i = 0; i < venues.size(); i++) {
				int optionNum = i + 1;
				displayUserMessage((optionNum + ") " + venues.get(i).getName()));
			}
		} else {
			displayUserMessage("\n*** No results ***");
		}
		displayUserMessage("R) Return to previous screen");
		System.out.println();
		return in.nextLine();
	}

	
	public void displayVenueInfo(Venue venue) {
		
		displayUserMessage(venue.getName());
		displayUserMessage("Location: " + venue.getLocation());
		System.out.print("Categories: " + venue.getCategories());
		System.out.println();
		displayUserMessage(venue.getDescription());
		System.out.println();
	}
	
	public void listSpaces(List<Space> spaces, Venue venue) {
		System.out.println();
		displayUserMessage(venue.getName());
		System.out.println();
		if (spaces.size() > 0) {
			System.out.printf("%2s  %-30s  %-10s  %-10s  %-10s  %-10s%n"," ",  "Name", "Open", "Closed", "Daily Rate", "Max.Occupancy");
			for (int i = 0; i < spaces.size(); i++) {
				int optionNum = i + 1;
				String spaceName = spaces.get(i).getName();
				int spaceOpen = spaces.get(i).getOpenFrom();
				int spaceClosed = spaces.get(i).getOpenTo();
				BigDecimal dailyRate = spaces.get(i).getRate();
				int maxOccupancy = spaces.get(i).getMaxOccupancy();
				System.out.printf("%2s  %-30s  %-10s  %-10s  %-10s  %-10s%n", "#" + optionNum, spaceName, spaceOpen, spaceClosed, dailyRate, maxOccupancy);
			}
		} else {
			displayUserMessage("\n*** No results ***");
		}
		System.out.println();
	}

	public double listAvailableSpaces(List<Space> spaces, int venueId, int numberOfDays) {
		double totalCost = 0;
		System.out.println();
		if (spaces.size() > 0) {
			System.out.printf("%8s  %-30s  %-10s  %-15s  %-15s  %-10s%n","Space #", "Name", "Daily Rate", "Max Occupancy", "Accessible?", "Total Cost");
			for (int i = 0; i < spaces.size(); i++) {
				int optionNum = i + 1;
				int spaceId = spaces.get(i).getSpaceId();
				String spaceName = spaces.get(i).getName();
				BigDecimal dailyRate = spaces.get(i).getRate();
				int maxOccupancy = spaces.get(i).getMaxOccupancy();
				boolean isAccessible = spaces.get(i).isAccessible();
				totalCost = dailyRate.doubleValue() * numberOfDays;
				System.out.printf("%8s  %-30s  %-10s  %-15s  %-15s  %-10s%n", spaceId, spaceName, dailyRate, maxOccupancy, isAccessible, totalCost);
			}
		}
		else {
			displayUserMessage("No Available Spaces");
		} return totalCost;
		
	}
	
	public String redoSearch() {
		displayUserMessage("Would you like to search again? Y/N");
		return in.nextLine();
	}
	
}
