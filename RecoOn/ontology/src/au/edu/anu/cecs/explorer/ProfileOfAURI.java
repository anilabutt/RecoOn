/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package au.edu.anu.cecs.explorer;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.HashMap;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

/**
 *
 * @author anila
 */
public class ProfileOfAURI extends LoggerService {

    /**
	 * Default constructor to initializes logging by its parent.
	 */
	
    QuadStore store = QuadStore.getDefaultStore();
    VirtGraph connection = store.getConnection();
    
	public ProfileOfAURI() {
		super(ProfileOfAURI.class.getName());
	}

        public ArrayList<Relation> getProfile(String uri, String graph) {
        	ArrayList<Relation> arrayList = new ArrayList<Relation>();
        	Model model= ModelFactory.createDefaultModel();
            try {
                    String sparql = getProfileQuery(uri, graph);
                  //  System.out.println(sparql);
                    QuadStore store = QuadStore.getDefaultStore();
                    model = store.execConstruct(sparql);

                   // System.out.println(" Model size is : " + model.size());
                    
                    Property props = model.getProperty("http://au.csiro.browser#property");
                    Property label = model.getProperty("http://www.w3.org/2000/01/rdf-schema#label");
                    Property range = model.getProperty("http://www.w3.org/2000/01/rdf-schema#range");

                    NodeIterator nIt = model.listObjectsOfProperty(props);
                    
                  	while(nIt.hasNext()){
                    	Relation mb = new Relation();
                        String propertyIRI = "";
                        String propertyLabel="";
                        String rangeIRI="";
                        String rangeLabel="";
                  		
                 	 	RDFNode rs = nIt.nextNode();
                 	 	propertyIRI = rs.toString();
                  		
                 	 	Resource property = model.getResource(propertyIRI);

                  		//getLabel of property
                  		NodeIterator nIt2 = model.listObjectsOfProperty(property,label);
                  		if(nIt2.hasNext()){
                  			propertyLabel = nIt2.nextNode().toString();}
                  		
                  		//getObject Value i.e. range
                     	NodeIterator rangeIt = model.listObjectsOfProperty(property,range);
    					
                     	if(rangeIt.hasNext()){
                     		rangeIRI = rangeIt.nextNode().toString();}
    					
    					Resource rangeuri = model.getResource(rangeIRI);
    					NodeIterator rangeLabelIterator = model.listObjectsOfProperty(rangeuri,label);
    					
                     	if(rangeLabelIterator.hasNext()){
                     		rangeLabel = rangeLabelIterator.nextNode().toString();}
                     	
    					//System.out.println(propertyIRI + "	" + propertyLabel +"	" + rangeIRI +"		"+ rangeLabel );
    					
    					mb.setProperty(propertyIRI);
    					mb.setPropertyLabel(propertyLabel);
    					mb.setValue(rangeIRI);
    					mb.setValueLabel(rangeLabel);
                        arrayList.add(mb);
                  	}
  

		}catch(Exception e){
			logger.info("Cant Return Dimensions : "+e);
			//return "" + e;
		}
           // System.out.println(arrayList);
              return arrayList;
        }
        
