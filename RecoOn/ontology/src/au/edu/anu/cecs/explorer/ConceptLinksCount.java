/*
 * Copyright (c) 2015, ANU and/or its constituents or affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package au.edu.anu.cecs.explorer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;


public class ConceptLinksCount extends LoggerService{

	public ConceptLinksCount() {
		super(ConceptLinksCount.class.getName());
	}

	
	public int countOutlinksOfConceptInOntology(String ontologyId, String uri){
		 	
		int count = 0;
		String sparql ="PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
	            "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
	            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+ 
	           
	            " CONSTRUCT  {<"+uri+"> rdfs:property ?property} "
	            + " FROM <"+ ontologyId+"> "
	            + " WHERE { "
	            + " {<"+uri+"> rdfs:subClassOf ?res. ?res owl:onProperty ?property} "
//	             + " {<"+uri+"> rdfs:subClassOf+ ?res. ?res owl:onProperty ?property} "
	            + " UNION {"
	          //  + "  {<"+uri+"> rdfs:subClassOf+ ?class. ?property rdfs:domain ?class.}  UNION "
	            + "{?property rdfs:domain <"+uri+">}}"
	            + " } ";

	          logger.info("Prepared SPARQL query successfully");
	          logger.info(sparql);
	       	
	       /**
	        * Execution of SPARQL query.
	        *  
	        * */
  	
	       try {
	    	  
	    	   QuadStore store = QuadStore.getDefaultStore();
	    	   Model model = store.execConstruct(sparql);
	    	   
	    	   NodeIterator class_uri = model.listObjects();        	   
	    	   count = class_uri.toList().size();
	    	   
//	    	   while(class_uri.hasNext()){
//	    		   String label = class_uri.nextNode().toString();
//    			   //System.out.println(label.toString());   		   
//	    		   
//	    	   }   
	    	   
	       } catch(Exception exp) {
	    	   logger.info("Exception in getSearchResults "+exp);
	       }
	       return count;
	   }

	
	public int countInlinksOfConceptInOntology(String ontologyId, String uri){
		 	
		int count = 0;
		String sparql ="PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
	            "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
	            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+ 
	           
	            " CONSTRUCT  {<"+uri+"> rdfs:property ?property} "
	            + "FROM <"+ontologyId+">"
	            + "	WHERE {"
	            + "{?someuri rdfs:subClassOf ?res. ?res owl:onProperty ?property. "
//	            + "{?someuri rdfs:subClassOf+ ?res. ?res owl:onProperty ?property. "
	            + "?res ?resProp <"+uri+">."
	            + "FILTER ((?resProp=owl:allValuesFrom)||(?resProp=owl:someValuesFrom)) } "
	            + "UNION "
	            + "{?property rdfs:range <"+uri+">.} "
//	            + "UNION "
//	            + "{<"+uri+"> rdfs:subClassOf+ ?class. ?property rdfs:range ?class.} "
	            + "} ";

		      logger.info("Prepared SPARQL query successfully");
	          logger.info(sparql);
	       	
	       /**
	        * Execution of SPARQL query.
	        *  
	        * */
  	
	       try {
	    	  
	    	   QuadStore store = QuadStore.getDefaultStore();
	    	   Model model = store.execConstruct(sparql);
	    	   
	    	   NodeIterator class_uri = model.listObjects();
        	   
	    	   count = class_uri.toList().size();
//	    	   while(class_uri.hasNext()){
//	    		   String label = class_uri.nextNode().toString();
//    			   //System.out.println(label.toString());   		   
//	    		   
//	    	   }       
	       } catch(Exception exp) {
	    	   logger.info("Exception in getSearchResults "+exp);
	       }
	       return count;
	   }
	

	public int countOutlinksOfConceptInCorpus(String uri){
		 	
		int count = 0;
		String sparql ="PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
	            "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
	            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+ 
	           
	            " CONSTRUCT  {<"+uri+"> rdfs:property ?property} "
	            + " WHERE { GRAPH ?graph { "
	            + " {<"+uri+"> rdfs:subClassOf ?res. ?res owl:onProperty ?property} "
//	                  + " {<"+uri+"> rdfs:subClassOf+ ?res. ?res owl:onProperty ?property} "
	            + " UNION "
//	            + " { {<"+uri+"> rdfs:subClassOf+ ?class. ?property rdfs:domain ?class.} "
//	            + " UNION "
	            + "{?property rdfs:domain <"+uri+">}"
//	            		+ "}"
	            + " } }";

	          logger.info("Prepared SPARQL query successfully");
	          logger.info(sparql);
	       	
	       /**
	        * Execution of SPARQL query.
	        *  
	        * */
  	
	       try {
	    	  
	    	   QuadStore store = QuadStore.getDefaultStore();
	    	   Model model = store.execConstruct(sparql);
	    	   
	    	   NodeIterator class_uri = model.listObjects();
        	   
	    	  count = class_uri.toList().size();
//	    	   while(class_uri.hasNext()){
//	    		   String label = class_uri.nextNode().toString();
//    			   //System.out.println(label.toString());   		   
//	    		   
//	    	   }
	                      
	       } catch(Exception exp) {
	       		logger.info("Exception in getSearchResults "+exp);
	       }
	       return count;
	   }


public int countInlinksOfConceptInCorpus(String uri){
	 	
	int count = 0;
	String sparql ="PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
            "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+ 
           
            " CONSTRUCT  {<"+uri+"> rdfs:property ?property} "
            + "	WHERE { GRAPH ?graph {"
            + "{?someuri rdfs:subClassOf ?res. ?res owl:onProperty ?property. "
//            + "{?someuri rdfs:subClassOf+ ?res. ?res owl:onProperty ?property. "
            + "?res ?resProp <"+uri+">."
            + "FILTER ((?resProp=owl:allValuesFrom)||(?resProp=owl:someValuesFrom)) } "
            + "UNION "
            + "{?property rdfs:range <"+uri+">.} "
//            + "UNION "
//            + "{<"+uri+"> rdfs:subClassOf+ ?class. ?property rdfs:range ?class.} "
            + "} }";

		 logger.info("Prepared SPARQL query successfully");
         logger.info(sparql);
       	
       /**
        * Execution of SPARQL query.
        *  
        * */
	
       try {
    	  
    	   QuadStore store = QuadStore.getDefaultStore();
    	   Model model = store.execConstruct(sparql);
    	   
    	   NodeIterator class_uri = model.listObjects(); 
    	   count = class_uri.toList().size();
    	   
//    	   while(class_uri.hasNext()){
//    		   String label = class_uri.nextNode().toString();
//			   //System.out.println(label.toString());   		   
//    		   
//    	   }       
       } catch(Exception exp) {
       		logger.info("Exception in getSearchResults "+exp);
       }
       return count;
   }
}
