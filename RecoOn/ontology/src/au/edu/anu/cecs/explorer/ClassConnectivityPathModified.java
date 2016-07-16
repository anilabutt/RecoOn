package au.edu.anu.cecs.explorer;

import java.util.ArrayList;
import java.util.List;

public class ClassConnectivityPathModified {

	public static void main(String[] args)	{
		  
//		getConnectionMatric("http://xmlns.com/foaf/0.1/");
		createConnectionMatrix("http://xmlns.com/foaf/0.1/");
	//	createConnectionMatrix();


	}
	
	public static void getConnectionMatric(String ontology) {
		
		SerializedClassConnectivity serialize = new SerializedClassConnectivity();
		Vertex[] vertices = serialize.deSerializeData(ontology);

		Dijkstra dijkstra = new Dijkstra();

//		dijkstra.computePaths(vertices[0]);
//	        for (Vertex v : vertices)
//	        {
//	        	//System.out.println("Distance to " + v + ": " + v.minDistance);
//	        	List<Vertex> path = dijkstra.getShortestPathTo(v);
//	        	//System.out.println("Path: " + path);
//	        }
	}
	
	public static void createConnectionMatrix() {
		
//		BetweenessMeasureDijkstra dijkstra = new BetweenessMeasureDijkstra();
		CorpusDataRetrieval retrieval = new CorpusDataRetrieval();
		ProfileOfAURI profileClass = new ProfileOfAURI();
	    
		double nopath = Double.POSITIVE_INFINITY;
			
		ArrayList<String> ontologies = retrieval.getExistingLoadedOntology();
		
		for(int count=0; count<ontologies.size(); count++) {

			String ontology = ontologies.get(count);
			//System.out.println( "OntologyId : " +count+ " OntologyIRI : " + ontology);
		
			/**
			 * Get all classes in an ontology
			 */
		
			ArrayList<String> classList = retrieval.getClassList(ontology);
			
			//create vertex for each node
			
			Vertex[] nodes = new Vertex[classList.size()];
			
			for(int classIndex = 0 ; classIndex< classList.size() ; classIndex++) {
				Vertex v0 = new Vertex(classList.get(classIndex));	
				nodes[classIndex] = v0;
			}

			////System.out.println("vertex : " + nodes.length );
			
				
			for(int classIndex = 0 ; classIndex< classList.size() ; classIndex++) {					
				String classIRI = classList.get(classIndex);
				Vertex vertex = nodes[classIndex];
				
				ArrayList<String> connectedClasses = profileClass.getAllRelationOfClasses(classIRI, ontology);
				vertex.adjacencies = new Edge[connectedClasses.size()];
				
				////System.out.println( "connected classes for *** : " + classIRI);

				for(int conIdx=0; conIdx < connectedClasses.size() ; conIdx++) {
				
					String secondClass = connectedClasses.get(conIdx);					
				
					if(classList.contains(secondClass)) {
						////System.out.println( "classe : " +conIdx +" is : "+ secondClass);
						Vertex secondVertex = nodes[classList.indexOf(secondClass)];
						vertex.adjacencies[conIdx] = new Edge(secondVertex, 1); 
					}
				}
			}
			
			//System.out.println(ontology + " loaded ");
//			SerializedClassConnectivity serialize = new SerializedClassConnectivity();
//			serialize.serializeData(nodes, ontology); 
			
			for (Vertex n : nodes)
	        {
//				dijkstra.computePaths(n);
//				//Edge[] edges = vertices[0].adjacencies;
//		        for (Vertex v : nodes)
//		        {
//		        	//System.out.println("Distance to " + v + ": " + v.minDistance);
//		        	List<Vertex> path = dijkstra.getShortestPathTo(v);
//		        	//System.out.println("Path: " + path);
//		        }
	        }
			
			
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
			}


//			BetweenessMeasureDijkstra dijkstra = new BetweenessMeasureDijkstra();
//			 for (Vertex n : nodes)
//		     {
//				//System.out.println(" ********************  Shortest paths for *********** " + n); 
//			 	dijkstra.computePaths(n);
//		        for (Vertex v : nodes)
//		        {
//		        	//System.out.println("Distance to " + v + ": " + v.minDistance);
//		        	List<Vertex> path = dijkstra.getShortestPathTo(v);
//		        	//System.out.println("Path: " + path);
//		        }
//		      }
    }
	
}
