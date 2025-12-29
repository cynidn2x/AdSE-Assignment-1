package fr.univnantes.trainreservation;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import fr.univnantes.trainreservation.impl.CityImpl;
import fr.univnantes.trainreservation.impl.TrainImpl;
import fr.univnantes.trainreservation.impl.TicketReservationSystemImpl;
import fr.univnantes.trainreservation.util.TimeManagement;

import java.time.ZoneId;
import java.time.Instant;

public class SimpleJUnitTest {

    private TicketReservationSystem system;
    private City city1;
    private City city2;
    private Train rajaTrain;
    private Train fadakTrain;
    private Instant today;
    private Instant tomorrow;

    @Before // The Prefix stage
    public void setUp() {
        ZoneId timeZone = ZoneId.systemDefault();
        system = new TicketReservationSystemImpl(timeZone);

        city1 = new CityImpl("Jolfa");
        city2 = new CityImpl("Sanandaj");
        system.addCity(city1);
        system.addCity(city2);


        rajaTrain = new TrainImpl("Raja", 2);
        fadakTrain = new TrainImpl("Fadak", 300);
        system.addTrain(rajaTrain);
        system.addTrain(fadakTrain);
        today = TimeManagement.createInstant("2025-07-20 12:00", ZoneId.systemDefault()); 
        tomorrow = TimeManagement.createInstant("2025-07-21 13:00", ZoneId.systemDefault());
        
    }


    @Test
    public void testCreateTripWithTheSameOriginAndDestination() throws TripException, IllegalArgumentException {

        assertThrows(IllegalArgumentException.class, () -> {
            system.createTrip(city1, city1, rajaTrain, today, tomorrow);
        });
    }

    @Test
    public void testTripWithDisorganizedDepartureTime() throws TripException, IllegalArgumentException {
        assertThrows(IllegalArgumentException.class, () -> { 
        system.createTrip(city1, city2, rajaTrain, tomorrow, today);
        });
    }

    @Test
    public void testTripWithSameDepartureAndArrival() throws TripException, IllegalArgumentException {
        assertThrows(IllegalArgumentException.class, () -> { 
        system.createTrip(city1, city2, rajaTrain, today, today);
        });
    }

    @Test 
    public void testCreateTripWithValidInput() throws TripException{
        assertNotNull("Trip should be created", system.createTrip(city1, city2, rajaTrain, today, tomorrow));
    }

    @Test
    public void testCreateTripWithNullTrain() throws TripException , IllegalArgumentException {
        assertThrows(IllegalArgumentException.class, () -> {
            system.createTrip(city1, city2, null, today, tomorrow);
        });
    }

    @Test
    public void testBookTicketOnFullTrain() throws TripException, ReservationException {
        Trip trip1 = system.createTrip(city1, city2, rajaTrain, today, tomorrow);
        trip1.bookTicket("Alireza");
        trip1.bookTicket("Parvin");
        assertThrows(ReservationException.class, () -> {
            trip1.bookTicket("Matin");
        });
    }

    @Test
    public void testBookTicketOnNonFullTrain() throws TripException, ReservationException {
        Trip trip1 = system.createTrip(city1, city2, fadakTrain, today, tomorrow);
        assertNotNull("Ticket should be booked", trip1.bookTicket("Alireza"));
    }

    @Test
    public void testBookTicketOnCanceledTrip() throws TripException, ReservationException {
        Trip trip1 = system.createTrip(city1, city2, fadakTrain, today, tomorrow);
        trip1.cancel();
        assertThrows(ReservationException.class, () -> {
            trip1.bookTicket("Alireza");
        });
    }
    
    @Test
    public void testCancelTrip() throws TripException {
        Trip trip1 = system.createTrip(city1, city2, fadakTrain, today, tomorrow);
        trip1.cancel();
        assertTrue("Trip should be canceled", trip1.isCancelled());
    }
    @Test
    public void testCancelTicket() throws TripException, ReservationException {
        Trip trip1 = system.createTrip(city1, city2, fadakTrain, today, tomorrow);
        Ticket ticket = trip1.bookTicket("Alireza");
        trip1.cancelTicket(ticket);
        assertTrue("Ticket should be in the cancelled tickets", trip1.getCancelledTickets().contains(ticket) == true);
        assertTrue("Ticket should not be in the booked tickets", trip1.getBookedTickets().contains(ticket) == false);
    }
    @Test
    public void testCancelTicketTwice() throws TripException, ReservationException {
        Trip trip1 = system.createTrip(city1, city2, fadakTrain, today, tomorrow);
        Ticket ticket = trip1.bookTicket("Alireza");
        trip1.cancelTicket(ticket);
        assertThrows(ReservationException.class, () -> {
            trip1.cancelTicket(ticket);
        });
    }
}
