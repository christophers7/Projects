/**
 * Name: Christopher Scullin
 * 
 * Class Description: This class constructs the GUI for the program, 
 * contains a listeners for the Build Directed Graph and Topological Order buttons, 
 * receives user input via text box for file and class(index) selection and 
 * sends it the DFS class for graph construction and topological ordering then displays 
 * the compilation order, and contains the Main method.
 * 
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class Main extends JFrame {
	
	// Fields
	private JButton buildGraphBtn = new JButton("Build Directed Graph");
	private JButton orderBtn = new JButton("Topological Order");
	private static JTextField inputFileTxt = new JTextField(20);
	private JTextField recompileTxt = new JTextField(20);
	private JLabel inputLbl = new JLabel("Input file name:", JLabel.RIGHT);
	private JLabel recompileLbl = new JLabel("Class to recompile:", JLabel.RIGHT);
	private JPanel btnPanel = new JPanel(new GridBagLayout());
	private JTextArea outputTxt = new JTextArea(9, 50);
	private JPanel outputPanel = new JPanel();
	private JPanel myPanel = new JPanel();
	
	// Constructor
	public Main () {		
            setTitle("Class Dependency Graph");
            setSize(575, 305);
            setVisible(true);  		
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
	     
	    // Add button to panel	
	    btnPanel.setBorder(BorderFactory.createBevelBorder(0));
	    GridBagConstraints c = new GridBagConstraints();
	    c.insets = new Insets(7,7,7,7);
	    c.gridx = 0;
	    c.gridy = 1;
	    btnPanel.add(inputLbl,c);
	    c.gridx = 1;
	    c.gridy = 1;
	    btnPanel.add(inputFileTxt,c);	  
	    c.gridx = 2;
	    c.gridy = 1;
	    btnPanel.add(buildGraphBtn,c);	    
	    buildGraphBtn.setPreferredSize(new Dimension(150, 27));
	    c.gridx = 0;
	    c.gridy = 2;
	    btnPanel.add(recompileLbl,c);
	    c.gridx = 1;
	    c.gridy = 2;
	    btnPanel.add(recompileTxt,c);
	    c.gridx = 2;
	    c.gridy = 2;
	    btnPanel.add(orderBtn,c);	    
	    orderBtn.setPreferredSize(new Dimension(135, 27));
	    
	    // Prevent editing of output text
	    outputTxt.setEditable(false);
	    
	    // Add output text area to panel
	    outputPanel.add(outputTxt);
	    outputPanel.setBackground(Color.WHITE);
	    outputPanel.setBorder(BorderFactory.createTitledBorder("Recompilation Order"));
	    
	    // Add all panels vertically to one panel
	    myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
	    myPanel.add(btnPanel);
	    myPanel.add(outputPanel);
	    myPanel.setBackground(Color.WHITE);
	    
	    // Add panel to frame
	    add(myPanel);	
	    
	    // Add event handler to Build Directed Graph button
	    buildGraphBtn.addActionListener(new ActionListener(){ 
	    	public void actionPerformed(ActionEvent e){
	    		try {
		    		DFS.resetGraph();
		    		DFS.buildGraph(inputFileTxt.getText());	
		    		if (DFS.vertices != null) {
		    			JOptionPane.showMessageDialog(null, "Graph Built Sucessfully");
		    		} // End if statement
	    		} catch (RuntimeException fnfe){
	    			JOptionPane.showMessageDialog(null, "Error");	
	    		} // End catch block
	    }}); // End listener
	    
	    // Add event handler to Topological Order button
	    orderBtn.addActionListener(new ActionListener(){ 
	    	public void actionPerformed(ActionEvent e){
	    		try {
	    			if (DFS.vertices != null) {
	    				String result = DFS.topologicalOrder(recompileTxt.getText());
	    				outputTxt.setText(result);
	    				DFS.resetOutput();
	    			} else {
	    				JOptionPane.showMessageDialog(null, "Graph Has Not Been Built");
	    			} // End else statement
	    		} catch (CycleDetectedException cde){
	    			JOptionPane.showMessageDialog(null, "A Cycle Has Been Detected");	    	                
	    		} catch (InvalidClassException ice){
	    			JOptionPane.showMessageDialog(null, "Invalid Class Name");	    	                
	    		} // End catch block
	    }}); // End listener
	} // End constructor 
	
	// Main method
	public static void main (String args[]) {
		Main gui = new Main();
	} // End main
}