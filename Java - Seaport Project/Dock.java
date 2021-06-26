/*
* File Name: Dock.java
* Date: March 31, 2019
* Author: Christopher Scullin
* Purpose: Creates a dock and holds the data for
* a ship the is currently docked at it. 
*/

import java.util.Scanner;

class Dock extends Thing {
    
    Ship ship;
    
    public Dock(Scanner sc) {
        super(sc);
    } // End Scanner constructor 
    
    public String getName(){
        return name;
    } // End method getName
    
    public int getIndex(){
        return index;
    } // End method getIndex
    
    public int getParent(){
        return parent;
    } // End method getParent
    
    public Ship getShip(){
        return ship;
    } // End method getShip
    
    public String toString () {
        String st = "Dock: " + super.toString();
        if (ship != null) st += "\n " + ship;       
        return st;
    } // End method toString
} // End class Dock
