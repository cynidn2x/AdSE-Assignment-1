Feature: Trip Management
  As a user of the train reservation system
  I want to manage trips, book tickets, and handle cancellations
  So that I can reserve seats and manage train journey information

  Scenario: Successfully book a ticket on an available trip
    Given a trip from Paris to Lyon exists
    And the trip is not cancelled
    And the trip has available seats
    When I book a ticket for passenger "John Doe"
    Then the booking should be successful
    And the ticket should be in the booked tickets list
    And the passenger name should be "John Doe"

  Scenario: Cannot book a ticket when trip is at maximum capacity
    Given a trip from Paris to Lyon exists
    And the trip has reached maximum passenger capacity
    When I attempt to book a ticket for passenger "Jane Smith"
    Then a ReservationException should be thrown
    And the trip booked tickets count should remain unchanged

  Scenario: Cancelling a trip cancels all associated tickets
    Given a trip from Paris to Lyon exists
    And the trip has 3 booked tickets
    When I cancel the trip
    Then the trip should be marked as cancelled
    And all 3 booked tickets should be moved to cancelled tickets
    And the booked tickets list should be empty

  Scenario: Verify trip delays are correctly calculated
    Given a trip from Paris to Lyon with planned departure at "2025-12-25T10:00:00Z"
    And planned arrival at "2025-12-25T12:00:00Z"
    When I add a departure delay of 30 minutes
    And I add an arrival delay of 45 minutes
    Then the real departure time should be "2025-12-25T10:30:00Z"
    And the real arrival time should be "2025-12-25T12:45:00Z"
    And the trip should be marked as delayed

  Scenario: Cancel a specific ticket and verify trip tracking
    Given a trip from Paris to Lyon exists
    And the trip has 2 booked tickets for passengers "Alice" and "Bob"
    When I cancel the ticket for passenger "Alice"
    Then the booked tickets list should contain only "Bob"
    And the cancelled tickets list should contain "Alice"
    And the trip should not be cancelled
