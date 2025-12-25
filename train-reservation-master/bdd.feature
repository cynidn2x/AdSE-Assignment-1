Feature: BDD Feature for Train Reservation System

    @happypaths
    Scenario: Admin can create a new trip
        Given Arrival time has been created
        And Departure time has been created
        And Destination city has been created
        And Departure city has been created
        When the Admin attempts to create a new trip
        Then the system creates a new trip

    @happypaths
    Scenario: Admin can delay a trip's arrival time
        Given a trip has been created by the Admin
        When the user requests to delay the trip arrival time
        Then the system adds a delay to the trip arrival time

    @happypaths
    Scenario: Admin can delay a trip's departure time
        Given a trip has been created by the Admin
        When the user requests to delay the trip departure time
        Then the system adds a delay to the trip departure time

    @happypaths
    Scenario: User can book a train ticket
        Given the user has searched for available trains
        When the user selects a train and provides passenger details
        And makes a payment
        Then the system confirms the booking and provides a ticket
    
    @happypaths
    Scenario: User can search for available trips between two cities
        Given a trip has been created successfully
        When the user enters departure and destination cities along with travel dates
        Then the system displays a list of available trains matching the criteria
    
    @exceptionpaths
    Scenario: User cannot book a ticket on a fully booked train
        Given a trip has been created
        And the trip has reached maximum passenger capacity
        When the user attempts to book a ticket
        Then the system throws a ReservationException indicating no available seats

    @exceptionpaths
    Scenario: User cannot book a ticket on a cancelled trip
        Given a trip has been created
        And the trip has been cancelled
        When the user attempts to book a ticket
        Then the system throws a ReservationException indicating the trip has been cancelled

    @exceptionpaths
    Scenario: User cannot book tickets with departure time in the past
        Given a trip has been created
        And the trip departure time has already passed
        When the user attempts to book a ticket
        Then the system throws a ReservationException indicating invalid departure time

    @exceptionpaths
    Scenario: Admin cannot create a trip with the same departure and destination cities
        Given a city has been created 
        When the Admin enters the same city for both departure and destination
        Then the system throws a ValidationException indicating invalid trip details

    @exceptionpaths
    Scenario: Admin cannot create a trip with arrival time before departure time
        Given a departure time has been created
        And an arrival time has been created
        When the Admin enters an arrival time that is before the departure time
        Then the system throws a ValidationException indicating invalid trip details