package cn.ouju.htt.json;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class JsonUtils {
    public static final String SUCCESS = "200";
    public static final String EMPTY = "empty";
    public static final String ERROR = "error";
    public static final String KEY_CODE = "code";
    public static final String KEY_DATA = "data";
    public static final String KEY_MSG = "message";
    public static final String KEY_TIME = "time";
    public static final String FAILE = "201";
    public static final String MISS = "301";
    public static final String NOLOAD = "401";
    public static final String KEY_RESUIT = "result";

    private JSONObject mJsonObj;
    private String apiName;

    public JsonUtils() {
        this(null);
    }

    public JsonUtils(JSONObject object) {
        if (object == null) {
            mJsonObj = new JSONObject();
        } else {
            mJsonObj = object;
        }
        Log.d("-----------", mJsonObj.toString());
    }

    public final String getCode() {
        return "" + getInt(KEY_CODE);
    }

    public final String getMsg() {
        return getString(KEY_MSG);
    }


    public JSONObject getOrign() {
        return mJsonObj;
    }

    /**
     * 获取对象list
     *
     * @param key
     * @param t
     * @return
     */
    public <T extends EntityImp> List<T> getEntityList(String key, T t) {
        if (!mJsonObj.has(key)) {
            return null;
        }

        try {
            JSONArray jsArr = mJsonObj.getJSONArray(key);
            if (jsArr == null || jsArr.length() == 0) {
                return null;
            }
            List<T> res = new ArrayList<T>();
            T nt = t;
            for (int i = 0; i < jsArr.length(); i++) {
                if (nt == null) {
                    nt = t.newObject();
                }
                nt.paseFromJson(new JsonUtils(jsArr.getJSONObject(i)));
                res.add(nt);
                nt = null;
            }
            return res;
        } catch (JSONException e) {
            Log.d("解析异常", "解析异常");
        }
        return null;
    }


    /**
     * 获取对象list
     *
     * @param parentKey
     * @param childKey
     * @param t
     * @return
     */
    public <T extends EntityImp> List<T> getEntityList(String parentKey,
                                                       String childKey, T t) {
        try {
            JSONObject parentObj = mJsonObj.getJSONObject(parentKey);
            if (parentObj == null) {
                return null;
            }

            JSONArray jsArr = parentObj.getJSONArray(childKey);
            if (jsArr == null || jsArr.length() == 0) {
                return null;
            }
            List<T> res = new ArrayList<>();
            T nt = t;
            for (int i = 0; i < jsArr.length(); i++) {
                if (nt == null) {
                    nt = t.newObject();
                }
                nt.paseFromJson(new JsonUtils(jsArr.getJSONObject(i)));
                res.add(nt);
                nt = null;
            }
            return res;
        } catch (JSONException e) {
        }
        return null;
    }

    public <T extends EntityImp> T getEntity(T t) {
        T nt = t;

        nt.paseFromJson(new JsonUtils(mJsonObj));
        return nt;


    }

    public <T extends EntityImp> T getEntity(String key, T t) {
        T nt = t;
        try {
            JSONObject object = mJsonObj.getJSONObject(key);
            nt.paseFromJson(new JsonUtils(object));
            return nt;
        } catch (JSONException e) {
        }
        return null;

    }

    public <T extends EntityImp> T getEntity(String parentKey, String childKey,
                                             T t) {
        try {
            JSONObject parentObject = mJsonObj.getJSONObject(parentKey);
            if (parentObject == null) {
                return null;
            }
            T nt = t;
            JSONObject childObject = parentObject.getJSONObject(childKey);
            nt.paseFromJson(new JsonUtils(childObject));
            return nt;
        } catch (JSONException e) {
        }
        return null;

    }

    public int getInt(String key) {
        if (mJsonObj == null) {
            return Integer.MIN_VALUE;
        }
        if (mJsonObj.has(key)) {
            try {
                return mJsonObj.getInt(key);
            } catch (JSONException e) {
                return Integer.MIN_VALUE;
            }
        } else {
            return Integer.MIN_VALUE;
        }
    }

    public int getInt(String parentKey, String key) {
        if (mJsonObj == null) {
            return Integer.MIN_VALUE;
        }
        try {
            JSONObject jsonObject = mJsonObj.getJSONObject(parentKey);
            if (jsonObject.has(key)) {
                return jsonObject.getInt(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Integer.MAX_VALUE;
    }


    public String getString(String key) {

        if (mJsonObj.has(key)) {
            try {
                return mJsonObj.getString(key);
            } catch (JSONException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public String getString(String key, String parentKey) {
        try {
            JSONObject jsonObject = mJsonObj.getJSONObject(parentKey);
            if (jsonObject.has(key)) {
                return jsonObject.getString(key);
            } else {
                return "";
            }

        } catch (JSONException e) {
            return "";
        }
    }

    public double getDouble(String key) {
        if (mJsonObj.has(key)) {
            try {
                return mJsonObj.getDouble(key);
            } catch (JSONException e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public double getDouble(String parentKey, String key) {
        try {
            JSONObject jsonObject = mJsonObj.getJSONObject(parentKey);
            if (jsonObject.has(key)) {
                return jsonObject.getDouble(key);
            } else {
                return 0;
            }

        } catch (JSONException e) {
            return 0;
        }
    }

    public long getLong(String key) {
        if (mJsonObj.has(key)) {
            try {
                return mJsonObj.getLong(key);
            } catch (JSONException e) {
                return 0;
            }
        } else {
            return 0;
        }
    }


    public boolean getBoolean(String key) {
        if (mJsonObj.has(key)) {
            try {
                return mJsonObj.getBoolean(key);
            } catch (JSONException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public JSONObject getJSONObject(String key) {
        if (mJsonObj.has(key)) {
            try {
                return mJsonObj.getJSONObject(key);
            } catch (JSONException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public JSONArray getJSONArray(String key) {
        if (mJsonObj.has(key)) {
            try {
                return mJsonObj.getJSONArray(key);
            } catch (JSONException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public List<String> getStringList(String key) {
        ArrayList<String> list = new ArrayList<String>();
        if (mJsonObj.has(key)) {
            try {
                JSONArray array = mJsonObj.getJSONArray(key);
                for (int i = 0; i < array.length(); i++) {
                    String r = (String) array.get(i);
                    list.add(r);
                }
                return list;
            } catch (JSONException e) {
                return list;
            }
        } else {
            return list;
        }
    }
    public List<String> getStringList(String key, String parentKey) {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONObject jsonObject = mJsonObj.getJSONObject(parentKey);
            if (jsonObject.has(key)) {
                JSONArray array = jsonObject.getJSONArray(key);
                for (int i = 0; i < array.length(); i++) {
                    String r = (String) array.get(i);
                    list.add(r);
                }
                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * 获取接口名字
     *
     * @return
     */
    public String getApiName() {
        return apiName;
    }

    /**
     * 设置接口名字
     *
     * @param apiName
     */
    public void setApiName(String apiName) {
        this.apiName = apiName;
    }
}
