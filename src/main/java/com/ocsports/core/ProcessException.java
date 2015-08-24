/*
 * Title         ProcessException.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 */
package com.ocsports.core;

import java.io.*;
import java.util.HashMap;

public class ProcessException extends Exception {
    private String    message;
    private String    exceptionType;
    private Class     exceptionClass;
    private String    originalStackTrace;
    private HashMap   params;

    public ProcessException(Exception e) {
        this.message = e.getMessage();
        if(e instanceof ProcessException) {
            //There is a rare situation where a sub-class of ProcessException
            //may be passed into another ProcessException. Without the below
            //processing we lose the ability to distinguish what the true
            //subclass is, and thus end up with a plane old ProcessException.
            if(!e.getClass().getName().equals("com.ocsports.core.ProcessException")) {
                exceptionClass = e.getClass();
                exceptionType = exceptionClass.getName();
            }
            else {
                this.exceptionClass = ((ProcessException)e).getExceptionTypeClass();
                this.exceptionType = ((ProcessException)e).getExceptionType();
            }
            this.originalStackTrace = ((ProcessException)e).getStackTraceAsString();
            //this.nestedExceptions = ((ProcessException)e).getNestedExceptions();
        }
        else {
            exceptionClass = e.getClass();
            exceptionType = exceptionClass.getName();
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            PrintStream stackStream = new PrintStream(byteOut);
            e.printStackTrace(stackStream);

            this.originalStackTrace = byteOut.toString();
        }
    }

    public ProcessException(String message) {
        Exception e = new Exception(message);
        this.message = message;
        exceptionClass = e.getClass();
        exceptionType = exceptionClass.getName();

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        PrintStream stackStream = new PrintStream(byteOut);
        e.printStackTrace(stackStream);

        this.originalStackTrace = byteOut.toString();
    }

    public String getExceptionType() {
        return this.exceptionType;
    }
    
    public Class getExceptionTypeClass() {
        return this.exceptionClass;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(String _message) {
        this.message = _message;
    }

    public void addParams(HashMap _params) {
        this.params = _params;
    }

    public void addParam(String _key, String _value) {
        if(this.params == null) {
            this.params = new HashMap();
        }
        params.put(_key, _value);
    }
    
    public HashMap getParams() {
        return this.params;
    }
    
    public String getStackTraceAsString() {
        StringBuffer buffer = new StringBuffer();
        StackTraceElement[] stack = this.getStackTrace();
        for( int i=0,len=stack.length;i<len;i++) {
            buffer.append( stack[i] );
            if( buffer.lastIndexOf(")") == buffer.length() - 1 ) {
                buffer.append( "\nat " );
            }
        }
        return buffer.toString();
    }
}
