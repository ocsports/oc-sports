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

    public PasswordAuthentication getPasswordAuthentication() {
        String smtpUser = PropertiesHelper.getProperty(PropList.SMTP_USER);
        String smtpPwd = PropertiesHelper.getProperty(PropList.SMTP_PWD);
        return new PasswordAuthentication(smtpUser, smtpPwd);
    }
}
