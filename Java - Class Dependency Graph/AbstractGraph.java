/**
 * Name: Christopher Scullin
 * 
 * Class Description: This class implements the Graph
 * interface to construct a graph based on data received from DFS.java. This
 * class contains the Depth First Search method used for topological ordering.
 * 
 */

import java.util.*;

public abstract class AbstractGraph<V> implements Graph<V> {
  protected List<V> vertices = new ArrayList<V>(); // Store vertices
  protected List<List<Integer>> neighbors 
    = new ArrayList<List<Integer>>(); // Adjacency lists

  /** Construct an empty graph */
  protected AbstractGraph() {
  }
  
  /** Construct a graph from edges and vertices stored in arrays */
  protected AbstractGraph(int[][] edges, V[] vertices) {
    for (int i = 0; i < vertices.length; i++)
      this.vertices.add(vertices[i]);
    
    createAdjacencyLists(edges, vertices.length);
  }

  /** Construct a graph from edges and vertices stored in List */
  protected AbstractGraph(List<Edge> edges, List<V> vertices) {
  for (int i = 0; i < vertices.size(); i++)
    this.vertices.add(vertices.get(i));
      
  createAdjacencyLists(edges, vertices.size());
  }

  /** Construct a graph for integer vertices 0, 1, 2 and edge list */
  protected AbstractGraph(List<Edge> edges, int numberOfVertices) {
    for (int i = 0; i < numberOfVertices; i++) 
      vertices.add((V)(new Integer(i))); // vertices is {0, 1, ...}
    
    createAdjacencyLists(edges, numberOfVertices);
  }

  /** Construct a graph from integer vertices 0, 1, and edge array */
  protected AbstractGraph(int[][] edges, int numberOfVertices) {
    for (int i = 0; i < numberOfVertices; i++) 
      vertices.add((V)(new Integer(i))); // vertices is {0, 1, ...}
    
    createAdjacencyLists(edges, numberOfVertices);
  }

  /** Create adjacency lists for each vertex */
  private void createAdjacencyLists(int[][] edges, int numberOfVertices) {     
    // Create a linked list
    for (int i = 0; i < numberOfVertices; i++) {
      neighbors.add(new ArrayList<Integer>());
    }
    for (int i = 0; i < edges.length; i++) {
    		int u = edges[i][0];
    		int v = edges[i][1];   		
    		neighbors.get(u).add(v);    	
    }
  }

  /** Create adjacency lists for each vertex */
  private void createAdjacencyLists(List<Edge> edges, int numberOfVertices) {     
    // Create a linked list for each vertex
    for (int i = 0; i < numberOfVertices; i++) {
      neighbors.add(new ArrayList<Integer>());
    }

    for (Edge edge: edges) {
      neighbors.get(edge.u).add(edge.v);
    }
  }

  @Override /** Return the number of vertices in the graph */
  public int getSize() {
    return vertices.size();
  }

  @Override /** Return the vertices in the graph */
  public List<V> getVertices() {
    return vertices;
  }

  @Override /** Return the object for the specified vertex */
  public V getVertex(int index) {
    return vertices.get(index);
  }

  @Override /** Return the index for the specified vertex object */
  public int getIndex(V v) {
    return vertices.indexOf(v);
  }

  @Override /** Return the neighbors of the specified vertex */
  public List<Integer> getNeighbors(int index) {
    return neighbors.get(index);
  }

  @Override /** Return the degree for a specified vertex */
  public int getDegree(int v) {
    return neighbors.get(v).size();
  }

  @Override /** Print the edges */
  public void printEdges() {
    for (int u = 0; u < neighbors.size(); u++) {
      System.out.print(getVertex(u) + " (" + u + "): ");
      for (int j = 0; j < neighbors.get(u).size(); j++) {
        System.out.print("(" + u + ", " +
          neighbors.get(u).get(j) + ") ");
      }
      System.out.println();
    }
  }

  @Override /** Clear graph */
  public void clear() {
    vertices.clear();
    neighbors.clear();
  }
  
  @Override /** Add a vertex to the graph */  
  public void addVertex(V vertex) {
    vertices.add(vertex);
    neighbors.add(new ArrayList<Integer>());
  }

  @Override /** Add an edge to the graph */  
  public void addEdge(int u, int v) {
    neighbors.get(u).add(v);
    neighbors.get(v).add(u);
  }
  
  /** Edge inner class inside the AbstractGraph class */
  public static class Edge {
    public int u; // Starting vertex of the edge
    public int v; // Ending vertex of the edge

    /** Construct an edge for (u, v) */
    public Edge(int u, int v) {
      this.u = u;
      this.v = v;
    }
  }
  
  // Method to perform DFS
  @Override
  public Tree dfs(int s) {
	  List<Integer> searchOrder = new ArrayList<Integer>();
	  MyStack<Integer> stack = new MyStack<>(vertices.size());
	  boolean[] isVisited = new boolean[vertices.size()];
	  isVisited[s] = true; // Vertex v visited	  
	  dfSearch(s, stack, isVisited);	  
	  stack.push(s);
	  while (!stack.isEmpty()) {		  
		  try {
			  searchOrder.add(stack.pop());
		  } catch (Exception e) {
			  e.printStackTrace();
		  } // End catch block
	  } // End while loop
	  return new Tree(s, searchOrder);
  } // End method
  
  // Recursive method to push unvisited vertices to stack for DFS
  private void dfSearch(int v, MyStack<Integer> stack, boolean[] isVisited) {	      
	  for (int i : neighbors.get(v)) {
		  if (!isVisited[i]) {  
			  stack.push(i);
			  dfSearch(i, stack, isVisited); // Recursive search      
		  }	else if (isVisited[i]) {
			  throw new CycleDetectedException();
		  } // End else if statement
	  } // End for loop
  } // End method

  /** Tree inner class inside the AbstractGraph class */
  public class Tree {
    private int root; // The root of the tree
    private int[] parent; // Store the parent of each vertex
    private List<Integer> searchOrder; // Store the search order
    
    /** Construct a tree with root and searchOrder */
    public Tree(int root, List<Integer> searchOrder) {
      this.root = root;      
      this.searchOrder = searchOrder;
    } // End constructor

    /** Return the root of the tree */
    public int getRoot() {
      return root;
    } // End method

    /** Return the parent of vertex v */
    public int getParent(int v) {
      return parent[v];
    } // End method

    /** Return an array representing search order */
    public List<Integer> getSearchOrder() {
      return searchOrder;
    } // End method

    /** Return number of vertices found */
    public int getNumberOfVerticesFound() {
      return searchOrder.size();
    } // End method
    
    /** Return the path of vertices from a vertex to the root */
    public List<V> getPath(int index) {
      ArrayList<V> path = new ArrayList<V>();

      do {
        path.add(vertices.get(index));
        index = parent[index];
      }
      while (index != -1);

      return path;
    }

    /** Print a path from the root to vertex v */
    public void printPath(int index) {
      List<V> path = getPath(index);
      System.out.print("A path from " + vertices.get(root) + " to " +
        vertices.get(index) + ": ");
      for (int i = path.size() - 1; i >= 0; i--)
        System.out.print(path.get(i) + " ");
    }

    /** Print the whole tree */
    public void printTree() {
      System.out.println("Root is: " + vertices.get(root));
      System.out.print("Edges: ");
      for (int i = 0; i < parent.length; i++) {
        if (parent[i] != -1) {
          // Display an edge
          System.out.print("(" + vertices.get(parent[i]) + ", " +
            vertices.get(i) + ") ");
        }
      }
      System.out.println();
    }
  }
}