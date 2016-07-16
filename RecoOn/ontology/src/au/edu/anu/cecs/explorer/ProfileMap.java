package au.edu.anu.cecs.explorer;

import java.io.IOException;
import java.util.HashMap;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;


public class ProfileMap {
	
	private static ProfileMap defaultMap;
	
	private PrimaryTreeMap<String, HashMap<String,HashMap<String, String>>> profile_materialized_map;
	
	private RecordManager recMan;

	public static ProfileMap getDefaultMap() {
		if(defaultMap==null) {
			defaultMap = new ProfileMap();
		}
		return defaultMap;
	}
	
	private ProfileMap(){
		String path= PathString.getPathString();
		String fileprofile = path+ "preCompData/profile/classes/profile_database";
//		String fileprofile = "/home/anila/workspace/ontology/preCompData/profile/classes/profile_database";
//		String fileprofile = "/www_exp/data/rankings/userstudy/firstEval/profile_database";
		
		try {
			recMan = RecordManagerFactory.createRecordManager(fileprofile);
			String recordprofile = "profile_table";
			profile_materialized_map = recMan.treeMap(recordprofile);
//			System.out.println("connection established :");
		} catch (IOException e) {
			System.out.println("can not connect because of :" + e);}
	}
	
	public void save_profile_map(String ontologyId , HashMap<String, HashMap<String,String>> map ) {
		// TODO Auto-generated method stub
		try {
			
			profile_materialized_map.put(ontologyId, map);
		    recMan.commit();
		    
		    /** close record manager */
		   // recMan.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public PrimaryTreeMap<String, HashMap<String,HashMap<String,String>>> get_profile_map() {
		return this.profile_materialized_map;
		
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
