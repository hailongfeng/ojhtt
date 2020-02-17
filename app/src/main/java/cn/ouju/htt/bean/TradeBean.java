package cn.ouju.htt.bean;


import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class TradeBean implements EntityImp {

    /**
     * trade_id : 交易ID
     * user_no : 用户编号
     * number : 数量
     * price : 单价
     * amount : 总价
     * status : 状态，0：进行中；1：达成意向；2：发起者取消；3：管理员取消
     */

    private String trade_id;
    private String user_no;
    private String number;
    private String price;
    private String amount;
    private String status;
    private int type;
    private String phone;
    private int buy_sell;
    /**
     * add_time : 时间
     */

    private String add_time;
    /**
     * order_id : 交易ID
     * order_no : 交易编号
     * buy_uno : 买家编号
     * buy_phone : 买家手机号
     * sale_uno : 卖家编号
     * sale_phone : 卖家手机号
     * cny_amount : 交易金额（人民币）
     * usd_amount : 交易金额（美元）
     * pay_time : 支付时间
     * operate_time : 操作时间
     * picture : 支付凭证
     */

    private String order_id;
    private String order_no;
    private String buy_uno;
    private String buy_phone;
    private String sale_uno;
    private String sale_phone;
    private String cny_amount;
    private String usd_amount;
    private String pay_time;
    private String operate_time;
    private String picture;

    @Override
    public TradeBean newObject() {
        return new TradeBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        trade_id = jsonUtils.getString("trade_id");
        user_no = jsonUtils.getString("user_no");
        number = jsonUtils.getString("number");
        price = jsonUtils.getString("price");
        amount = jsonUtils.getString("amount");
        status = jsonUtils.getString("status");
        add_time = jsonUtils.getString("add_time");
        phone = jsonUtils.getString("phone");
        order_id = jsonUtils.getString("order_id");
        order_no = jsonUtils.getString("order_no");
        buy_uno = jsonUtils.getString("buy_uno");
        buy_phone = jsonUtils.getString("buy_phone");
        sale_uno = jsonUtils.getString("sale_uno");
        sale_phone = jsonUtils.getString("sale_phone");
        cny_amount = jsonUtils.getString("cny_amount");
        usd_amount = jsonUtils.getString("usd_amount");
        pay_time = jsonUtils.getString("pay_time");
        operate_time = jsonUtils.getString("operate_time");
        picture = jsonUtils.getString("picture");
    }

    public int getBuy_sell() {
        return buy_sell;
    }

    public void setBuy_sell(int buy_sell) {
        this.buy_sell = buy_sell;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getBuy_uno() {
        return buy_uno;
    }

    public void setBuy_uno(String buy_uno) {
        this.buy_uno = buy_uno;
    }

    public String getBuy_phone() {
        return buy_phone;
    }

    public void setBuy_phone(String buy_phone) {
        this.buy_phone = buy_phone;
    }

    public String getSale_uno() {
        return sale_uno;
    }

    public void setSale_uno(String sale_uno) {
        this.sale_uno = sale_uno;
    }

    public String getSale_phone() {
        return sale_phone;
    }

    public void setSale_phone(String sale_phone) {
        this.sale_phone = sale_phone;
    }

    public String getCny_amount() {
        return cny_amount;
    }

    public void setCny_amount(String cny_amount) {
        this.cny_amount = cny_amount;
    }

    public String getUsd_amount() {
        return usd_amount;
    }

    public void setUsd_amount(String usd_amount) {
        this.usd_amount = usd_amount;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getOperate_time() {
        return operate_time;
    }

    public void setOperate_time(String operate_time) {
        this.operate_time = operate_time;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}
