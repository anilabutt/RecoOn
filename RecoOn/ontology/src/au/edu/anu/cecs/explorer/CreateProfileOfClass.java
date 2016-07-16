package au.edu.anu.cecs.explorer;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateProfileOfClass {

	public static void main(String[] args){
		
		CorpusDataRetrieval retrieval = new CorpusDataRetrieval();
		ProfileMap profileMapStructure = ProfileMap.getDefaultMap();
		

		try{
			
		ArrayList<String> ontologies = retrieval.getExistingLoadedOntology();
		
		for(int count=0; count<ontologies.size(); count++) {

			String ontology = ontologies.get(count);
//			System.out.println( "OntologyId : " +count+ " OntologyIRI : " + ontology);
		
			/**
			 * Get all classes in an ontology
			 */
		
			ArrayList<String> classList = retrieval.getClassList(ontology);
	
	
			/**
			 * Declare and Initialize a hashmap to store statistics about all classes of an ontology 
			 * 
			 */
	
			HashMap<String, HashMap<String,String>> ontologyClassMap = new HashMap<String, HashMap<String,String>>();
	
		
			/**
			 * Process classList to get all classes
			 */

		
			for(int classIndex = 0 ; classIndex< classList.size() ; classIndex++) {
		
				HashMap<String, String> relationMap = new HashMap<String, String>();
			//get ontology at index i
				String classId = classList.get(classIndex);
//				System.out.println( "classId : " +classId);
				
				ProfileOfAURI profileQuery = new ProfileOfAURI();
			
				HashMap<String,String> classInfo = new HashMap<String,String>();
				classInfo = profileQuery.getLabelComment(classId, ontology);
				
				relationMap.put("label", classInfo.get("label"));
				relationMap.put("comment", classInfo.get("comment"));
				
				ArrayList<Relation> relations = new ArrayList<Relation>();
				relations = profileQuery.getProfile(classId, ontology);
				
				for(int i=0; i<relations.size() ;i++) {
					Relation relStruct = new Relation();
					relStruct = relations.get(i);
					String property = relStruct.getProperty()+"|"+relStruct.getPropertyLabel();
					String range = relStruct.getValue()+"|"+relStruct.getValueLabel();
					relationMap.put(property,range);
				}				
				ontologyClassMap.put(classId, relationMap);
			}

				profileMapStructure.save_profile_map(ontology, ontologyClassMap);
			
		}
		} catch(Exception e) {
			
		} finally {
			profileMapStructure.closeConnection();
		}
		
	}
}
