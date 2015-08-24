/*
 * Title         MyTextFile.java
 * Created       July 30, 2009
 * Author        Paul Charlton
 * Modified      7/30/2009 - created in package com.ocsports.core;
 */
package com.ocsports.core;

import java.io.*;

public class MyFile {

    public static String getFileContents(String myfile) throws ProcessException {
        try {
            File f = new File(myfile);
            BufferedReader br = new BufferedReader( new FileReader(f) );

            StringBuffer contents = new StringBuffer();
            String text = null;
            while( (text = br.readLine()) != null ) {
                contents.append( text );
                contents.append( System.getProperty("line.separator") );
            }

            return contents.toString();
        }
        catch (FileNotFoundException e) {
            throw new ProcessException(e);
        } 
        catch (IOException e) {
            throw new ProcessException(e);
        } 
    }
}
