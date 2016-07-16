package au.edu.anu.cecs.explorer;

import java.io.Serializable;

public class Vertex implements Comparable<Vertex>, Serializable 
{
    public final String name;
    public Edge[] adjacencies;
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous;
    
    public Vertex() { 
    	name = ""; 
    }
    
    public Vertex(String argName) { 
    	name = argName; 
    }
    
    public String toString() { 
    	return name; 
    }
    
    public int compareTo(Vertex other) {
        return Double.compare(minDistance, other.minDistance);
    }
    
    public boolean equals(String name){
    	
    	boolean found=false;
    	
    	if(this.name.equalsIgnoreCase(name)) {
    		found=true;
    	}
    	
    	return found;
    }
}
