/*
* File Name: Person.java
* Date: March 31, 2019
* Author: Christopher Scullin
* Purpose: Constructs a person with an associated skill. Also
* constructs a panel for each worker. The Update Worker Status 
* button will update the worker avaliability lable based on the
* each worker's busy flag.
*/

import java.awt.Color;
import java.awt.*;
import java.util.Scanner;
import javax.swing.*;

class Person extends Thing {
    String skill = "";
    boolean busy = false;
    JPanel wPanel;
    JLabel wNameLbl;
    JLabel wSkillLbl; 
    JLabel wSpLbl; 
    JLabel wStatusLbl;
    SeaPort sp;

    public Person (Scanner sc, java.util.HashMap <Integer, SeaPort> hmsp) {
        super(sc);
        if (sc.hasNext()) skill = sc.next();
        sp = hmsp.get(parent);        
    } // End Scanner constructor
    
    public String getName(){
        return name;
    } // End method getName
    
    public int getIndex(){
        return index;
    } // End method getIndex
    
    public String getSkill(){
        return skill;
    } // End method getSkill
    
    public int getSP(){
        return sp.getIndex();
    } // End method getSP
    
    public boolean getBusy(){
        return busy;
    } // End method getBusy
    
    public void setBusy(boolean changeStatus){
        busy = changeStatus;
    } // End setBusy
    
    public JPanel workerPanel(){
        wPanel = new JPanel(new GridLayout(1,4));
        wNameLbl = new JLabel("Worker: " + name, JLabel.CENTER);
        wSkillLbl = new JLabel("Skill: " + skill, JLabel.CENTER);
        wSpLbl = new JLabel("Seaport: " + sp.getName(), JLabel.CENTER);
        wStatusLbl = new JLabel("Available", JLabel.CENTER);
 
        wStatusLbl.setOpaque(true);
        wStatusLbl.setBackground(Color.GREEN);
        
        wNameLbl.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        wSkillLbl.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        wSpLbl.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        wStatusLbl.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        wPanel.add(wNameLbl);
        wPanel.add(wSkillLbl);
        wPanel.add(wSpLbl);
        wPanel.add(wStatusLbl);
        wPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        
        return wPanel;  
    } // End method workerPanel
    
    public void updateWorker(){
        if (getBusy()) {
            wStatusLbl.setBackground(Color.RED);
            wStatusLbl.setText("Unavailable");
        } else {
            wStatusLbl.setBackground(Color.GREEN);
            wStatusLbl.setText("Available");
        } // End else statement
    } // End method updateWorker
    
    public String toString () {
        String st = "Person: " + super.toString() + " " + skill;
        return st;
    } // End method toString
} // End class Person
