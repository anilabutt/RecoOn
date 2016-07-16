/*
 * Copyright (c) 2015, ANU and/or its constituents or affiliates. All rights reserved.
 * Use is subject to license terms.
 */

package au.edu.anu.cecs.explorer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 *
 * @author anila
 */
public class VirtuosoRepositoryQuery extends LoggerService {

    /**
	 * Default constructor to initializes logging by its parent.
	 */
	
	private HashMap<String, HashMap<String, ArrayList<String>>> results = new HashMap<String, HashMap<String, ArrayList<String>>>();
	
	public VirtuosoRepositoryQuery() {
		super(VirtuosoRepositoryQuery.class.getName());
	}

/*
 * select ontology that contains all query keywords 
 * */
	
	
	@SuppressWarnings("unchecked")
	public JSONArray selectOntologiesAsJSON(List<String> queryWords){
		 	
		 	JSONArray resultSet = new JSONArray();
		 
		 	/**
	    	 * Writing SPARQL query dynamically based on the number of keywords in the query.
	    	 * */
	    	
	    	String firstWord = "";
	   	
	    	if(queryWords.isEmpty()) {
//	    		logger.info("querywords number is  : "+ queryWords.size()); 
	    		}
	    	else { firstWord = queryWords.get(0); }
	   	
	    	String prefixes = " PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
	           " PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
	           " PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
	    	
	    	String constructStart = " CONSTRUCT { ?resource rdfs:graph ?ontology. ?resource rdfs:label ?value. ";
	        String constructBody = "";   

	    	for(int i=1; i< queryWords.size(); i++ ){
	    		constructBody = constructBody + "?resource"+i+" rdfs:graph ?ontology. ?resource"+i+" rdfs:label ?value"+i+".";		
	    	}
	    	
	        String constructEnd = "}" ;
	        
	        String conditionStart = " Where {GRAPH ?ontology {" +
	           " { ?resource rdfs:label ?value." +
	           " { {?resource rdf:type rdfs:Class.} UNION { ?resource rdf:type owl:Class.} }" +
		        " FILTER (regex(?value, \""+ firstWord +"\", \"i\"))} ";       
	       
	    	String conditionBody  = ""; 
	       
		      	for(int i=1; i< queryWords.size(); i++ ){
		      		conditionBody = conditionBody + " { ?resource"+i+" rdfs:label ?value"+i+"." +
		    			  " { {?resource"+i+" rdf:type rdfs:Class.} UNION { ?resource"+i+" rdf:type owl:Class.} }" +
		    			  " FILTER (regex(?value"+i+", \""+ queryWords.get(i) +"\", \"i\"))} ";  
		      	}
		      	
		      	String conditionEnd =  " }}" ;
		      	
		      	String sparql = prefixes+constructStart+constructBody+constructEnd+conditionStart+conditionBody+conditionEnd;

//	       logger.info("GET query prepared");
//	       logger.info(sparql);
	       	
	       /**
	        * Execution of SPARQL query.
	        *  
	        * */
  	
	       try {
	    	  
	    	   QuadStore store = QuadStore.getDefaultStore();
	    	  
	    	   /**
		    	 * Data variables initialization.
		    	 * */
	    	   
	    	   Model result = store.execConstruct(sparql);
	    	   
	    	   Property graph_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#graph");
	    	   Property label_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
	           
	    	   /**
	            * Node Iterator contains OntologiesURIs or OntologyID that contains all query keywords.
	            */
	           NodeIterator graphs = result.listObjectsOfProperty(graph_property) ;
	           
	           /**
	            * Process Node Iterator to access graph Ids sequentially.
	            * */
	           
	           while (graphs.hasNext()){
	        	   JSONObject results = new JSONObject();  


        		   //graph Id
	        	   RDFNode graphId = graphs.nextNode();
//	        	   logger.info(graphId.toString());
	        	   results.put("ontology", graphId.toString());
	               
	        	   /**
	                * Get matched resources for each graph in the Node Iterator
	                * */
	        	   ResIterator uris = result.listSubjectsWithProperty(graph_property,graphId);
	        	   
	        	   
	        	   /**
	                * Process Resource Iterator to access resource Ids sequentially.
	                * */
        		   JSONArray resources = new JSONArray();
//	        	   HashMap<String, ArrayList<String>> matchedResources  =new HashMap<String, ArrayList<String>>();
	        	   
	        	   while (uris.hasNext()){


	        		   //matched resource Id
	        		   Resource resource = uris.nextResource();
//	        		   System.out.println(resource.toString());

	        		   /**
	                    * Get all labels for a resource
	                    * */
	        		   NodeIterator labelList = result.listObjectsOfProperty(resource, label_property);
	        		   
	        		   //list to store labels of a resource
	        		   //ArrayList<String> labels = new ArrayList<String>();
	        		   JSONObject res = new JSONObject();  
	        		   res.put("resource", resource.toString());
	        		   
	        		   while (labelList.hasNext()){
	        			   String label = labelList.nextNode().toString();
	        			  //labels.add(label);
	        			   res.put("label", label.toString());
	        		   }        		   
	        		   //matchedResources.put(resource.toString(), labels);
	        		   resources.add(res);
	        	   }
	        	   
	        	   results.put("resources", resources);
	        	   resultSet.add(results);
	        	   //results.put(graphId.toString(), matchedResources);
	           }
	           
	                      
	       } catch(Exception exp) {
	       	logger.info("Exception in getSearchResults "+exp);
	       }
	       return resultSet;
	   }

//	@SuppressWarnings("unchecked")
//	public JSONArray selectActiveRankScore(List<String> queryWords){
//		
//		HashMap<String, Double> aktive_rank = new HashMap<String, Double>();
//		
//		AktiveRankScore ak = new AktiveRankScore();
//		aktive_rank = ak.getRanks(queryWords);
//		
//		JSONArray ontologies = new JSONArray();
//		
//		for(Map.Entry<String, Double> entry: aktive_rank.entrySet() ) {
//			JSONObject object = new JSONObject();
//			object.put("ontology", entry.getKey());
//			object.put("rank", entry.getValue());
//			
//			ontologies.add(object);
//		}
//		
//		return ontologies;
//	}
	
