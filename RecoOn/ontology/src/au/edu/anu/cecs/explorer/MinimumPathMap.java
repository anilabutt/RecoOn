/*
 * Copyright (c) 2015, ANU and/or its constituents or affiliates. All rights reserved.
 * Use is subject to license terms.
 */

package au.edu.anu.cecs.explorer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;


public class MinimumPathMap {
	
	private static MinimumPathMap defaultMap;
	public PrimaryTreeMap<String, HashMap<String, HashMap<String,ArrayList<String>>>> minimum_paths;
	public RecordManager recMan;

	
	public static MinimumPathMap getDefaultMap() {
		if(defaultMap==null) {
			defaultMap = new MinimumPathMap();
		}
		return defaultMap;
	}
	
	private MinimumPathMap(){
		String path= PathString.getPathString();
		String fileName = path+"preCompData/profile/classes/classConnectivity/corpus_minimum_paths_database";
//		String fileName = "/home/anila/workspace/ontology/preCompData/profile/classes/classConnectivity/corpus_minimum_paths_database";
		//System.out.println(fileName);
		try {
			recMan = RecordManagerFactory.createRecordManager(fileName);
			String recordName = "Corpus_Minimum_Path_Map";
			minimum_paths = recMan.treeMap(recordName);
			//System.out.println("connection established :");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		//	System.out.println("can not connect because of :" + e);
		}
	}
	
	public void save_minimum_path_Value(String ontologyId , HashMap<String, HashMap<String,ArrayList<String>>> ontologyTfIdfs) {
		// TODO Auto-generated method stub
		try {
			
			minimum_paths.put(ontologyId, ontologyTfIdfs);
		    recMan.commit();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public PrimaryTreeMap<String, HashMap<String, HashMap<String,ArrayList<String>>>> get_classConnectivity_Value() {
		// TODO Auto-generated method stub
		return this.minimum_paths;
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
