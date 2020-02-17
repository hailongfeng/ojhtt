package cn.ouju.htt.bean;

import java.util.List;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

/**
 * Created by Administrator on 2018/2/3.
 */

public class OrderBean implements EntityImp {

    /**
     * id : 1390
     * order_no : M-2018010922380796
     * status : 0
     * amount : 100.00
     * add_time : 2018-01-09
     * goods_list : [{"id":"11","title":"卡勒斯博嘉干红葡萄酒","pic_url":"https://www.fei.sg/Upload/mall/20170823/s_599cf22cad33b.jpg","price":"100.00","quantity":"1"}]
     * goods_num : 1
     */

    private String id;
    private String order_no;
    private String status;
    private String total;
    private String add_time;
    private List<GoodsListBean> goods_list;
    private String order_id;
    /**
     * user_no : M287
     * receiver : Jdjd
     * receiver_phone : 64646646464
     * receiver_address : 新疆省直辖行政单位五家渠市 Hdududid
     * remark :
     * amount : 2,365.00
     * shipping_set : 0
     * shipping : 0.00
     * operate_remark : dfg
     * operate_time : 2018-12-17 09:38:13
     */

    private String user_no;
    private String receiver;
    private String receiver_phone;
    private String receiver_address;
    private String remark;
    private String amount;
    private String shipping_set;
    private String shipping;
    private String operate_remark;
    private String operate_time;

    @Override
    public OrderBean newObject() {
        return new OrderBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        id = jsonUtils.getString("id");
        order_no = jsonUtils.getString("order_no");
        status = jsonUtils.getString("status");
        total = jsonUtils.getString("total");
        add_time = jsonUtils.getString("add_time");
        order_id = jsonUtils.getString("order_id");
        goods_list = jsonUtils.getEntityList("goods_list", new GoodsListBean());
        user_no = jsonUtils.getString("user_no");
        receiver = jsonUtils.getString("receiver");
        receiver_phone = jsonUtils.getString("receiver_phone");
        receiver_address = jsonUtils.getString("receiver_address");
        remark = jsonUtils.getString("remark");
        amount = jsonUtils.getString("amount");
        shipping_set = jsonUtils.getString("shipping_set");
        shipping = jsonUtils.getString("shipping");
        operate_remark = jsonUtils.getString("operate_remark");
        operate_time = jsonUtils.getString("operate_time");
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }


    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }

    public String getUser_no() {
        return user_no;
    }

    public void setUser_no(String user_no) {
        this.user_no = user_no;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver_phone() {
        return receiver_phone;
    }

    public void setReceiver_phone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
    }

    public String getReceiver_address() {
        return receiver_address;
    }

    public void setReceiver_address(String receiver_address) {
        this.receiver_address = receiver_address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getShipping_set() {
        return shipping_set;
    }

    public void setShipping_set(String shipping_set) {
        this.shipping_set = shipping_set;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getOperate_remark() {
        return operate_remark;
    }

    public void setOperate_remark(String operate_remark) {
        this.operate_remark = operate_remark;
    }

    public String getOperate_time() {
        return operate_time;
    }

    public void setOperate_time(String operate_time) {
        this.operate_time = operate_time;
    }
}
