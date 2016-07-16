package au.edu.anu.cecs.explorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jdbm.PrimaryTreeMap;



public class OntologyPopularity {

	public HashMap<String, Double> getOntologyPopularity(HashMap<String, Double> coverageSim) {
		
		HashMap<String, Double> popularity = new HashMap<String, Double>();
		
		GraphReuseInfoMap graph = GraphReuseInfoMap.getDefaultMap();
		PrimaryTreeMap<String,ArrayList<String>> graph_reuse_map = graph.get_graph_reuse_map();
		
		double max_popularity = 0.0;  double min_popularity=0.0;
		
		for(Map.Entry<String, Double> entry: coverageSim.entrySet()) {				
		
			String ontology = entry.getKey();
			double count=0.0;
			
			if(graph_reuse_map.containsKey(ontology)) {
				count = Double.parseDouble(graph_reuse_map.get(ontology).size()+"");
				
				if(count > max_popularity) {
					max_popularity = count;
				} if(count < min_popularity) {
					min_popularity = count;
				}
			}

			popularity.put(ontology, count);	
		}
		
		//System.out.println(max_popularity);

		for(Map.Entry<String, Double> entry2: popularity.entrySet()) {
			String ontologyId = entry2.getKey();
			Double value = entry2.getValue();
			
			value = ((value - min_popularity) / (max_popularity - min_popularity));
			popularity.put(ontologyId, value);
		}
		
	//	System.out.println(sortByValues(popularity));
		
		return sortByValues(popularity);
	}
	
	
	 private HashMap<String, Double> sortByValues(HashMap<String, Double> map) { 
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	                  .compareTo(((Map.Entry) (o1)).getValue());
	            }
	       });

	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap<String, Double> sortedHashMap = new LinkedHashMap<String, Double>();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey().toString(), Double.parseDouble(entry.getValue().toString()));
	       } 
	       return sortedHashMap;
	  }
}
