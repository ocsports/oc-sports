/*
 * Title         EmailAuthenticator.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.core;
 */
package com.ocsports.core;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EmailAuthenticator extends Authenticator {
    private static final String SMTP_AUTH_USER = "administrator@oc-sports.com";
    private static final String SMTP_AUTH_PWD  = "**Chicken99";

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
    }
}
