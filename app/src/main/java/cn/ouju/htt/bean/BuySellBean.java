package cn.ouju.htt.bean;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class BuySellBean implements EntityImp {

    /**
     * trade_id : 5
     * user_no : M571
     * phone : 15617133108
     * number : 150
     * price : 1.25
     * amount : 1312.50
     * status : 0
     * add_time : 2018-09-27 14:34:27
     */

    private String trade_id;
    private String user_no;
    private String phone;
    private String number;
    private String price;
    private String amount;
    private String status;
    private String add_time;
    private String type;

    @Override
    public BuySellBean newObject() {
        return new BuySellBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        trade_id = jsonUtils.getString("trade_id");
        user_no = jsonUtils.getString("user_no");
        phone = jsonUtils.getString("phone");
        number = jsonUtils.getString("number");
        price = jsonUtils.getString("price");
        amount = jsonUtils.getString("amount");
        status = jsonUtils.getString("status");
        add_time = jsonUtils.getString("add_time");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }

    public String getUser_no() {
        return user_no;
    }

    public void setUser_no(String user_no) {
        this.user_no = user_no;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
