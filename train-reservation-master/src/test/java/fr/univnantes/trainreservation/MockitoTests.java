package fr.univnantes.trainreservation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import fr.univnantes.trainreservation.impl.CityImpl;
import fr.univnantes.trainreservation.impl.TrainImpl;
import fr.univnantes.trainreservation.impl.TicketReservationSystemImpl;
import fr.univnantes.trainreservation.util.TimeManagement;

import java.time.ZoneId;
import java.time.Instant;
import java.time.Duration;


@RunWith(MockitoJUnitRunner.class)
public class MockitoTests {
    
    @Mock
    private City mockCity1;

    @Mock
    private City mockCity2;

    @Mock
    private Train mockTrain;

    @Mock
    private Ticket mockTicket;



    private Instant today;
    private Instant tomorrow;
    private TicketReservationSystem system;

    @Before
    public void setUp() {
        ZoneId timeZone = ZoneId.systemDefault();
        system = new TicketReservationSystemImpl(timeZone);

        when(mockCity1.getName()).thenReturn("Yasooj");
        when(mockCity2.getName()).thenReturn("Kashan");

        when(mockTrain.getName()).thenReturn("Fadak");
        when(mockTrain.getMaxPassengers()).thenReturn(100);

        today = TimeManagement.createInstant("2025-08-01 12:00", ZoneId.systemDefault());
        tomorrow = TimeManagement.createInstant("2025-08-02 12:00", ZoneId.systemDefault());

        system.addCity(mockCity1);
        system.addCity(mockCity2);
        system.addTrain(mockTrain);

    }

    @Test
    public void testArrivalDelayTrip() throws TripException {

        Instant dayAfterTomorrow = TimeManagement.createInstant("2025-08-03 12:00", ZoneId.systemDefault());
        Trip trip1 = system.createTrip(mockCity1, mockCity2, mockTrain, today, tomorrow);
        system.delayTripArrival(trip1, Duration.ofDays(1));

        assertTrue(trip1.isDelayed());
        assertEquals(dayAfterTomorrow, trip1.findRealArrivalTime());
    }

    @Test
    public void testDepartureDelayTrip() throws TripException {

        Instant dayAfterTomorrow = TimeManagement.createInstant("2025-08-03 12:00", ZoneId.systemDefault());
        Trip trip1 = system.createTrip(mockCity1, mockCity2, mockTrain, today, tomorrow);
        system.delayTripDeparture(trip1, Duration.ofDays(1));

        assertTrue(trip1.isDelayed());
        assertEquals(tomorrow, trip1.findRealDepartureTime());
        assertEquals(dayAfterTomorrow, trip1.findRealArrivalTime());
    }
}