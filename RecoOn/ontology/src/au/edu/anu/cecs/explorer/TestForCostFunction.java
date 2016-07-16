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

public class TestForCostFunction {
	
	 SimilarityCalculation simcal = new SimilarityCalculation();
	 HashMap<String, HashMap<String, Double>> labelMatch = new HashMap<String, HashMap<String, Double>>(); 
	 HashMap<String,HashMap<String, ArrayList<String>>> allResults = new HashMap<String,HashMap<String, ArrayList<String>>>();

	public HashMap<String, Double> getRankedOntologies(HashMap<String,HashMap<String, HashMap<String, ArrayList<String>>>> results) {
		
		HashMap<String,HashMap<String, ArrayList<CandidateResource>>> LabelCostBasedResults = getLabelCost(results);
		
		HashMap<String, Double> ontologyLabelCosts = getLabelCostOfAnOntology(LabelCostBasedResults);
		
		CoverageSimilarityModel cs = new CoverageSimilarityModel();
		HashMap<String, Double> ontologyCoverage = cs.getCoverageSimilarity(results);
			
		OntologyPopularity ontProp = new OntologyPopularity();
		HashMap<String, Double> ontologyPopularity = ontProp.getOntologyPopularity(ontologyCoverage);
		
//		System.out.println(" ontologyLabelCosts : " +ontologyLabelCosts); 
//		System.out.println(" ontologyCoverage : " + ontologyCoverage); 
//		System.out.println(" ontologyPopularity : " +ontologyPopularity); 
//		
		HashMap<String, Double> result = new HashMap<String, Double>();
		
		for(Map.Entry<String, Double> entry : ontologyLabelCosts.entrySet()){
			
			String ontology = entry.getKey();
			double labelCost = entry.getValue();
			
			double finalRank = 0.55 * labelCost + 0.25 * ontologyCoverage.get(ontology) + 0.20 * ontologyPopularity.get(ontology);
//			System.out.println( "finalRank " + finalRank + " labelCost : " + labelCost + " Coverage : " + ontologyCoverage.get(ontology) + "Popularity : " +ontologyPopularity.get(ontology));
			
			result.put(ontology, round(finalRank,3));
		}
		
		return result;
	} 
	
	
	public HashMap<String,HashMap<String, ArrayList<String>>> getResults() {
		return allResults;
	}
	
