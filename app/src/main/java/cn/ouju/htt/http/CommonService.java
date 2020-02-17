package cn.ouju.htt.http;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import cn.ouju.htt.utils.LanguageUtils;
import cn.ouju.htt.utils.MD5;
import cn.ouju.htt.utils.SecurityUtils;
import cn.ouju.htt.utils.UserUtils;
import cn.ouju.htt.utils.VersionCodeUtils;

/**
 * Created by Administrator on 2017/9/8.
 */

public class CommonService extends BaseService {

    public static void query(String service, Map<String, Object> params, String url, HttpListener listener, Handler handler) {
        Protocol protocol = openHttpProtocol();
        protocol.setHandler(handler);
        protocol.setListener(listener);
        protocol.setService(service);
        protocol.setUrl(url);
        TreeMap<String, Object> treeMap = new TreeMap<>();
        if (params != null && params.size() > 0) {
            Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (!TextUtils.isEmpty(entry.getValue().toString())) {
                    treeMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        treeMap.put("device", "android");
        treeMap.put("timestamp", System.currentTimeMillis() / 1000);
        treeMap.put("version", VersionCodeUtils.getInstance().getVersionCode());
        String language = UserUtils.getString("language", null);
        if (!TextUtils.isEmpty(language)) {
            treeMap.put("language", language);
        } else {
            treeMap.put("language", LanguageUtils.getLanguage());
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(new Date(System.currentTimeMillis()));
        String api_token = "V1" + service.replace("/", "") + time + "7Es9PIa9zazPHlf3V6LoAO7lw0C0puM1";
        treeMap.put("api_token", MD5.md(api_token));
        StringBuffer sb = new StringBuffer();
        if (treeMap.size() > 0) {
            Iterator<Map.Entry<String, Object>> iterator = treeMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (!TextUtils.isEmpty(entry.getValue().toString())) {
                    protocol.addParams(entry.getKey(), entry.getValue());
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        String result = sb.toString().replaceFirst("&", "");
        String security = SecurityUtils.getHMAC(result);
        protocol.addParams("sign", "DTI_APP_API_" + security);
        protocol.post();
    }

    public static void upLoad(String service, Map<String, Object> params, String url, HttpListener listener, Handler handler, Map<String, File> file) {
        Protocol protocol = openHttpProtocol();
        protocol.setHandler(handler);
        protocol.setListener(listener);
        protocol.setService(service);
        protocol.setUrl(url);
        TreeMap<String, Object> treeMap = new TreeMap<>();
        if (params != null && params.size() > 0) {
            Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (!TextUtils.isEmpty(entry.getValue().toString())) {

                    treeMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        treeMap.put("device", "android");
        String language = UserUtils.getString("language", null);
        if (!TextUtils.isEmpty(language)) {
            treeMap.put("language", language);
        } else {
            treeMap.put("language", LanguageUtils.getLanguage());
        }
        treeMap.put("timestamp", (System.currentTimeMillis() / 1000));
        treeMap.put("version", VersionCodeUtils.getInstance().getVersionCode());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(new Date(System.currentTimeMillis()));
        String api_token = "V1" + service.replace("/", "") + time + "7Es9PIa9zazPHlf3V6LoAO7lw0C0puM1";
        treeMap.put("api_token", MD5.md(api_token));
        StringBuffer sb = new StringBuffer();
        if (treeMap.size() > 0) {
            Iterator<Map.Entry<String, Object>> iterator = treeMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (!TextUtils.isEmpty(entry.getValue().toString())) {
                    protocol.addParams(entry.getKey(), entry.getValue());
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        String result = sb.toString().replaceFirst("&", "");
        String security = SecurityUtils.getHMAC(result);
        protocol.addParams("sign", "DTI_APP_API_" + security);

        protocol.upload(file);
    }
}
