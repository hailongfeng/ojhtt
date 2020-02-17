package cn.ouju.htt.bean;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class EmailBean implements EntityImp {

    /**
     * id : 3
     * title : asdasda
     * content : <p><img src="/Upload/editor/20180702/1530521470639925.png" title="1530521470639925.png" alt="我的二维码.png"/>zhe ceshi yixia&nbsp;</p>
     * send_time : 2018-07-02 16:51:35
     */

    private String id;
    private String title;
    private String content;
    private String send_time;

    @Override
    public EmailBean newObject() {
        return new EmailBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        id = jsonUtils.getString("id");
        title = jsonUtils.getString("title");
        content = jsonUtils.getString("content");
        send_time = jsonUtils.getString("send_time");
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }
}
