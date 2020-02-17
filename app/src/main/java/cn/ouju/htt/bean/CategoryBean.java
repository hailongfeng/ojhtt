package cn.ouju.htt.bean;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class CategoryBean implements EntityImp {

    /**
     * id : 8
     * title : 分类名称
     */

    private String id;
    private String title;
    private int flag;
    @Override
    public CategoryBean newObject() {
        return new CategoryBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        id = jsonUtils.getString("id");
        title = jsonUtils.getString("title");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
