package au.edu.anu.cecs.explorer;

import java.util.HashMap;

public class ResultWithRanks {

	private HashMap<String, Double> rank_map;
	private HashMap<String, HashMap<String,String>> result_map;
	
	public void setRankMap(HashMap<String, Double> map){
		this.rank_map= map;
	}
	
	public void setResultMap(HashMap<String, HashMap<String,String>> map){
		this.result_map = map;
	}
	
	public HashMap<String, Double> getRankMap(){
		return this.rank_map;
	}
	
	public HashMap<String, HashMap<String,String>> getResultMap() {
		return this.result_map;
	}
 }
