package au.edu.anu.cecs.explorer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdbm.PrimaryTreeMap;

public class MinimumPathAsHashMap {

	
	   
	public static void main(String[] args)	{
		  
		createConnectionMatrix();


	}
	

	public static void createConnectionMatrix() {
		
		CorpusDataRetrieval retrieval = new CorpusDataRetrieval();
//		ProfileOfAURI profileClass = new ProfileOfAURI();
		
		ClassConnectivityMap classConnectMap = ClassConnectivityMap.getDefaultMap();
		PrimaryTreeMap<String, HashMap<String, HashMap<String,Double>>> connectivity = classConnectMap.get_classConnectivity_Value();
			
		ArrayList<String> ontologies = retrieval.getExistingLoadedOntology();
	
		try {
			
			for(int count=0; count<ontologies.size(); count++) {

				String ontology = ontologies.get(count);
//				System.out.println( "OntologyId : " +count+ " OntologyIRI : " + ontology);
		
				/**
				 * Get all classes in an ontology
				 */
		
				//ArrayList<String> classList = retrieval.getClassList(ontology);
				
				HashMap<String, HashMap<String, Double>> classConnectivityOfOntology = connectivity.get(ontology);
				
				for(Map.Entry<String, HashMap<String, Double>> entry: classConnectivityOfOntology.entrySet()) {					
				
				String classIRI = entry.getKey();
				HashMap<String, Double> classConnectivity = entry.getValue();
				
					for(Map.Entry<String, Double> entery2: classConnectivity.entrySet()) {
						String classIRI2 = entry.getKey();
						int dist = Integer.parseInt(entry.getValue().toString()); 
					}				
				}
			
//			System.out.println(ontology + " loaded ");
			classConnectMap.save_classConnectivity_Value(ontology, classConnectivityOfOntology);
		}
		} catch (Exception e) {
			
		} finally {
			classConnectMap.closeConnection();
		}
	}	
}
