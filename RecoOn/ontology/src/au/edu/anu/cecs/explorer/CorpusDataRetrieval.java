/*
 /*
 * Copyright (c) 2015, ANU and/or its constituents or affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package au.edu.anu.cecs.explorer;

import java.util.ArrayList;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;

public class CorpusDataRetrieval extends LoggerService{

	public CorpusDataRetrieval() {
		super(CorpusDataRetrieval.class.getName());
	}
	
	
	public ArrayList<String> getExistingLoadedOntology(){

		QuadStore store = QuadStore.getDefaultStore();
		ArrayList<String> list = new ArrayList<String>();
		 
		String sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
	            "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
	            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+ 
	            
	            "CONSTRUCT { ?graph rdfs:contains ?graph }"
				+ " WHERE {GRAPH ?graph {?subject ?property ?object}} ORDER BY (?graph)";
		
		//logger.info("Prepared SPARQL query successfully");
        //logger.info(sparql);
     	

		try { 
			Model model = store.execConstruct(sparql);
		    NodeIterator graph_uri = model.listObjects(); 
 	   
		    while(graph_uri.hasNext()){
		    	String graphs = graph_uri.nextNode().toString();
		    	list.add(graphs.toString());   		   
 		    }
		}catch(Exception exp) {
			 logger.info("Exception in getSearchResults "+exp);
		}
			return list;	         
	 }
	
	
	/**
	 * This method takes ontology Id as a parameter and returns list of concepts defined in this ontology.
	 * @param graphIRI: URI of the ontology
	 * @return list of concepts 
	 */
	
	public ArrayList<String> getClassList(String graphIRI){
  		
		QuadStore store = QuadStore.getDefaultStore();
  		ArrayList<String> classList = new ArrayList<String>();
  		
  		String sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
	            "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
	            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
  						
	            " CONSTRUCT {<"+graphIRI+"> owl:Class ?class}"
				+ " FROM <"+graphIRI+"> "
				+ " WHERE { {?class rdf:type rdfs:Class} UNION {?class rdf:type owl:Class.} UNION {?property rdfs:domain ?class} UNION {?property rdfs:range ?class}"
				+ " FILTER (!(isBlank(?class)) && (?class != owl:Thing))}";
		
  		//logger.info("Prepared SPARQL query successfully");
       // logger.info(sparql);
		
        try { 
			Model model = store.execConstruct(sparql);
		    NodeIterator graph_uri = model.listObjects(); 
 	   
		    while(graph_uri.hasNext()){
		    	String graphs = graph_uri.nextNode().toString();
		    	classList.add(graphs.toString());   		   
 		    }
		}catch(Exception exp) {
			 logger.info("Exception in getSearchResults "+exp);
		}
			return classList;	        
  	}
	
	public ArrayList<String> getObjectPropertyList(String graphIRI){
  		
		QuadStore store = QuadStore.getDefaultStore();
  		ArrayList<String> propList = new ArrayList<String>();
  		
  		String sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
	            "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
	            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
  						
	            " CONSTRUCT {<"+graphIRI+"> owl:Property ?property}"
				+ " FROM <"+graphIRI+"> "
				+ " WHERE { ?property rdf:type owl:ObjectProperty."
				+ " }";
		
  	//	logger.info("Prepared SPARQL query successfully");
     //   logger.info(sparql);
		
        try { 
			Model model = store.execConstruct(sparql);
		    NodeIterator property_uri = model.listObjects(); 
 	   
		    while(property_uri.hasNext()){
		    	String property = property_uri.nextNode().toString();
		    	propList.add(property.toString());   		   
 		    }
		}catch(Exception exp) {
			 logger.info("Exception in getSearchResults "+exp);
		}
			return propList;	        
  	}
	
	public ArrayList<String> getPropertyList(String graphIRI){
  		
		QuadStore store = QuadStore.getDefaultStore();
  		ArrayList<String> propList = new ArrayList<String>();
  		
  		String sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
	            "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
	            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
  						
	            " CONSTRUCT {<"+graphIRI+"> owl:Property ?property}"
				+ " FROM <"+graphIRI+"> "
				+ " WHERE { {?property rdf:type owl:ObjectProperty.} "
							+ "UNION {?property rdf:type owl:ObjectProperty.} "
							+ "UNION {?property rdf:type rdf:Property}"
				+ " }";
		
  		//logger.info("Prepared SPARQL query successfully");
       // logger.info(sparql);
		
        try { 
			Model model = store.execConstruct(sparql);
		    NodeIterator property_uri = model.listObjects(); 
 	   
		    while(property_uri.hasNext()){
		    	String property = property_uri.nextNode().toString();
		    	propList.add(property.toString());   		   
 		    }
		}catch(Exception exp) {
			 logger.info("Exception in getSearchResults "+exp);
		}
			return propList;	        
  	}
	
	
	public ArrayList<String> getDataPropertyList(String graphIRI){
  		
		QuadStore store = QuadStore.getDefaultStore();
  		ArrayList<String> propList = new ArrayList<String>();
  		
  		String sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
	            "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
	            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
  						
	            " CONSTRUCT {<"+graphIRI+"> owl:Property ?property}"
				+ " FROM <"+graphIRI+"> "
				+ " WHERE { ?property rdf:type owl:DatatypeProperty"
				+ " }";
		
//  		logger.info("Prepared SPARQL query successfully");
//        logger.info(sparql);
		
        try { 
			Model model = store.execConstruct(sparql);
		    NodeIterator property_uri = model.listObjects(); 
 	   
		    while(property_uri.hasNext()){
		    	String property = property_uri.nextNode().toString();
		    	propList.add(property.toString());   		   
 		    }
		}catch(Exception exp) {
			 logger.info("Exception in getSearchResults "+exp);
		}
			return propList;	        
  	}
}
