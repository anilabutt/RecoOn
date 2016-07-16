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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class OntologySearchFromVirtuoso {

	private HashMap<String, Double> rankMap = new HashMap<String, Double>();
	

	public HashMap<String, HashMap<String, String>> getResultSet(String query, String mode) {
		// TODO Auto-generated method stub
		
		String[] words = query.split(" ");
				
		ArrayList<String> keyword = new ArrayList<String>();
		
		for (String s: words) {
			keyword.add(s); }
		
		 VirtuosoRepositoryQuery query_rep = new VirtuosoRepositoryQuery();
		 JSONArray list = query_rep.selectRankedOntologiesAsJSON(keyword);
		
		 String ontologies = "" ;

         HashMap<String, HashMap<String, String>> results = new HashMap<String, HashMap<String, String>>();
		 JSONParser parser = new JSONParser();
		
		for(int i=0 ; i< list.size() ; i++) {
			JSONObject varObject = (JSONObject)list.get(i);	
			
			String ontology = varObject.get("ontology").toString();
			
			double score = Double.parseDouble(varObject.get("rank").toString());
			
			rankMap.put(ontology, score);
			
		//	System.out.println("-----------------" + ontology +"-----------------");			
			
			JSONArray resources = (JSONArray)varObject.get("resources");
			
			HashMap<String, String> resourceMap = new HashMap<String, String>();
		
			for(int j=0 ; j< resources.size() ; j++) {
				
				JSONObject res = (JSONObject)resources.get(j);	
			
				String resource = res.get("resource").toString();
				String label = res.get("label").toString();
				
				resourceMap.put(resource, label);
			}
			resourceMap.put("rank", score+"");
			results.put(ontology, resourceMap);
		}

		return results;
	}
	
	public HashMap<String, Double> getRankMap() {
		return this.sortByValues(rankMap);
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
