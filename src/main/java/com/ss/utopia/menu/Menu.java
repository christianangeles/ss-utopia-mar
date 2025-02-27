/**
 * 
 */
package com.ss.utopia.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ss.utopia.entity.Booking;
import com.ss.utopia.entity.BookingPayment;
import com.ss.utopia.entity.BookingUser;
import com.ss.utopia.entity.Flight;
import com.ss.utopia.entity.FlightBookings;
import com.ss.utopia.entity.Passenger;
import com.ss.utopia.entity.User;
import com.ss.utopia.services.EmployeeService;
import com.ss.utopia.services.ReadService;
import com.ss.utopia.services.TravelerService;
import com.ss.utopia.services.UpdateService;

/**
 * @author Christian Angeles
 *
 */
public class Menu {
	private StringBuilder menu;
	private UserInput input;
	private List<Flight> flightList;
	private Flight flight;
	private User user;

	public Menu() {
		menu = new StringBuilder();
		input = new UserInput();
		flightList = new ArrayList<>();
		user = null;
		flight = null;
	}
	
	/**
	 * @return the flightList
	 */
	public List<Flight> getFlightList() {
		return this.flightList;
	}
	/**
	 * 
	 * @return
	 */
	public StringBuilder utopiaMenu() {
		System.out.println(
				  "Welcome to the Utopia Airlines Management System. Which category of a user are you:\n"
				+ "1) Employee\n"
				+ "2) Administrator\n"
				+ "3) Traveler\n");
		input.setInput();
		return input.getInput();
	}
	/**
	 * 
	 * @return
	 */
	public StringBuilder empMenu() {
		System.out.println(
				  "1) Enter flights you manage\n"
				+ "2) Return to previous\n");
		input.setInput();
		return input.getInput();
	}
	/**
	 * 
	 * @return
	 */
	public StringBuilder empFlights() {
		ReadService service = new ReadService();
		flightList = service.getFlightList();
		menu.setLength(0);
		int count = 1;
		for(Flight flight: flightList) {
			menu.append(count+") ");
			menu.append(flight.getRoute().getOriginAirport().getAirportID()+", "+flight.getRoute().getOriginAirport().getCity()+" -> ");
			menu.append(flight.getRoute().getDestAirport().getAirportID()+", "+flight.getRoute().getDestAirport().getCity()+"\n");
			count++;
		}
		menu.append(count+") Return to previous\n");
		System.out.println(menu);
		input.setInput();
		return input.getInput();
	}
	/**
	 * 
	 * @param flightID
	 * @return
	 */
	public StringBuilder empFlightDetail(Integer flightID) {
		ReadService service = new ReadService();
		if(flightList.size() > 0 && flightID > 0) {
			flight = service.getFlight(flightList.get(flightID-1)).get(0);
			System.out.println(
					  "1) View more details about the flight\n"
					+ "2) Update the details of the flight\n"
					+ "3) Add seats to flight\n"
					+ "4) Return to previous\n");
			input.setInput();
		}
		return input.getInput();
	}
	/**
	 * 
	 * @return
	 */
	public StringBuilder empViewFlight() {
		menu.setLength(0);
		menu.append(
				  "You have chosen to view the Flight with Flight Id: "+flight.getFlightID()
				+ " and Departure Airport: "+flight.getRoute().getOriginAirport().getCity()
				+ " and Arrival Airport: "+flight.getRoute().getDestAirport().getCity()+".\n\n"
				+ "Departure Airport: "+flight.getRoute().getOriginAirport().getAirportID()
				+ " | Arrival Airport: "+flight.getRoute().getDestAirport().getAirportID()+" |\n"
				+ "Departure Date: "+flight.getDepartureTime().toString().split(" ")[0]
				+ " | Departure Time: "+flight.getDepartureTime().toString().split(" ")[1]+" |\n\n"
				+ "Available Seats:\n"
				+ "1) Check reserved seats\n"
				+ "2) Return to previous\n");
		System.out.println(menu);
		input.setInput();
		return input.getInput();
	}
	/**
	 * 
	 * @return
	 */
	public StringBuilder empCheckSeats() {
		menu.setLength(0);
		menu.append("Reserved seats: "+flight.getReservedSeats()+"\n");
		System.out.println(menu);
		return input.getInput();
	}
	/**
	 * 
	 * @return
	 */
	public StringBuilder empUpdateFlight() {
		EmployeeService service = new EmployeeService();
		MenuUtil util = new MenuUtil();
		menu.setLength(0);
		menu.append(
				"You have chosen to update the Flight with Flight Id: "+flight.getFlightID()
				+ " and Flight Origin: "+flight.getRoute().getOriginAirport().getAirportID()
				+ " and Flight Destination: "+flight.getRoute().getDestAirport().getAirportID()+".\n"
				+ "Enter ‘quit’ at any prompt to cancel operation.\n");
		System.out.println(menu);
		flight = util.empUpdateFlightUtil(flight);
		service.updateFlightRoute(flight, flight.getRoute());
		
		return input.getInput();
	}
	/**
	 * 
	 * @return
	 */
	public StringBuilder empAddSeat() {
		UpdateService service = new UpdateService();
		MenuUtil util = new MenuUtil();
		System.out.println(
				  "Reserve a seat for flight?\n"
				+ "1) Add seat\n"
				+ "2) Return to previous\n");
		input.setInput();
		if(input.getInput().charAt(0) == '1') {
			flight = util.empUpdateSeatUtil(flight);
			service.updateFlight(flight);
		}
		return input.getInput();
	}
	/**
	 * 
	 * @return
	 */
	public boolean checkMembership() {
		ReadService service = new ReadService();
		List<User> userlist = new ArrayList<>();
		userlist = service.getUserList();
		System.out.println("Enter the your Membership Number:\n");
		input.setInput();
		int member = 0;
		try {
			member = Integer.parseInt(input.getInput().toString());
			for(User u: userlist) {
				if(u.getUserID().equals(member)) {
					user = u;
					return true;
				}
			}
		}
		catch(NumberFormatException nfEx) {
			System.out.println("NumberFormatException: "+nfEx.getMessage()+"\n");
		}
		System.out.println("Membership Number not found.\n");
		return false;
	}
	/**
	 * 
	 * @return
	 */
	public StringBuilder travelMenu() {
		System.out.println(
				  "1) Book a ticket\n"
				+ "2) Cancel an upcoming trip\n"
				+ "3) Return to previous\n");
		input.setInput();
		return input.getInput();
	}
	/**
	 * 
	 * @return
	 */
	public StringBuilder travelFlights() {
		ReadService service = new ReadService();
		flightList = service.getFlightList();
		menu.setLength(0);
		int count = 1;
		System.out.println("Pick the flight you want to book a ticket for:\n");
		for(Flight flight: flightList) {
			menu.append(count+") ");
			menu.append(flight.getRoute().getOriginAirport().getAirportID()+", "+flight.getRoute().getOriginAirport().getCity()+" -> ");
			menu.append(flight.getRoute().getDestAirport().getAirportID()+", "+flight.getRoute().getDestAirport().getCity()+"\n");
			count++;
		}
		menu.append(count+") Return to previous\n");
		System.out.println(menu);
		input.setInput();
		return input.getInput();
	}
	/**
	 * 
	 * @param flightID
	 * @return
	 */
	public StringBuilder travelBooking(Integer flightID) {
		ReadService service = new ReadService();
		if(flightList.size() > 0 && flightID > 0) {
			flight = service.getFlight(flightList.get(flightID-1)).get(0);
			System.out.println(
					  "1) View flight details\n"
					+ "2) Book this flight\n"
					+ "3) Return to previous\n");
			input.setInput();
		}
		return input.getInput();
	}
	/**
	 * 
	 * @return
	 */
	public StringBuilder travelViewFlight() {
		menu.setLength(0);
		menu.append(
				  "You have chosen to view the Flight with Flight Id: "+flight.getFlightID()
				+ " and Departure Airport: "+flight.getRoute().getOriginAirport().getCity()
				+ " and Arrival Airport: "+flight.getRoute().getDestAirport().getCity()+".\n\n"
				+ "Departure Airport: "+flight.getRoute().getOriginAirport().getAirportID()
				+ " | Arrival Airport: "+flight.getRoute().getDestAirport().getAirportID()+" |\n"
				+ "Departure Date: "+flight.getDepartureTime().toString().split(" ")[0]
				+ " | Departure Time: "+flight.getDepartureTime().toString().split(" ")[1]+" |\n\n"
				+ "Available Seats: "+(flight.getAirplane().getAirplaneTypeID().getMaxCap()-flight.getReservedSeats()));
		System.out.println(menu);
		input.setInput();
		return input.getInput();
	}
	/**
	 * 
	 */
	public void travelBookFlight() {
		ReadService service = new ReadService();
		TravelerService tservice = new TravelerService();
		List<Booking> booklist = new ArrayList<>();
		List<Passenger> passengerlist = new ArrayList<>();
		Booking book = new Booking();
		FlightBookings fbook = new FlightBookings();
		BookingPayment bookp = new BookingPayment();
		BookingUser booku = new BookingUser();
		Passenger passenger = new Passenger();
		

		if(flight.getReservedSeats() < flight.getAirplane().getAirplaneTypeID().getMaxCap()) {
			flight.setReservedSeats(flight.getReservedSeats()+1);

			booklist = service.getBookingList();
			if(booklist.size() > 0) {
				book.setBookingID(booklist.size()+1);
			}
			else {
				book.setBookingID(1);
			}
			book.setIsActive(1);
			Integer rand = new Random().nextInt(10000)+1;
			book.setConfirmCode(rand.toString());
			
			fbook.setBookingID(book.getBookingID());
			fbook.setFlightID(flight.getFlightID());
			
			bookp.setBookingID(book);
			bookp.setRefunded(0);
			rand = new Random().nextInt(10000)+1;
			bookp.setStripeID(rand.toString());
			
			booku.setBookingID(book.getBookingID());
			booku.setUserID(user.getUserID());
			
			passengerlist = service.getPassengerList();
			if(passengerlist.size() > 0) {
				passenger.setPassengerID(passengerlist.size()+1);
			}
			else {
				passenger.setPassengerID(1);
			}
			passenger.setBookingID(book);
			passenger.setFamilyName(user.getFamilyName());
			passenger.setGivenName(user.getGivenName());
			System.out.println("Enter your address:\n");
			input.setInput();
			passenger.setAddress(input.getInput().toString());
			System.out.println("Enter your gender:\n");
			input.setInput();
			passenger.setGender(input.getInput().toString());
			System.out.println("Enter your date of birth:\n");
			input.setInput();
			passenger.setDob(input.getInput().toString());
			
			tservice.addBookedFlight(book, bookp, booku, passenger, fbook, flight);
		}
		else {
			System.out.println("Book flight unsuccessful");
		}
	}
	/**
	 * 
	 * @return
	 */
	public StringBuilder travelCancelList() {
		ReadService service = new ReadService();
		flightList = service.getFlightListByUserID(user.getUserID());
		menu.setLength(0);
		int count = 1;
		for(Flight flight: flightList) {
			menu.append(count+") ");
			menu.append(flight.getRoute().getOriginAirport().getAirportID()+", "+flight.getRoute().getOriginAirport().getCity()+" -> ");
			menu.append(flight.getRoute().getDestAirport().getAirportID()+", "+flight.getRoute().getDestAirport().getCity()+"\n");
			count++;
		}
		menu.append(count+") Return to previous\n");
		System.out.println(menu);
		input.setInput();
		return input.getInput();
	}
	/**
	 * 
	 * @param flightID
	 * @return
	 */
	public StringBuilder travelCancelBooking(Integer flightID) {
		ReadService service = new ReadService();
		if(flightList.size() > 0 && flightID > 0) {
			flight = service.getFlight(flightList.get(flightID-1)).get(0);
			System.out.println(
					  "1) View flight details\n"
					+ "2) Cancel this flight\n"
					+ "3) Return to previous\n");
			input.setInput();
		}
		return input.getInput();
	}
	/**
	 * 
	 */
	public void travelCancelFlight() {
		//update booking isActive = 0
		//update booking payment refunded = 1
		//delete flight booking for booking id
		//update flight reserved seats
	}
	/**
	 * 
	 * @return
	 */
	public StringBuilder adminMenu() {
		System.out.println(
				  "1) Add/Update/Delete/Read Flights\n"
				+ "2) Add/Update/Delete/Read Seats\n"
				+ "3) Add/Update/Delete/Read Tickets and Passengers\n"
				+ "4) Add/Update/Delete/Read Airports\n"
				+ "5) Add/Update/Delete/Read Travelers\n"
				+ "6) Add/Update/Delete/Read Employees\n"
				+ "7) Override Trip Cancellation for a ticket\n"
				+ "8) Return to previous\n");
		
		input.setInput();
		return input.getInput();
	}
}
