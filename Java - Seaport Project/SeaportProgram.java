/*
* File Name: SeaportProgram.java
* Date: March 31, 2019
* Author: Christopher Scullin
* Purpose: Constructs the GUI and provides methods for 
* searching and file reading. Also, contains the main method.
* The readFile method creates hashmaps to add file data to for
* more efficient searching. Comparable is implemented within the
* listener to perform sorting funtions. Job tab is created upon 
* reading a file. Worker tab is populated after pressing the read 
* file button.
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class SeaportProgram extends JFrame {
    static final long serialVersionUID = 123L;
    
    JTabbedPane tabs = new JTabbedPane();
    JTextArea jta = new JTextArea ();
    JTextArea jtaSort = new JTextArea ();
    JTextArea jtaSearch = new JTextArea ();
    JComboBox <String> jcb;
    JComboBox <String> jcbs;
    JTextField jtf;
    JTree tree;
    World world;
    HashMap<Integer, SeaPort> hmsp;
    HashMap<Integer, Dock> hmd;
    HashMap<Integer, Ship> hms;
    HashMap<Integer, Person> hmp;
    
    public SeaportProgram () {
        setTitle ("Sea Port");
        setSize (900, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible (true);   
        
        JPanel wrkMain = new JPanel(new GridLayout(4,4));
        
        JScrollPane jsp = new JScrollPane(jta);
        JScrollPane jspSort = new JScrollPane(jtaSort);
        JScrollPane jspSearch = new JScrollPane(jtaSearch);       
        JScrollPane jspW = new JScrollPane(wrkMain);
        
        tabs.add("Main", jsp);
        tabs.add("Sort", jspSort);
        tabs.add("Search", jspSearch);
        tabs.add("Workers", jspW);
        
        add(tabs, BorderLayout.CENTER);
        
        JButton jbr = new JButton ("Select File");
        JButton jbs = new JButton ("Search");
        JButton jbst = new JButton ("Sort");
        JButton jbw = new JButton ("Update Worker Status");
        
        JLabel jls = new JLabel ("Search target");
        JLabel jlst = new JLabel ("Sort: ");
        
        jtf = new JTextField (10);
        
        jcb = new JComboBox <String> ();
        jcb.addItem ("Name");
        jcb.addItem ("Index");
        jcb.addItem ("Skill");
        
        jcbs = new JComboBox <String> ();        
        jcbs.addItem ("Ships in queue by weight");
        jcbs.addItem ("Ships in queue by length");
        jcbs.addItem ("Ships in queue by width");
        jcbs.addItem ("Ships in queue by draft");
        jcbs.addItem ("Ships in queue by name");
        jcbs.addItem ("Seaports by name");
        jcbs.addItem ("Docks by name");
        jcbs.addItem ("Ships by name");
        jcbs.addItem ("Persons by name");
        
        JPanel jp = new JPanel ();
        jp.add (jbr);
        jp.add (jls);
        jp.add (jtf);
        jp.add (jcb);
        jp.add (jbs);
        jp.add (jlst);
        jp.add (jcbs);
        jp.add (jbst);    
        jp.add (jbw); 
        
        add(jp, BorderLayout.PAGE_START);
        
        validate();
        
        jbr.addActionListener((ActionEvent e) -> {
            readFile();           
            jta.setText(world.toString());
            createTree();
            for(Person mp: SeaPort.persons){wrkMain.add(mp.workerPanel());}
            JScrollPane jspJ = new JScrollPane(Job.pnl);
            tabs.add("Jobs", jspJ);       
        }); // End listener        
        jbs.addActionListener ((ActionEvent e) -> {
            search ((String)(jcb.getSelectedItem()), jtf.getText());
        }); // End listener
        jbw.addActionListener ((ActionEvent e) -> {
            for(Person mp: SeaPort.persons){mp.updateWorker();}
        }); // End listener
        jbst.addActionListener ((ActionEvent e) -> {
            if(jcbs.getSelectedItem().equals("Ships in queue by weight")){
                SeaPort.que.sort(Comparator.comparing(Ship::getWeight));
                String st = "\n\n --- List of ships in queue by weight:";
                for (Ship ms: SeaPort.que ) st += "\n   > " + ms + " - weight: " + ms.getWeight(); 
                jtaSort.append(st);
            } else if(jcbs.getSelectedItem().equals("Ships in queue by length")){
                SeaPort.que.sort(Comparator.comparing(Ship::getLength));
                String st = "\n\n --- List of ships in queue by length:";
                for (Ship ms: SeaPort.que ) st += "\n   > " + ms + " - length: " + ms.getLength(); 
                jtaSort.append(st);
            } else if(jcbs.getSelectedItem().equals("Ships in queue by width")){
                SeaPort.que.sort(Comparator.comparing(Ship::getWidth));
                String st = "\n\n --- List of ships in queue by width:";
                for (Ship ms: SeaPort.que ) st += "\n   > " + ms + " - width: " + ms.getWidth(); 
                jtaSort.append(st);
            } else if(jcbs.getSelectedItem().equals("Ships in queue by draft")){
                SeaPort.que.sort(Comparator.comparing(Ship::getDraft));
                String st = "\n\n --- List of ships in queue by draft:";
                for (Ship ms: SeaPort.que ) st += "\n   > " + ms + " - draft: " + ms.getDraft(); 
                jtaSort.append(st);
            } else if(jcbs.getSelectedItem().equals("Ships in queue by name")){
                SeaPort.que.sort(Comparator.comparing(Ship::getName));
                String st = "\n\n --- List of ships in queue by name:";
                for (Ship ms: SeaPort.que ) st += "\n   > " + ms; 
                jtaSort.append(st);
            } else if(jcbs.getSelectedItem().equals("Seaports by name")){
                World.ports.sort(Comparator.comparing(SeaPort::getName));
                String st = "\n\n --- List of seaports by name:";
                for (SeaPort msp: World.ports ) st += "\n   > Seaport: " + msp.getName() 
                        + " " + msp.getIndex(); 
                jtaSort.append(st);
            } else if(jcbs.getSelectedItem().equals("Docks by name")){
                SeaPort.docks.sort(Comparator.comparing(Dock::getName));
                String st = "\n\n --- List of docks by name:";
                for (Dock md: SeaPort.docks ) st += "\n   > Dock: " + md.getName() 
                        + " " + md.getIndex() + " - vessel moored: " + md.getShip(); 
                jtaSort.append(st);
            }else if(jcbs.getSelectedItem().equals("Ships by name")){
                SeaPort.ships.sort(Comparator.comparing(Ship::getName));
                String st = "\n\n --- List of ships by name:";
                for (Ship ms: SeaPort.ships ) st += "\n   > " + ms; 
                jtaSort.append(st);
            } else if(jcbs.getSelectedItem().equals("Persons by name")){
                SeaPort.persons.sort(Comparator.comparing(Person::getName));
                String st = "\n\n --- List of persons by name:";
                for (Person mp: SeaPort.persons ) st += "\n   > " + mp; 
                jtaSort.append(st);
            } // End else if statement
        }); // End listener
    } // end no-parameter constructor
    
    public void readFile () {
        String st = "world 0 0";
        Scanner sc = new Scanner(st);
        hmsp = new HashMap<>();
        hmd = new HashMap<>();
        hms = new HashMap<>();
        hmp = new HashMap<>();
        world = new World(sc);
        JFileChooser jfc = new JFileChooser ("."); 
        int i = jfc.showOpenDialog(this);    
        if(i == JFileChooser.APPROVE_OPTION){    
            File f = jfc.getSelectedFile();    
            String filepath = f.getPath();    
            try{  
                BufferedReader br = new BufferedReader(new FileReader(filepath));    
                String line = "";                         
                while((line = br.readLine()) != null){ 
                    if(!line.startsWith("//")) {
                        world.process(line, hmsp, hmd, hms, hmp); 
                    } // End if statement
                } // End while loop                     
                br.close();    
            }catch (Exception ex) {ex.printStackTrace();  }           
        } // End if statement       
    } // end method readFile   
    
    public void search (String type, String search) {
        if(type.equals("Name")) jtaSearch.setText("\n" + world.searchName(search, hms, hmd, hmsp, hmp));
        if(type.equals("Index")) jtaSearch.setText("\n" + world.searchIndex(Integer.parseInt(search), hms, hmd, hmsp, hmp));
        if(type.equals("Skill")) jtaSearch.setText("\n" + world.searchSkill(search, hmp));
    } // end method search
    
    public void createTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("World");
        if(!World.ports.isEmpty()) {
            for(SeaPort msp: World.ports) {
                root.add(new DefaultMutableTreeNode(msp.getName() + " " + msp.getIndex()));
                Enumeration children = root.children();
                while(children.hasMoreElements()){
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
                    DefaultMutableTreeNode dock = new DefaultMutableTreeNode("Docks");   
                    for (Dock md: SeaPort.docks) {      
                        dock.add(new DefaultMutableTreeNode(md.getName() + " " + md.getIndex()));
                        Enumeration c = dock.children();
                        while(c.hasMoreElements()){
                            DefaultMutableTreeNode n = (DefaultMutableTreeNode) c.nextElement();
                                n.add(new DefaultMutableTreeNode(hms.get(md.ship.getIndex())));
                        } // End while loop
                    } // End for loop
                    DefaultMutableTreeNode shipQ = new DefaultMutableTreeNode("Ships in Queue");
                    for (Ship ms: SeaPort.que) {
                        shipQ.add(new DefaultMutableTreeNode(ms.getName() + " " + ms.getIndex()));   
                    } // End for loop                                     
                    DefaultMutableTreeNode pers = new DefaultMutableTreeNode("Persons");
                    for (Person mp: SeaPort.persons) {
                        pers.add(new DefaultMutableTreeNode(mp.getName() + " " + mp.getIndex() + " " + mp.getSkill()));   
                    } // End for loop
                    DefaultMutableTreeNode jbs = new DefaultMutableTreeNode("Jobs");
                    for (Job mj: Ship.jobs) {
                        jbs.add(new DefaultMutableTreeNode(mj.getName() + " " + mj.getIndex()));   
                    } // End for loop
                    node.add(dock);
                    node.add(shipQ);
                    node.add(pers);
                    node.add(jbs);
                } // End while loop
            } // End for loop
        } // End if statement
        
        tree = new JTree(root);
        JScrollPane jspTree = new JScrollPane(tree);
        tabs.add("Tree", jspTree);
    } // End method createTree
    
    public static void main (String [] args) {
        SeaportProgram sp = new SeaportProgram ();
    } // end main
} // end class SeaportProgram