	@SuppressWarnings("unchecked")
	
	public JSONArray selectRankedOntologiesAsJSON(List<String> queryWords){
		 	
		
		HashMap<String,HashMap<String, HashMap<String, ArrayList<String>>>> results = this.selectOntologies(queryWords);
		
		TestForCostFunction costFunction = new TestForCostFunction();	
		
		HashMap<String, HashMap<String,ArrayList<String>>> ontologyMap = new HashMap<String, HashMap<String,ArrayList<String>>>();
		
		for(Map.Entry<String,HashMap<String, HashMap<String, ArrayList<String>>>> entry: results.entrySet()) {
			String query = entry.getKey(); 
			HashMap<String, HashMap<String,ArrayList<String>>> value = entry.getValue();
			
			for (Map.Entry<String, HashMap<String,ArrayList<String>>> entry2: value.entrySet()) {
				String ont = entry2.getKey();
				HashMap<String,ArrayList<String>> matches = entry2.getValue();
				
				HashMap<String,ArrayList<String>> resources = new HashMap<String,ArrayList<String>>();				
				
				if(ontologyMap.containsKey(ont)) {
					resources = ontologyMap.get(ont);
				}
				
				for(Map.Entry<String, ArrayList<String>> entry3: matches.entrySet()) {
					String res = entry3.getKey();
					if(resources.containsKey(res)){					
					} else {
						resources.put(res, entry3.getValue());
					}				
				}
				
				ontologyMap.put(ont, resources);				
			}	
			
		}
		
		HashMap<String, Double> rankMap = costFunction.getRankedOntologies(results);
		
		JSONArray resultSet = new JSONArray();
		
		for(Map.Entry<String, Double> entry: rankMap.entrySet()){
		
			String ont = entry.getKey();
			double score = entry.getValue();
			
			JSONObject result = new JSONObject();  
			result.put("ontology", ont);
			result.put("rank", score);
		
			JSONArray resources = new JSONArray();
		
			HashMap<String, ArrayList<String>> matchedResources  = ontologyMap.get(ont);
			
			for(Map.Entry<String, ArrayList<String>> entry2 : matchedResources.entrySet()){	
				JSONObject res = new JSONObject();
				String resource = entry2.getKey();
				
				ArrayList<String> labels = entry2.getValue();
				String label="";
				
				if(labels.size()==1) {
					label = labels.get(0); }
				else {
					for(String s: labels) {
						label = label + " | " + s ;						
					}
				}
				res.put("resource", resource);
				res.put("label", label);	
				
				resources.add(res);
			}
			
			result.put("resources", resources);
			resultSet.add(result);		
		}
		
//		System.out.println(resultSet);   
	       return resultSet;
	   }
	
