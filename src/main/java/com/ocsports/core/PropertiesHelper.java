package com.ocsports.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

/*
 * 
 * @created    September, 2014
 * @author     Paul Charlton
 */
public class PropertiesHelper {

	private static final String PROPS_FILE = "/oc-sports.properties";
	private static Properties props;
	private static Logger log = Logger.getLogger(PropertiesHelper.class.getName());
	private static Map poolWinners;
	private static Map lockWinners;
	private static Map survivorWinners;

	public static String getProperty(String propName) {
		String propValue = null;
		try {
			if (props == null) {
				props = new Properties();
//				props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPS_FILE));
				props.load( PropertiesHelper.class.getResourceAsStream(PROPS_FILE) );
			}
			if (propName == null) {
				throw new IllegalArgumentException("Illegal property name: null.");
			}

			propValue = props.getProperty(propName);
			if (propValue != null) {
				propValue = propValue.trim();
			}

			return propValue;
		} catch (IllegalArgumentException iae) {
			log.error(iae.getMessage());
		} catch (java.io.IOException ioe) {
			log.error(ioe.getMessage());
		}
		return propValue;
	}

	/**
	 * 
	 * @return  the league ID for the current default league
	 */
	public static int getDefaultLeagueId() {
		return Integer.valueOf(getProperty(PropList.DEFAULT_LEAGUE_ID)).intValue();
	}

	/**
	 * 
	 * @return  should the sign up link on the home page be shown
	 * @throws ProcessException 
	 */
	public static boolean isShowSignup() {
		String p = getProperty(PropList.SHOW_SIGNUP);
		return (p == null ? false : Boolean.valueOf(p).booleanValue());
	}

	public static Map getPoolWinnersList() {
		if (lockWinners == null) {
			loadPastWinners();
		}
		return poolWinners;
	}

	public static Map getLockWinnersList() {
		if (lockWinners == null) {
			loadPastWinners();
		}
		return lockWinners;
	}

	public static Map getSurvivorWinnersList() {
		if (lockWinners == null) {
			loadPastWinners();
		}
		return lockWinners;
	}

	private static void loadPastWinners() {
		poolWinners = new LinkedHashMap();
		survivorWinners = new LinkedHashMap();
		lockWinners = new LinkedHashMap();

		int currYear = DateHelper.getCurrentYear();
		for (int i = 0; i > -20; i--) {
			String prop = getProperty(PropList.POOL_WINNER_PREFIX + (currYear - i));
			if (prop != null && prop.length() > 0) {
				poolWinners.put(String.valueOf(currYear - i), prop);
			}

			prop = PropertiesHelper.getProperty(PropList.LOCKS_WINNER_PREFIX + (currYear - i));
			if (prop != null && prop.length() > 0) {
				lockWinners.put(String.valueOf(currYear - i), prop);
			}

			prop = PropertiesHelper.getProperty(PropList.SURVIVOR_WINNER_PREFIX + (currYear - i));
			if (prop != null && prop.length() > 0) {
				survivorWinners.put(String.valueOf(currYear - i), prop);
			}
		}
	}
}
