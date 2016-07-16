package au.edu.anu.cecs.explorer;

import java.util.ArrayList;
import java.util.List;

public class ClassConnectivityPath {

	public static void main(String[] args)	{
		  
//		getConnectionMatric("http://xmlns.com/foaf/0.1/");
//		createConnectionMatrix("http://xmlns.com/foaf/0.1/");
		createConnectionMatrix();


	}
	
	public static void getConnectionMatric(String ontology) {
		
		SerializedClassConnectivity serialize = new SerializedClassConnectivity();
		Vertex[] vertices = serialize.deSerializeData(ontology);
		
		Dijkstra dijkstra = new Dijkstra();
		
		
		Edge[] edges = vertices[0].adjacencies;
		//System.out.println("Calculate Distance for Class : " +  vertices[0].name +  "  has " + edges.length + "edges" );
		
		for (Edge e : edges)
	        {
	        	//System.out.println(" Edge with " + e.target.name + ": " + e.weight);
	        }
			
//		dijkstra.computePaths(vertices[0]);
//	        for (Vertex v : vertices)
//	        {
//	        	System.out.println("Distance to " + v + ": " + v.minDistance);
//	        	List<Vertex> path = dijkstra.getShortestPathTo(v);
//	        	System.out.println("Path: " + path);
//	        }
	}
	
	public static void createConnectionMatrix() {
		CorpusDataRetrieval retrieval = new CorpusDataRetrieval();
		//ProfileMap profileMapStructure = ProfileMap.getDefaultMap();
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
			
			/**
			 * Process classList to get all classes
			 */

			
			//create vertex for each node
		
			Vertex[] nodes = new Vertex[classList.size()];
			
			for(int classIndex = 0 ; classIndex< classList.size() ; classIndex++) {
				Vertex v0 = new Vertex(classList.get(classIndex));	
				v0.adjacencies = new Edge[classList.size()];
				nodes[classIndex] = v0;
			}

			//System.out.println("vertex : " + nodes.length );
			
				
			for(int classIndex = 0 ; classIndex< classList.size() ; classIndex++) {					
				String classIRI = classList.get(classIndex);
				Vertex vertex = nodes[classIndex];
									
				for(int secondIndex = 0 ; secondIndex< classList.size() ; secondIndex++) {
				
					String secondClassIRI = classList.get(secondIndex);
					Vertex secondVertex = nodes[secondIndex];
					
					boolean connected=false;
					
					if(classIRI.equalsIgnoreCase(secondClassIRI)) {
						vertex.adjacencies[secondIndex] = new Edge(secondVertex, 1);
					} else {
						connected = profileClass.getRelationAmongClasses(classIRI, secondClassIRI, ontology);
					}
				
					if(connected == true) {
						//System.out.println("hello I am connected . . ");
						vertex.adjacencies[secondIndex] = new Edge(secondVertex, 1);
					} else {
						vertex.adjacencies[secondIndex] = new Edge(secondVertex, nopath);
					}	
				}	
			}

			SerializedClassConnectivity serialize = new SerializedClassConnectivity();
			serialize.serializeData(nodes, ontology);


//	Vertex v0 = new Vertex("anila");
//	Vertex v1 = new Vertex("ali");
//	Vertex v2 = new Vertex("omer");
//	Vertex v3 = new Vertex("haroon");
//	Vertex v4 = new Vertex("husnain");
//
//
//	v0.adjacencies =new Edge[]{ new Edge(v1, 5),
//	                             new Edge(v2, 10),
//                               new Edge(v3, 8) };
//	v1.adjacencies = new Edge[]{ new Edge(v0, 5),
//	                             new Edge(v2, 3),
//	                             new Edge(v4, 7) };
//	v2.adjacencies = new Edge[]{ new Edge(v0, 10),
//                               new Edge(v1, 3) };
//	v3.adjacencies = new Edge[]{ new Edge(v0, 8),
//	                             new Edge(v4, 2) };
//	v4.adjacencies = new Edge[]{ new Edge(v1, 7),
//                               new Edge(v3, 2) };
//	Vertex[] vertices = { v0, v1, v2, v3, v4 };
        

    }
	}

	public static void createConnectionMatrix(String ontology) {
		double nopath = Double.POSITIVE_INFINITY;
		CorpusDataRetrieval retrieval = new CorpusDataRetrieval();
		//ProfileMap profileMapStructure = ProfileMap.getDefaultMap();
		ProfileOfAURI profileClass = new ProfileOfAURI();

		
			ArrayList<String> classList = retrieval.getClassList(ontology);
		
			/**
			 * Process classList to get all classes
			 */

			
			//create vertex for each node
		
			Vertex[] nodes = new Vertex[classList.size()];
			
			for(int classIndex = 0 ; classIndex< classList.size() ; classIndex++) {
				Vertex v0 = new Vertex(classList.get(classIndex));	
				v0.adjacencies = new Edge[classList.size()];
				nodes[classIndex] = v0;
			}

			//System.out.println("vertex : " + nodes.length );
			
				
			for(int classIndex = 0 ; classIndex< classList.size() ; classIndex++) {					
				String classIRI = classList.get(classIndex);
				Vertex vertex = nodes[classIndex];
									
				for(int secondIndex = 0 ; secondIndex< classList.size() ; secondIndex++) {
				
					String secondClassIRI = classList.get(secondIndex);
					Vertex secondVertex = nodes[secondIndex];
					
					boolean connected=false;
					
					if(classIRI.equalsIgnoreCase(secondClassIRI)) {
						vertex.adjacencies[secondIndex] = new Edge(secondVertex, 1);
					} else {
						connected = profileClass.getRelationAmongClasses(classIRI, secondClassIRI, ontology);
					}
				
					if(connected == true) {
						//System.out.println("hello I am connected . . ");
						vertex.adjacencies[secondIndex] = new Edge(secondVertex, 1);
					} else {
						vertex.adjacencies[secondIndex] = new Edge(secondVertex, nopath);
					}	
				}	
			}

			SerializedClassConnectivity serialize = new SerializedClassConnectivity();
			serialize.serializeData(nodes, ontology);

    }
	
}
