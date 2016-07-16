package au.edu.anu.cecs.explorer;

import java.util.*;

import jdbm.PrimaryTreeMap;
 
public class MinimumPathLengthDijkstra {

   public static void main(String[] args) {
	   
	   	MinimumPathMapLength minimum_path_map_class = MinimumPathMapLength.getDefaultMap();
	   	PrimaryTreeMap<String, HashMap<String, HashMap<String,Double>>> minimum_pathsMap = minimum_path_map_class.get_classConnectivity_Value();

	   	CorpusDataRetrieval retrieval = new CorpusDataRetrieval();
			
		ArrayList<String> ontologies = retrieval.getExistingLoadedOntology();
		HashMap<String, HashMap<String, Double>> paths = new HashMap<String, HashMap<String, Double>>();
		
		try { 
			for(int count=988; count<ontologies.size(); count++) {
			
				String ontology = ontologies.get(count);
				//String ontology = "http://www.lehigh.edu/~zhp2/2004/0401/univ-bench.owl#";
//				System.out.println( " Ontology Id : " + ontology + "     index: "  + count);

//				paths = minimum_pathsMap.get(ontology);
//				System.out.println(paths);
//				paths.clear();
				paths = getMinimumPathsForOntology(ontology);

//				System.out.println( paths.size() );
				minimum_path_map_class.save_minimum_path_Value(ontology, paths);
			//	paths.clear();
			} 
		} catch(Exception e) {
			System.out.println( "e  : " + e);
		} finally{
			minimum_path_map_class.closeConnection();
		}
	   
//	   for(Map.Entry<String, HashMap<String, ArrayList<String>>> entry: paths.entrySet()) {
//		   System.out.println( " ******** PATHS FOR THE CLASS : " + entry.getKey() + " ****** ");
//		   
//		   for(Map.Entry<String, ArrayList<String>> entry2: entry.getValue().entrySet()) {
//			   System.out.println("Target : " + entry2.getKey());
//			   System.out.println("Path : " + entry2.getValue());
//		   }
//	   }
	   
   }
   
   public static HashMap<String, HashMap<String, Double>> getMinimumPathsForOntology(String ontology) {

	   HashMap<String, HashMap<String, Double>> map = createConnectionMatrix(ontology);
//	   System.out.println("createConnectionMatrix  : " + map);

	   HashMap<String, HashMap<String, Double>> miniPaths = new HashMap<String, HashMap<String, Double>>();
	   
	   //loop map to get the size of graph to be
	   int size=0;
	   for(Map.Entry<String, HashMap<String, Double>> entry: map.entrySet()) {		   
		   size = size + entry.getValue().size();
//		   System.out.println("size  : " + size);
   		}

	   Graph.Edge[] GRAPH= new Graph.Edge[size];
	   
	   int counter=0;
	   for(Map.Entry<String, HashMap<String, Double>> entry: map.entrySet()) {		   
		   
		   String class1  = entry.getKey();
//		   System.out.println("class1  : " + class1);
		   
		   HashMap<String, Double> classCon = entry.getValue();
//		   System.out.println("classCon  : " + classCon);
		   
		   
		   for(Map.Entry<String, Double> entry2: classCon.entrySet()) {			   
			   GRAPH[counter] = new Graph.Edge(class1, entry2.getKey(), entry2.getValue().intValue());
			   counter++;
		   }
		  // System.out.println(GRAPH.length);
   		}
	   
	   CorpusDataRetrieval retrieval = new CorpusDataRetrieval();
	   
	   ArrayList<String> classList = retrieval.getClassList(ontology);
	  // System.out.println( "FROM getMinimumPathsForOntology   "  + classList.size() );
	   
	   for(int classIndex = 0 ; classIndex< classList.size() ; classIndex++) {		

		   String classURI = classList.get(classIndex);
		   	   
		  // System.out.println( "FROM getMinimumPathsForOntology   "  + classURI);
		   String START = classURI;
		
		   Graph g = new Graph(GRAPH);
		   
			if (g.dijkstra(START)==-1)  {
				
			} else {
			
				HashMap<String, Double> miniPathOfClass = new HashMap<String, Double>();
				
				for(int classIndex2 = 0 ; classIndex2< classList.size() ; classIndex2++) {	
					String END = classList.get(classIndex2);
//					System.out.println(START + "   " + END);
					if(START.equalsIgnoreCase(END)) { 
					
					} else {
						ArrayList<String> paths = g.printPath(END);
//						System.out.println(paths);
						if(paths.size()<1) { 
				   		
						} else {
							//System.out.println(paths);
							double sizeOfPath = paths.size()-1;
//							System.out.println();
							miniPathOfClass.put(END,sizeOfPath);
						}

						miniPaths.put(START, miniPathOfClass);
					}
				}
			}
	   }
	//   System.out.println(miniPaths);
	   
	   return miniPaths;
   }
   
   public static HashMap<String, HashMap<String, Double>> createConnectionMatrix(String ontology) {

		ClassConnectivityMap classConnectMap = ClassConnectivityMap.getDefaultMap();
		PrimaryTreeMap<String, HashMap<String, HashMap<String,Double>>> connectivity = classConnectMap.get_classConnectivity_Value();
		
		HashMap<String, HashMap<String, Double>> classConnectivityOfOntology = new HashMap<String, HashMap<String, Double>>();
		if(connectivity.containsKey(ontology)) { 
			classConnectivityOfOntology= connectivity.get(ontology);
		}
		return classConnectivityOfOntology;
				
	}	
}
 
