package au.edu.anu.cecs.explorer;

import java.io.IOException;
import java.util.HashMap;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;


public class PropertyProfileMap {
	
	private static PropertyProfileMap defaultMap;
	
	private PrimaryTreeMap<String, HashMap<String,HashMap<String, HashMap<String,String>>>> property_profile_materialized_map;
	
	private RecordManager recMan;

	public static PropertyProfileMap getDefaultMap() {
		if(defaultMap==null) {
			defaultMap = new PropertyProfileMap();
		}
		return defaultMap;
	}
	
	private PropertyProfileMap(){
		String path= PathString.getPathString();
		String fileprofile = path+"preCompData/profile/property/property_profile_database";

//		String fileprofile = "/home/anila/workspace/ontology/preCompData/profile/property/property_profile_database";
//		String fileprofile = "/www_exp/data/rankings/userstudy/firstEval/profile_database";
		
		try {
			recMan = RecordManagerFactory.createRecordManager(fileprofile);
			String recordprofile = "profile_table";
			property_profile_materialized_map = recMan.treeMap(recordprofile);
//			System.out.println("connection established :");
		} catch (IOException e) {
			System.out.println("can not connect because of :" + e);}
	}
	
	public void save_property_profile_map(String ontologyId , HashMap<String, HashMap<String,HashMap<String,String>>> map ) {
		// TODO Auto-generated method stub
		try {
			
			property_profile_materialized_map.put(ontologyId, map);
		    recMan.commit();
		    
		    /** close record manager */
		   // recMan.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public PrimaryTreeMap<String, HashMap<String,HashMap<String,HashMap<String,String>>>> get_profile_map() {
		return this.property_profile_materialized_map;
		
		//return profile_map;
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
