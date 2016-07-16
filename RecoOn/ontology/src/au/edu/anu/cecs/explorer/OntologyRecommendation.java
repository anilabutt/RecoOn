/*
 * Copyright (c) 2015, ANU and/or its constituents or affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package au.edu.anu.cecs.explorer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONArray;

// Plain old Java Object it does not extend as class or implements 
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/stats")
public class OntologyRecommendation {
 

  // This method is called if TEXT_PLAIN is request
  @GET
  @Path("/tf")
  @Produces(MediaType.APPLICATION_JSON)
  public JSONArray getSubClasses(@QueryParam("uri") String classURI, @QueryParam("ontology") String ontologyId ) {

	  VirtuosoOntologyBrowsing query = new VirtuosoOntologyBrowsing();
	  JSONArray resultString = query.getSubClasses(classURI, ontologyId);
	 
	  return resultString;
  }
  
} 