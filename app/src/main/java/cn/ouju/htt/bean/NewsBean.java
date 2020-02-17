package cn.ouju.htt.bean;


import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class NewsBean implements EntityImp {

    /**
     * id : 5
     * title : 关于交易中心在内测的公告
     * add_time : 2018-05-11 14:19:32
     */

    private String id;
    private String title;
    private String add_time;
    private String content;

    @Override
    public NewsBean newObject() {
        return new NewsBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        id = jsonUtils.getString("id");
        title = jsonUtils.getString("title");
        add_time = jsonUtils.getString("add_time");
        content = jsonUtils.getString("content");
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
