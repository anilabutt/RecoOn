package au.edu.anu.cecs.explorer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ClassConnectivityPathAsHashMap {

	public static void main(String[] args)	{
		  
	//	getConnectionMatric("http://xmlns.com/foaf/0.1/");
//		createConnectionMatrix("http://xmlns.com/foaf/0.1/");
		createConnectionMatrix();


	}
	
	public static void getConnectionMatric(String ontology) {
		
		SerializedClassConnectivity serialize = new SerializedClassConnectivity();
		Vertex[] vertices = serialize.deSerializeData(ontology);
		
		for (Vertex v : vertices)
        {
			String name = v.name;
        	//System.out.println(" Edge with " + name + ": " );
        }
		
		
		Dijkstra dijkstra = new Dijkstra();
		
		
		Edge[] edges = vertices[0].adjacencies;
		//System.out.println("Calculate Distance for Class : " +  vertices[0].name +  "  has " + edges.length + "edges" );
		
		for (Edge e : edges)
	        {
				String target = e.target.name;
	        	//System.out.println(" Edge with " + e.target.name + ": " + e.weight);
	        }
//			
//		dijkstra.computePaths(vertices[0]);
//	        for (Vertex v : vertices)
//	        {
//	        	//System.out.println("Distance to " + v + ": " + v.minDistance);
//	        	List<Vertex> path = dijkstra.getShortestPathTo(v);
//	        	//System.out.println("Path: " + path);
//	        }
	}
	
	public static void createConnectionMatrix() {
		
		CorpusDataRetrieval retrieval = new CorpusDataRetrieval();
		ProfileOfAURI profileClass = new ProfileOfAURI();
		
		ClassConnectivityMap classConnectMap = ClassConnectivityMap.getDefaultMap();
	    
		double nopath = Double.POSITIVE_INFINITY;
			
		ArrayList<String> ontologies = retrieval.getExistingLoadedOntology();
	
		try {
		for(int count=0; count<ontologies.size(); count++) {

			String ontology = ontologies.get(count);
			//System.out.println( "OntologyId : " +count+ " OntologyIRI : " + ontology);
		
			/**
			 * Get all classes in an ontology
			 */
		
			ArrayList<String> classList = retrieval.getClassList(ontology);
			
			HashMap<String, HashMap<String, Double>> classConnectivityOfOntology = new 	HashMap<String, HashMap<String, Double>>();
			
			for(int classIndex = 0 ; classIndex< classList.size() ; classIndex++) {					
				
				String classIRI = classList.get(classIndex);
				HashMap<String, Double> classConnectivity = new	HashMap<String, Double>();
								
				ArrayList<String> connectedClasses = profileClass.getAllRelationOfClasses(classIRI, ontology);

				for(int conIdx=0; conIdx < connectedClasses.size() ; conIdx++) {
				
					String secondClass = connectedClasses.get(conIdx);					
				
					if(classList.contains(secondClass)) {
						classConnectivity.put(secondClass, 1.0); 
					}
				}
				
				classConnectivityOfOntology.put(classIRI, classConnectivity);
			}
			
			//System.out.println(ontology + " loaded ");
			classConnectMap.save_classConnectivity_Value(ontology, classConnectivityOfOntology);
		}
		} catch (Exception e) {
			
		} finally {
			classConnectMap.closeConnection();
		}
	}

	public static void createConnectionMatrix(String ontology) {
		
		double nopath = Double.POSITIVE_INFINITY;
		CorpusDataRetrieval retrieval = new CorpusDataRetrieval();
		ProfileOfAURI profileClass = new ProfileOfAURI();

		
			ArrayList<String> classList = retrieval.getClassList(ontology);
		
//			classList.add("http://www.w3.org/2002/07/owl#Thing");
			
			/**
			 * Process classList to get all classes
			 */

			
			//create vertex for each node
		
			Vertex[] nodes = new Vertex[classList.size()];
			
			for(int classIndex = 0 ; classIndex< classList.size() ; classIndex++) {
				Vertex v0 = new Vertex(classList.get(classIndex));	
				nodes[classIndex] = v0;
			}

			//System.out.println("vertex : " + nodes.length );
			
				
			for(int classIndex = 0 ; classIndex< classList.size() ; classIndex++) {					
				String classIRI = classList.get(classIndex);
				Vertex vertex = nodes[classIndex];
				
				ArrayList<String> connectedClasses = profileClass.getAllRelationOfClasses(classIRI, ontology);
				vertex.adjacencies = new Edge[connectedClasses.size()];
				
				//System.out.println( "connected classes for *** : " + classIRI);

				for(int conIdx=0; conIdx < connectedClasses.size() ; conIdx++) {
				
				String secondClass = connectedClasses.get(conIdx);					
				
				if(classList.contains(secondClass)) {
					//System.out.println( "classe : " +conIdx +" is : "+ secondClass);
					Vertex secondVertex = nodes[classList.indexOf(secondClass)];
					vertex.adjacencies[conIdx] = new Edge(secondVertex, 1); 
				}
			}
				
//				for(int conIdx=0; conIdx < connectedClasses.size() ; conIdx++) {
//					
//					String secondClass = connectedClasses.get(conIdx);					
//					
//					if(classList.contains(secondClass)) {
//						//System.out.println( "classe : " +conIdx +" is : "+ secondClass);
//						Vertex secondVertex = nodes[classList.indexOf(secondClass)];
//						vertex.adjacencies[conIdx] = new Edge(secondVertex, 1); 
//					}
//				}
				
//				for(int secondIndex=0; secondIndex <classList.size(); secondIndex++) {
//					
//					String secondClass = classList.get(secondIndex);
//					Vertex secondVertex = nodes[secondIndex];
//					
//					if( connectedClasses.contains(secondClass) ) {
//						vertex.adjacencies[secondIndex] = new Edge(secondVertex, 1);
//					} else {
//						//vertex.adjacencies[secondIndex] = new Edge(secondVertex, nopath);
//					}					
//				}
			}

			SerializedClassConnectivity serialize = new SerializedClassConnectivity();
			serialize.serializeData(nodes, ontology);
    }
	
}
