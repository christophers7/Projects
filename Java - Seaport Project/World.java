/*
* File Name: World.java
* Date: March 31, 2019
* Author: Christopher Scullin
* Purpose: Recieves data from selected file and populates
* sea ports, docks, ships, and persons with said data. Also,
* contains methods for assigning a ship to a dock and searching
* by name, index, and skill. Data is added to the corresponding
* hashmap. The search methods utilized the hashmaps for its searches.
*/

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JPanel;

public class World extends Thing {
    
    static ArrayList<SeaPort> ports = new ArrayList<SeaPort>();   
    
    public World(Scanner sc) {        
        super(sc);
    } // End Scanner constructor 
        
    public void process(String st, java.util.HashMap <Integer, SeaPort> hmsp,
            java.util.HashMap <Integer, Dock> hmd, 
            java.util.HashMap <Integer, Ship> hms,
            java.util.HashMap <Integer, Person> hmp) {
        Scanner sc = new Scanner(st);
        if (!sc.hasNext())
            return;
        switch (sc.next()) {
            case "port" : addPort(sc, hmsp);        
            break; 
            case "dock" : addDock(sc, hmd);        
            break;
            case "pship" : addPShip(sc, hms, hmd, hmsp);        
            break;
            case "cship" : addCShip(sc, hms, hmd, hmsp);        
            break;
            case "person" : addPerson(sc, hmp, hmsp);        
            break;
            case "job" : addJob(sc, hms, hmsp, hmd, hmp);        
            break;
        } // End switch case
    } // End method process
    
    public void addJob(Scanner sc, java.util.HashMap <Integer, Ship> hms,
            java.util.HashMap <Integer, SeaPort> hmsp,
            java.util.HashMap <Integer, Dock> hmd,
            java.util.HashMap <Integer, Person> hmp){
        Job jb = new Job(sc, hms, hmsp, hmd, hmp);
        for (Ship ms: SeaPort.ships) {
            if (ms.index == jb.parent) {
                ms.jobs.add(jb);               
            } // End if statement
        } // End for loop
    } // End method addJob
    
    public void addPort(Scanner sc, java.util.HashMap <Integer, SeaPort> hmsp) {
       SeaPort msp = new SeaPort(sc);
       ports.add(msp);
       hmsp.put(msp.getIndex(), msp);
    } // End method addPort
    
    public void addDock(Scanner sc, java.util.HashMap <Integer, Dock> hmd) {
        Dock dock = new Dock(sc);
        for(SeaPort p: ports) {
            if(p.index == dock.parent) p.docks.add(dock); hmd.put(dock.getIndex(), dock);     
        } // End for loop
    } // End method addDock
    
    public void addPShip(Scanner sc, java.util.HashMap <Integer, Ship> hms, 
            java.util.HashMap <Integer, Dock> hmd, 
            java.util.HashMap <Integer, SeaPort> hmsp) {
        PassengerShip ps = new PassengerShip(sc);
        assignShip(ps, hms, hmd, hmsp);
    } // End method addPShip
   
    public void addCShip(Scanner sc, java.util.HashMap <Integer, Ship> hms, 
            java.util.HashMap <Integer, Dock> hmd, 
            java.util.HashMap <Integer, SeaPort> hmsp) {
        CargoShip cs = new CargoShip(sc);
        assignShip(cs, hms, hmd, hmsp);
    } // End method addCShip
    
    public void addPerson(Scanner sc, java.util.HashMap <Integer, Person> hmp,
            java.util.HashMap <Integer, SeaPort> hmsp) {
        Person per = new Person(sc, hmsp);
        for(SeaPort p: ports) {
            if(p.index == per.parent) p.persons.add(per); hmp.put(per.getIndex(), per);
        } // End for loop
    } // End method addPerson
    
    public Ship getShipByIndex(int x, java.util.HashMap <Integer, Ship> hms) {
        if(hms.containsKey(x)) {
            return hms.get(x);              
        } // End if statement  
        return null;
    } // End getShipByIndex 
    
