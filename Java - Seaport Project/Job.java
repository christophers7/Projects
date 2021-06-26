
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;

/*
* File Name: Job.java
* Date: April 26, 2019
* Author: Christopher Scullin
* Purpose: Builds a job thread and creates a panel with a 
* progress bar to represent job completion. Buttons on the 
* panel will suspend or cancel of job once pressed. Utilizes 
* workers as resources when requirments are present. Will cancel
* a job when the worker skills are not avaliable. Will wait if the
* skills are present but the corresponing worker is busy. Once all
* jobs are complete or cancelled, it tells the ship to leave the dock
* and searches for another ship in queue.
*/

public class Job extends Thing implements Runnable {
    
    double duration = 0;
    ArrayList<String> requirements = new ArrayList<String>();
    ArrayList<Person> workers;
    static Random rn = new Random ();
    static JPanel pnl = new JPanel(new GridLayout(2,2));  
    JPanel jbP;
    int jobIndex;   
    long jobTime;  
    int reqCount;
    String jobName = "";   
    JProgressBar pm = new JProgressBar ();
    boolean goFlag = true, noKillFlag = true;   
    JButton jbGo   = new JButton ("Pause");   
    JButton jbKill = new JButton ("Cancel"); 
    Status status = Status.SUSPENDED;
    SeaPort sp;
    Dock d;
    Ship s;
    Thread t;
    boolean noWorkerFlag = false;
    enum Status {RUNNING, SUSPENDED, WAITING, NOWORKER, DONE, CANCELLED}; 
    
    public Job (Scanner sc, java.util.HashMap <Integer, Ship> hms,
            java.util.HashMap <Integer, SeaPort> hmsp,
            java.util.HashMap <Integer, Dock> hmd,
            java.util.HashMap <Integer, Person> hmp) {
        super(sc);
        if (sc.hasNextDouble()) duration = sc.nextDouble();
        if (sc.hasNext()) {
            while (sc.hasNext()) {
                requirements.add(sc.next());
                reqCount++;
            } // End while loop
        } // End if statement 
        s = hms.get(parent);
        if (hmsp.containsKey(s.getParent())){
            sp = hmsp.get(s.getParent());
        } else if (hmd.containsKey(s.getParent())){
            d = hmd.get(s.getParent());
            sp = hmsp.get(d.getParent());
        } // End else if statement
        buildJobPanel();
        s.increamentJobCount();
        this.t = new Thread(this);
        this.t.start();
    } // End Scanner constructor     
       
    public void buildJobPanel(){
        jbP = new JPanel();
        jbP.setLayout(new BoxLayout(jbP,1));
        jbP.setBorder(BorderFactory.createLineBorder(Color.black));       
        pm = new JProgressBar ();     
        pm.setStringPainted(true);     
        jbP.add (pm);            
        jbP.add (new JLabel ("Ship: " + s.getName(), SwingConstants.CENTER));     
        jbP.add (new JLabel (name, SwingConstants.CENTER));        
        jbP.add (jbGo);     
        jbP.add (jbKill); 
        jbP.add (new JLabel ("Seaport: " + sp.getName(), SwingConstants.CENTER)); 
        if (d != null){
            jbP.add (new JLabel ("Dock: " + d.getName(), SwingConstants.CENTER));
        } // End if statement
        jbGo.addActionListener (new ActionListener () {       
            public void actionPerformed (ActionEvent e) {         
                toggleGoFlag();       
        }}); // End listener
        jbKill.addActionListener (new ActionListener () {       
            public void actionPerformed (ActionEvent e) {         
                setKillFlag();       
        }}); // End listener
        pnl.add(jbP);
    } // End method buildJobPanel
    
    public String getName(){
        return name;
    } // End method getName
    
    public int getIndex(){
        return index;
    } // End method getIndex
    
    public ArrayList<String> getRequirements(){
        return requirements;
    } // End method getRequirements
    
    public int getSP(){
        return sp.getIndex();
    } // End method getSP
    
    public int getReqCount(){
        return reqCount;
    } // End method getReqCount
    
    public void toggleGoFlag () { 
        goFlag = !goFlag;   
    } // end method toggleRunFlag
    
