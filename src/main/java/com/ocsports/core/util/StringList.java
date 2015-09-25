/*
 * Title         StringList.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.core.util;
 */
package com.ocsports.core.util;

public class StringList {
    
    public static String removeKeyFromList(String oldList, String key) {
        String newList = "";
        if(oldList != null || !oldList.equals("")) {
            int n = oldList.indexOf(",");
            while( n > 0 ) {
                String oldKey = oldList.substring(0, n);
                if( !oldKey.equals(key) ) {
                    StringList.addKeyToList(newList, oldKey);
                }
                oldList = oldList.substring(n+1);
                n = oldList.indexOf(",");
            }
            newList += oldList;
        }
        return newList;
    }
    
    public static String addKeyToList(String list, String key) {
        if(list == null || list.equals("")) {
            list = key;
        } else {
            list += "," + key;
        }
        return list;
    }
}
