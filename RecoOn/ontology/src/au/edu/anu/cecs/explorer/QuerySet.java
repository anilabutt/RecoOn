package au.edu.anu.cecs.explorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class QuerySet {

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		System.out.println(getQueriesAsString());
//	}
	
	public HashMap<String, ArrayList<String>> getQueries() {
		
		HashMap<String, ArrayList<String>> queries = new HashMap<String, ArrayList<String>>();
		
		String query = "";
		ArrayList<String> derivedQueries =new ArrayList<String>(); 
		
		query = "person";
		derivedQueries.add("person agent");
		derivedQueries.add("person organization");
		derivedQueries.add("person organization project");
		derivedQueries.add("person student professor university");
		
		queries.put(query, derivedQueries);
		
		query = "organization";
		ArrayList<String> derivedQueries2 =new ArrayList<String>(); 
		derivedQueries2.add("organization location");
		derivedQueries2.add("organization student");
		derivedQueries2.add("organization student course");
		derivedQueries2.add("organization student course university");
		
		queries.put(query, derivedQueries2);
		
		query = "event";
		ArrayList<String> derivedQueries3 =new ArrayList<String>(); 
		derivedQueries3.add("event location");
		derivedQueries3.add("event conference");
		derivedQueries3.add("event conference paper");
		derivedQueries3.add("event conference paper article");
		
		queries.put(query, derivedQueries3);
		
		query = "author";
		ArrayList<String> derivedQueries4 =new ArrayList<String>(); 
		derivedQueries4.add("author publication");
		derivedQueries4.add("author newspaper");
		derivedQueries4.add("author publication research");
		derivedQueries4.add("author publication research issue");
		
		queries.put(query, derivedQueries4);
		
		query = "name";
		ArrayList<String> derivedQueries5 =new ArrayList<String>(); 
		derivedQueries5.add("name person");
		derivedQueries5.add("name title");
		derivedQueries5.add("name person agent");
		
		queries.put(query, derivedQueries5);
		 
		query = "title";
		ArrayList<String> derivedQueries6 =new ArrayList<String>(); 
		derivedQueries6.add("title identifier");
		derivedQueries6.add("title organization");
		derivedQueries6.add("title author document");
		
		queries.put(query, derivedQueries6);
		
		query = "address";
		ArrayList<String> derivedQueries7 =new ArrayList<String>(); 
		derivedQueries7.add("address organization");
		derivedQueries7.add("address organization place");
		derivedQueries7.add("address organization place country");
		
		queries.put(query, derivedQueries7);
		
		query = "location";
		ArrayList<String> derivedQueries8 =new ArrayList<String>(); 
		derivedQueries8.add("location place");
		derivedQueries8.add("location place geographic");
		
		queries.put(query, derivedQueries8);
		
		query = "music";
		ArrayList<String> derivedQueries9 =new ArrayList<String>(); 
		derivedQueries9.add("music event");
		derivedQueries9.add("music group");
		
		queries.put(query, derivedQueries9);
		
		query = "time";
		ArrayList<String> derivedQueries10 =new ArrayList<String>(); 
		derivedQueries10.add("time date");
		
		queries.put(query, derivedQueries10);
		
		return queries;
		
	}
	
	public static String getQueriesAsString() {
		
		String queries = "";
		
	ArrayList<String> derivedQueries =new ArrayList<String>(); 
		

		derivedQueries.add("person agent");
		derivedQueries.add("person organization");
		derivedQueries.add("person organization project");
		derivedQueries.add("person student professor university");
		
		derivedQueries.add("organization location");
		derivedQueries.add("organization student");
		derivedQueries.add("organization student course");
		derivedQueries.add("organization student course university");
		
		derivedQueries.add("event location");
		derivedQueries.add("event conference");
		derivedQueries.add("event conference paper");
		derivedQueries.add("event conference paper article");
		
		
		derivedQueries.add("author publication");
		derivedQueries.add("author newspaper");
		derivedQueries.add("author publication research");
		derivedQueries.add("author publication research issue");
		
		derivedQueries.add("name person");
		derivedQueries.add("name title");
		derivedQueries.add("name person agent");
		
		derivedQueries.add("title identifier");
		derivedQueries.add("title organization");
		derivedQueries.add("title author document");
		
		derivedQueries.add("address organization");
		derivedQueries.add("address organization place");
		derivedQueries.add("address organization place country");
		derivedQueries.add("location place");
		derivedQueries.add("location place geographic");
		
		derivedQueries.add("music event");
		derivedQueries.add("music group");
		
		derivedQueries.add("time date");
		
		long seed = System.nanoTime();
		Collections.shuffle(derivedQueries, new Random(seed));	
	
		for(int i=0; i <derivedQueries.size() ; i++) {
			String s = derivedQueries.get(i);
			
			if(i==0) {
				queries = queries + s;
			} else {
				queries = queries + "," +s ; 
			}
			
		}

		return queries;
	} 
	
	
	public ArrayList<String> getQueriesAsList() {


		ArrayList<String> derivedQueries =new ArrayList<String>(); 
		

		derivedQueries.add("person agent");
		derivedQueries.add("person organization");
		derivedQueries.add("person organization project");
		derivedQueries.add("person student professor university");
		
		derivedQueries.add("organization location");
		derivedQueries.add("organization student");
		derivedQueries.add("organization student course");
		derivedQueries.add("organization student course university");
		
		derivedQueries.add("event location");
		derivedQueries.add("event conference");
		derivedQueries.add("event conference paper");
		derivedQueries.add("event conference paper article");
		
		
		derivedQueries.add("author publication");
		derivedQueries.add("author newspaper");
		derivedQueries.add("author publication research");
		derivedQueries.add("author publication research issue");
		
		derivedQueries.add("name person");
		derivedQueries.add("name title");
		derivedQueries.add("name person agent");
		
		derivedQueries.add("title identifier");
		derivedQueries.add("title organization");
		derivedQueries.add("title author document");
		
		derivedQueries.add("address organization");
		derivedQueries.add("address organization place");
		derivedQueries.add("address organization place country");
		derivedQueries.add("location place");
		derivedQueries.add("location place geographic");
		
		derivedQueries.add("music event");
		derivedQueries.add("music group");
		
		derivedQueries.add("time date");
		
		
		return derivedQueries;
		
	}

}
