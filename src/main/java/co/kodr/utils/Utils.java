package co.kodr.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import co.kodr.utils.db.DBConstants;

/**
 * Placeholder for common utility methods
 * 
 * @author anurag
 *
 */
public class Utils {
	private final static Logger logger = Logger.getLogger(Utils.class);

	public static Properties readConfiguration() {
		Properties config = new Properties();
		try {
			config.load(Utils.class.getClassLoader().getResourceAsStream(DBConstants.DB_PROPERTIES));
		} catch (IOException e) {
			logger.error("Error opening properties file:", e);
		}
		return config;
	}
}