	/*
	 * select ontologies that contains one or more query keywords 
	 * */
	
	@SuppressWarnings("unchecked")
	public JSONArray selectOntologiesWithQueryTermsAsJSON(List<String> queryWords){
	 	
	 	JSONArray resultSet = new JSONArray();
	 
	 	/**
    	 * Writing SPARQL query dynamically based on the number of keywords in the query.
    	 * */
    	
    	String firstWord = "";
   	
    	if(queryWords.isEmpty()) {
    		logger.info("querywords number is  : "+ queryWords.size()); }
    	else { 
    		firstWord = queryWords.get(0); 
    		}
    	
      	
          String prefixes = " PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
              " PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
              " PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
              
          String construct = " CONSTRUCT {?resource rdfs:graph ?ontology. ?resource rdfs:label ?value. }" ;
        
          String conditionStart  = " Where {GRAPH ?ontology "+ 
          		"{ ?resource rdfs:label ?value." +
          		" { {?resource rdf:type rdfs:Class.} UNION { ?resource rdf:type owl:Class.} }" +
  	        	" FILTER (regex(?value, \""+ firstWord +"\", \"i\")";
	       
          
          String conditionBody  = ""; 
	      	for(int i=1; i<queryWords.size(); i++ ){
	      		conditionBody = conditionBody + " || regex(?value, \""+ queryWords.get(i) +"\", \"i\")";
	      	}

	      String conditionEnd =  ") }}" ;
	      String sparql = prefixes+construct+conditionStart+conditionBody+conditionEnd;

//          logger.info("GET query prepared");
//          logger.info(sparql);
          /**
	        * Execution of SPARQL query.
	        *  
	        * */
 	
	       try {
	    	  
	    	   QuadStore store = QuadStore.getDefaultStore();
	    	  
	    	   /**
		    	 * Data variables initialization.
		    	 * */
	    	   
	    	   Model result = store.execConstruct(sparql);
	    	   
	    	   Property graph_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#graph");
	    	   Property label_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
	           
	    	   /**
	            * Node Iterator contains OntologiesURIs or OntologyID that contains all query keywords.
	            */
	           NodeIterator graphs = result.listObjectsOfProperty(graph_property) ;
	           
	           /**
	            * Process Node Iterator to access graph Ids sequentially.
	            * */
	           
	           while (graphs.hasNext()){
	        	   JSONObject results = new JSONObject();  


       		   //graph Id
	        	   RDFNode graphId = graphs.nextNode();
//	        	   logger.info(graphId.toString());
	        	   results.put("ontology", graphId.toString());
	               
	        	   /**
	                * Get matched resources for each graph in the Node Iterator
	                * */
	        	   ResIterator uris = result.listSubjectsWithProperty(graph_property,graphId);
	        	   
	        	   
	        	   /**
	                * Process Resource Iterator to access resource Ids sequentially.
	                * */
       		   JSONArray resources = new JSONArray();
//	        	   HashMap<String, ArrayList<String>> matchedResources  =new HashMap<String, ArrayList<String>>();
	        	   
	        	   while (uris.hasNext()){


	        		   //matched resource Id
	        		   Resource resource = uris.nextResource();
//	        		   System.out.println(resource.toString());

	        		   /**
	                    * Get all labels for a resource
	                    * */
	        		   NodeIterator labelList = result.listObjectsOfProperty(resource, label_property);
	        		   
	        		   //list to store labels of a resource
	        		   //ArrayList<String> labels = new ArrayList<String>();
	        		   JSONObject res = new JSONObject();  
	        		   res.put("resource", resource.toString());
	        		   
	        		   while (labelList.hasNext()){
	        			   String label = labelList.nextNode().toString();
	        			  //labels.add(label);
	        			   res.put("label", label.toString());
	        		   }        		   
	        		   //matchedResources.put(resource.toString(), labels);
	        		   resources.add(res);
	        	   }
	        	   
	        	   results.put("resources", resources);
	        	   resultSet.add(results);
	        	   //results.put(graphId.toString(), matchedResources);
	           }
	           
	                      
	       } catch(Exception exp) {
	       	logger.info("Exception in getSearchResults "+exp);
	       }
	       return resultSet;
      }
	
	
    public HashMap<String, HashMap<String, ArrayList<String>>> constructOntologies(List<String> queryWords){

    	/**
    	 * Data variables initialization.
    	 * */
    	
    	Model result =  ModelFactory.createDefaultModel();
    	
    	//property
    	Property graph_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#graph");
    	Property label_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
   	
    	/**
    	 * Writing SPARQL query dynamically based on the number of keywords in the query.
    	 * */
    	
    	String firstWord = "";
   	
    	if(queryWords.isEmpty()) {
//    		logger.info("querywords number is  : "+ queryWords.size()); 
    		}
    	else { firstWord = queryWords.get(0); }
   	
    	String prefixes = " PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
           " PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
           " PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
    	
    	String constructStart = " CONSTRUCT { ?resource rdfs:graph ?ontology. ?resource rdfs:label ?value. ";
        String constructBody ="";   

    	for(int i=1; i< queryWords.size(); i++ ){
    		constructBody = "?resource"+i+" rdfs:graph ?ontology. ?resource"+i+" rdfs:label ?value"+i+".";		
    	}
    	
        String constructEnd = "}" ;
        
        String conditionStart = " Where {GRAPH ?ontology {" +
           " { ?resource rdfs:label ?value." +
           " { {?resource rdf:type rdfs:Class.} UNION { ?resource rdf:type owl:Class.} }" +
	        " FILTER (regex(?value, \""+ firstWord +"\", \"i\"))} ";       
       
    	String conditionBody  = ""; 
       
	      	for(int i=1; i< queryWords.size(); i++ ){
	      		conditionBody = conditionBody + " { ?resource"+i+" rdfs:label ?value"+i+"." +
	    			  " { {?resource"+i+" rdf:type rdfs:Class.} UNION { ?resource"+i+" rdf:type owl:Class.} }" +
	    			  " FILTER (regex(?value"+i+", \""+ queryWords.get(i) +"\", \"i\"))} ";  
	      	}
	      	
	      	String conditionEnd =  " }}" ;
	      	
	      	String sparql = prefixes+constructStart+constructBody+constructEnd+conditionStart+conditionBody+conditionEnd;

//       logger.info("GET query prepared");
//       logger.info(sparql);
       	
       /**
        * Execution of SPARQL query.
        *  
        * */
       
       try {
    	   QuadStore store = QuadStore.getDefaultStore();
           result = store.execConstruct(sparql);
           
           /**
            * Node Iterator contains OntologiesURIs or OntologyID that contains all query keywords.
            */
           NodeIterator graphs = result.listObjectsOfProperty(graph_property) ;
         
           /**
            * Process Node Iterator to access graph Ids sequentially.
            * */
           while (graphs.hasNext()){
        	  
        	   //graph Id
        	   RDFNode graphId = graphs.nextNode();
//        	   logger.info(graphId.toString());
               
        	   /**
                * Get matched resources for each graph in the Node Iterator
                * */
        	   ResIterator uris = result.listSubjectsWithProperty(graph_property,graphId);
        	   
        	   
        	   /**
                * Process Resource Iterator to access resource Ids sequentially.
                * */
        	   HashMap<String, ArrayList<String>> matchedResources  =new HashMap<String, ArrayList<String>>();
        	   
        	   while (uris.hasNext()){
        		   
        		   //matched resource Id
        		   Resource resource = uris.nextResource();
//        		   System.out.println(resource.toString());
        		   
        		   /**
                    * Get all labels for a resource
                    * */
        		   NodeIterator labelList = result.listObjectsOfProperty(resource, label_property);
        		   
        		   //list to store labels of a resource
        		   ArrayList<String> labels = new ArrayList<String>();
        		   
        		   while (labelList.hasNext()){
        			   String label = labelList.nextNode().toString();
        			   labels.add(label);
        		   }        		   
        		   matchedResources.put(resource.toString(), labels);
        	   }
        	   
        	   results.put(graphId.toString(), matchedResources);
           }
           
                      
       } catch(Exception exp) {
       	logger.info("Exception in getSearchResults "+exp);
       }
       return results;
   }
    
