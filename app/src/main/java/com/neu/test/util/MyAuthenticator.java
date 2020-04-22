package com.neu.test.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * created by Viki on 2020/4/18
 * system login name : lg
 * created time : 16:32
 * email : 710256138@qq.com
 */
public class MyAuthenticator extends Authenticator {
    String userName = null;
    String password = null;
    public MyAuthenticator() {
    }
    public MyAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }

}
