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


public class FrequencyMapSimple {
	
	private static FrequencyMapSimple defaultMap;
	public PrimaryTreeMap<String, HashMap<String, HashMap<String,Integer>>> frequency_Map;
	public RecordManager recMan;

	
	public static FrequencyMapSimple getDefaultMap() {
		if(defaultMap==null) {
			defaultMap = new FrequencyMapSimple();
		}
		return defaultMap;
	}
	
	private FrequencyMapSimple(){
		String path= PathString.getPathString();
		String fileName = path +"preCompData/frequency/corpus_frequency_inferenceless_database";
		
//		String fileName = "/home/anila/workspace/ontology/preCompData/frequency/corpus_frequency_inferenceless_database";
//		//System.out.println(fileName);
		try {
			recMan = RecordManagerFactory.createRecordManager(fileName);
			String recordName = "Corpus_Count_inferenceless_ValueMap";
			frequency_Map = recMan.treeMap(recordName);
			//System.out.println("connection established :");
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//	System.out.println("can not connect because of :" + e);
		}
	}
	
	public void save_frequency_Value(String ontologyId , HashMap<String, HashMap<String,Integer>> ontologyTfIdfs) {
		// TODO Auto-generated method stub
		try {
			
			frequency_Map.put(ontologyId, ontologyTfIdfs);
		    recMan.commit();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public PrimaryTreeMap<String, HashMap<String, HashMap<String,Integer>>> get_frequency_Value() {
		// TODO Auto-generated method stub
		return this.frequency_Map;
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
