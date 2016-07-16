package au.edu.anu.cecs.explorer;

import java.util.HashMap;
import java.util.Map;

import jdbm.PrimaryTreeMap;


public class WriteProfile {
	
	
	public String getProfile(String classId, String ontologyId) {
		String html="";
		String[] tokens;
		String id ="";
		String type="";
		if(classId.contains("|")) {
			tokens= classId.split("\\|") ;
			id = tokens[0];
			type = tokens[1];
		} else {
			id =  classId;
			type = "class";
		}
		
		
		if(type.equalsIgnoreCase("class")) {
//			System.out.println("GET Profile of a class");			
			html = this.getRelationsForClass(id, ontologyId);
			
		} else if(type.equalsIgnoreCase("property")) {
//			System.out.println("GET Profile of a property");
			html = this.getDomainRangeForProperty(id, ontologyId);
		}
		
		return html;
	}
	
public String getRelationsForClass(String classId, String ontologyId){
		
		ProfileMap profileMapStruct = ProfileMap.getDefaultMap();
		PrimaryTreeMap<String, HashMap<String,HashMap<String, String>>> classProfileMap = profileMapStruct.get_profile_map();
		
		String html="";
		html = "<h2>"+classId+"</h2>";
		html = html+ "<table width=\"100%\" cellspacing=\"5\" cellpadding=\"5\">";
		
		String innerHtml=""; String _innerHtml="";
		if(classProfileMap.containsKey(ontologyId)) {
			
			HashMap<String,HashMap<String, String>> classProfileOfOntology  = classProfileMap.get(ontologyId);
			
			if(classProfileOfOntology.containsKey(classId)) {
				
				HashMap<String, String> classProfile = classProfileOfOntology.get(classId);

				if(classProfile.size()<1) {
					html = html + "<tr hight=\"20\" bgcolor=\"#F2F2F2\">" +
							"<td> <font color=\"#333333\"> No attributes defined for this class </font> </td>" +
							"</tr> ";
				} else {				
					_innerHtml = "<tr hight=\"20\" bgcolor=\"#F2F2F2\">" +
						"<td> <font color=\"#333333\"> <b>"+ "Property" +" </b></font> </td>" +
						"<td> <font color=\"#333333\"> <b>" + "Rang" +" </b></font> </td>"+
						"</tr> ";
					innerHtml = innerHtml +_innerHtml  ;
				}
				
//				String label =  classProfile.get("label");
//				String comment = classProfile.get("comment");
				
				try{
					for(Map.Entry<String, String> entry: classProfile.entrySet()) {
						String key = entry.getKey();
						String value = entry.getValue();
						String property, propLabel, range, rangeLabel;
						
//						if(key.equalsIgnoreCase("label") || key.equalsIgnoreCase("comment")) {
//							System.out.println("key " + key );
//						} else {
							propLabel = key;
							rangeLabel = value;
							
							if(key.contains("|")) {
								//System.out.println();
								String[] keys= key.split("\\|");
								property = keys[0];
								propLabel = removeLanguageTypeTags(keys[keys.length-1]);
							} else {
								property = "";
								propLabel = "";
							}
//							
						
//						
							if(value.equalsIgnoreCase("|")) {
								range = "";
								rangeLabel = "";
							} else {
								if(value.contains("|")) {
									String[] labels= value.split("\\|");
									range = labels[0];
									rangeLabel =removeLanguageTypeTags(labels[labels.length-1]);
								} else {
									range = "";
									rangeLabel = "";
								}
							}
							
							if (propLabel.equalsIgnoreCase("") && rangeLabel.equalsIgnoreCase("")) {
								
							} else {
						
//								System.out.println("Property " + propLabel + "rangeLabel " + rangeLabel );
								innerHtml = innerHtml + "<tr hight=\"20\" bgcolor=\"#F2F2F2\">" +
									"<td> <font color=\"#333333\">"+ propLabel +"</font> </td>" +
									"<td> <font color=\"#333333\">" + rangeLabel +"</font> </td>"+
									"</tr> ";
								}
							}
					if(innerHtml.equalsIgnoreCase(_innerHtml)) {
						 html = html + "<tr hight=\"20\" bgcolor=\"#F2F2F2\">" +
								"<td> <font color=\"#333333\"> No attributes defined for this class </font> </td>" +
								"</tr> ";
					} else {
						html = html +innerHtml;
					}
//						}
					} catch(Exception e) {
						System.out.println("Exception" + e);
					}			
			} else {
				html = html + "<tr hight=\"20\" bgcolor=\"#F2F2F2\">" +
						"<td> <font color=\"#333333\"> No attributes defined for this class </font> </td>" +
						"</tr> ";
			}
			
		} else {
			html = html + "<tr hight=\"20\" bgcolor=\"#F2F2F2\">" +
					"<td> <font color=\"#333333\"> No attributes defined for this class </font> </td>" +
					"</tr> ";
		}
		
		html = html + "</table>";

		return html;
	}
	

	public String getDomainRangeForProperty(String propertyId, String ontologyId){
		
		PropertyProfileMap profileMapStruct = PropertyProfileMap.getDefaultMap();
		PrimaryTreeMap<String, HashMap<String,HashMap<String, HashMap<String, String>>>> propertyProfileMap = profileMapStruct.get_profile_map();
		
		String html="";
		html = "<h2>"+propertyId+"</h2>";
		html = html+ "<table width=\"100%\" cellspacing=\"5\" cellpadding=\"5\">";
		
		if(propertyProfileMap.containsKey(ontologyId)) {
			
			HashMap<String,HashMap<String, HashMap<String, String>>> propertyProfileOfOntology  = propertyProfileMap.get(ontologyId);
			
			if(propertyProfileOfOntology.containsKey(propertyId)) {
				
				
				HashMap<String, HashMap<String,String>> propertyProfile = propertyProfileOfOntology.get(propertyId);

				HashMap<String,String> domains =  propertyProfile.get("domain");
				HashMap<String,String> ranges = propertyProfile.get("range");
				
				html = html + "<tr hight=\"20\" bgcolor=\"#F2F2F2\">" +
						"<td> <font color=\"#333333\">";
				try{
					for(Map.Entry<String, String> entry: domains.entrySet()) {
						String key = entry.getKey();
						String value = entry.getValue();
						if (value.equalsIgnoreCase("")) {
							html = html+ key +"</br>";
						} else {
							html = html+ value +"</br>";
						}

					}
					
					html = html + "</font> </td>" ;
					
					html = html + "<td> <font color=\"#333333\">";
					
					for(Map.Entry<String, String> entry: ranges.entrySet()) {
						String key = entry.getKey();
						String value = entry.getValue();
						if (value.equalsIgnoreCase("")) {
							html = html+ key +"</br>";
						} else {
							html = html+ value +"</br>";
						}
					}
						
					html = html + "</font> </td>" ;
								
					html = html + "</tr> ";
					

				} catch(Exception e) {
					System.out.println("Exception" + e);
				}
			}
			
			html = html + "</table>";
		}

		return html;
	}
	
	public String removeLanguageTypeTags(String aString) {
		String value ="";
		
		if(aString.contains("^")) {
			value = aString.split("\\^")[0];
		} else if (aString.contains("@")) {
			value = aString.split("@")[0];
		} else if (aString.contains("#")) {
			String[] splitString = aString.split("#");
			value = splitString[splitString.length-1];
		} else {
			value =aString;
		}
		
		return value;
	}
	
}