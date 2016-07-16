/*
 * Copyright (c) 2015, ANU and/or its constituents or affiliates. All rights reserved.
 * Use is subject to license terms.
 */

package au.edu.anu.cecs.explorer;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Configuration interface for this API.
 * <p>
 * Usage Example: Configuration.getProperty(Configuration.STORE_PATH)
 * <p>
 * See {@link Configuration#getProperty(String)}
 * 
 * @author anila butt
 */
public class Configuration {
	
	/** Property key for virtuoso instance */
	public static final String VIRTUOSO_INSTANCE = "virtuoso.instance";
	
	/** Property key for virtuoso port */
	public static final String VIRTUOSO_PORT = "virtuoso.port";
	
	/** Property key for virtuoso username */
	public static final String VIRTUOSO_USERNAME = "virtuoso.username";
	
	/** Property key for virtuoso password */
	public static final String VIRTUOSO_PASSWORD = "virtuoso.password";
	
	/** Default instance of this class */
	private static Configuration instance;
	
	/** Default logger */
	private Logger logger;
	
	/** Default properties */
	private Properties properties = new Properties();
	
	/**
	 * External classes should invoke static methods
	 * instead of instantiating this class.
	 * 
	 * @see #getProperty(String)
	 */
	private Configuration() {
		logger = Logger.getLogger(getClass().getName());
		try {
			// Try loading configuration file otherwise fall back to a default path
			properties.load(getClass().getResourceAsStream("config.properties"));
		} catch (IOException iox) {
			//properties.setProperty(STORE_PATH, "/home/anila/browser/store");
			logger.severe("Error in reading store configuration "+ iox);
		}
	}
	
	/**
	 * Returns default configuration object.
	 */
	private static Configuration getDefaults() {
		if(instance==null) {
			instance = new Configuration();
		}
		return instance;
	}
	
	/**
	 * Gets value of the specified configuration property.
	 * 
	 * @param key Configuration key
	 */
	public static String getProperty(String key) {
		Configuration config = Configuration.getDefaults();
		return config.properties.getProperty(key);
	}
}