    public HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> selectOntologies(List<String> queryWords){

    	/**
    	 * Writing SPARQL query dynamically based on the number of keywords in the query.
    	 * */
    	HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> queryMatches = new HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>();
    	
    	for (String q: queryWords) {
    		
    		HashMap<String, HashMap<String, ArrayList<String>>> qMatches = new HashMap<String, HashMap<String, ArrayList<String>>>();
    		
    		String sparql =  " PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"
    		        + " PREFIX owl:<http://www.w3.org/2002/07/owl#>"
    		        + " PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
    		        + " CONSTRUCT { ?resource rdfs:graph ?ontology. ?resource rdfs:label ?label. }"
    				+ " Where { "
    				+ " GRAPH ?ontology { "
    				+ " { {?resource rdf:type rdfs:Class.} UNION { ?resource rdf:type owl:Class.} }" 
    				+ " ?resource rdfs:label ?label." 
    				+ " FILTER (regex(?label, \""+ q +"\", \"i\")) "
    				+ "}}";   
    		
//    		logger.info("GET query prepared");
//    	    logger.info(sparql);
    	    try {
    	    	   QuadStore store = QuadStore.getDefaultStore();
    	           Model result = store.execConstruct(sparql);
    	          
    	           Property graph_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#graph");
    	    	   Property label_property = result.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
    	           
    	    	   /**
    	            * Node Iterator contains OntologiesURIs or OntologyID that contains all query keywords.
    	            */
    	           NodeIterator graphs = result.listObjectsOfProperty(graph_property) ;
    	           
    	           /**
    	            * Process Node Iterator to access graph Ids sequentially.
    	            * */
    	           
    	           
    	           while (graphs.hasNext()){
    	        	   
    	        	   
            		   //graph Id
    	        	   RDFNode graphId = graphs.nextNode();
    	        	   
    	        	   //logger.info(graphId.toString());
    	               
    	        	   /**
    	                * Get matched resources for each graph in the Node Iterator
    	                * */
    	        	   ResIterator uris = result.listSubjectsWithProperty(graph_property,graphId);
    	        	   
    	        	   
    	        	   /**
    	                * Process Resource Iterator to access resource Ids sequentially.
    	                * */
    	        //	   HashMap<String, String> rMatches = new HashMap<String, String>();
    	        	   HashMap<String, ArrayList<String>> matchedResources  =new HashMap<String, ArrayList<String>>();
    	        	   
    	        	   while (uris.hasNext()){

    	        		   //matched resource Id
    	        		   Resource resource = uris.nextResource();
    	        		   //System.out.println(resource.toString());

    	        		   /**
    	                    * Get all labels for a resource
    	                    * */
    	        		   NodeIterator labelList = result.listObjectsOfProperty(resource, label_property);
    	        		   
    	        		   //list to store labels of a resource
    	        		   ArrayList<String> labels = new ArrayList<String>();
    	        		   String label ="";
    	        		   while (labelList.hasNext()){
    	        			   label = labelList.nextNode().toString();
    	        			   
    	        			   if(label.contains("_")) {
    	        				   label = label.replace("_", " ");
    	        			   }    	        			   
    	        			   
    	        			   if(label.contains("^^")) {
    	        				   label = label.split("\\^")[0];
        	        			   if(containsCaseInsensitive(label,labels)) {} else labels.add(label);
    	        			   } else if (label.contains("@")) {
    	        				   if(label.endsWith("@en") || label.endsWith("@en-us")){
    	        					   label = label.split("@")[0]; 
    	        					   if(containsCaseInsensitive(label,labels)) {} else labels.add(label);
    	        				   } else {
    
    	        				   }
    	        			   } else {     			      			   
    	        				   if(containsCaseInsensitive(label,labels)) {} else labels.add(label);  }
    	        		   } 
	        			   if(labels.size()==0){
        				   labels.add(label);
        			   }
    	        		   matchedResources.put(resource.toString(), labels);
    	        	   }
    	        	   
    	        	   qMatches.put(graphId.toString(), matchedResources);
    	           }
    	        	   
    	          
    	       } catch(Exception exp) {
    	       	logger.info("Exception in getSearchResults "+exp);
    	       }
    	      // return result;
    	
    	    queryMatches.put(q, qMatches);
    	} 
    	return queryMatches;
   }
    
    public boolean containsCaseInsensitive(String strToCompare, ArrayList<String>list)
    {
        for(String str:list)
        {
            if(str.equalsIgnoreCase(strToCompare))
            {
                return(true);
            }
        }
        return(false);
    }
 
}