     public void setKillFlag () {
        noKillFlag = false; 
        jbKill.setBackground (Color.red);
    } // End method setKillFlag 
     
    public void setNoWorker () {
        noWorkerFlag = true; 
    } // End method setNoWorker 
     
    public void noWorker(){
        setKillFlag();
        setNoWorker();
        jbP.add (new JLabel ("Skill requirements not met", SwingConstants.CENTER));
    } // End method noWorker
     
    void showStatus (Status st) {
        status = st;
        switch (status) {  
            case RUNNING:   
                jbGo.setBackground (Color.green);
                jbGo.setText ("Running");     
                break;    
            case SUSPENDED:   
                jbGo.setBackground (Color.yellow);       
                jbGo.setText ("Suspended");         
                break;     
            case WAITING:         
                jbGo.setBackground (Color.orange);         
                jbGo.setText ("Waiting turn");         
                break;
            case NOWORKER:         
                jbGo.setBackground (Color.orange);         
                jbGo.setText ("Waiting on worker");         
                break;
            case DONE:         
                jbGo.setBackground (Color.red);         
                jbGo.setText ("Done");         
                break; 
            case CANCELLED:         
                jbGo.setBackground (Color.red);         
                jbGo.setText ("Job Cancelled");         
                break;
        } // end switch on status   
    } // end showStatus 
    
    public boolean isDocked(){
        return s.getIsMoored();
    } // End method isDocked
    
    public synchronized boolean isWaiting() {
        if (!requirements.isEmpty()){
            workers = sp.getResources(this);
            if (workers == null) {
                return true;
            } else {
                return false;
            } // End else statement
        } else {
            return false;
        } // End else statement
    } // End synchronized
    
    @Override
    public void run () {     
        long time = System.currentTimeMillis();     
        long startTime = time;
        double stopTime = time + 1000 * duration;    
        double runTime = stopTime - time; 
        
        synchronized (s.jobs) {       
            while (!isDocked()) {         
                showStatus (Status.WAITING);
                try {           
                    s.jobs.wait();
                } catch (InterruptedException e) {                     
                } // end try/catch block                 
            } // end while loop           
        } // end sychronized     
        
        if (noKillFlag && d == null){
          jbP.add (new JLabel ("Dock: " + s.getDockName(), SwingConstants.CENTER));  
        } // End if statement
        
        synchronized (sp) {       
            while (isWaiting() && !noWorkerFlag) {         
                showStatus (Status.NOWORKER);
                try {           
                    sp.wait();
                } catch (InterruptedException e) {                     
                } // end try/catch block                 
            } // end while loop 
            
            if(noWorkerFlag){
                workers=new ArrayList<>();
            } // End if statement
        } // end sychronized       
        
        if (!requirements.isEmpty() && !workers.isEmpty()){
            for (Person mp: workers) {
                jbP.add (new JLabel ("Worker: " + mp.getName() + " " + mp.getSkill(), SwingConstants.CENTER));
            } // End for loop
        } else if (requirements.isEmpty()) {
            jbP.add (new JLabel ("No requirements specified", SwingConstants.CENTER));
        } // End if statement
        
        while (time < stopTime && noKillFlag) {       
            try {         
                Thread.sleep (100);
            } catch (InterruptedException e) {}       
                if (goFlag) {         
                    showStatus (Status.RUNNING);
                    time += 100;        
                    pm.setValue ((int)(((time - startTime) / runTime) * 100));                      
                } else {
                    showStatus (Status.SUSPENDED);
                } // End else statement    
        } // End running          
        
        if (time >= stopTime){
            showStatus (Status.DONE);
            s.increamentCount();
            s.checkComplete();
        } else if (!noKillFlag){
            showStatus (Status.CANCELLED);            
            s.increamentCount();
            s.checkComplete();
        } // End else statement
        
        synchronized(sp){
            if (!requirements.isEmpty() && !workers.isEmpty()) {              
                sp.returnWorkers(workers);
                sp.notifyAll();
            } // End if statement      
        } // End synchronized
    } // End method run - implements runnable
        
    public String toString () {
        String st = "Job: " + super.toString();
        for (String rq: requirements ) st += " " + rq;
        return st;
    } // End method toString
} // End class Job
