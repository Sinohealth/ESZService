package com.sinohealth.eszservice.service.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator{
	
	String userName = null;  
    String password = null;  
  
    public MailAuthenticator() {  
    }  
  
    public MailAuthenticator(String userName, String password) {  
        this.userName = userName;  
        this.password = password;  
    }  
  
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);  
    }  

}
