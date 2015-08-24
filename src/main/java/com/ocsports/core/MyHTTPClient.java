/*
 * Title         MyHTTPClient.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.core;
 */
package com.ocsports.core;

import java.io.*;
import java.util.zip.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.log4j.Logger;

public class MyHTTPClient {
    private static final int BYTES_LENGTH   = 1024*16;
    private static final Logger log         = Logger.getLogger( MyHTTPClient.class.getName() );
    
    public static String getURL( String url ) throws ProcessException {
        HttpMethod meth = null;
        try {
            meth = new GetMethod(url);
            meth.setRequestHeader( "Accept-Encoding", "gzip,compress" );

            HttpClient httpClient = new HttpClient();
            int rc = httpClient.executeMethod(meth);
            String body = MyHTTPClient.getResponseBody(meth);

            return body;
        }
        catch(java.net.ConnectException ce) {
            throw new ProcessException(ce);
        }
        catch(java.io.IOException ioe) {
            throw new ProcessException(ioe);
        }
        finally {
            if(meth != null) meth.releaseConnection();
        }
    }
    
    public static String getResponseBody(HttpMethod meth) throws java.io.IOException {
        Header encode = meth.getResponseHeader("Content-Encoding");
        String body = "";
        InputStream stream = null;

        if(encode == null)
            stream = new BufferedInputStream(meth.getResponseBodyAsStream());
        else if(encode.getValue().indexOf("gzip") >= 0)
            stream = new GZIPInputStream( meth.getResponseBodyAsStream() );
        else if(encode.getValue().indexOf("compress") >= 0)
            stream = new InflaterInputStream( meth.getResponseBodyAsStream() );
        else if(encode.getValue().indexOf("deflate") >= 0)
            stream = new InflaterInputStream( meth.getResponseBodyAsStream() );

        if(stream != null) {
            byte[] bytes = new byte[BYTES_LENGTH];
            StringBuffer buffer = new StringBuffer();
            int len;
            while( (len = stream.read(bytes)) > 0) {
                buffer.append( new String(bytes, 0, len) );
            }
            body = buffer.toString();
        }
        else {
            body = meth.getResponseBodyAsString();
        }

        return body;
    }
}
