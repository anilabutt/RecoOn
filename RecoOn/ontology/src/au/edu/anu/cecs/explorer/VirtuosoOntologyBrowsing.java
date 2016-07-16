/*
 * Copyright (c) 2015, ANU and/or its constituents or affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package au.edu.anu.cecs.explorer;

import java.util.HashMap;
import java.util.Map;

import jdbm.PrimaryTreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;

public class VirtuosoOntologyBrowsing extends LoggerService {
	
	public VirtuosoOntologyBrowsing() {
		super(VirtuosoOntologyBrowsing.class.getName());
	}

	
	@SuppressWarnings("unchecked")
	public JSONArray getSubClasses(String classURI, String ontologyId) {

		 JSONArray classes = new JSONArray();
		 Model result = ModelFactory.createDefaultModel();
		 
		//property
	    Property label_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
		 
		 String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
				 "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
				 "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+	            
		  		" CONSTRUCT {?class rdfs:label ?label} " +
		  		" FROM <"+ ontologyId +"> " +
		  		" WHERE { "+
		  		" ?class rdfs:subClassOf <"+ classURI +"> ." +
		  		" ?class rdfs:label ?label."
		  		+ "}";
		 
//		 logger.info("GET query prepared");
//	     logger.info(sparql);
	       
	       
		  try {
	    	   QuadStore store = QuadStore.getDefaultStore();
	           result = store.execConstruct(sparql);
	           /**
                * Get matched resources for each graph in the Node Iterator
                * */
        	   ResIterator class_uri = result.listSubjectsWithProperty(label_property);
        	   
        	   while (class_uri.hasNext()){
        		   
        		   JSONObject candidate_result = new JSONObject();  
        		   
        		   //matched resource Id
        		   Resource resource = class_uri.nextResource();
        		   candidate_result.put("class", resource.toString());
        		   
        		   /**
                    * Get all labels for a resource
                    * */
        		   NodeIterator labelList = result.listObjectsOfProperty(resource, label_property);
         		   
        		   while (labelList.hasNext()){
        			   String label = labelList.nextNode().toString();
        			   candidate_result.put("label", label.toString());
        		   }        		   
        		   classes.add(candidate_result);
        	   }
        	   
		  }catch(Exception ex){
			  
		  }
          return classes; 
  }
	
	@SuppressWarnings("unchecked")
	public JSONObject getProfileAsJSON(String classURI, String ontologyId) {

		ProfileMap profileMapStruct = ProfileMap.getDefaultMap();
		PrimaryTreeMap<String, HashMap<String,HashMap<String, String>>> classProfileMap = profileMapStruct.get_profile_map();
		JSONObject profileJSON = new JSONObject();
		
//		System.out.println(classProfileMap.size());
		if(classProfileMap.containsKey(ontologyId)) {
			
			HashMap<String,HashMap<String, String>> classProfileOfOntology  = classProfileMap.get(ontologyId);
			
			if(classProfileOfOntology.containsKey(classURI)) {
				
				HashMap<String, String> classProfile = classProfileOfOntology.get(classURI);

				
				profileJSON.put("label", classProfile.get("label"));
				profileJSON.put("comment", classProfile.get("comment"));
				
				
				JSONArray relationJSON = new JSONArray();
				
				for(Map.Entry<String, String> entry: classProfile.entrySet()) {
					
					String key = entry.getKey();

						if(key.equalsIgnoreCase("label") || key.equalsIgnoreCase("comment")){
							
						} else {
							String value = entry.getValue();
							JSONObject relJSON = new JSONObject();
							relJSON.put(key, value);	
//							System.out.println(key + "		" + value);
							
							relationJSON.add(relJSON);
						}
				}

				profileJSON.put("relations", relationJSON);
				
			}	
			
		}

          return profileJSON; 
  }


	
	@SuppressWarnings("unchecked")
	public HashMap<String,String> getSubClassesAsHashMap(String classURI, String ontologyId) {

		HashMap<String,String> classes = new HashMap<String,String>();
		 Model result = ModelFactory.createDefaultModel();
		 
		//property
	    Property label_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
		 
		 String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
				 "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
				 "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+	            
		  		" CONSTRUCT {?class rdfs:label ?label} " +
		  		" FROM <"+ ontologyId +"> " +
		  		" WHERE { "+
		  		"  { ?class rdfs:subClassOf <"+ classURI +">."+
		  		"  ?class rdfs:label ?label. } "+
		  		"	UNION " +
		  		"  { ?class rdfs:subClassOf <"+ classURI +">."
		  		+ "{{?class rdf:type rdfs:Class} UNION {?class rdf:type owl:Class}} "+
		  		" OPTIONAL {?class rdfs:label ?label.} } "
		  		+ "}";
		 
//		 logger.info("GET query prepared");
//	     logger.info(sparql);
	       
	       
		  try {
	    	   QuadStore store = QuadStore.getDefaultStore();
	           result = store.execConstruct(sparql);
	           /**
                * Get matched resources for each graph in the Node Iterator
                * */
        	   ResIterator class_uri = result.listSubjectsWithProperty(label_property);
        	   
        	   while (class_uri.hasNext()){
        		   
        		  // JSONObject candidate_result = new JSONObject();  
        		   
        		   //matched resource Id
        		   Resource resource = class_uri.nextResource();
        		   String classIRI = resource.toString();
        		   
        		   /**
                    * Get all labels for a resource
                    * */
        		   NodeIterator labelList = result.listObjectsOfProperty(resource, label_property);
         		   
        		   String label=classIRI;
        		   while (labelList.hasNext()){
        			   label = labelList.nextNode().toString();
        		   }        		   
        		   classes.put(classIRI, label);
        	   }
        	   
		  }catch(Exception ex){
			  
		  }
          return classes; 
  }
	
	@SuppressWarnings("unchecked")
	public HashMap<String,String> getSuperClassesAsHashMap(String classURI, String ontologyId) {

		HashMap<String,String> classes = new HashMap<String,String>();
		 Model result = ModelFactory.createDefaultModel();
		 
		//property
	    Property label_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
		 
		 String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
				 "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
				 "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+	            
		  		" CONSTRUCT {?class rdfs:label ?label} " +
		  		" FROM <"+ ontologyId +"> " +
		  		" WHERE { "+
		  		" <"+ classURI +"> rdfs:subClassOf ?class." +
		  		" OPTIONAL {?class rdfs:label ?label.}" +
		  		"}";
		 
		// logger.info("GET query prepared");
	    // logger.info(sparql);
	       
	       
		  try {
	    	   QuadStore store = QuadStore.getDefaultStore();
	           result = store.execConstruct(sparql);
	           /**
                * Get matched resources for each graph in the Node Iterator
                * */
        	   ResIterator class_uri = result.listSubjectsWithProperty(label_property);
        	   
        	   while (class_uri.hasNext()){
        		   
        		  // JSONObject candidate_result = new JSONObject();  
        		   
        		   //matched resource Id
        		   Resource resource = class_uri.nextResource();
        		   String classIRI = resource.toString();
        		   
        		   /**
                    * Get all labels for a resource
                    * */
        		   NodeIterator labelList = result.listObjectsOfProperty(resource, label_property);
         		   
        		   String label=classIRI;
        		   while (labelList.hasNext()){
        			   label = labelList.nextNode().toString();
        		   }        		   
        		   classes.put(classIRI, label);
        	   }
        	   
		  }catch(Exception ex){
			  
		  }
          return classes; 
  }

	   public String getLabel(String ontologyId, String classUri){
		   
		   String classLabel = "";
		   
		   String sparql ="PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
   	            "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
   	            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
   	            "CONSTRUCT { <"+classUri+"> rdfs:label ?label.}"
   	            + "FROM <"+ontologyId+">"
   	            + "WHERE {<"+classUri+"> rdfs:label ?label.}" ;
       
       //	System.out.println(sparql);
       	try{
       		
       		QuadStore store = QuadStore.getDefaultStore();
            Model model = store.execConstruct(sparql);
            
            Property label = model.getProperty("http://www.w3.org/2000/01/rdf-schema#label");

            NodeIterator nIt = model.listObjectsOfProperty(label);
            
            
          		while(nIt.hasNext()){
          			classLabel = nIt.nextNode().toString();
          		} 
          		
          		if (classLabel.contains("#")) {
          			String[] conlabels = classLabel.split("#");
          			classLabel= conlabels[conlabels.length-1];
          			
          		} else if(classLabel.contains("@")) {
          			String[] conlabels = classLabel.split("@");
          				classLabel= conlabels[conlabels.length-2];
          		} else {
          			
          		}
          	}catch(Exception e){
          		System.out.println("Exception in getLabel Function  :" + e);
          	}

       	return classLabel;
       }
	
		@SuppressWarnings("unchecked")
		public HashMap<String,String> getSuperObjectPropertiesAsHashMap(String propertyURI, String ontologyId) {

			HashMap<String,String> properties = new HashMap<String,String>();
			 Model result = ModelFactory.createDefaultModel();
			 
			//property
		    Property label_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
			 
			 String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
					 "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
					 "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+	            
			  		" CONSTRUCT {?property rdfs:label ?label} " +
			  		" FROM <"+ ontologyId +"> " +
			  		" WHERE { "+
			  		" <"+ propertyURI +"> rdfs:subPropertyOf ?property."+
					" ?property rdf:type owl:ObjectProperty."+
			  		" OPTIONAL {?property rdfs:label ?label.} "+
			  		" }";
			 
//			 logger.info("GET query prepared");
//		     logger.info(sparql);
		       
		       
			  try {
		    	   QuadStore store = QuadStore.getDefaultStore();
		           result = store.execConstruct(sparql);
		           /**
	                * Get matched resources for each graph in the Node Iterator
	                * */
	        	   ResIterator property_uri = result.listSubjectsWithProperty(label_property);
	        	   
	        	   while (property_uri.hasNext()){
	        		   
	        		  // JSONObject candidate_result = new JSONObject();  
	        		   
	        		   //matched resource Id
	        		   Resource resource = property_uri.nextResource();
	        		   String propertyIRI = resource.toString();
	        		   
	        		   /**
	                    * Get all labels for a resource
	                    * */
	        		   NodeIterator labelList = result.listObjectsOfProperty(resource, label_property);
	         		   
	        		   String label=propertyIRI;
	        		   
	        		   while (labelList.hasNext()){
	        			   label = labelList.nextNode().toString();
	        		   }        		   
	        		   properties.put(propertyIRI, label);
	        	   }
	        	   
			  }catch(Exception ex){
				  
			  }
	          return properties; 
	  }
		
		@SuppressWarnings("unchecked")
		public HashMap<String,String> getSuperDatatypePropertiesAsHashMap(String propertyURI, String ontologyId) {

			HashMap<String,String> properties = new HashMap<String,String>();
			 Model result = ModelFactory.createDefaultModel();
			 
			//property
		    Property label_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
			 
			 String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
					 "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
					 "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+	            
			  		" CONSTRUCT {?property rdfs:label ?label} " +
			  		" FROM <"+ ontologyId +"> " +
			  		" WHERE { "+
			  		"  <"+propertyURI+"> rdfs:subPropertyOf ?property."
			  		+ "?property rdf:type owl:DatatypeProperty."+
			  		"  OPTIONAL {?property rdfs:label ?label.} "+
			  		" }";
			 
//			 logger.info("GET query prepared");
//		     logger.info(sparql);
		       
		       
			  try {
		    	   QuadStore store = QuadStore.getDefaultStore();
		           result = store.execConstruct(sparql);
		           /**
	                * Get matched resources for each graph in the Node Iterator
	                * */
	        	   ResIterator property_uri = result.listSubjectsWithProperty(label_property);
	        	   
	        	   while (property_uri.hasNext()){
	        		   
	        		  // JSONObject candidate_result = new JSONObject();  
	        		   
	        		   //matched resource Id
	        		   Resource resource = property_uri.nextResource();
	        		   String propertyIRI = resource.toString();
	        		   
	        		   /**
	                    * Get all labels for a resource
	                    * */
	        		   NodeIterator labelList = result.listObjectsOfProperty(resource, label_property);
	         		   
	        		   String label=propertyIRI;
	        		   
	        		   while (labelList.hasNext()){
	        			   label = labelList.nextNode().toString();
	        		   }        		   
	        		   properties.put(propertyIRI, label);
	        	   }
	        	   
			  }catch(Exception ex){
				  
			  }
	          return properties; 
	  }
		
		@SuppressWarnings("unchecked")
		public HashMap<String,String> getSubObjectPropertiesAsHashMap(String propertyURI, String ontologyId) {

			HashMap<String,String> properties = new HashMap<String,String>();
			 Model result = ModelFactory.createDefaultModel();
			 
			//property
		    Property label_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
			 
			 String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
					 "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
					 "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+	            
			  		" CONSTRUCT {?property rdfs:label ?label} " +
			  		" FROM <"+ ontologyId +"> " +
			  		" WHERE { "+
			  		"  ?property rdfs:subPropertyOf <"+ propertyURI +">."
					+ "?property rdf:type owl:ObjectProperty."+
			  		"  OPTIONAL {?property rdfs:label ?label.} "+
			  		" }";
			 
//			 logger.info("GET query prepared");
//		     logger.info(sparql);
		       
		       
			  try {
		    	   QuadStore store = QuadStore.getDefaultStore();
		           result = store.execConstruct(sparql);
		           /**
	                * Get matched resources for each graph in the Node Iterator
	                * */
	        	   ResIterator property_uri = result.listSubjectsWithProperty(label_property);
	        	   
	        	   while (property_uri.hasNext()){
	        		   
	        		  // JSONObject candidate_result = new JSONObject();  
	        		   
	        		   //matched resource Id
	        		   Resource resource = property_uri.nextResource();
	        		   String propertyIRI = resource.toString();
	        		   
	        		   /**
	                    * Get all labels for a resource
	                    * */
	        		   NodeIterator labelList = result.listObjectsOfProperty(resource, label_property);
	         		   
	        		   String label=propertyIRI;
	        		   
	        		   while (labelList.hasNext()){
	        			   label = labelList.nextNode().toString();
	        		   }        		   
	        		   properties.put(propertyIRI, label);
	        	   }
	        	   
			  }catch(Exception ex){
				  
			  }
	          return properties; 
	  }
		
		@SuppressWarnings("unchecked")
		public HashMap<String,String> getSubDataPropertiesAsHashMap(String propertyURI, String ontologyId) {

			HashMap<String,String> properties = new HashMap<String,String>();
			Model result = ModelFactory.createDefaultModel();
			 
			//property
		    Property label_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
			 
			 String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
					 "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
					 "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+	            
			  		" CONSTRUCT {?property rdfs:label ?label} " +
			  		" FROM <"+ ontologyId +"> " +
			  		" WHERE { "+
			  		" ?property rdfs:subPropertyOf <"+ propertyURI +">."
					+ "?property rdf:type owl:DatatypeProperty."+
			  		"  OPTIONAL {?property rdfs:label ?label.} "+
			  		" }";
			 
//			 logger.info("GET query prepared");
//		     logger.info(sparql);
		       
		       
			  try {
		    	   QuadStore store = QuadStore.getDefaultStore();
		           result = store.execConstruct(sparql);
		           /**
	                * Get matched resources for each graph in the Node Iterator
	                * */
	        	   ResIterator property_uri = result.listSubjectsWithProperty(label_property);
	        	   
	        	   while (property_uri.hasNext()){
	        		   
	        		  // JSONObject candidate_result = new JSONObject();  
	        		   
	        		   //matched resource Id
	        		   Resource resource = property_uri.nextResource();
	        		   String propertyIRI = resource.toString();
	        		   
	        		   /**
	                    * Get all labels for a resource
	                    * */
	        		   NodeIterator labelList = result.listObjectsOfProperty(resource, label_property);
	         		   
	        		   String label=propertyIRI;
	        		   
	        		   while (labelList.hasNext()){
	        			   label = labelList.nextNode().toString();
	        		   }        		   
	        		   properties.put(propertyIRI, label);
	        	   }
	        	   
			  }catch(Exception ex){
				  
			  }
	          return properties; 
	  }
}
