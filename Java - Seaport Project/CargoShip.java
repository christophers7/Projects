/*
* File Name: CargoShip.java
* Date: March 31, 2019
* Author: Christopher Scullin
* Purpose: Constructs a cargo ship and with its cargo
* value, volume, and weight.
*/

import java.util.Scanner;

public class CargoShip extends Ship {
    double cargoValue = 0;
    double cargoVolume = 0;
    double cargoWeight = 0;
    
    public CargoShip(Scanner sc) {
        super(sc);
        if (sc.hasNextDouble()) cargoWeight = sc.nextDouble();
        if (sc.hasNextDouble()) cargoVolume = sc.nextDouble();
        if (sc.hasNextDouble()) cargoValue = sc.nextDouble();
    } // End Scanner constructor
    
    public String toString () {
        String st = "Cargo ship: " + super.toString();
        return st;
    } // End method toString
} // End class CargoShip
