package cn.ouju.htt.bean;

import android.widget.ImageView;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class MeMenuBean implements EntityImp {
    private String tabs;
    private int img_res;

    @Override
    public MeMenuBean newObject() {
        return new MeMenuBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {

    }

    public String getTabs() {
        return tabs;
    }

    public void setTabs(String tabs) {
        this.tabs = tabs;
    }

    public int getImg_res() {
        return img_res;
    }

    public void setImg_res(int img_res) {
        this.img_res = img_res;
    }
}
