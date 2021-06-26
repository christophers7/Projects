/*
* File Name: Ship.java
* Date: March 31, 2019
* Author: Christopher Scullin
* Purpose: Parent class of PassengerShip.java and
* CargoShip.java and assigns values common to both. 
* Also, utilizes methods to determine if all jobs are 
* complete. If they are, the ship will depart the dock
* and another ship in queue will be docked. That ship 
* will then be bemoved from queue.
*/

import java.util.ArrayList;
import java.util.Scanner;

class Ship extends Thing {
    double draft = 0;
    double length = 0;
    double weight = 0;
    double width = 0;
    static ArrayList<Job> jobs = new ArrayList<Job>();
    boolean isMoored = false;
    int count = 0;    
    int jobCount = 0;
    String dockName = "";
    public Ship(Scanner sc) {
        super(sc);
        if (sc.hasNextDouble()) weight = sc.nextDouble();
        if (sc.hasNextDouble()) length = sc.nextDouble();
        if (sc.hasNextDouble()) width = sc.nextDouble();
        if (sc.hasNextDouble()) draft = sc.nextDouble();
    } // End Scanner constructor 
    
    public void dock(){
        isMoored = true; 
        synchronized (jobs) {                         
            jobs.notifyAll();
        } // End synchronized
    } // End method dock
    
    public void depart(){
        isMoored = false;
        for(Dock md: SeaPort.docks){
            if (md.getIndex() == parent){
                md.ship = null;
            } // End if statement
        } // End for loop
    } // End method depart
    
    public boolean getIsMoored(){
        return isMoored;
    } // End method getIsMoored
    
    public void increamentCount(){
        count++;
    } // End method increamentCount
    
    public void increamentJobCount(){
        jobCount++;
    } // End method increamentJobCount
    
    public void checkComplete(){       
        if(jobCount <= count){
            depart();
            for (Ship ms: SeaPort.que){
                if(!ms.isMoored){
                    for (Dock md: SeaPort.docks){
                        if (md.ship==null){
                            md.ship = ms;
                            ms.dock();
                            ms.setDockName(md.getName());
                            SeaPort.que.remove(ms);
                            return;
                        } // End if statement
                    } // End for loop                   
                } // End if statement
            } // End for loop
        } // End if statement       
    } // End method checkComplete
    
    public String getName(){
        return name;
    } // End method getName
    
    public int getIndex(){
        return index;
    } // End method getIndex
    
    public String getDockName(){
        return dockName;
    } // End method getDockName
    
    public void setDockName(String dName){
        dockName = dName;
    } // End method setDockName
    
    public int getParent(){
        return parent;
    } // End method getParent
    
    public double getDraft(){
        return draft;
    } // End method getIndex
    
    public double getLength(){
        return length;
    } // End method getIndex
    
    public double getWeight(){
        return weight;
    } // End method getIndex
    
    public double getWidth(){
        return width;
    } // End method getIndex
    
    public String toString () {
        String st = super.toString();
        return st;
    } // End method toString
} // End class Dock
