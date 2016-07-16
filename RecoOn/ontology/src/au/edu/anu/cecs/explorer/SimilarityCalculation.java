package au.edu.anu.cecs.explorer;

import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class SimilarityCalculation {
		
        private ILexicalDatabase db = new NictWordNet();
        
        private RelatednessCalculator[] rcs = {
        	  new HirstStOnge(db), new LeacockChodorow(db), new Lesk(db),  new WuPalmer(db),
              new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db) };

//        public static void main(String[] args) {
//        	
//        	String qWord="event"; 
//        	String mLabel = "event Unit";
//        	
//        	double jaccard_score = getJaccardsimilarity(qWord, mLabel);
//        	System.out.println( "Jaccard Similarity between \""+ qWord +"\" and \"" + mLabel + "\" is  : " + jaccard_score);
//        	
//        	boolean semilarity_score = getSemanticSimilarity(qWord, mLabel);
//        	System.out.println( "Semantic Similarity between \""+ qWord +"\" and \"" + mLabel + "\" is  : " + semilarity_score);
//        }
        

        /**
         * Jaccard Similarity
         * 
         * */
        
        public double getJaccardsimilarity(String qWord, String mLabel) {
        	
        	double qLength = Double.parseDouble(qWord.length()+"");
        	double mLength = Double.parseDouble(mLabel.length()+"");

        	double JaccardSim_score = qLength / mLength; 
        	return JaccardSim_score;
        }
        
   
        /**
         * 
         *  Semantic Similarity of words.
         * 
         * **/
        
        public boolean getSemanticSimilarity(String qWord, String mLabel ) {

//        	System.out.println("INNNNN");
        	boolean resnik_score = false;
            String MATCHED_TOKEN = getMatchedToken(qWord, mLabel);
            String MORPHED_MATCHED_TOKEN  = this.getMorphRoot(MATCHED_TOKEN.toLowerCase(), POS.NOUN);
//      		System.out.println(qWord +"  "+MORPHED_MATCHED_TOKEN);
            if(MORPHED_MATCHED_TOKEN==null ||MORPHED_MATCHED_TOKEN.equalsIgnoreCase("")){
            	resnik_score = false;
            } else {
            	MATCHED_TOKEN = MORPHED_MATCHED_TOKEN;
                resnik_score = senseMatch(MATCHED_TOKEN, qWord);
            }
            
           return resnik_score;
        }
        
        public String getMatchedToken(String qWord, String mLabel) {        	
        	String MATCHED_TOKEN = "";
        	String[] labelTokens = {mLabel};
        	
        	if(mLabel.contains(" ")) {        	
        		labelTokens = mLabel.split(" ");
        	} else {
        		labelTokens = mLabel.split("(?=[A-Z])");
        	}	
        		for(String s: labelTokens){
        			s = s.toLowerCase();
        			if(s.contains(qWord.toLowerCase())) {
        				MATCHED_TOKEN = s;
        			} 
        		}        	 	
        	return MATCHED_TOKEN;        	
        }
        
    	private boolean senseMatch(String matchedWord, String word) {
//      		System.out.println(word +"  "+matchedWord + " " );
      		String path= PathString.getPathString();
      		System.setProperty("wordnet.database.dir", path+"preCompData/wordnet/");
      		boolean senseChecker= false;
      		if(matchedWord.equalsIgnoreCase(word)) {
      			senseChecker= true;
      		} else {
    		NounSynset nounSynset;
    		WordNetDatabase database = WordNetDatabase.getFileInstance();
    		Synset[] synsets = database.getSynsets(matchedWord, SynsetType.NOUN);
    		for (int i = 0; i < synsets.length; i++) {
    		    nounSynset = (NounSynset)(synsets[i]);
    		    String definition = nounSynset.getDefinition();
    		    if (definition.contains(" "+word+" ")){
    		    	senseChecker = true;
    		    }
    		}
      		}
//      		System.out.println(word +"  "+matchedWord + " " + senseChecker);
    		return senseChecker;
    	}
    	
    	// Retrieve the morphological root for the word / part of speech
    	// Good to call before other relation methods
    	public String getMorphRoot(String word, POS pos) {
    		Dictionary _dict = Dictionary.getInstance();
    		try {
    			
    			// use lookupIndexWord to do morphological analysis
    			IndexWord iword = _dict.lookupIndexWord(pos, word);
    			if (iword == null) return null;
    			return iword.getKey().toString();
    		} catch (Exception e) {
    			System.err.println(e);
    			e.printStackTrace();
    		} finally {
    			//Dictionary.uninstall();
    		}
    		
    		return null;
    	}
}


