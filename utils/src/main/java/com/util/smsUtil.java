package com.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class smsUtil {
    public static String sendSms(String phone,String code){
          String username = "a406748653";
          String pwd = "xt130414";

        String httpUrl = "http://api.smsbao.com/sms";
        String Content = "【盈满资管】您的验证码为"+code+"，"+"在1分钟内有效。";
        StringBuffer httpArg = new StringBuffer();
        httpArg.append("u=").append(username).append("&");
        httpArg.append("p=").append(md5(pwd)).append("&");
        httpArg.append("m=").append(phone).append("&");
        httpArg.append("c=").append(Content);
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(httpUrl+"?"+httpArg.toString(), String.class);
        return forObject;
    }
    public static String md5(String plainText) {
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }
    public static String encodeUrlString(String str, String charset) {
        String strret = null;
        if (str == null)
            return str;
        try {
            strret = java.net.URLEncoder.encode(str, charset);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return strret;
    }
}
