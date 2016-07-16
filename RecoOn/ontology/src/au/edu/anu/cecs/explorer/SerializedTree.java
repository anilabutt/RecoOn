package au.edu.anu.cecs.explorer;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import au.edu.anu.cecs.explorer.tree.Resource;
import au.edu.anu.cecs.explorer.tree.TreeNode;

public class SerializedTree {

	public void serializeData(TreeNode root) {
		try
	      {
			 String fileName = (((Resource)root.getUserObject()).uri);
//			 System.out.println("fileName  : "+ fileName);
			 String path= PathString.getPathString();
	         FileOutputStream fileOut = new FileOutputStream(path + "preCompData/data/"+fileName+".ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(root);
	         out.close();
	         fileOut.close();
//	         System.out.println("Serialized data is saved");
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}
	
	
	public TreeNode deSerializeData(String fileName) {
		fileName = getID(fileName);
	      
		TreeNode node = new TreeNode();
	    
		try
	      {
//	    	  System.out.println(fileName);
	    	  String path= PathString.getPathString();
	         FileInputStream fileIn = new FileInputStream(path + "preCompData/data/"+fileName+".ser");
//	    	  System.out.println(fileName + " seond");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         
//	         System.out.println(fileName + " third");
	         
	         node = (TreeNode) in.readObject();
	         
//	         System.out.println(fileName + " 4th");
	         in.close();
	         fileIn.close();
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return node;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println(c);
	         //c.printStackTrace();
	         return node;
	      }
	      return node;
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
