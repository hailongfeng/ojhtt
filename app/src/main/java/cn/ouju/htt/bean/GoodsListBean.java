package cn.ouju.htt.bean;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

/**
 * Created by Administrator on 2018/2/3.
 */

public class GoodsListBean implements EntityImp {

    private String id;
    private String title;
    private String pic_url;
    private String price;
    private String quantity;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public GoodsListBean newObject() {
        return new GoodsListBean();
    }


    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        id = jsonUtils.getString("id");
        title = jsonUtils.getString("title");
        pic_url = jsonUtils.getString("pic_url");
        price = jsonUtils.getString("price");
        quantity = jsonUtils.getString("quantity");
    }
}

