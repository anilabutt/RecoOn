package au.edu.anu.cecs.explorer;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SerializedClassConnectivity {

	public void serializeData(Vertex[] vertices, String ontologyId) {
		try
	      {
			 String fileName = getID(ontologyId);
			//System.out.println("fileName  : "+ fileName); home/anila/workspace/ontology
			 String path= PathString.getPathString();
	         FileOutputStream fileOut = new FileOutputStream(path + "preCompData/profile/classes/classConnectivity/"+fileName+".ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(vertices);
	         out.close();
	         fileOut.close();
//	         System.out.println("Serialized data is saved");
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}
	
	
	public Vertex[] deSerializeData(String fileName) {
		fileName = getID(fileName);
	      
		Vertex[] vertices={};

		String path= PathString.getPathString();
		try
	      {
	         FileInputStream fileIn = new FileInputStream(path+"preCompData/profile/classes/classConnectivity/"+fileName+".ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         vertices = (Vertex[]) in.readObject();

	         in.close();
	         fileIn.close();
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return vertices;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println(c);
	         //c.printStackTrace();
	         return vertices;
	      }
	      return vertices;
	}
	
	public String getID(String text){
		//String plaintext = "your text here";
		MessageDigest m;
		String hashtext="";
		try {
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(text.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			hashtext = bigInt.toString(16);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// Now we need to zero pad it if you actually want the full 32 chars.
		while(hashtext.length() < 32 ){
		  hashtext = "0"+hashtext;
		}
		return hashtext;
	}
}
