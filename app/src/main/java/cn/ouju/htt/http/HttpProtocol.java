package cn.ouju.htt.http;


import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.ouju.htt.constant.AppConstant;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/9/8.
 */

public class HttpProtocol extends Protocol {
    private OkHttpClient.Builder client = new OkHttpClient.Builder();
    private List<NameValuePair> params = new ArrayList<>();

    @Override
    void post() {
        listener.showProgress();
        StringBuffer sb = new StringBuffer();
        FormBody.Builder body = new FormBody.Builder();
        sb.append(getService());
        if (params != null && params.size() > 0) {
            for (NameValuePair nvp : params) {
                body.add(nvp.getName(), nvp.getValue());
                sb.append("&" + nvp.getName() + "=" + nvp.getValue());
            }
        }
        sb.replace(0, sb.length(), sb.toString().replaceFirst("&", "?"));

        String url = getUrl();
        if (url == null || "".equals(url)) {
            url = AppConstant.BASE_URL + sb.toString();
        }
        Log.d("---------service-url:", getService() + "----- " + url);
        RequestBody body1 = body.build();
        Request request = new Request.Builder().url(url).post(body1).build();
        if (getService().equals("Ltc/userSettleAccounts")) {
            client.connectTimeout(120, TimeUnit.SECONDS);
        } else {
            client.connectTimeout(30, TimeUnit.SECONDS);
        }

        Call call = client.build().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = handler.obtainMessage();
                msg.what = AppConstant.CONSTANT_NUMBER_ONE;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String result = response.body().string();
                    Message msg = handler.obtainMessage();
                    msg.what = AppConstant.CONSTANT_NUMBER_TWO;
                    List<Object> lists = new ArrayList<>();
                    lists.add(getService());
                    try {
                        JSONObject jo = new JSONObject(result);
                        JsonUtils json = new JsonUtils(jo);
                        String token = json.getString("token");
                        if (!TextUtils.isEmpty(token)) {
                            UserUtils.saveString("token", token);
                        }
                        lists.add(json);
                        msg.obj = lists;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Message msg = handler.obtainMessage();
                    msg.what = AppConstant.CONSTANT_NUMBER_ONE;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    @Override
    public Protocol addParams(String key, Object value) {
        getParams().add(new BasicNameValuePair(key, value.toString()));
        return this;
    }

    private List<NameValuePair> getParams() {
        return params;
    }

    MediaType mediaType = MediaType.parse("image/png");

    void upload(Map<String, File> file) {
        listener.showProgress();
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);
        if (params != null && params.size() > 0) {
            for (NameValuePair nvp : params) {
                multipartBody.addFormDataPart(nvp.getName(), nvp.getValue());
            }
        }
        Iterator<Map.Entry<String, File>> iterator = file.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, File> entry = iterator.next();
            multipartBody.addFormDataPart(entry.getKey(), entry.getValue().getName(), RequestBody.create(mediaType, entry.getValue()));
        }
        RequestBody requestBody = multipartBody.build();
        client.connectTimeout(60, TimeUnit.SECONDS);
        Request.Builder request = new Request.Builder();
        request.url(AppConstant.BASE_URL + getService() + "/");
        request.post(requestBody);
        Request request1 = request.build();
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.readTimeout(60, TimeUnit.SECONDS);
        client.writeTimeout(60, TimeUnit.SECONDS);
        Call call = client.build().newCall(request1);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = handler.obtainMessage();
                msg.what = AppConstant.CONSTANT_NUMBER_ONE;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String result = response.body().string();
                    Message msg = handler.obtainMessage();
                    msg.what = AppConstant.CONSTANT_NUMBER_TWO;
                    List<Object> lists = new ArrayList<>();
                    lists.add(getService());
                    try {
                        JSONObject jo = new JSONObject(result);
                        JsonUtils json = new JsonUtils(jo);
                        String token = json.getString("token");
                        if (!TextUtils.isEmpty(token)) {
                            UserUtils.saveString("token", token);
                        }
                        lists.add(json);
                        msg.obj = lists;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Message msg = handler.obtainMessage();
                    msg.what = AppConstant.CONSTANT_NUMBER_ONE;
                    handler.sendMessage(msg);
                }
            }
        });
    }
}
