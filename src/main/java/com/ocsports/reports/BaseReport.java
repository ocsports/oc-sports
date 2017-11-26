package com.ocsports.reports;

import java.util.Date;


public abstract class BaseReport implements java.io.Serializable {
    // how much time can pass before we auto-set stale on this object
    public long expirationTime = 60l * 60l * 1000l; // 1 hour
    // timestamp when the build() method was last executed
    public Date lastUpdated = null;
    // flag indicating the data in this report instance is no longer valid
    public boolean stale = true;

    // flag to determine when the report goes stale; based on time and data
	public boolean isStale() {
        if(lastUpdated == null || stale) {
            // data is stale or the report has never been built
            return true;
        }
        // check the expiration time on the report
        long currTime = (new java.util.Date()).getTime();
        long timeSinceLastBuild = currTime - lastUpdated.getTime();
        return (timeSinceLastBuild >= expirationTime);
    }

    public boolean buildSuccessful() {
        lastUpdated = new java.util.Date();
        stale = false;
        return true;
    }

    public boolean buildFailed() {
        lastUpdated = null;
        stale = true;
        return false;
    }

    // used to gather data and build the report structure
    abstract boolean build();
    // create a duplicate object so we don't pass around the original copy
	abstract BaseReport copy();
}
