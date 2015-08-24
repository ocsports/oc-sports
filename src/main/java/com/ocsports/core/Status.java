/*
 * Title         Status.java
 * Created       July 30, 2009
 * Author        Paul Charlton
 * Modified      
 */
package com.ocsports.core;

public interface Status {
    public static final int PICK_HOME_TEAM              = 0;
    public static final int PICK_AWAY_TEAM              = 1;
    public static final int PICK_FAVORED                = 2;
    public static final int PICK_UNDERDOG               = 3;

    public static final int GAME_STATUS_WON             = 1;
    public static final int GAME_STATUS_LOST            = -1;
    public static final int GAME_STATUS_NO_DECISION     = 0;

    public static final int SURVIVOR_STATUS_WON         = 1;
    public static final int SURVIVOR_STATUS_LOST        = -1;
    public static final int SURVIVOR_STATUS_NO_DECISION = 0;

    public static final int LOCK_STATUS_WON             = 2;
    public static final int LOCK_STATUS_LOST            = -1;
    public static final int LOCK_STATUS_TIE             = 1;
    public static final int LOCK_STATUS_NO_DECISION     = 0;
}