	public HashMap<String, Double> getLabelCostOfAnOntology(HashMap<String,HashMap<String, ArrayList<CandidateResource>>> LabelCostBasedResults) {
		
		HashMap<String, Double> labelCost = new HashMap<String, Double>();
		
		double queryWordCount = LabelCostBasedResults.size();
		
		double max_ontologyMatchingCost = 0.0;
		double min_ontologyMatchingCost = 0.0;
		
		for(Map.Entry<String,HashMap<String, ArrayList<CandidateResource>>> entry: LabelCostBasedResults.entrySet()) {
			
			String queryTerm = entry.getKey();
			HashMap<String, ArrayList<CandidateResource>> matchedOntologies = entry.getValue();
			
			HashMap<String, Double> noOfMatchedRes = sortByValues(labelMatch.get(queryTerm));
			double maxMatches =0.0;
			
			for(Map.Entry<String, Double> _entry: noOfMatchedRes.entrySet() ) {
				maxMatches = _entry.getValue();
				break;	}
			

			for(Map.Entry<String, ArrayList<CandidateResource>> entry2: matchedOntologies.entrySet()) {
				
				String ontology = entry2.getKey();
				ArrayList<CandidateResource> resList = entry2.getValue();
				
				double ontologyMatchingCost = 0.0;
				
				double maxRelevance = 0.0;
				double average=0.0;
				for(CandidateResource r: resList) {
					
					if(r.score>maxRelevance) {
						maxRelevance = r.score;
					}
					average = average + r.score;
				}
				
				if(average == maxRelevance) {
					
				} else {
					maxRelevance = maxRelevance + (average/matchedOntologies.size());
				}
				ontologyMatchingCost = maxRelevance;
				ontologyMatchingCost =ontologyMatchingCost/maxMatches;
				ontologyMatchingCost = ontologyMatchingCost / queryWordCount;
				
				if(labelCost.containsKey(ontology)) {
					double score = labelCost.get(ontology);
					ontologyMatchingCost = ontologyMatchingCost + score;
				} 
				
				labelCost.put(ontology, ontologyMatchingCost);
				
				if(ontologyMatchingCost>max_ontologyMatchingCost) {
					max_ontologyMatchingCost = ontologyMatchingCost; }
				
				if(ontologyMatchingCost<min_ontologyMatchingCost) {
					min_ontologyMatchingCost = ontologyMatchingCost;
				}
				
			}
		}
		
		
		for(Map.Entry<String, Double> entry: labelCost.entrySet()){
			String ont = entry.getKey();
			double ontologyMatchingCost = entry.getValue();
			
			double norm_ontologyMatchingCost = (( ontologyMatchingCost - min_ontologyMatchingCost) / (max_ontologyMatchingCost - min_ontologyMatchingCost));
			labelCost.put(ont, norm_ontologyMatchingCost);
			
		}
		
		return sortByValues(labelCost);
	}
	
	
	public HashMap<String,HashMap<String, ArrayList<CandidateResource>>> getLabelCost(HashMap<String,HashMap<String, HashMap<String, ArrayList<String>>>> results) {

		WordNet wn = new WordNet();
		HashMap<String,HashMap<String, ArrayList<CandidateResource>>> LabelCostBasedResults= new HashMap<String,HashMap<String, ArrayList<CandidateResource>>>();
		
		//System.out.println(results);
		
		for(Map.Entry<String,HashMap<String, HashMap<String, ArrayList<String>>>> entry: results.entrySet() ) {
			String key = entry.getKey();
			//System.out.println( " ********  QueryWord : \"" + key + " \"********** ");
			HashMap<String, HashMap<String, ArrayList<String>>> map = entry.getValue();
			
			HashMap<String, ArrayList<CandidateResource>> ontology_map = new HashMap<String, ArrayList<CandidateResource>>();
			for(Map.Entry<String,HashMap<String, ArrayList<String>>> entry2: map.entrySet() ) {
				
				ArrayList<CandidateResource> resourceList = new ArrayList<CandidateResource>();
				
				ArrayList<String> resources = new ArrayList<String>();
				
				String key2 = entry2.getKey();
				//System.out.println( " %%%% Ontology : \"" + key2 + " \" %%%% ");
				HashMap<String, ArrayList<String>> map2 = entry2.getValue();
				
				for(Map.Entry<String, ArrayList<String>> entry3: map2.entrySet()) {
					String key3 = entry3.getKey();
					ArrayList<String> labels = entry3.getValue();
					

					//System.out.println(  " Resource : " + key3  );
					//System.out.println(  " Labels : " + labels  );
					
					double matchedLabelScore = 0.0;
					String matchedLabel = "";
					String uri="";
					if(key3.contains("#")) {
						uri = key3.split("#")[1];
					} else if(key3.contains("/")){
						uri = key3.split("\\/")[key3.split("\\/").length -1];
					}
					
					if(key.equalsIgnoreCase(uri)) {
						double labelMatchScore =0.0;
						boolean similarity_score = simcal.getSemanticSimilarity(key, uri);
						
						if(similarity_score == true) {
							labelMatchScore = simcal.getJaccardsimilarity(key, uri); }
						
						if(labelMatchScore>matchedLabelScore) {
							matchedLabelScore = labelMatchScore;
							matchedLabel = uri;
						}
//						System.out.println(key3);
						
					} else {
					for(String s: labels){
						
						double labelMatchScore =0.0;
						boolean similarity_score = simcal.getSemanticSimilarity(key, s);
						
						if(similarity_score == true) {
							labelMatchScore = simcal.getJaccardsimilarity(key, s); }
						
						if(labelMatchScore>matchedLabelScore) {
							matchedLabelScore = labelMatchScore;
							matchedLabel = s;
						} 
					}
					}
					//System.out.println(matchedLabel + "  "+ matchedLabelScore );
					
					if (matchedLabelScore>0.0) {
						CandidateResource resource = new CandidateResource(key3, matchedLabel, matchedLabelScore);
						resourceList.add(resource);
						resources.add(key3);
					}
						
				}
				if (resourceList.size() > 0) {
					ontology_map.put(key2, resourceList);
				}
				
				
			
				/*
				 * 
				 * 
				 * */
				
				HashMap<String, Double> resourceCount; 
			
				if(labelMatch.containsKey(key)){
					resourceCount = labelMatch.get(key);
				} else {
					resourceCount = new HashMap<String, Double>();
				}
				resourceCount.put(key2, Double.parseDouble(resourceList.size()+""));
				labelMatch.put(key, resourceCount);
				
				/*
				 * 
				 * 
				 * */
				HashMap<String, ArrayList<String>> matches;
				
				if(allResults.containsKey(key2)){
					matches = allResults.get(key2);
				} else {
					matches = new HashMap<String, ArrayList<String>>();
				}
				
				if (resources.size() > 0) {
					matches.put(key, resources);
				}
				
				allResults.put(key2, matches);
			}
			
			LabelCostBasedResults.put(key, ontology_map);
		}
		return LabelCostBasedResults;
	} 
	
	
		public void getStructureCost() {
			
//			System.out.println("  ***********  In STRUCTURE COST *********** ");
			MinimumPathMap minPathMap = MinimumPathMap.getDefaultMap();
			PrimaryTreeMap<String, HashMap<String, HashMap<String,ArrayList<String>>>> minimum_paths = minPathMap.get_classConnectivity_Value();
			
			
				
				/**
				 * Find Structural cost for each ontology for the given query
				 * */
				
				for(Map.Entry<String, HashMap<String, ArrayList<String>>> resEntry: allResults.entrySet()) {
					
					//variable to store the structural cost for this ontology
					double structuralCost = 0.0;
					
					//get ontology
					String ontology = resEntry.getKey();
//					System.out.println(" ontology " + ontology);
					//get list of resources matched to the query term for this ontology
					
					HashMap<String, ArrayList<String>> ontology_match_results = resEntry.getValue();
					
					/**
					 * Get the minimumpath map
					 * */
					HashMap<String, HashMap<String,ArrayList<String>>> min_path_map = new HashMap<String, HashMap<String,ArrayList<String>>>() ;
					
					//get the minimum paths store for this ontology
					if(minimum_paths.containsKey(ontology)) {
						min_path_map = minimum_paths.get(ontology);
					}
					
					
					/**
					 * 
					 * */
					//If the ontology does not have minimum path information, then structural cost is zero for this ontology. else find the cost
					
					if(min_path_map.size() > 0) {
//						System.out.println(" min_path_map : " + min_path_map.size());
						
						structuralCost++;						
						
						// get queryTerms and their matched resources for the ontology
						for(Map.Entry<String,ArrayList<String>> entry2: ontology_match_results.entrySet()) {
							
							//This variable stores the cost for a given queryTerm to the ontology. 
							double structurCostOfRes = 0.0;
							
//							String queryTerm = entry2.getKey();
//							ArrayList<String> resources = entry2.getValue();
//							System.out.println(" queryTerm : " + queryTerm);
//							//for each matched resource to a queryTerm, find the connectivity to the resources matching to other query terms
//							for(String s: resources) {
//								
//								System.out.println(" resource1 : " + s);
//								//variable to store the connectivity of the resource.
//								double s_connectivity = 0.0;
//								
//								//if there is minimum path information for thie resource. get it
//								
//								if(min_path_map.containsKey(s)) { 
//									
//									// All minimum paths for resource s
//									HashMap<String,ArrayList<String>> min_path_ofClass = min_path_map.get(s);
//									
//									System.out.println(" min_path_ofClass : " + min_path_ofClass.size());
//									//
//									for(Map.Entry<String,ArrayList<String>> entry3: ontology_match_results.entrySet()) {
//										
//										
//										String queryTerm2 = entry3.getKey();
//									
//										System.out.println(" queryTerm2 : " + queryTerm2);
//										if(queryTerm2.equalsIgnoreCase(queryTerm)) {
//											System.out.println(" queryTerm is equal to queryTerm2 : " );
//										}
//										else{
//												ArrayList<String> resources2 = entry3.getValue();
//												if(resources2.contains(s)) {
//													s_connectivity =1.0;
//												} else {
//													
//													double max_connectivity =0.0;
//											
//													for(String s2: resources2) {
//													
//														System.out.println(" resources2 : " + s2);
//														if(min_path_ofClass.containsKey(s2)) {
//															
//															//System.out.println("Shortest Path of " + s + "  and " + s2 + " is " +  (min_path_ofClass.get(s2).size() -1));
////															double length = 1 / (min_path_ofClass.get(s2).size() -1);
//																
////															if(length>max_connectivity) {
////																max_connectivity = length;
////															}															
//														}
//														//System.out.println("Shortest Path of " + s + "  and " + s2 + " is " +  (min_path_ofClass.get(s2).size() -1));
////														
//													}
//												}
//											}	
//										}		
//									}
//											
//								}					
							}
					} else {}
//					System.out.println(" structuralCost : " + structuralCost);
				}
				//System.out.println(allResults);
//				for (Map.Entry<String,HashMap<String, ArrayList<CandidateResource>>> entry: results.entrySet()) {
//					String queryTerm = entry.getKey();
//					HashMap<String, ArrayList<CandidateResource>> ontology_map = entry.getValue();
//					
//					for(Map.Entry<String, ArrayList<CandidateResource>>)
//					
//				}				


		return;
	} 

		public double round(double value, int places) {
		    if (places < 0) throw new IllegalArgumentException();

		    long factor = (long) Math.pow(10, places);
		    value = value * factor;
		    long tmp = Math.round(value);
		    return (double) tmp / factor;
		}
		

	 private static HashMap<String, Double> sortByValues(HashMap<String, Double> map) { 
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
