/*
 * Title         MyEmailer.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.core;
 */
package com.ocsports.core;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServletRequest;

import com.ocsports.models.UserModel;

public class MyEmailer {
    public static final String CONTENT_PLAIN       = "text/plain";
    public static final String CONTENT_HTML        = "text/html";

    //private static final String SMTP_HOST_NAME      = "10.10.32.137";
    //private static final String EMAIL_FROM_NAME     = "Football Draft";
    //private static final String EMAIL_FROM_ADDRESS  = "pcharlton@winecountrygiftbaskets.com";

    private static final String SMTP_HOST_NAME      = "mail.oc-sports.com";
    private static final String EMAIL_FROM_NAME     = "OC Sports Administrator";
    private static final String EMAIL_FROM_ADDRESS  = "administrator@oc-sports.com";
    private static final String DEFAULT_TO_ADDRESS  = "administrator@oc-sports.com";
    private static final boolean DEBUG_STATUS       = false;

    public static boolean sendEmailMsg(String[] toRecips, String[] ccRecips, String[] bccRecips, String subject, String message, String contentType) {
        boolean sendOk = false;
        try {
            Properties props = new Properties();
            props.put( "mail.host", SMTP_HOST_NAME );
            props.put( "mail.smtp.host", SMTP_HOST_NAME );
            props.put( "mail.smtp.auth", "true" );

            Session session = Session.getInstance( props, new EmailAuthenticator() );
            session.setDebug( DEBUG_STATUS );

            if( contentType == null || contentType.length() == 0 ) {
                contentType = CONTENT_PLAIN;
            }

            Message msg = new MimeMessage( session );
            msg.setSubject( subject );
            msg.setContent( message, contentType );
            msg.setSentDate( new java.util.Date() );

            InternetAddress fromAddress = new InternetAddress( EMAIL_FROM_ADDRESS, EMAIL_FROM_NAME );
            msg.setFrom( fromAddress );

            InternetAddress[] replyTo = new InternetAddress[] { fromAddress };
            msg.setReplyTo( replyTo );

            if( toRecips != null ) {
                InternetAddress[] addressTo = new InternetAddress[ toRecips.length ];
                for (int i = 0; i < toRecips.length; i++) {
                    addressTo[i] = new InternetAddress( toRecips[i] );
                }
                msg.setRecipients( Message.RecipientType.TO, addressTo );
            }
            if( ccRecips != null ) {
                InternetAddress[] addressTo = new InternetAddress[ ccRecips.length ];
                for (int i = 0; i < ccRecips.length; i++) {
                    addressTo[i] = new InternetAddress( ccRecips[i] );
                }
                msg.setRecipients( Message.RecipientType.CC, addressTo );
            }
            if( bccRecips != null ) {
                InternetAddress[] addressTo = new InternetAddress[ bccRecips.length ];
                for (int i = 0; i < bccRecips.length; i++) {
                    addressTo[i] = new InternetAddress( bccRecips[i] );
                }
                msg.setRecipients( Message.RecipientType.BCC, addressTo );
            }
            if( toRecips == null && ccRecips == null && bccRecips == null ) {
                msg.addRecipient( Message.RecipientType.TO, new InternetAddress(DEFAULT_TO_ADDRESS) );
            }
            Transport.send(msg);
            sendOk = true;
        }
        catch(Exception e) {
            System.out.println( e.getMessage() );
            e.printStackTrace();
        }
        return sendOk;
    }
    
    public static boolean sendForgotPwd_Plain(UserModel um) {
        String subject = "OC Sports - Login Information";

        StringBuffer msg = new StringBuffer();
        msg.append("Here is the login information you requested.");
        msg.append("\n\nLogin ID: " + um.getLoginId());
        msg.append("\nPassword: " + um.getLoginPwd());
        msg.append("\nEmail: " + um.getEmail());
        msg.append("\nEmail 2: " + um.getEmail2());
        msg.append("\n\nOC Sports Administrator");
        msg.append("\nadministrator@oc-sports.com");
        msg.append("\nwww.oc-sports.com");

        String[] toRecips = new String[] { um.getEmail() };
        String[] ccRecips = null;
        if(um.getEmail2() != null && !um.getEmail2().equals("")) {
            ccRecips = new String[] { um.getEmail2() };
        }

        return MyEmailer.sendEmailMsg(toRecips, ccRecips, null, subject, msg.toString(), MyEmailer.CONTENT_PLAIN );
    }

    public static boolean sendForgotPwd_Html(UserModel um, HttpServletRequest request) throws ProcessException {
        try {
            String subject = "OC Sports Password Request";
            String msgBody = null;

            msgBody = MyFile.getFileContents(request.getContextPath() + "/email/forgot_pwd.html");
            msgBody = msgBody.replaceAll( "##NAME##", um.getFullName() );
            msgBody = msgBody.replaceAll( "##LOGIN_ID##", um.getLoginId() );
            msgBody = msgBody.replaceAll( "##LOGIN_PWD##", um.getLoginPwd() );

            String[] toRecips = new String[] { um.getEmail() };
            String[] ccRecips = null;
            if(um.getEmail2() != null && !um.getEmail2().equals("")) {
                ccRecips = new String[] { um.getEmail2() };
            }

            return MyEmailer.sendEmailMsg(toRecips, ccRecips, null, subject, msgBody, MyEmailer.CONTENT_HTML );
        }
        catch(ProcessException pe) {
            throw pe;
        }
    }
}
