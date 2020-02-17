package cn.ouju.htt.utils;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2017/9/7.
 */

public class SecurityUtils {
    public static String getHMAC(String params) {

        try {
            //加密密钥
            String secret = "HB5Igg1O1NxsXgTAkj1j5Fj3LGgTAFFP";
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            String hash = Base64.encode(sha256_HMAC.doFinal(params.getBytes()));
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
}