        public HashMap<String, HashMap<String, String>> getPropertyProfile(String property, String graph) {
        	
        	HashMap<String, HashMap<String, String>> propertyProfileMap = new HashMap<String, HashMap<String, String>>();
        	Model model= ModelFactory.createDefaultModel();
        	Resource propertyURI = model.createResource(property);
            
        	try {
                    String sparql = this.getProfileOfProperty(property, graph);
                    QuadStore store = QuadStore.getDefaultStore();
                    model = store.execConstruct(sparql);
                 
                    Property domain = model.getProperty("http://www.w3.org/2000/01/rdf-schema#domain");
                    Property range = model.getProperty("http://www.w3.org/2000/01/rdf-schema#range");
                    Property label = model.getProperty("http://www.w3.org/2000/01/rdf-schema#label");

                    NodeIterator domainIt = model.listObjectsOfProperty(propertyURI ,domain);
                    HashMap<String, String> domains = new HashMap<String, String>();
                    
                  	while(domainIt.hasNext()) {
                    	
                  		String domainId = "";
                        String domainLabel="";
                  		
                 	 	RDFNode rs = domainIt.nextNode();
                 	 	domainId = rs.toString();
                  		
                 	 	Resource domainRes = model.createResource(domainId);
                 	 	
                  		//getLabel of property
                  		NodeIterator nIt2 = model.listObjectsOfProperty(domainRes,label);
                  		
                  		if(nIt2.hasNext()){
                  			domainLabel = nIt2.nextNode().toString();
                  		}
                  			
//                  		System.out.println( property + "	" + domainRes.toString() + "	" +domainLabel);
                  		domains.put(domainId, domainLabel);
                  	}	
                  	
                  	propertyProfileMap.put("domain",domains);
                  		
                  	//getObject Value i.e. range
                    NodeIterator rangeIt = model.listObjectsOfProperty(propertyURI, range);
                    
                    HashMap<String, String> ranges = new HashMap<String, String>();
    					
                    while(rangeIt.hasNext()){

                    	String rangeIRI="";
                        String rangeLabel="";
                      
                        // rangeIRI = rangeIt.nextNode().toString();
                        RDFNode rs = rangeIt.nextNode();
                 	 	rangeIRI = rs.toString();
                  		
                 	 	Resource rangeRes = model.createResource(rangeIRI);
                 	 	
                  		//getLabel of property
                  		NodeIterator nIt2 = model.listObjectsOfProperty(rangeRes,label);
                  		
                  		if(nIt2.hasNext()){
                  			rangeLabel = nIt2.nextNode().toString();
                  		}
                  		ranges.put(rangeIRI, rangeLabel);
                    }
                    
                    propertyProfileMap.put("range",ranges);

		}catch(Exception e){
			logger.info("Cant Return Dimensions : "+e);
			//return "" + e;
		}
           // System.out.println(arrayList);
              return propertyProfileMap;
        }
        

