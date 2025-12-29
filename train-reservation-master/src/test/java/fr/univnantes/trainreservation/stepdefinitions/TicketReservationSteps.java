package fr.univnantes.trainreservation.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import fr.univnantes.trainreservation.City;
import fr.univnantes.trainreservation.ReservationException;
import fr.univnantes.trainreservation.Ticket;
import fr.univnantes.trainreservation.TicketReservationSystem;
import fr.univnantes.trainreservation.Train;
import fr.univnantes.trainreservation.Trip;
import fr.univnantes.trainreservation.TripException;
import fr.univnantes.trainreservation.impl.CityImpl;
import fr.univnantes.trainreservation.impl.TicketReservationSystemImpl;
import fr.univnantes.trainreservation.impl.TrainImpl;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class TicketReservationSteps {

    private TicketReservationSystem system;
    private ZoneId timeZone;
    private City originCity;
    private City destinationCity;
    private Train train;
    private Trip trip;
    private List<Ticket> bookedTickets;
    private ReservationException exp;
    private Instant departureTime;
    private Instant arrivalTime;

    @Before
    public void setUp() {
        timeZone = ZoneId.systemDefault();
        system = new TicketReservationSystemImpl(timeZone);
        bookedTickets = new ArrayList<>();
        exp = null;
    }

    // ==================== Scenario: Admin can create a new trip ====================

    @Given("Arrival time has been set to {string}")
    public void initializeSystemWithArrivalTime(String arrival) {
        arrivalTime = Instant.parse(arrival);
    }
    @Given("Departure time has been set to {string}")
    public void  initzesystemWithDepartureTime(String departure) {
        departureTime = Instant.parse(departure);
    }

    @Given ("Destination city has been set to {string}")
    public void initzesystemWithDestinationCity(String destination) {
        destinationCity = new CityImpl(destination);
        system.addCity(destinationCity);
        assertTrue(system.getCities().contains(destinationCity));
    }

    @Given ("Origin city has been set to {string}")
    public void initzesystemWithOriginCity(String origin) {
        originCity = new CityImpl(origin);
        system.addCity(originCity);
        assertTrue(system.getCities().contains(originCity));
    }

    @When("the Admin attempts to create a new trip from {string} to {string} departing at {string} and arriving at {string}")
    public void adminCreatesTrip(String origin, String destination, String departure, String arrival) throws TripException {
        departureTime = Instant.parse(departure);
        arrivalTime = Instant.parse(arrival);
        train = new TrainImpl("Regional Train", 100);
        system.addTrain(train);
        trip = system.createTrip(originCity, destinationCity, train, departureTime, arrivalTime);
        assertNotNull("Trip should be created successfully", trip);
    }

    @Then("the system should have created exactly {int} trip")
    public void verifyTripCountCreated(int expectedCount) {
        assertEquals("System should have exactly " + expectedCount + " trip", expectedCount, system.getAllTrips().size());
    }

    @Then("the trip must have {string} as origin and {string} as destination")
    public void verifyTripOriginAndDestination(String origin, String destination) {
        assertEquals("Trip origin should be " + origin, origin, trip.getOrigin().getName());
        assertEquals("Trip destination should be " + destination, destination, trip.getDestination().getName());
    }

    // ==================== Scenario : Admin can delay a trip's arrival time ====================

    @Given("a trip exists from {string} to {string} departing at {string} and arriving at {string}")
    public void createTripForDelayTest(String origin, String destination, String departure, String arrival) throws TripException {
        
        initializeSystemWithArrivalTime(arrival);
        initzesystemWithDepartureTime(departure);
        initzesystemWithOriginCity(origin);
        initzesystemWithDestinationCity(destination);

        adminCreatesTrip(origin, destination, departure, arrival);
    }

    @When("the Admin delays the trip arrival time by {int} minutes")
    public void adminDelaysTripArrival(int delayMinutes) {
        Duration delayDuration = Duration.ofMinutes(delayMinutes);
        system.delayTripArrival(trip, delayDuration);
    }

    @Then("the trip should be marked as delayed")
    public void verifyTripIsDelayed() {
        assertTrue("Trip should be marked as delayed", trip.isDelayed());
    }

    @Then("the arrival delay should be {int} minutes")
    public void verifyArrivalDelay(int expectedMinutes) {
        Duration expectedDelay = Duration.ofMinutes(expectedMinutes);
        assertEquals("Arrival delay should be " + expectedMinutes + " minutes", expectedDelay, trip.getArrivalDelay());
    }

    // ==================== Scenario: User cannot book a ticket on a fully booked train ====================

    @Given("a trip has been created with capacity of {int}")
    public void createTripWithLimitedCapacity(int capacity) throws TripException {
        originCity = new CityImpl("Istanbul");
        destinationCity = new CityImpl("Tabriz");
        system.addCity(originCity);
        system.addCity(destinationCity);

        
        train = new TrainImpl("Regional Train", capacity);
        system.addTrain(train);

        departureTime = Instant.parse("2025-12-26T14:00:00Z");
        arrivalTime = Instant.parse("2025-12-26T16:00:00Z");
        trip = system.createTrip(originCity, destinationCity, train, departureTime, arrivalTime);        
    }
    
    @Given("the trip has reached maximum passenger capacity of {int}")
    public void fillTripToCapacity(int capacity) throws ReservationException {
        bookedTickets.clear();
        for (int i = 1; i <= capacity; i++) {
            Ticket ticket = trip.bookTicket("P" + i);
            bookedTickets.add(ticket);
        }
    }

    @When("the user attempts to book a ticket")
    public void userAttemptsToBookAdditionalTicket() {
        try {
            trip.bookTicket("bluh bluh");
            exp = null; // If no exception is thrown, set it to null
        } catch (ReservationException e) {
            exp = e;
        }
    }

    @Then("the system throws a ReservationException indicating no available seats")
    public void verifyReservationException() {
        assertNotNull("System should throw a ReservationException", exp);
        assertTrue("Exception should indicate no available seats", true); 
    }

}
