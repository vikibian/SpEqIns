package com.neu.test.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.neu.test.activity.LoginActivity;
import com.neu.test.activity.SplashActivity;

/**
 * created by Viki on 2020/4/18
 * system login name : lg
 * created time : 16:36
 * email : 710256138@qq.com
 */
public class SendMailUtil {
    private static final String TAG = "SendMailUtil";
    //qq
//    private static final String HOST = "smtp.qq.com";
//    private static final String PORT = "465";// 587
//    private static final String FROM_ADD = "2152234494@qq.com"; //发送方邮箱
//    private static final String FROM_PSW = "inyjfzsajlxqebji";//发送方邮箱授权码

//    //163
//    private static final String HOST = "smtp.163.com";
//    private static final String PORT = "25"; //或者465  994
//    private static final String FROM_ADD = "wkbian@163.com";
//    private static final String FROM_PSW = "YIFIHEVAGYCEMUPC";
//    private static final String TO_ADD = "2584770373@qq.com";


    public static boolean send(String toAdd, String subject, String content){
        boolean flag = false;
        final MailInfo mailInfo = creatMail(toAdd,subject,content);
        final MailSender sms = new MailSender();
        flag = sms.sendHtmlMail(mailInfo);

        return flag;
    }

    @NonNull
    private static MailInfo creatMail(String toAdd, String subject, String content) {
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(LoginActivity.emailInfo.getHOST());
        mailInfo.setMailServerPort(LoginActivity.emailInfo.getPORT());
        mailInfo.setValidate(true);
        mailInfo.setUserName(LoginActivity.emailInfo.getFROMADD()); // 你的邮箱地址
        mailInfo.setPassword(LoginActivity.emailInfo.getFROMPSW());// 您的邮箱密码
        mailInfo.setFromAddress(LoginActivity.emailInfo.getFROMADD()); // 发送的邮箱
        mailInfo.setToAddress(toAdd); // 发到哪个邮件去
        mailInfo.setSubject(subject); // 邮件主题
        mailInfo.setContent(content); // 邮件文本
        return mailInfo;
    }
}

