/*
 * Copyright (c) 2015, ANU and/or its constituents or affiliates. All rights reserved.
 * Use is subject to license terms.
 */

package au.edu.anu.cecs.explorer;


import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtInfGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;




/**
 * RDF triple store interface for the API. Services can't instantiate this store, instead they
 * should use {@link #getDefaultStore} method to get its singleton.
 * 
 * @author anila butt
 */
public class QuadStore {

	/** Default instance of the triple store */
	private static QuadStore defaultStore;

	/** Default logger */
	private Logger logger;
	
	/** Sesame repository for the quad store*/
	private VirtGraph  metagraph = null;
	
	private VirtGraph connection = null;
	
	private String connectionURL = null;
	
	private String username = null;
	
	private String password = null;
	
	/** Sesame Repository connection */
	//private RepositoryConnection connection= null;
	/**
	 * Gets default triple store instance to avoid synchronization faults 
	 */
	public static QuadStore getDefaultStore() {
		if(defaultStore==null) {
			defaultStore = new QuadStore();
		}
		return defaultStore;
	}
	
	/**
	 * Initializes this triple store
	 */
	private QuadStore() {
//		logger = Logger.getLogger(getClass().getName());
	
//		connectionURL = "jdbc:virtuoso://" + Configuration.getProperty(Configuration.VIRTUOSO_INSTANCE) + ":" + Configuration.getProperty(Configuration.VIRTUOSO_PORT);
//		logger.info("Connection URL :" + connectionURL);
//		
//		username = Configuration.getProperty(Configuration.VIRTUOSO_USERNAME);
//		logger.info("VIRTUOSO USER :" + username);
//		
//		password = Configuration.getProperty(Configuration.VIRTUOSO_PASSWORD);
//		logger.info("VIRTUOSO PASSWORD :" + password);
	
		
		connection = new VirtGraph("jdbc:virtuoso://localhost:1111", "dba", "dba");

		//graph = new VirtGraph(context, connectionURL, username , password );
		
		//metagraph = new VirtGraph(metadata, connectionURL, username , password );
		
	//	connection = new VirtGraph(connectionURL, username , password );
		
//		logger.info("Connection establised . . . ");
		
		
	}
	
	public VirtGraph getConnection(){
		
		return connection;
	}
	
	public void insertMetaGraphData(List<Triple> triple) {
		//logger.info("Loading data from : " + strurl);
		try {
				metagraph.getBulkUpdateHandler().add(triple);
//	            logger.info("Triple loaded successfully");			
			} catch (Exception e) {
				logger.severe("Error[" + e + "]");
			}
        //logger.info("TDB triple store initialized");
	}
	
	
	public boolean loadGraph(String uri){
		boolean flag = false;
		String loadString  = "LOAD <"+uri+">  INTO <"+uri+"> ";
		try {
		VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(loadString, connection);
		vur.exec(); 
		flag = true;
		} catch (Exception e){
			
		}
		return flag;
	}
	
	
	/**
	 * Inserts RDF data into virtuoso QUAD store 
	 * 
	 * @param filepath Input file or directory path
	 * @return Number of triples inserted into this triple store 
	 */
	public void insert(File filepath) {
//		logger.info("Loading data from file: " + filepath);
		
		//Model model = new TDBModel();
		boolean directory = filepath.isDirectory();
//		
		if(directory) {	 
			File[] files = filepath.listFiles();
	        for(File file:files) {
	        	
	            try {
//	                logger.info(file.getAbsolutePath());
	                String[] fileNames = file.getAbsolutePath().split("_");
	                String file_name = "http://www.mowldata/"+fileNames[fileNames.length-1];
	                System.out.println(file_name);
	                loadGraph(file.getAbsolutePath());
//	                VirtGraph graph = new VirtGraph(file_name, connectionURL, username , password );
//	    			graph.read(file.getAbsolutePath(), OntMediaType.MIME_RDF_XML);
	              //  logger.info("File loaded successfully");
	            } catch(Exception e) {
	                logger.info("Cant load file: "+e);
	            }
	        }
		} else {
			VirtGraph graph = new VirtGraph(filepath.getAbsolutePath(), connectionURL, username , password );
			try {
//				logger.info(filepath.getAbsolutePath());	    			
				graph.read(filepath.getAbsolutePath(), OntMediaType.MIME_RDF_XML);
//	            logger.info("File loaded successfully");			
			} catch (Exception e) {
				logger.severe("Error[" + e + "]");
			}
		}
//        logger.info("TDB triple store initialized");
	}
	
