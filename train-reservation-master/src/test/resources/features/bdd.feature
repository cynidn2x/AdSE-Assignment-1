Feature: BDD Feature for Train Reservation System

    @happypaths @implementation
    Scenario: Admin can create a new trip
        Given Arrival time has been set to "2025-12-26T12:00:00Z"
        And Departure time has been set to "2025-12-26T10:00:00Z"
        And Destination city has been set to "Isfahan"
        And Origin city has been set to "Tehran"
        When the Admin attempts to create a new trip from "Tehran" to "Isfahan" departing at "2025-12-26T10:00:00Z" and arriving at "2025-12-26T12:00:00Z"
        Then the system should have created exactly 1 trip
        And the trip must have "Tehran" as origin and "Isfahan" as destination

    @happypaths  @implementation
    Scenario: Admin can delay a trip's arrival time
        Given a trip exists from "Kerman" to "Yazd" departing at "2025-12-26T10:00:00Z" and arriving at "2025-12-26T12:00:00Z"
        When the Admin delays the trip arrival time by 4 minutes
        Then the trip should be marked as delayed
        And  the arrival delay should be 4 minutes

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
    
    @exceptionpaths @implementation
    Scenario: User cannot book a ticket on a fully booked train
        Given a trip has been created with capacity of 2 
        And the trip has reached maximum passenger capacity of 2
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