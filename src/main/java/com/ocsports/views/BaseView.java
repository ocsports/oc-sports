/*
 * Title         BaseModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.views;

public interface BaseView extends java.io.Serializable {
	
	abstract Object clone();
	abstract String lastUpdated();
	abstract boolean isStale();
}
