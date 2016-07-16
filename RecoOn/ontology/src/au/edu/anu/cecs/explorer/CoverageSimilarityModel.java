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

public class CoverageSimilarityModel {

	private HashMap<String, Double> max_link_map = new HashMap<String, Double>();
	
	public static void main(String[] args) {
		
	}
	
	public HashMap<String, Double> getCoverageSimilarity(HashMap<String,HashMap<String, HashMap<String, ArrayList<String>>>> results) {
		
		HashMap<String, Double> coverageSim = new HashMap<String, Double>();
		
		HashMap<String, HashMap<String,HashMap<String,Double>>> coverageSimForOntologies = processCoverageSimilarity(results);
		
		double max_coverage = 0.0; double min_coverage=0.0;
		for(Map.Entry<String, HashMap<String,HashMap<String,Double>>> entry: coverageSimForOntologies.entrySet()) {
			
			String ontology = entry.getKey();
			HashMap<String,HashMap<String,Double>> matchedResources = entry.getValue();
			double ontCoverageSim = 0.0;
			
			double queryLength = max_link_map.size();


			for(Map.Entry<String,HashMap<String,Double>> entry2: matchedResources.entrySet()) {
				String queryTerm = entry2.getKey();
				HashMap<String,Double> matches = entry2.getValue();
				double totalSim =0.0;
					for(Map.Entry<String, Double> entry3: matches.entrySet()) {
						totalSim = totalSim + entry3.getValue();
					}
					double max_length = max_link_map.get(queryTerm);
				
					if(max_length>0) {
						ontCoverageSim = ontCoverageSim + ((1/queryLength) * (totalSim/max_link_map.get(queryTerm)));
//						System.out.println("ontCoverageSim : " +ontCoverageSim + " totalSim : " + totalSim + " MAX_LINKS : " + max_length);
					} else {
						ontCoverageSim = ontCoverageSim + ((1/queryLength) * (totalSim));
//						System.out.println("ontCoverageSim : " +ontCoverageSim + " totalSim : " + totalSim + " MAX_LINKS : " + max_length);
					}

					
				}
			
			if(ontCoverageSim>max_coverage) {
				max_coverage = ontCoverageSim; }
			
			if(ontCoverageSim<min_coverage) {
				min_coverage = ontCoverageSim;
			}
			coverageSim.put(ontology, ontCoverageSim);
		}
		
		for(Map.Entry<String, Double> entry: coverageSim.entrySet()){
			String ont = entry.getKey();
			double coverage = entry.getValue();
			double coverage_difference = max_coverage - min_coverage;
			if(coverage_difference >0) {
			double norm_coverage = (( coverage - min_coverage) / (max_coverage - min_coverage));
			coverageSim.put(ont, norm_coverage);
			}
		}
		
		
		//System.out.println(sortByValues(coverageSim));
		
		return sortByValues(coverageSim);
	}
	
	public HashMap<String, HashMap<String,HashMap<String,Double>>> processCoverageSimilarity(HashMap<String,HashMap<String, HashMap<String, ArrayList<String>>>> results) {
		// TODO Auto-generated method stub

		FrequencyMapSimple frequencyMapModels = FrequencyMapSimple.getDefaultMap();
		//HashMap<String,Integer> sorted_ontology_rank = new HashMap<String,Integer>();
		
		HashMap<String, HashMap<String,HashMap<String,Double>>> ontologyToQueryToResMatches = new HashMap<String, HashMap<String,HashMap<String,Double>>>();

		try {
		
			PrimaryTreeMap<String, HashMap<String, HashMap<String,Integer>>> frequency_Map = frequencyMapModels.get_frequency_Value();
					
			for(Map.Entry<String,HashMap<String, HashMap<String, ArrayList<String>>>> entry: results.entrySet()) {
				
				String queryTerm = entry.getKey();
				HashMap<String, HashMap<String, ArrayList<String>>> ontologies = entry.getValue();
				
				//max links for this query term in any ontology.
				double max_links = 0.0;
				
				HashMap<String, HashMap<String, Integer>> ontologyfreqMap = new HashMap<String, HashMap<String, Integer>>(); 
							
				for(Map.Entry<String, HashMap<String, ArrayList<String>>> entry2: ontologies.entrySet()) {
					
					String ontology = entry2.getKey();
					HashMap<String, ArrayList<String>> resources = entry2.getValue();
					
//					System.out.println( " * * * *  " + ontology + " * * * *  " );
					HashMap<String,HashMap<String,Double>> queryToResMatches = new HashMap<String,HashMap<String,Double>>();
					
					if(frequency_Map.containsKey(ontology)) {
						ontologyfreqMap = frequency_Map.get(ontology);
					}
					
					HashMap<String, Double> resourceCount = new HashMap<String, Double>();
					
					for(Map.Entry<String, ArrayList<String>> entry3: resources.entrySet()) {
						
						double linkCount= 0.0;
						String resource = entry3.getKey();						
						
						if(ontologyfreqMap.containsKey(resource)) {
							HashMap<String, Integer> classfreqMap = new HashMap<String, Integer>(); 
							classfreqMap = ontologyfreqMap.get(resource);
							
							linkCount = linkCount  + (0.6)*classfreqMap.get("otfo") + (0.4)*classfreqMap.get("itfo");
//							System.out.println( " " + resource + ":  in links :  " + classfreqMap.get("itfo") + ":  out links :  " + classfreqMap.get("otfo"));
//							for(Map.Entry<String, Integer> link_entry: classfreqMap.entrySet()){
//								linkCount = linkCount + link_entry.getValue();
//							} 
							//System.out.println(linkCount + " for " + resource);
							resourceCount.put(resource, linkCount);
						
							if(max_links < linkCount) {
								max_links = linkCount; }
							
						} else{
							
						}
						//System.out.println(resourceCount);
						
					}					
					
					if(ontologyToQueryToResMatches.containsKey(ontology)) {
						queryToResMatches = ontologyToQueryToResMatches.get(ontology);
						queryToResMatches.put(queryTerm, resourceCount);
					}  else {
						queryToResMatches.put(queryTerm, resourceCount);
					}
					ontologyToQueryToResMatches.put(ontology, queryToResMatches);
				}	
				
				max_link_map.put(queryTerm, max_links);
			}
		
		}
		catch(Exception e) {
			System.out.println("Exception : " + e);
		}
		finally{
			//frequencyMapModel.closeConnection();
		}
//		System.out.println(max_link_map);
		
		return ontologyToQueryToResMatches;
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
