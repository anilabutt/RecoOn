package au.edu.anu.cecs.explorer;

import java.util.ArrayList;

import au.edu.anu.cecs.explorer.tree.TreeNode;

public class WriteATree {
	
	ArrayList<String> hierarchy = new ArrayList<String>();
	TreeNode resource = new TreeNode();
	
//	public static void main(String[] args) {
//		String ontId = "http://schema.rdfs.org/all";
//		SerializedTree tree = new SerializedTree();	
//		
//		TreeNode root = new TreeNode();
//		root = tree.deSerializeData(ontId);
//		
//		
//		TreeNode node = getMatchedResource(root, "http://schema.org/ProductModel");
//		Resource res = (Resource)node.getUserObject();
//		System.out.println(res.uri + "     " + res.label);
//		
//		System.out.println(getPath(node));
//	}
	
	public String getTreeViewForOntology(String ontId,String resource){
		
		SerializedTree tree = new SerializedTree();	
		
		TreeNode root = new TreeNode();
		root = tree.deSerializeData(ontId);
		
		TreeNode node = getMatchedResource(root, resource);
//		Resource res = (Resource)node.getUserObject();
//		System.out.println(res.uri + "     " + res.label);
//		
		ArrayList<String> list = getPath(node);
		list.add(resource);
		
		String html="";
		html = "<div class=\"treeview\" id=\"treeview\" ont-id=\""+ontId+"\">";
		html = html + printTree(root, "class" , list);
		html = html + "</div>";
		//System.out.println(html);
		return html;
	}
	
	public String getObjectPropertyTreeViewForOntology(String ontId){
		
		SerializedObjectPropTree tree = new SerializedObjectPropTree();	
		
		TreeNode root = new TreeNode();
		root = tree.deSerializeData(ontId);
		
		ArrayList<String> list = new ArrayList<String>();
		String html="";
		html = "<div class=\"treeview\" id=\"OPropTree\" ont-id=\""+ontId+"\">";
		html = html + printTree(root, "property", list);
		html = html + "</div>";
//		System.out.println(html);
		return html;
	}
	
	public String getDatatypePropertyTreeViewForOntology(String ontId){
		
		ArrayList<String> list = new ArrayList<String>();
		SerializedDatatypePropTree tree = new SerializedDatatypePropTree();	
		
		TreeNode root = new TreeNode();
		root = tree.deSerializeData(ontId);
		
		String html="";
		html = "<div class=\"treeview\" id=\"DPropTree\" ont-id=\""+ontId+"\">\n";
		html = html + printTree(root, "property",list);
		html = html + "</div>";
		//System.out.println(html);
		return html;
	}
	
	public String printTree(TreeNode root, String type, ArrayList<String> list){

		String html = "";
		String label = ((au.edu.anu.cecs.explorer.tree.Resource)root.getUserObject()).label;
		if(label.contains("^")){
			label = label.split("\\^")[0];
		}
		String uri  =((au.edu.anu.cecs.explorer.tree.Resource)root.getUserObject()).uri;
		
		if(label.equalsIgnoreCase("")) {
			label=uri;
		}

		if(root.hasChildren()) {
			
			
			if(list.contains(uri)) {
				html = html + "<ul> <li class=\"\">\n"
						+ "<div>\n"
						+ "<p>\n";
				html = html + "<a class=\"sc\" onClick=\"return UnHide(this)\"> &#9660;</a>\n";
				html = html + "<a class=\"popper\" data-uri=\""+uri+"|" + type+"\" title=\""+uri+"\" data-popbox=\"pop\"><font color=\"#00008B\">"+label+"</font></a></p></div>\n";

			} else {	
				html = html + "<ul> <li class=\"cl\">\n"
						+ "<div>\n"
						+ "<p>\n";
				html = html + "<a class=\"sc\" onClick=\"return UnHide(this)\"> &#9658;</a>\n";
				html = html + "<a class=\"popper\" data-uri=\""+uri+"|" + type+"\" title=\""+uri+"\" data-popbox=\"pop\">"+label+"</a></p></div>\n";

			}
			
			TreeNode[] children = root.children();
			for(int i=0; i<children.length ; i++){
				html =html + printTree(children[i],type, list);
			}
			
			html =html + "</li> </ul>\n";
		
		} else {
//			System.out.println(label+"      " + uri);
			if(list.contains(uri)) {
				html = html + "<ul><li> <div> <p> <a class=\"i popper\" title=\""+uri+"\"  data-uri=\""+uri+"|" + type+"\" data-popbox=\"pop\"><font color=\"#00008B\">"+label+"</font></a> </p></div> </li></ul>\n";

			}else {
				html = html + "<ul><li> <div> <p> <a class=\"i popper\" title=\""+uri+"\"  data-uri=\""+uri+"|" + type+"\" data-popbox=\"pop\">"+label+"</a> </p></div> </li></ul>\n";
			}
		}
		return html;
		
	}	
	
	public TreeNode getMatchedResource(TreeNode root,  String res){

		if(root != null){
	        if((((au.edu.anu.cecs.explorer.tree.Resource)root.getUserObject()).uri).equalsIgnoreCase(res)){
	           return root;
	        } else {
	            if(root.hasChildren()) {
	        		TreeNode[] children = root.children();
	        		for(int i=0; i<children.length ; i++){
	        			TreeNode foundNode = getMatchedResource(children[i],res);
	        			if(foundNode !=null) {
	        				return foundNode;
	        			}
	        		}		
	        	} 
	           
	         }
	    }
		 return null;	
	}
	
	public ArrayList<String> getPath(TreeNode root){

		 ArrayList<String> list = new ArrayList<String>();
		 
		 TreeNode parent = root.getParent();
		 
		 while(parent != null) {
			 list.add(((au.edu.anu.cecs.explorer.tree.Resource)parent.getUserObject()).uri);
			 parent = parent.getParent();
		 }

		 return list;	
	}
	
}
