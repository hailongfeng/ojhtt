package cn.ouju.htt.bean;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class ReleaseBean implements EntityImp {

    /**
     * id : 2
     * user_no : M827
     * status : 0
     * amount : 500.55000000
     * add_time : 2018-09-27 11:53:50
     * return_number : 0
     * return_time : null
     * return_is_over : 0
     * order_no : null
     * return_amount : 0
     * minus_amount : 500.55
     */

    private String id;
    private String user_no;
    private String status;
    private String amount;
    private String add_time;
    private String return_number;
    private String return_time;
    private String return_is_over;
    private String order_no;
    private String return_amount;
    private String minus_amount;

    @Override
    public ReleaseBean newObject() {
        return new ReleaseBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        id = jsonUtils.getString("id");
        user_no = jsonUtils.getString("user_no");
        status = jsonUtils.getString("status");
        amount = jsonUtils.getString("amount");
        add_time = jsonUtils.getString("add_time");
        return_number = jsonUtils.getString("return_number");
        return_time = jsonUtils.getString("return_time");
        return_is_over = jsonUtils.getString("return_is_over");
        order_no = jsonUtils.getString("order_no");
        return_amount = jsonUtils.getString("return_amount");
        minus_amount = jsonUtils.getString("minus_amount");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_no() {
        return user_no;
    }

    public void setUser_no(String user_no) {
        this.user_no = user_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getReturn_number() {
        return return_number;
    }

    public void setReturn_number(String return_number) {
        this.return_number = return_number;
    }

    public String getReturn_time() {
        return return_time;
    }

    public void setReturn_time(String return_time) {
        this.return_time = return_time;
    }

    public String getReturn_is_over() {
        return return_is_over;
    }

    public void setReturn_is_over(String return_is_over) {
        this.return_is_over = return_is_over;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getReturn_amount() {
        return return_amount;
    }

    public void setReturn_amount(String return_amount) {
        this.return_amount = return_amount;
    }

    public String getMinus_amount() {
        return minus_amount;
    }

    public void setMinus_amount(String minus_amount) {
        this.minus_amount = minus_amount;
    }
}
