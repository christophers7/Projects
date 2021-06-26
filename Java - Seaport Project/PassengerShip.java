/*
* File Name: PassengerShip.java
* Date: March 31, 2019
* Author: Christopher Scullin
* Purpose: Constructs a passenger ship and with a
* number of passengers, rooms, and occupied rooms.
*/

import java.util.Scanner;

public class PassengerShip extends Ship {
    int numberOfOccupiedRooms = 0;
    int numberOfPassengers = 0;
    int numberOfRooms = 0;
    
    public PassengerShip (Scanner sc) {
        super(sc);
        if (sc.hasNextInt()) numberOfPassengers = sc.nextInt();
        if (sc.hasNextInt()) numberOfRooms = sc.nextInt();
        if (sc.hasNextInt()) numberOfOccupiedRooms = sc.nextInt();
    } // End Scanner constructor 
    
    public String toString () {
        String st = "Passenger ship: " + super.toString();
        return st;
    } // End method toString
} // End class PassengerShip