        public ArrayList<String> getProfileOfClass(String uri, String graph) {
        	ArrayList<String> arrayList = new ArrayList<String>();
        	Model model= ModelFactory.createDefaultModel();


            try {
                    String sparql = getProfileQuery(uri, graph);
                    System.out.println(sparql);
                    QuadStore store = QuadStore.getDefaultStore();
                    model = store.execConstruct(sparql);

//                    System.out.println(" Model size is : " + model.size());
                    
                    Property props = model.getProperty("http://au.csiro.browser#property");
                    Property label = model.getProperty("http://www.w3.org/2000/01/rdf-schema#label");
                    Property range = model.getProperty("http://www.w3.org/2000/01/rdf-schema#range");

                    NodeIterator nIt = model.listObjectsOfProperty(props);
                    
                  	while(nIt.hasNext()){
                    	
                        String propertyIRI = "";
                        String propertyLabel="";
                        String rangeIRI="";
                        String rangeLabel="";
                  		
                 	 	RDFNode rs = nIt.nextNode();
                 	 	propertyIRI = rs.toString();
                  		
                 	 	Resource property = model.getResource(propertyIRI);

                  		//getLabel of property
                  		NodeIterator nIt2 = model.listObjectsOfProperty(property,label);
                  		if(nIt2.hasNext()){
                  			propertyLabel = nIt2.nextNode().toString();}
                  		if (propertyLabel.contains("^")){
                        	propertyLabel  = propertyLabel.split("\\^")[0];
                        } if (propertyLabel.contains("@")){
                        	propertyLabel = propertyLabel.split("@")[0];
                        } if (propertyLabel.equalsIgnoreCase("")){
                        	propertyLabel = propertyIRI;
                        } 
                  		//getObject Value i.e. range
                     	NodeIterator rangeIt = model.listObjectsOfProperty(property,range);
    					
                     	if(rangeIt.hasNext()){
                     		rangeIRI = rangeIt.nextNode().toString();}
    					
    					Resource rangeuri = model.getResource(rangeIRI);
    					NodeIterator rangeLabelIterator = model.listObjectsOfProperty(rangeuri,label);
    					
                     	if(rangeLabelIterator.hasNext()){
                     		rangeLabel = rangeLabelIterator.nextNode().toString();}
                     	
//    					System.out.println(propertyIRI + "	" + propertyLabel +"	" + rangeIRI +"		"+ rangeLabel );

                        arrayList.add(propertyIRI + "|" + propertyLabel);
                  	}
  

		}catch(Exception e){
			logger.info("Cant Return Dimensions : "+e);
			//return "" + e;
		}
            System.out.println(arrayList);
              return arrayList;
        }

        
        public HashMap<String,String> getLabelComment(String uri, String graph){
        	HashMap<String,String> descrption = new HashMap<String,String>();
        	String sparql ="PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
    	            "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
    	            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
    	            "CONSTRUCT { <"+uri+"> rdfs:label ?label."
    	            		+ " <"+uri+"> rdfs:comment ?comment.}"
    	            		+ "FROM <"+graph+">"
    	            		+ "WHERE{ <"+uri+"> rdfs:label ?label."
    	            	//	+ "FILTER(langMatches(lang(?label), \"EN\"))"
    	            		+ "OPTIONAL { {<"+uri+"> rdfs:comment ?comment.} UNION {<"+uri+"> rdfs:description ?comment.}}"
    	            		+ "} " ;
        
        	System.out.println(sparql);
        	try{
        	 QuadStore store = QuadStore.getDefaultStore();
             Model model = store.execConstruct(sparql);
             
             Property label = model.getProperty("http://www.w3.org/2000/01/rdf-schema#label");
             Property comment = model.getProperty("http://www.w3.org/2000/01/rdf-schema#comment");

             NodeIterator nIt = model.listObjectsOfProperty(label);
             
             String classLabel = "";
           	while(nIt.hasNext()){
                 classLabel = nIt.nextNode().toString();
                 if(classLabel.contains("@en")){
                	 break;
                 }
            }

            NodeIterator nIt2 = model.listObjectsOfProperty(comment);
           
            String classComments = "";
            while(nIt2.hasNext()){
                classComments = nIt2.nextNode().toString();
            }
           
            if (classLabel.contains("^")) {
      			String[] conlabels = classLabel.split("\\^");
      			classLabel= conlabels[0];
      		} 
            
            if(classLabel.contains("@")) {
      			String[] conlabels = classLabel.split("@");
      				classLabel= conlabels[conlabels.length-2];
      		}  else if (classLabel.contains("#")) {
      			String[] conlabels = classLabel.split("#");
      			classLabel= conlabels[conlabels.length-1];
      			
      		} else {}
            
            descrption.put("label", classLabel);
            descrption.put("comment", classComments);
            
            
           	}catch(Exception e){
           		System.out.println(e);
           	}

        	return descrption;
        }

  

        public String getProfileQuery(String uri, String graph){

        String sparql ="PREFIX csiro:<http://au.csiro.browser#>" +
            "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
            "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
          
            "CONSTRUCT {<"+uri+"> csiro:property ?property. " +
            "?property rdfs:label ?propLabel." +
            "?property rdfs:range ?range. " +
            "?range rdfs:label ?rangeLabel.} "+
 //          "WHERE {{{<http://xmlns.com/foaf/0.1/Agent> rdfs:subClassOf ?res. ?res owl:onProperty ?property.} UNION {{<http://xmlns.com/foaf/0.1/Agent> rdfs:subClassOf ?class. ?property rdfs:domain ?class.} UNION {?property rdfs:domain <http://xmlns.com/foaf/0.1/Agent>}} } {{?res ?resProp ?range.FILTER ((?resProp = owl:allValuesFrom) || (?resProp = owl:someValuesFrom) )} UNION {?property rdfs:range ?range.}} OPTIONAL {?property rdfs:label ?propLabel.} Optional {?range rdfs:label ?rangeLabel.}}";
             
  			"FROM <"+graph+"> "
  			+ "WHERE {"
            	+ "{"
            		+ "{<"+uri+"> rdfs:subClassOf ?res. "
            		+ "?res owl:onProperty ?property.} "
            		+ "UNION "
            		+ "{?property rdfs:domain <"+uri+">}"
            	+ "}"
            	+ "OPTIONAL{"
            		+ "{?res ?resProp ?range."
            		+ "FILTER ((?resProp = owl:allValuesFrom) || (?resProp = owl:someValuesFrom) || (?resProp = owl:cardinality) || (?resProp = owl:minCardinality) || (?resProp = owl:maxCardinality))} "
            		+ "UNION "
            		+ "{?property rdfs:range ?range.}"
            	+ "}"
            	+ "OPTIONAL {?property rdfs:label ?propLabel.}"
            	+ "Optional {?range rdfs:label ?rangeLabel.}"
            	+ "}";

         // logger.info("Prepared SPARQL query successfully");
         logger.info(sparql);
          return sparql;
        }

