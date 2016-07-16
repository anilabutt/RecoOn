package au.edu.anu.cecs.explorer;

import java.util.ArrayList;

public class OntologyBean {

	private String ontology="";
	private double score=0;
	private ArrayList<ResourceBean> resources;
	
	public void setOntology(String ontologyId){
		this.ontology = ontologyId;
	}
	
	public void setScore(double score){
		this.score = score;
	}
	
	public void ResourceBean(ArrayList<ResourceBean> resources){
		this.resources = resources;
	}
	
	public String getOntology(){
		return this.ontology;
	}
	
	public double getScore(){
		return this.score;
	}
	
	public ArrayList<ResourceBean> getResources(){
		return this.resources;
	}
	
}
