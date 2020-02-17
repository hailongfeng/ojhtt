package cn.ouju.htt.bean;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class MiningBean implements EntityImp {

    /**
     * number : 570.00000000
     * add_time : 2018-12-20 15:55:39
     */

    private String number;
    private String add_time;

    @Override
    public MiningBean newObject() {
        return new MiningBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        number = jsonUtils.getString("number");
        add_time = jsonUtils.getString("add_time");
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
