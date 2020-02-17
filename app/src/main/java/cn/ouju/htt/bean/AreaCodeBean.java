package cn.ouju.htt.bean;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class AreaCodeBean implements EntityImp {

    /**
     * name : Angola
     * chinese_name : 安哥拉
     * code : 244
     * is_default : 0
     */

    private String name;
    private String chinese_name;
    private String code;
    private String is_default;

    @Override
    public AreaCodeBean newObject() {
        return new AreaCodeBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        name = jsonUtils.getString("name");
        chinese_name = jsonUtils.getString("chinese_name");
        code = jsonUtils.getString("code");
        is_default = jsonUtils.getString("is_default");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChinese_name() {
        return chinese_name;
    }

    public void setChinese_name(String chinese_name) {
        this.chinese_name = chinese_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }
}
