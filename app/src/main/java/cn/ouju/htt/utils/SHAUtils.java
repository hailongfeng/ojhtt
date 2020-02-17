package cn.ouju.htt.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/9/7.
 */

public class SHAUtils {
    public static String getSHA(String str) {
        String result = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("SHA-1");
            md5.update(str.getBytes());
            byte[] b = md5.digest();
            result = bytes2Hex(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
