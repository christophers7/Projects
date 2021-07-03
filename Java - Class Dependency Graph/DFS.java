/**
 * Name: Christopher Scullin
 * 
 * Class Description: This class utilizes user input from Main.java
 * to populate the vertices and edges arrays. It can then send the array data to 
 * AbstractGraph.java for graph construction and sorting and returns output data to
 * Main.java.
 * 
 */

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class DFS {
	// Fields
	static String outputExp = "";
	static String[] vertices;
	static int numIndex = 0;	
    static int[][] edges; 

    // Method to build graph from file
    public static void buildGraph(String fileExp) {
    	try {
	    	File graphFile = new File(fileExp);
	    	Scanner graphScanner = new Scanner(graphFile);	    	
	    	String line = "";
	    	String[] lineArr;
	    	numIndex = 0;
	    	int v = 0;
	    	int sub = 0;
	    	while (graphScanner.hasNextLine()) {
	    		line = line + graphScanner.nextLine();
	    		sub++;
	    	} // End while loop
	    	lineArr = line.split(" ");
	    	numIndex = lineArr.length - sub;
	    	int end = lineArr.length;
	    	for(int i = 0; i < end; i++) {
	    		for (int j = i + 1; j < end; j++) {
	    			if (lineArr[i].equals(lineArr[j])) {                  
	    				int shiftLeft = j;
	    	            for (int k = j+1; k < end; k++, shiftLeft++) {
	    	            	lineArr[shiftLeft] = lineArr[k];
	    	            } // End for loop
	    	            end--;
	    	            j--;
	    	        } // End if statement
	    	    } // End for loop
	    	} // End for loop
	    	vertices = new String[end];
	    	for(int i = 0; i < end; i++, v++) {
	    		vertices[v] = lineArr[i];	    			
	    	} // End for loop
	    	graphScanner.close();
	    	createEdges(fileExp);	    	    		    	
    	} catch (FileNotFoundException e) {
    		JOptionPane.showMessageDialog(null, "File Did Not Open");
		} // End catch block
    } // End method
    
    // Method to create edges
    public static void createEdges(String fileExp) {
    	try {
	    	File file = new File(fileExp);
	    	Scanner sc = new Scanner(file);	    	
	    	String line = "";
	    	String[] lineArr;	
	    	int i = 0;
	    	int r = 0;	    	
	    	edges = new int[numIndex][];
	    	while (sc.hasNextLine()) {
	    		line = sc.nextLine();
	    		lineArr = line.split(" ");
	    		numIndex = numIndex + lineArr.length;
	    		int numArr[] = new int[lineArr.length - 1];
	    		for (int x = 0; x < lineArr.length; x++) {
	    			if (x == 0) {
	    				i = findIndex(lineArr[x]);
	    			} else if (x > 0) {
	    				int j = x - 1;
	    				numArr[j] = findIndex(lineArr[x]);	    				
	    			} // End else if statement
	    		} // End for loop	    			    		
	    		for (int c = 0; c < numArr.length; c++, r++) {	    			
	    			int[] col = {i, numArr[c]};
	    			edges[r] = col;
	    		} // End for loop
	    	} // End while loop	    	
	    	sc.close();
    	} catch (FileNotFoundException e) {
    		JOptionPane.showMessageDialog(null, "File Did Not Open");
		} // End catch block
    } // End method
    
    // Method to return the index of a vertex
    public static int findIndex(String s) {
    	for (int i = 0; i < vertices.length; i++) {
    		if (vertices[i].equals(s)) {
    			return i;
    		} // End if statement
    	} // End for loop
    	return -1;
    } // End method
    
    // Method to reset graph
    public static void resetGraph() {
    	vertices = null;
    	edges= null;
    } // End method
    
    // Method to reset output string
    public static void resetOutput() {
    	outputExp = "";
    } // End method
    
    // Method to return class dependencies 
    public static String topologicalOrder(String indexExp) {
    	String check = "";
    	for (int x = 0; x < vertices.length; x++) {
    		if (indexExp.equals(vertices[x])) {
    			check = vertices[x]; 
    			break;
    		} else {
    			check = "";
    		} // End else statement
    	} // End for loop
	    if (check.equals(indexExp)) {
		    Graph<String> graph = new UnweightedGraph<String>(edges, vertices);     
		    AbstractGraph<String>.Tree dfs = graph.dfs(graph.getIndex(indexExp));      	
		    java.util.List<Integer> searchOrders = dfs.getSearchOrder();
		    for (int i = 0; i < searchOrders.size(); i++) {	      
		      outputExp = (outputExp + graph.getVertex(searchOrders.get(i)) + " ");	    	    
		    } // End for loop
	    } else {
	    	throw new InvalidClassException();
	    } // End else statement
		return outputExp;		    
    } // End method    
} // End class