package cn.ouju.htt.http;

import android.os.Handler;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/8.
 */

public abstract class Protocol {
    private String url;
    private String service;
    public HttpListener listener;
    public Handler handler;

    abstract void post();

    abstract void upload(Map<String, File> file);

    public abstract Protocol addParams(String key, Object value);

    public String getUrl() {
        return url;
    }

    public String getService() {
        return service;
    }

    public Protocol setUrl(String url) {
        this.url = url;
        return this;
    }


    public void setService(String service) {
        this.service = service;
    }

    public void setListener(HttpListener listener) {
        this.listener = listener;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
