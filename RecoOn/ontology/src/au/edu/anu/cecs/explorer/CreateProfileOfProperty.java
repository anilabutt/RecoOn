package au.edu.anu.cecs.explorer;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateProfileOfProperty {

	public static void main(String[] args){
		
		CorpusDataRetrieval retrieval = new CorpusDataRetrieval();
		PropertyProfileMap propertyProfileMapStructure = PropertyProfileMap.getDefaultMap();
		

		try{
			
		ArrayList<String> ontologies = retrieval.getExistingLoadedOntology();
		
		for(int count=0; count<ontologies.size(); count++) {

			String ontology = ontologies.get(count);
		//	System.out.println( "OntologyId : " +count+ " OntologyIRI : " + ontology);
		
			/**
			 * Get all classes in an ontology
			 */
		
			ArrayList<String> classList = retrieval.getPropertyList(ontology);
	
//			System.out.println(" OntologyIRI : " + ontology + "     " +classList.size());
	
			/**
			 * Declare and Initialize a hashmap to store statistics about all classes of an ontology 
			 * 
			 */
	
			HashMap<String, HashMap<String, HashMap<String, String>>> ontologyPropertyProfile = new HashMap<String, HashMap<String, HashMap<String, String>>>();
			
		
			/**
			 * Process classList to get all classes
			 */

		
			for(int propertyIndex = 0 ; propertyIndex< classList.size() ; propertyIndex++) {
		
				
				//get ontology at index i
				String propertyId = classList.get(propertyIndex);
//				System.out.println( "propertyId : " +propertyId);
//				
				ProfileOfAURI profileQuery = new ProfileOfAURI();				 
//				
				HashMap<String, HashMap<String, String>> propertyProfileMap = profileQuery.getPropertyProfile(propertyId, ontology);
//				
//				System.out.println(propertyProfileMap);
				ontologyPropertyProfile.put(propertyId, propertyProfileMap);
			}
//
			propertyProfileMapStructure.save_property_profile_map(ontology, ontologyPropertyProfile);
			
		}
		} catch(Exception e) {
			
		} finally {
			propertyProfileMapStructure.closeConnection();
		}
		
	}
}
