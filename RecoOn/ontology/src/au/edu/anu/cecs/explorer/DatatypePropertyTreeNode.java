package au.edu.anu.cecs.explorer;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import au.edu.anu.cecs.explorer.tree.Resource;
import au.edu.anu.cecs.explorer.tree.TreeNode;


public class DatatypePropertyTreeNode {

	static HashMap<String, HashMap<String,String>> subDataProperties = new HashMap<String, HashMap<String,String>>();
	
	public static void addLeafNodes(TreeNode root, HashMap<String, String> subDataPropertiesOfClass){		
		
		try {
		if(subDataPropertiesOfClass.size()>0) {
			//System.out.print("is a non-leaf node");

			for(Map.Entry<String, String> entry: subDataPropertiesOfClass.entrySet()){
				
				String propertyURI = entry.getKey();
				
				if(propertyURI.equalsIgnoreCase(((Resource)root.getUserObject()).uri)) { } else {
					
					String propLabel = entry.getValue();
					Resource property = new Resource();
					property.uri = propertyURI;
					property.label = propLabel;
				
				TreeNode child = new TreeNode();
				child.setUserObject(property);
				
				root.add(child);				
				
				HashMap<String, String> subDataPropertiesForChild = new HashMap<String, String>();
				if(subDataProperties.containsKey(propertyURI)) {
				subDataPropertiesForChild = subDataProperties.get(propertyURI);}
				//System.out.println("" +((Employee)root.getUserData()).name + " is parent of : \""+((Employee)children[i].getUserData()).name);
				addLeafNodes(child, subDataPropertiesForChild);}
			}
			
		} else {
			//System.out.println("is the leaf node");
			//System.out.print("" +((Employee)root.getUserData()).name + " is a childless node : \"");
		}} catch(Exception e) {
			System.out.print(e);
			return;
		}
		
		
	}
	
	public static void printTree(TreeNode root){
		Resource e = new Resource();

		if(root.hasChildren()) {
			//System.out.print("is a non-leaf node");
//			System.out.println(((Resource)root.getUserObject()).label);
			TreeNode[] children = root.children();
			for(int i=0; i<children.length ; i++){
				//System.out.println("		"+((Resource)children[i].getUserData()).label);
				printTree(children[i]);
			}
		} else {
//			System.out.println("		" +((Resource)root.getUserObject()).label);
			
		}
		
		
	}	
	
	public static String getID(String text){
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
