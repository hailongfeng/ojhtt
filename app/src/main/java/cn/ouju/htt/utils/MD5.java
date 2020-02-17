package cn.ouju.htt.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String md(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        MessageDigest mdt = null;
        try {
            mdt = MessageDigest.getInstance("MD5");
            byte b[] = mdt.digest(str.getBytes());
            String result = "";
            for (byte bs : b) {
                String temp = Integer.toHexString(bs & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;

            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
