package au.edu.anu.cecs.explorer;

import java.io.*;
import java.util.*;

import net.didion.jwnl.*;
import net.didion.jwnl.data.*;
import net.didion.jwnl.data.list.*;
import net.didion.jwnl.data.relationship.Relationship;
import net.didion.jwnl.data.relationship.RelationshipFinder;
import net.didion.jwnl.data.relationship.RelationshipList;
import net.didion.jwnl.dictionary.Dictionary;


public class WordNet {

	// Note possible POS: POS.NOUN, 
	//                    POS.ADJECTIVE, 
	//                    POS.VERB, 
	//                    POS.ADVERB
	 String path= PathString.getPathString();
	public final String WORDNET_PROPS = path+"preCompData/file_properties.xml"; //"wordnet_properties.xml";
	public int SENSE_LIMIT = 2;
	
	private Dictionary _dict = null;
	
	public WordNet() {
		
		try {
			// Initialize JWNL (this must be done before JWNL can be used)
			JWNL.initialize(new FileInputStream(WORDNET_PROPS));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
//		SENSE_LIMIT = sense_limit;
		_dict = Dictionary.getInstance();
	}
	
	// Retrieve the morphological root for the word / part of speech
	// Good to call before other relation methods
	public String getMorphRoot(String word, POS pos) {
		try {
			// use lookupIndexWord to do morphological analysis
			IndexWord iword = _dict.lookupIndexWord(pos, word);
			if (iword == null) return null;
			return iword.getKey().toString();
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
		return null;
	}
	
	// Additional tests: words, senses, relationships
	public void initialTests() {
		
		try {
//			System.out.println("Word and senses:");
			IndexWord word = _dict.getIndexWord(POS.VERB, "run");
			Synset[] senses = word.getSenses();
			for (int i = 0; i < senses.length; i++) {
//				System.out.println(word + ": " + senses[i].getGloss());
			}

//			System.out.println("Synsets:"); // Only prints one Synset?!?
			Synset sense = word.getSense(2);
//			System.out.println(sense);
			PointerTargetNodeList relatedList = PointerUtils.getInstance()
					.getSynonyms(sense);
			Iterator i = relatedList.iterator();
			while (i.hasNext()) {
				PointerTargetNode related = (PointerTargetNode) i.next();
				Synset s = related.getSynset();
//				System.out.println(s);
			}
	
//			System.out.println("Relationships:"); // Does not print?
			Synset synset1 = senses[0];
			Synset synset2 = senses[1];
			Relationship rel2 = null;
			RelationshipList list = RelationshipFinder.getInstance()
					.findRelationships(synset1, synset2, PointerType.VERB_GROUP);
			if (!list.isEmpty()) {
				Relationship rel = (Relationship) list.get(0);
//				System.out.println(rel);
				rel2 = rel;
			}
	
			if (rel2 != null) {
//				System.out.println("The depth of this relationship is: "+ rel2.getDepth());
	
				PointerTargetNodeList nodelist = rel2.getNodeList();
				Iterator i2 = nodelist.iterator();
				while (i2.hasNext()) {
					PointerTargetNode related = (PointerTargetNode) i.next();
//					System.out.println(related.getSynset());
				}
			}
		} catch (JWNLException ex) { 
			System.err.println(ex);
		}
	}
	
}
