package au.edu.anu.cecs.explorer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class QueryStringParser {

	public ArrayList<String> parserQueryString(String queryString){
		
	//	System.out.println("within parseQueryFunction : " + queryString);
		ArrayList<String> queryWords = new ArrayList<String>();
		ArrayList<String> stopWords = new ArrayList<String>();

		try{
			  // Open the file that is the first 
			  // command line parameter
		     // String path = au.csiro.browser.Configuration.getProperty(Configuration.FILES_PATH+"/stopwords.txt");
		    //  "/usr/local/browser/files/stopwords"; //
			//  System.out.println(path);
				PathString path = new PathString();
			  FileInputStream fstream = new FileInputStream(path + "preCompData/stopwords.txt");
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  String urlString="";
			  int count = 0;
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null)   {
				  stopWords.add(strLine);
			  }
			  
			  System.out.println ("number of stop words : " + stopWords.size());
			  //Close the input stream
			  in.close();
			  
			  // Print the content on the console
			  StringTokenizer st = new StringTokenizer(queryString);

			  while (st.hasMoreElements()) {
				  String word = st.nextToken();
				  if (stopWords.contains(word)){
					  
				  } else {
					  queryWords.add(word);
				  }
			  }
			  
			  
		}catch (Exception e){//Catch exception if any
			System.err.println("WAJA Error: " + e.getMessage());
		}
		
		System.out.println(queryWords);
		return queryWords;
	}
	
}
