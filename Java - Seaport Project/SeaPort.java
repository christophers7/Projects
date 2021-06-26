/*
* File Name: SeaPort.java
* Date: March 31, 2019
* Author: Christopher Scullin
* Purpose: Stores and returns data for all docks, ships,
* and persons at the seaport. Contains methods for handling
* thread resources.
*/

import java.util.ArrayList;
import java.util.Scanner;

class SeaPort extends Thing {
    static ArrayList <Dock> docks = new ArrayList <Dock> ();
    static ArrayList <Ship> que = new ArrayList <Ship> ();
    final static ArrayList <Ship> ships = new ArrayList <Ship> ();
    static ArrayList <Person> persons = new ArrayList <Person> ();
    
    public SeaPort(Scanner sc) {
        super(sc);
    } // End Scanner constructor 
    
    public String getName(){
        return name;
    } // End method getName
    
    public int getIndex(){
        return index;
    } // End method getIndex
    
    synchronized ArrayList<Person> getResources(Job job) {
        ArrayList<Person> qualifiedWorkers = new ArrayList<Person>();
        boolean reqFulfilled = true;
        boolean check = true;
        
        checkLoop: for (String requirement : job.getRequirements()) {
            for (Person mp : persons) {
                if (mp.getSkill().equals(requirement) && mp.getSP() == job.getSP()) {                   
                    continue checkLoop;
                } // End if statement
            } // End for loop
            job.noWorker();
            check = false;
            break;
        } // End for loop
        
        if (check){
            reqLoop: for (String req : job.getRequirements()) {
                for (Person mp : persons) {
                    if (mp.getSkill().equals(req) && !mp.getBusy()) {
                        mp.setBusy(true); 
                        qualifiedWorkers.add(mp);
                        continue reqLoop;
                    } // End if statement
                } // End for loop
                reqFulfilled = false;            
                break;
            } // End for loop 
        } // End if statement
        
        if (reqFulfilled) {            
            return qualifiedWorkers;
        } else {
            returnWorkers(qualifiedWorkers);
            return null;
        } // End else statement 
    } // End synchronized 
    
    synchronized void returnWorkers(ArrayList<Person> resources) {
        resources.forEach((worker) -> {worker.setBusy(false);});      
    } // End synchronized
    
    public String toString () {
        String st = "\n\nSeaPort: " + super.toString();
        for (Dock md: docks) st += "\n\n" + md;
        st += "\n\n --- List of all ships in que:"; 
        for (Ship ms: que ) st += "\n   > " + ms; 
        st += "\n\n --- List of all ships:";
        for (Ship ms: ships) st += "\n   > " + ms;
        st += "\n\n --- List of all persons:";
        for (Person mp: persons) st += "\n   > " + mp;
        st += "\n\n --- List of all jobs:";
        for (Job mj: Ship.jobs) st += "\n   > " + mj;
      return st;    
    } // End method toString 
} // End class SeaPort
