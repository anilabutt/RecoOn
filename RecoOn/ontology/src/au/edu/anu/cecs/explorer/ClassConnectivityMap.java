/*
 * Copyright (c) 2015, ANU and/or its constituents or affiliates. All rights reserved.
 * Use is subject to license terms.
 */

package au.edu.anu.cecs.explorer;

import java.io.IOException;
import java.util.HashMap;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;


public class ClassConnectivityMap {
	
	private static ClassConnectivityMap defaultMap;
	public PrimaryTreeMap<String, HashMap<String, HashMap<String,Double>>> classConnectivity_Map;
	public RecordManager recMan;

	
	public static ClassConnectivityMap getDefaultMap() {
		if(defaultMap==null) {
			defaultMap = new ClassConnectivityMap();
		}
		return defaultMap;
	}
	
	private ClassConnectivityMap(){
		
		String path= PathString.getPathString();
		String fileName = path+"preCompData/profile/classes/classConnectivity/corpus_classConnectivity_database";
//		String fileName = "/home/anila/workspace/ontology/preCompData/profile/classes/classConnectivity/corpus_classConnectivity_database";
		
		try {
			recMan = RecordManagerFactory.createRecordManager(fileName);
			String recordName = "Corpus_Connectivity_ValueMap";
			classConnectivity_Map = recMan.treeMap(recordName);
			//System.out.println("connection established :");
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//	System.out.println("can not connect because of :" + e);
		}
	}
	
	public void save_classConnectivity_Value(String ontologyId , HashMap<String, HashMap<String,Double>> ontologyTfIdfs) {
		// TODO Auto-generated method stub
		try {
			
			classConnectivity_Map.put(ontologyId, ontologyTfIdfs);
		    recMan.commit();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public PrimaryTreeMap<String, HashMap<String, HashMap<String,Double>>> get_classConnectivity_Value() {
		// TODO Auto-generated method stub
		return this.classConnectivity_Map;
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
