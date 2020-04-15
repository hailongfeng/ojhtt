package cn.ouju.htt.utils;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.blankj.utilcode.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.ouju.htt.R;
import cn.ouju.htt.constant.AppConstant;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.v2.utils.Constant;

public class UpdateApp {
    private static DownloadBuilder builder;
    private static boolean b;

    public static void update(final Activity activity, final int flag, final JsonUtils jsonUtil) {
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(new Date(System.currentTimeMillis()));
        String api_token = "V1HomegetVersion" + time + "7Es9PIa9zazPHlf3V6LoAO7lw0C0puM1";
        sb.append("api_token=" + MD5.md(api_token));
        sb.append("&device=android");
        sb.append("&language=" + LanguageUtils.getLanguage());
        sb.append("&timestamp=" + "" + (System.currentTimeMillis() / 1000));
        sb.append("&version=" + VersionCodeUtils.getInstance().getVersionName());
        String security = SecurityUtils.getHMAC(sb.toString());
        String str = AppConstant.BASE_URL + "Home/getVersion/?device=android&language=" + LanguageUtils.getLanguage()+"&version=" +
                VersionCodeUtils.getInstance().getVersionName()  + "&timestamp=" + (System.currentTimeMillis() / 1000) + "&sign=DTI_APP_API_" + security + "&api_token=" + MD5.md(api_token);
        Log.d("-------", "------" + str);
        if (flag == 0) {
            builder = AllenVersionChecker.getInstance()
                    .requestVersion()
                    .setRequestUrl(str)
                    .request(new RequestVersionListener() {
                        @Nullable
                        @Override
                        public UIData onRequestVersionSuccess(DownloadBuilder downloadBuilder, String result) {
                            JsonUtils jsonUtils;
                            try {
                                LogUtils.d("onRequestVersionSuccess : "+result);
                                JSONObject jsonObject = new JSONObject(result);
                                jsonUtils = new JsonUtils(jsonObject);
                                if (jsonUtils.getCode().equals("501")) {
                                    b = true;
                                    return UIData.create().setTitle(activity.getString(R.string.news_version)).setContent(jsonUtils.getString("intro", "data")).setDownloadUrl(jsonUtils.getString("url", "data"));
                                } else if (jsonUtils.getCode().equals("200")) {
                                    b = false;
                                    String str = jsonUtils.getString("is_need_upgrade", "data");
                                    if (str.equals("1")) {
                                        return UIData.create().setTitle(activity.getString(R.string.news_version)).setContent(jsonUtils.getString("intro", "data")).setDownloadUrl(jsonUtils.getString("url", "data"));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        public void onRequestVersionFailure(String message) {
                            LogUtils.e("onRequestVersionFailure : "+message);
                        }
            });
            builder.setForceRedownload(true);
            if (b) {
                builder.setForceUpdateListener(new ForceUpdateListener() {
                    @Override
                    public void onShouldForceUpdate() {
                        activity.finish();
                    }
                });
            }

        } else {
            builder = AllenVersionChecker.getInstance()
                    .requestVersion()
                    .setRequestUrl(str)
                    .request(new RequestVersionListener() {
                @Nullable
                @Override
                public UIData onRequestVersionSuccess(DownloadBuilder downloadBuilder, String result) {
                    return UIData.create().setTitle(activity.getString(R.string.news_version)).setContent(jsonUtil.getString("intro", "data")).setDownloadUrl(jsonUtil.getString("url", "data"));
                }

                @Override
                public void onRequestVersionFailure(String message) {
                    LogUtils.e("onRequestVersionFailure : "+message);
                }
            });
            builder.setForceRedownload(true);
            builder.setForceUpdateListener(new ForceUpdateListener() {
                @Override
                public void onShouldForceUpdate() {
                    activity.finish();
                }
            });
        }
        builder.executeMission(activity);
        //builder.excuteMission(activity);
    }


    public static void update2(final Activity activity, final int flag, final JsonUtils jsonUtil) {
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(new Date(System.currentTimeMillis()));
        String api_token = "V1HomegetVersion" + time + "7Es9PIa9zazPHlf3V6LoAO7lw0C0puM1";
        sb.append("api_token=" + MD5.md(api_token));
        sb.append("&device=android");
        sb.append("&language=" + LanguageUtils.getLanguage());
        sb.append("&timestamp=" + "" + (System.currentTimeMillis() / 1000));
        sb.append("&version=" + VersionCodeUtils.getInstance().getVersionName());
        String security = SecurityUtils.getHMAC(sb.toString());
        String str = Constant.BASEURL + "Home/getVersion/?device=android&language=" + LanguageUtils.getLanguage()+"&version=" +
                VersionCodeUtils.getInstance().getVersionName()  + "&timestamp=" + (System.currentTimeMillis() / 1000) + "&sign=DTI_APP_API_" + security + "&api_token=" + MD5.md(api_token);
        Log.d("-------", "------" + str);
        if (flag == 0) {
            builder = AllenVersionChecker.getInstance()
                    .requestVersion()
                    .setRequestUrl(str)
                    .request(new RequestVersionListener() {
                        @Nullable
                        @Override
                        public UIData onRequestVersionSuccess(DownloadBuilder downloadBuilder, String result) {
                            JsonUtils jsonUtils;
                            try {
                                LogUtils.d("onRequestVersionSuccess : "+result);
                                JSONObject jsonObject = new JSONObject(result);
                                jsonUtils = new JsonUtils(jsonObject);
                                if (jsonUtils.getCode().equals("501")) {
                                    b = true;
                                    return UIData.create().setTitle(activity.getString(R.string.news_version)).setContent(jsonUtils.getString("intro", "data")).setDownloadUrl(jsonUtils.getString("url", "data"));
                                } else if (jsonUtils.getCode().equals("200")) {
                                    b = false;
                                    String str = jsonUtils.getString("is_need_upgrade", "data");
//                                  String downloadFile="https://www.xbext.com/download/xbrowser-release.apk";
                                    String downloadFile=jsonUtils.getString("url", "data");
                                    if (str.equals("1")) {
                                        return UIData.create()
                                                .setTitle(activity.getString(R.string.news_version))
                                                .setContent(jsonUtils.getString("intro", "data"))
                                                .setDownloadUrl(downloadFile);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        public void onRequestVersionFailure(String message) {
                            LogUtils.e("onRequestVersionFailure : "+message);
                        }
                    });
            builder.setForceRedownload(true);
            //if (b) {
                builder.setForceUpdateListener(new ForceUpdateListener() {
                    @Override
                    public void onShouldForceUpdate() {
                        activity.finish();
                    }
                });
            //}

        } else {
            builder = AllenVersionChecker.getInstance()
                    .requestVersion()
                    .setRequestUrl(str)
                    .request(new RequestVersionListener() {
                        @Nullable
                        @Override
                        public UIData onRequestVersionSuccess(DownloadBuilder downloadBuilder, String result) {
                            return UIData.create().setTitle(activity.getString(R.string.news_version)).setContent(jsonUtil.getString("intro", "data")).setDownloadUrl(jsonUtil.getString("url", "data"));
                        }

                        @Override
                        public void onRequestVersionFailure(String message) {
                            LogUtils.e("onRequestVersionFailure : "+message);
                        }
                    });
            builder.setForceRedownload(true);
            builder.setForceUpdateListener(new ForceUpdateListener() {
                @Override
                public void onShouldForceUpdate() {
                    activity.finish();
                }
            });
        }
        builder.executeMission(activity);
    }
}