        public String getProfileOfProperty(String uri, String graph){

        String sparql ="PREFIX csiro:<http://au.csiro.browser#>" +
            "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
            "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
            "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
          
            "CONSTRUCT {<"+uri+"> rdfs:domain ?domain. " +
            "?domain rdfs:label ?domainLabel." +
            "<"+uri+"> rdfs:range ?range. " +
            "?range rdfs:label ?rangeLabel.} "+
 //          "WHERE {{{<http://xmlns.com/foaf/0.1/Agent> rdfs:subClassOf ?res. ?res owl:onProperty ?property.} UNION {{<http://xmlns.com/foaf/0.1/Agent> rdfs:subClassOf ?class. ?property rdfs:domain ?class.} UNION {?property rdfs:domain <http://xmlns.com/foaf/0.1/Agent>}} } {{?res ?resProp ?range.FILTER ((?resProp = owl:allValuesFrom) || (?resProp = owl:someValuesFrom) )} UNION {?property rdfs:range ?range.}} OPTIONAL {?property rdfs:label ?propLabel.} Optional {?range rdfs:label ?rangeLabel.}}";
             
  			"FROM <"+graph+"> "
  			+ "WHERE {"
            	+ "{"
            		+ "{?domain rdfs:subClassOf ?res. "
            		+ "?res owl:onProperty <"+uri+">.} "
            		+ "UNION "
            		+ "{<"+uri+"> rdfs:domain ?domain}"
            	+ "}"
            	+ "OPTIONAL{"
            		+ "{?res ?resProp ?range."
            		+ "FILTER ((?resProp = owl:allValuesFrom) || (?resProp = owl:someValuesFrom) || (?resProp = owl:cardinality) || (?resProp = owl:minCardinality) || (?resProp = owl:maxCardinality))} "
            		+ "UNION "
            		+ "{<"+uri+"> rdfs:range ?range.}"
            	+ "}"
            	+ "OPTIONAL {?domain rdfs:label ?domainLabel.}"
            	+ "Optional {?range rdfs:label ?rangeLabel.}"
            	+ "}";

         // logger.info("Prepared SPARQL query successfully");
         logger.info(sparql);
          return sparql;
        }

  

      	public ArrayList<String> getSuperClassList(String concept, String graph){
          		//int length = 0 ;
          		
          		ArrayList<String> superClassList = new ArrayList<String>();
          		
          		System.out.println("**************** SuperClass for concept " + concept + " **************** ");
          		
          		String sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"
          				+ "SELECT DISTINCT ?object ?label "
          			//	+ "FROM <"+graph+"> "
          				+ "WHERE {GRAPH ?g {"
          				+ "{<"+concept+"> rdfs:subClassOf+ ?object."
          				+ " ?object rdf:type ?type."
          				+ "OPTIONAL {?object rdfs:label ?label."
          				+ "FILTER(langMatches(lang(?label), \"EN\"))} "
          				+ "FILTER ( (?type=owl:Class) || (?type = rdfs:Class) )"
          				+ "FILTER (!(isBlank(?object)))}}}";
        		
          		System.out.println(sparql);
          		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, connection);
       		
