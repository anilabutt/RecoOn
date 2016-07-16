package au.edu.anu.cecs.explorer;

public class ResourceBean {

	private String resourceIRI = "";
	private String resourceLabel= "";
	private double score = 0;
	
	public void setResourceIRI(String resourceIRI){
		this.resourceIRI = resourceIRI;
	}
	
	public void setResourceLabel(String resourceLabel){
		this.resourceLabel = resourceLabel;
	}
	
	public void setScore(double score){
		this.score = score;
	}
	
	public String getResourceIRI(){
		return this.resourceIRI ;
	}
	
	public String getResourceLabel(){
		return this.resourceLabel;
	}
	
	public double getScore(){
		return this.score;
	}
	
}
