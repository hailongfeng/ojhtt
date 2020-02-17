package cn.ouju.htt.bean;

import java.util.List;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class StoreBean implements EntityImp {
    private int num;
    /**
     * id : 7
     * title : 商品2333
     * cate_name : 分类名称
     * pic_url : http://gfc.oss-cn-hongkong.aliyuncs.com/Upload/mall/201812/10/5c0dcade7b0e9.png?x-oss-process=image%2Fresize%2Cm_lfit%2Cw_1000&OSSAccessKeyId=LTAIHWKaPnNCFbeq&Expires=1544580395&Signature=Lx6OWyTZhUqHFNfZiOGY3slhI38%3D
     * price : 22.00
     * sell_number : 0
     * inventory_number : 122
     * limit_number : 0
     */

    private String id;
    private String title;
    private String cate_name;
    private String pic_url;
    private String price;
    private String sell_number;
    private String inventory_number;
    private String limit_number;
    /**
     * status : 1
     * detail : <html xmlns="http://www.w3.org/1999/xhtml"><head><meta http-equiv="Content-Type" content="text/html; charset=utf-8" /></head><style>img{max-width:100%;}</style><body><p>33</p></body></html>
     */

    private String status;
    private String detail;
    /**
     * user_no : M287
     * goods_id : 7
     * quantity : 2
     * add_time : 2018-12-12 11:13:25
     * update_time : 2018-12-12 11:13:31
     * new_price : 22.00
     * errors : []
     */

    private String user_no;
    private String goods_id;
    private String quantity;
    private String add_time;
    private String update_time;
    private String new_price;
    private List<String> errors;

    @Override
    public StoreBean newObject() {
        return new StoreBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        id = jsonUtils.getString("id");
        title = jsonUtils.getString("title");
        cate_name = jsonUtils.getString("cate_name");
        pic_url = jsonUtils.getString("pic_url");
        price = jsonUtils.getString("price");
        sell_number = jsonUtils.getString("sell_number");
        inventory_number = jsonUtils.getString("inventory_number");
        limit_number = jsonUtils.getString("limit_number");
        status = jsonUtils.getString("status");
        detail = jsonUtils.getString("detail");
        user_no = jsonUtils.getString("user_no");
        goods_id = jsonUtils.getString("goods_id");
        quantity = jsonUtils.getString("quantity");
        add_time = jsonUtils.getString("add_time");
        update_time = jsonUtils.getString("update_time");
        new_price = jsonUtils.getString("new_price");
        errors=jsonUtils.getStringList("errors");
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
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

    public String getSell_number() {
        return sell_number;
    }

    public void setSell_number(String sell_number) {
        this.sell_number = sell_number;
    }

    public String getInventory_number() {
        return inventory_number;
    }

    public void setInventory_number(String inventory_number) {
        this.inventory_number = inventory_number;
    }

    public String getLimit_number() {
        return limit_number;
    }

    public void setLimit_number(String limit_number) {
        this.limit_number = limit_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUser_no() {
        return user_no;
    }

    public void setUser_no(String user_no) {
        this.user_no = user_no;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getNew_price() {
        return new_price;
    }

    public void setNew_price(String new_price) {
        this.new_price = new_price;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
