package au.edu.anu.cecs.explorer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

class Graph {
	   private final Map<String, Vertex> graph; // mapping of vertex names to Vertex objects, built from a set of Edges
	 
	   /** One edge of the graph (only used by Graph constructor) */
	   public static class Edge {
	      public final String v1, v2;
	      public final int dist;
	      public Edge(String v1, String v2, int dist) {
	         this.v1 = v1;
	         this.v2 = v2;
	         this.dist = dist;
	      }
	   }
	 
	   /** One vertex of the graph, complete with mappings to neighbouring vertices */
	   public static class Vertex implements Comparable<Vertex> {
	      public final String name;
	      public int dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be infinity
	      public Vertex previous = null;
	      public final Map<Vertex, Integer> neighbours = new HashMap<Vertex, Integer>();
	 
	      public Vertex(String name) {
	         this.name = name;
	      }
	 
	      private ArrayList<String> printPath(ArrayList<String> path) {
	    	 //int pathLength=0;
	         if (this == this.previous) {
	            //System.out.print(this.name);
	            path.add(this.name);
	         } else if (this.previous == null) {
	            // System.out.println(this.name + "(-1)");
	            //pathLength = -1;
	         } else {
	            this.previous.printPath(path);
	            path.add(this.name);
	           // System.out.print(" -> " + this.name + " ("+ this.dist +")");
	         }
	         return path;
	      }
	 
	      public int compareTo(Vertex other) {
	    	  if(dist>other.dist) return 1;
	    	  else return -1;
//	         return Integer.compare(dist, other.dist);
	      }
	   }
	 
	   /** Builds a graph from a set of edges */
	   public Graph(Edge[] edges) {
	      graph = new HashMap<String, Vertex>(edges.length);
	 
	      //one pass to find all vertices
	      for (Edge e : edges) {
	         if (!graph.containsKey(e.v1)) graph.put(e.v1, new Vertex(e.v1));
	         if (!graph.containsKey(e.v2)) graph.put(e.v2, new Vertex(e.v2));
	      }
	 
	      //another pass to set neighbouring vertices
	      for (Edge e : edges) {
	         graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
	         graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); // also do this for an undirected graph
	      }
	   }
	 
	   /** Runs dijkstra using a specified source vertex */ 
	   public int dijkstra(String startName) {
		  int pathLenght =0;
	      if (!graph.containsKey(startName)) {
	        // System.err.println(startName + "(-2)");
	         pathLenght = -1;
	         return pathLenght;
	      }
	      final Vertex source = graph.get(startName);
	      NavigableSet<Vertex> q = new TreeSet<Vertex>();
	 
	      // set-up vertices
	      for (Vertex v : graph.values()) {
	         v.previous = v == source ? source : null;
	         v.dist = v == source ? 0 : Integer.MAX_VALUE;
	         q.add(v);
	      }
	 
	      dijkstra(q);
	      pathLenght = q.size();
	      return pathLenght;
	   }
	 
	   /** Implementation of dijkstra's algorithm using a binary heap. */
	   private void dijkstra(final NavigableSet<Vertex> q) {      
	      Vertex u, v;
	      while (!q.isEmpty()) {
	 
	         u = q.pollFirst(); // vertex with shortest distance (first iteration will return source)
	         if (u.dist == Integer.MAX_VALUE) break; // we can ignore u (and any other remaining vertices) since they are -1
	 
	         //look at distances to each neighbour
	         for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {
	            v = a.getKey(); //the neighbour in this iteration
	 
	            final int alternateDist = u.dist + a.getValue();
	            if (alternateDist < v.dist) { // shorter path to neighbour found
	               q.remove(v);
	               v.dist = alternateDist;
	               v.previous = u;
	               q.add(v);
	            } 
	         }
	      }
	   }
	 
	   /** Prints a path from the source to the specified vertex */
	   public ArrayList<String> printPath(String endName) {
		   ArrayList<String> path = new ArrayList<String>();
		  // int pathLength=0;
	      if (!graph.containsKey(endName)) {
//	         System.out.println(endName + " (-1)" );
	         return path;
	      }
	      path = graph.get(endName).printPath(path);
	      return path;
	   }

	}