        		try {
        			
        			ResultSet results = vqe.execSelect();
        			int count =0;

        			while (results.hasNext()) {
        				QuerySolution result = results.nextSolution();
        				String uri = result.get("object").toString();
        				String label="";
        				if (result.contains("label")) {
        					label = result.getLiteral("label").getLexicalForm();
        				}
        				if (label.equalsIgnoreCase("")){
        					label = uri;
        				}
        				superClassList.add(label);
        			}

        		} catch(Exception e){
        			System.out.println(e);
        		} 	finally {
        		
        		}	
        		
          		return superClassList;
          	}
          	
          	public ArrayList<String> getSubClassList(String concept, String graph){
          		
          		ArrayList<String> subClassList = new ArrayList<String>();
          		
          		System.out.println("**************** Sub for concept " + concept + " **************** ");
          		
          		String sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"
          				+ "SELECT DISTINCT ?object ?label "
          				//+ "FROM <"+graph+">"
          				+ " WHERE { GRAPH ?g {"
          				+ "{?object rdfs:subClassOf+ <"+concept+">."
          				+ " ?object rdf:type ?type."
          				+ "OPTIONAL {?object rdfs:label ?label. "
          				+ "FILTER(langMatches(lang(?label), \"EN\"))"
          				+ "} "
          				+ "FILTER ( (?type=owl:Class) || (?type = rdfs:Class) )"
          				+ "FILTER (!(isBlank(?object)))}} }";
        		
          		System.out.println(sparql);
          		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, connection);
       		
        		try {
        			
        			ResultSet results = vqe.execSelect();
        			int count =0;

        			while (results.hasNext()) {
        				QuerySolution result = results.nextSolution();
        				String uri = result.get("object").toString();
        				String label="";
        				if (result.contains("label")) {
        					label = result.getLiteral("label").getLexicalForm();
        				}
        				if (label.equalsIgnoreCase("")){
        					label = uri;
        				}
        				subClassList.add(label);
        			}
        		} catch(Exception e){
        			System.out.println(e);
        		} 		
          		return subClassList;
          	}
          	
          	
          	public ArrayList<String> getSuperClasses(String concept, String graph){
          		//int length = 0 ;
          		
          		ArrayList<String> superClassList = new ArrayList<String>();
          		
          	//	System.out.println("**************** SuperClass for concept " + concept + " **************** ");
          		
          		String sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"
          				+ "SELECT DISTINCT ?object "
          				//+ "FROM <"+graph+"> "
          				+ "WHERE {GRAPH ?g "
          				+ "{<"+concept+"> rdfs:subClassOf ?object."
          				+ "FILTER (!(isBlank(?object)))"
          				+ " ?object rdf:type ?type."
          				+ "FILTER ( (?type=owl:Class) || (?type = rdfs:Class) )"
          				+ "} }";
        		
          		//System.out.println(sparql);
          		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, connection);
       		
        		try {
        			
        			ResultSet results = vqe.execSelect();
        			int count =0;

        			while (results.hasNext()) {
        				QuerySolution result = results.nextSolution();
        				String uri = result.get("object").toString();
        				System.out.println(uri);
        				superClassList.add(uri);
        			}

        		} catch(Exception e){
        			System.out.println(e);
        		} 	finally {
        		
        		}	
        		
          		return superClassList;
          	}
          	
          	public ArrayList<String> getSubClasses(String concept, String graph){
          		
          		ArrayList<String> subClassList = new ArrayList<String>();
          		
          	//	System.out.println("**************** Sub for concept " + concept + " **************** ");
          		
          		String sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"
          				+ "SELECT DISTINCT ?object "
          			//	+ "FROM <"+graph+">"
          				+ "WHERE {GRAPH ?g "
          				+ "{?object rdfs:subClassOf <"+concept+">."
          				+ "FILTER (!(isBlank(?object)))"
         				+ " ?object rdf:type ?type."
          				+ "FILTER ( (?type=owl:Class) || (?type = rdfs:Class) )"
          				+ "} }";
        		
          		//System.out.println(sparql);
          		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, connection);
       		
        		try {
        			
        			ResultSet results = vqe.execSelect();
        			int count =0;

        			while (results.hasNext()) {
        				QuerySolution result = results.nextSolution();
        				String uri = result.get("object").toString();
        				subClassList.add(uri);
        			}
        		} catch(Exception e){
        			System.out.println(e);
        		} 		
          		return subClassList;
          	}
          	
          	
       	public boolean getRelationAmongClasses(String con1, String con2, String graph){
       			
          		String sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
                      "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
                      "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
          				+ "ASK "
          				+ "FROM <"+graph+"> { "
          				+ "{ {?s1 rdfs:subClassOf ?s2.} UNION "
          				+ "{?property rdfs:domain ?s1. ?property rdfs:range ?s2} UNION "
          				+ "{?restriction rdfs:subClassOf ?s1. "
          				+ "?restriction owl:onProperty ?property. "
          				+ "?restriction ?resProp ?s2."
          				+ " FILTER ((?resProp = owl:allValuesFrom) || "
          					+ "(?resProp = owl:someValuesFrom))}"
          				+ "}"
          				+ "FILTER(?s1!=?s2). "
          				+ "FILTER ((?s1 = <"+con1+">) || (?s1 = <"+con2+">)) "
          				+ "FILTER ((?s2 = <"+con2+">) || (?s2 = <"+con1+">))"
          				+ "}";
        		
          		QuadStore store = QuadStore.getDefaultStore();
          		
          		System.out.println(sparql);
          		
          		boolean found = store.execBooleanQuery(sparql);
          		
          		System.out.println(" For :" +con1 + " : " + con2 + " connections is  : " + found);
          		return found;
          	}
       	
       	
      	public ArrayList<String> getAllRelationOfClasses(String con1, String graph){
   			
      	//	System.out.println( " ***** CLASS **** " + con1);
      		ArrayList<String> connectedClasses = new ArrayList<String>();
      		
      		String sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
                  "PREFIX owl:<http://www.w3.org/2002/07/owl#>"+
                  "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
      				+ "CONSTRUCT {?s1 owl:connected ?s2}"
      				+ "FROM <"+graph+"> { "
      				+ "{ "
      					+ "{?s1 rdfs:subClassOf ?s2.} UNION "
      					+ "{?property rdfs:domain ?s1. ?property rdfs:range ?s2} UNION "
      					+ "{?restriction rdfs:subClassOf ?s1. "
      						+ "?restriction owl:onProperty ?property. "
      						+ "?restriction ?resProp ?s2."
      						+ " FILTER ((?resProp = owl:allValuesFrom) || "
      						+ "(?resProp = owl:someValuesFrom))"
      					+ "}"
      				+ "}"
      				+ "?s1 rdf:type ?class."
      				+ "?s2 rdf:type ?class."
      				+ "FILTER(?s1!=?s2). "
      				+ "FILTER ((?s1 = <"+con1+">) || (?s2 = <"+con1+">)) "
      				+ "FILTER ((?class = rdfs:Class) || (?class = owl:Class))"
      				+ "}";
    		
      		QuadStore store = QuadStore.getDefaultStore();
      		
      		//System.out.println(sparql);
      		
      		Model model = store.execConstruct(sparql);
      		
      		Property connect = model.getProperty("http://www.w3.org/2002/07/owl#connected");
      		Resource resource = model.createResource(con1);
      		
      		ResIterator resIt = model.listSubjectsWithProperty(connect, resource);
      		
      		while(resIt.hasNext()) {
      			Resource res = resIt.nextResource();
      			//System.out.println("as a subject : " + res.toString());
      			connectedClasses.add(res.toString());
      		}
      		
      		NodeIterator nodeIt = model.listObjectsOfProperty(resource, connect);
      		
      		while(nodeIt.hasNext()) {
      			RDFNode res = nodeIt.nextNode();
      			//System.out.println("as a object : " + res.toString());
      			connectedClasses.add(res.toString());
      		}

      		//System.out.println(connectedClasses );
      		return connectedClasses;
      	}
}
