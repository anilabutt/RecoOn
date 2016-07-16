package au.edu.anu.cecs.explorer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;


public class GraphReuseInfoMap {
	
	private static GraphReuseInfoMap defaultMap;
	
	private PrimaryTreeMap<String,ArrayList<String>> graph_reuse_map;
	
	private RecordManager recMan;

	public static GraphReuseInfoMap getDefaultMap() {
		if(defaultMap==null) {
			defaultMap = new GraphReuseInfoMap();
		}
		return defaultMap;
	}
	
	public GraphReuseInfoMap(){
		String path= PathString.getPathString();
//		String fileName = "/home/anila/workspace/ontology/preCompData/grank/graph_reuse_map_database";
		String fileName = path + "preCompData/grank/graph_reuse_map_database";
		try {
			recMan = RecordManagerFactory.createRecordManager(fileName);
			String recordName = "graph_reuse_map_table";
			graph_reuse_map = recMan.treeMap(recordName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void save_graph_reuse_map(String sourceClass , ArrayList<String> outlinks) {
		// TODO Auto-generated method stub
		try {
			
			graph_reuse_map.put(sourceClass, outlinks);
		    recMan.commit();
		    
		    /** close record manager */
		   // recMan.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public PrimaryTreeMap<String,ArrayList<String>> get_graph_reuse_map() {
		return this.graph_reuse_map;
	}

	
	public void closeConnection(){
		try {
			recMan.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
