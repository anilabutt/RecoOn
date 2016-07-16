/*
 * Copyright (c) 2015, ANU and/or its constituents or affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package au.edu.anu.cecs.explorer;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

//import au.edu.anu.cecs.explorer.aktiveRank.AktiveRankScore;



// Plain old Java Object it does not extend as class or implements 
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/find")
public class OntFinder {
 
  // This method is called if TEXT_PLAIN is request
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public JSONArray getOntologiesAsText(@QueryParam("word") List<String> keyword) {
	  
	  VirtuosoRepositoryQuery query = new VirtuosoRepositoryQuery();
	  JSONArray resultString = query.selectOntologiesAsJSON(keyword);	  
	  return resultString;
  }
  
//This method is called if TEXT_PLAIN is request
 @GET
 @Path("/relations")
 @Produces(MediaType.APPLICATION_JSON)
 public JSONObject getProfileOfClass(@QueryParam("uri") String classURI, @QueryParam("ontology") String ontologyId) {
	  
	  VirtuosoOntologyBrowsing query = new VirtuosoOntologyBrowsing();
	  JSONObject resultString = query.getProfileAsJSON(classURI, ontologyId);
	  
	  return resultString;
 }
  
  // This method is called if TEXT_PLAIN is request
  @GET
  @Path("/or")
  @Produces(MediaType.APPLICATION_JSON)
  public JSONArray getOntologiesWithQueryTermsAsText(@QueryParam("word") List<String> keyword) {
	  
	  VirtuosoRepositoryQuery query = new VirtuosoRepositoryQuery();
//	  JSONArray resultString = query.selectOntologiesWithQueryTermsAsJSON(keyword);
	  JSONArray resultString = query.selectRankedOntologiesAsJSON(keyword);
	  return resultString;
  }
  
////  // This method is called if TEXT_PLAIN is request
//  @GET
//  @Path("/aktive")
//  @Produces(MediaType.APPLICATION_JSON)
//  public JSONArray getOntologiesRankedActiveRank(@QueryParam("word") List<String> keyword) {
//	  
//	  VirtuosoRepositoryQuery query = new VirtuosoRepositoryQuery();
//	  JSONArray resultString = query.selectActiveRankScore(keyword);
//	  return resultString;
//  }
  
  // This method is called if TEXT_PLAIN is request
//  @GET
//  @Path("/aktive")
//  @Produces(MediaType.APPLICATION_JSON)
//  public JSONObject getOntologiesRankedActiveRank(@QueryParam("word") List<String> keyword) {
//	  
//	  AktiveRankScore query = new AktiveRankScore();
//	  JSONObject resultString = query.getResults(keyword);
//	  return resultString;
//  }
  
//This method is called if TEXT_PLAIN is request
// @GET
// @Path("/aktive2")
// @Produces(MediaType.APPLICATION_JSON)
// public JSONArray getOntologiesRankedActiveRankAsJSONArray(@QueryParam("word") List<String> keyword) {
//	  
//	  AktiveRankScore query = new AktiveRankScore();
//	  JSONArray resultString = query.getResultsAsJSONArray(keyword);
//	  return resultString;
// }
  
  // This method is called if TEXT_PLAIN is request
  @GET
  @Path("/subClasses")
  @Produces(MediaType.APPLICATION_JSON)
  public JSONArray getSubClasses(@QueryParam("uri") String classURI, @QueryParam("ontology") String ontologyId ) {

	  VirtuosoOntologyBrowsing query = new VirtuosoOntologyBrowsing();
	  JSONArray resultString = query.getSubClasses(classURI, ontologyId);
	 
	  return resultString;
  }
  
  // This method is called if TEXT_PLAIN is request
  @GET
  @Path("/treeView")
  @Produces(MediaType.APPLICATION_XML)
  public String getTreeViewForOntology(@QueryParam("ontology") String ontologyId, @QueryParam("resource") String resourceId) {
	//  String ontologyId="http://softlayer.dl.sourceforge.net/project/vivo/Ontology/vivo-core-1.1.owl";
	  WriteATree query = new WriteATree();
	  String html = query.getTreeViewForOntology(ontologyId, resourceId);
//	 System.out.println(html);
	  return html;
  }
  
  // This method is called if TEXT_PLAIN is request
  @GET
  @Path("/optreeView")
  @Produces(MediaType.APPLICATION_XML)
  public String getObjectPropertyTreeViewForOntology(@QueryParam("ontology") String ontologyId) {
	  
	  WriteATree query = new WriteATree();
	  String html = query.getObjectPropertyTreeViewForOntology(ontologyId);
	 
	  return html;
  }
  
  // This method is called if TEXT_PLAIN is request
  @GET
  @Path("/dptreeView")
  @Produces(MediaType.APPLICATION_XML)
  public String getDataPropertyTreeViewForOntology(@QueryParam("ontology") String ontologyId) {
	  
	  WriteATree query = new WriteATree();
	  String html = query.getDatatypePropertyTreeViewForOntology(ontologyId);
	 
	  return html;
  }
  
  // This method is called if TEXT_PLAIN is request
  @GET
  @Path("/popup")
  @Produces(MediaType.APPLICATION_XML)
  public String getPopUpForClass(@QueryParam("uri") String classURI, @QueryParam("ontology") String ontologyId) {
	//  String ontologyId="http://softlayer.dl.sourceforge.net/project/vivo/Ontology/vivo-core-1.1.owl";
	//  System.out.println("classURI : " + classURI + "	ontologyId :	" + ontologyId);
	  WriteProfile query = new WriteProfile();
	  String html = query.getProfile(classURI, ontologyId);	 
	//  System.out.println( "FROM getPopUpForClass " + html);
	  return html;
  }
  
} 