	/**
	 * Inserts RDF data into virtuoso QUAD store from a url
	 * 
	 * @param filepath Input file or directory path
	 * @return Number of triples inserted into this triple store 
	 */
	public boolean insert(String strurl) {
		VirtGraph _graph = new VirtGraph(strurl, connectionURL, username , password );
		
//		logger.info("Loading data from : " + strurl);
		boolean flag = false;
		try {
			_graph.read(strurl, OntMediaType.MIME_RDF_XML);
				//graph.read(strurl, OntMediaType.MIME_RDF_XML);
				flag = true;
				if(flag == true) {
					
				} else {
					
				}
//	            logger.info("File loaded successfully");			
			} catch (Exception e) {
				flag = false;
				//logger.severe("Error[" + e + "]");
			} finally{
				_graph.close();
			}
//        logger.info("TDB triple store initialized");
		return flag;
	}

	/**
	 * Executes given Graph type query
	 *
	 * @param query The SPARQL construct or describe query
	 * @return The resulting graph as a string
	 * @throws MalformedQueryException 
	 * @throws RepositoryException 
	 */
   	public Model execConstruct(String query) {

//        Model queryModel = TDBFactory.createModel();
//        if(inf==true){
//        	ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, orthoModel);
//        	queryModel = ontModel;
//        }else{queryModel = orthoModel; }
   		
   		//Query sparql = QueryFactory.create(query);
   		//VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, connection);
   		
   		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (query, connection);
   		
   		Model _model = null;
//       logger.info("Query parsed successfully");
        try {
        	_model = vqe.execConstruct();
        } catch (Exception exp) {
        		logger.info("Can't process describe query because of "+exp);
        } finally {
        	vqe.close();
        	}
        return _model;
   	}
   	
   	public ResultSet execSelect(String query) {

   		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (query, connection);

//        logger.info("Query parsed successfully");
        
   		ResultSet results =null;

      try {
    	  results = vqe.execSelect();
//    	  logger.info("Result Set contains :" + results.getResultVars().toString() + results.getRowNumber());
      	  } catch (Exception exp) {
      		  logger.info("Can't process select query because of "+exp);
      	  } finally {
      		  vqe.close();
      	  }
      return results;
 	}
	
//	public GraphQueryResult execGraphQuery(String query) throws RepositoryException, MalformedQueryException {
//		
//		GraphQuery gQuery = connection.prepareGraphQuery(QueryLanguage.SPARQL, query);
//		logger.info("Parsed query successfully");
//		
//		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
//		RDFWriter writer = Rio.createWriter(RDFFormat.N3, byteout);
//		
//		//Graph graph = new GraphImpl();
//		String str = "";
//		GraphQueryResult qResult= null;
//		try {
//			qResult = gQuery.evaluate();
//			logger.info("Construct query returned  triples");
////		//	writer.startRDF();
////
////			for (int row = 0; qResult.hasNext(); row++) {
////				Statement pairs = qResult.next();
////				//writer.handleStatement(pairs);
////				model.add(pairs);
////			}
//			//writer.endRDF();
//			//str = byteout.toString();
//		}catch (Exception exp) {
//			logger.info("Can't process construct query because of "+exp);
//		} finally {
//			gQuery.clearBindings();
//		}
//		return qResult;
//	}
	
	/**
	 * Executes given Tuple type query
	 *
	 * @param query The SPARQL Select query
	 * @return The resulting tuples as a string
	 * @throws MalformedQueryException 
	 * @throws RepositoryException 
	 */
	
	
//	public String execTupleQuery(String query) throws RepositoryException, MalformedQueryException {
//		
//		TupleQuery tQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
//		logger.info("Parsed query successfully");
//		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
//		SPARQLResultsXMLWriter writer = new SPARQLResultsXMLWriter(byteout);
//		//RDFWriter writer = Rio.createWriter(RDFFormat.RDFXML, byteout);
//		String str = "";
//		try {
//			tQuery.evaluate(writer);
//			logger.info("Tuple query returned triples");			
//			str = byteout.toString();
//		}catch (Exception exp) {
//			logger.info("Can't process construct query because of "+exp);
//		} finally {
//			tQuery.clearBindings();
//		}
//		return str;
//	}
	
	/**
	 * Executes given boolean type query
	 *
	 * @param query The SPARQL ask query
	 * @return The resulting boolean
	 * @throws MalformedQueryException 
	 * @throws RepositoryException 
	 */
	
	public boolean execBooleanQuery(String query) {
			
   		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (query, connection);

//       logger.info("Query parsed successfully");
        
   		boolean results=false;

      try {
    	  results = vqe.execAsk();
//    	  logger.info("Result Set contains :" + results);
      	  } catch (Exception exp) {
      		  logger.info("Can't process ask query because of "+exp);
      	  } finally {
      		  vqe.close();
      	  }
		return results;
	}
}