    public SeaPort getSeaPortByIndex(int x, java.util.HashMap <Integer, SeaPort> hmsp) {
        if(hmsp.containsKey(x)) {
            return hmsp.get(x);              
        } // End if statement 
        return null;
    } // End method getSeaPortByIndex
   
    public Dock getDockByIndex(int x, java.util.HashMap <Integer, Dock> hmd) {
        if(hmd.containsKey(x)) {
            return hmd.get(x);              
        } // End if statement                
        return null;
    } // End method getDockByIndex
    
    public Person getPersonByIndex(int x, java.util.HashMap <Integer, Person> hmp) { 
        if(hmp.containsKey(x)) {
            return hmp.get(x);              
        } // End if statement              
        return null;
    } // End method getPersonByIndex

    public void assignShip(Ship ms, java.util.HashMap <Integer, Ship> hms, 
            java.util.HashMap <Integer, Dock> hmd, 
            java.util.HashMap <Integer, SeaPort> hmsp) {
        Dock md = getDockByIndex(ms.parent, hmd);
        if(md == null) { 
            getSeaPortByIndex(ms.parent, hmsp).ships.add(ms);
            getSeaPortByIndex(ms.parent, hmsp).que.add(ms);
            hms.put(ms.getIndex(), ms);
            return;
        } // End if statement
        md.ship = ms;
        getSeaPortByIndex(md.parent, hmsp).ships.add(ms);
        hms.put(ms.getIndex(), ms);
        ms.dock();
    } // End method assignShip 
    
    public String searchName(String search, java.util.HashMap <Integer, Ship> hms, 
            java.util.HashMap <Integer, Dock> hmd, 
            java.util.HashMap <Integer, SeaPort> hmsp, 
            java.util.HashMap <Integer, Person> hmp) {
        for(Map.Entry <Integer, SeaPort> sp: hmsp.entrySet()) {
            if (sp.getValue().getName().toLowerCase().equals(search.toLowerCase())) 
        return sp.getValue().toString();               
        } // End for loop
        for(Map.Entry <Integer, Dock> d: hmd.entrySet()) {
            if (d.getValue().getName().toLowerCase().equals(search.toLowerCase())) 
                return d.getValue().toString();               
        } // End for loop
        for(Map.Entry <Integer, Ship> s: hms.entrySet()) {
            if (s.getValue().getName().toLowerCase().equals(search.toLowerCase())) 
                return s.getValue().toString();               
        } // End for loop
        for(Map.Entry <Integer, Person> p: hmp.entrySet()) {
            if (p.getValue().getName().toLowerCase().equals(search.toLowerCase())) 
                return p.getValue().toString();               
        } // End for loop        
        return "*****Results for " + search + " not found*****";
    } // End method searchName
   
    public String searchIndex(int search, java.util.HashMap <Integer, Ship> hms, 
            java.util.HashMap <Integer, Dock> hmd, 
            java.util.HashMap <Integer, SeaPort> hmsp, 
            java.util.HashMap <Integer, Person> hmp) {
        if(hmsp.containsKey(search)) {
            return hmsp.get(search).toString();              
        } // End if statement           
        if(hmd.containsKey(search)) {
            return hmd.get(search).toString();              
        } // End if statement
        if(hms.containsKey(search)) {
            return hms.get(search).toString();              
        } // End if statement
        if(hmp.containsKey(search)) {
            return hmp.get(search).toString();              
        } // End if statement
        return "*****Results for " + search + " not found*****";
    } // End method searchIndex
   
    public String searchSkill(String search, java.util.HashMap <Integer, Person> hmp) {
        String st = "";
        for(Map.Entry <Integer, Person> p: hmp.entrySet()) {
                if (p.getValue().getSkill().toLowerCase().equals(search.toLowerCase())) 
                    st += p.getValue().toString() + '\n';
        } // End for loop
        if(st.equals("")) 
            st = "*****Results for " + search + " not found*****";        
        return st;
    } // End method searchSkill
    
    public String toString() {
        String st = "\n\n>>>>> The world: ";
        if(!ports.isEmpty())       
            for(SeaPort msp: ports) {
                st += msp;
            } // End for loop
        return st;
    } // End method toString
} // End class World
