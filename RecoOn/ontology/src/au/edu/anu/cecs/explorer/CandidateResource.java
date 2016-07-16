package au.edu.anu.cecs.explorer;

public class CandidateResource {
	
   /**
	 * 
	 */
   public String uri;
   public String label;
   public double score;
   
   public CandidateResource(String uri, String label, double score){
	   this.uri = uri;
	   this.label = label;
	   this.score = score;
   }

}