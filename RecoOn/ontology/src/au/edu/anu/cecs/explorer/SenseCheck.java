package au.edu.anu.cecs.explorer;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class SenseCheck {

//	 public static void main(String[] args) {
//     	
//     	String qWord="author"; 
//     	String mLabel = "authorship";
//     	
//     	System.out.println(senseMatch(mLabel, qWord) + "");
//     }

  	
  	private static boolean senseMatch(String matchedWord, String word){
  		String path= PathString.getPathString();
  		System.setProperty("wordnet.database.dir", path+"preCompData/wordnet/");
  		boolean senseChecker= false;
		NounSynset nounSynset;
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		Synset[] synsets = database.getSynsets(matchedWord, SynsetType.NOUN);
		for (int i = 0; i < synsets.length; i++) {
		    nounSynset = (NounSynset)(synsets[i]);
		    String definition = nounSynset.getDefinition();
		    if (definition.contains(" "+word+" ")){
		    	senseChecker = true;
		    	//System.out.println(definition);
		    }
		}
		
		return senseChecker;
	}
	
}
