package cn.ouju.htt.bean;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class TokenBean implements EntityImp {

    /**
     * id : 3
     * order_no : T2018092711351814
     * user_no : M827
     * ccm_num : 300
     * token_num : 5500
     * add_time : 2018-09-27 11:35
     * remarks : 当前的兑换比例为: 300:5500
     * ccm_tax : 90
     * ccm_total : 390
     */

    private String id;
    private String order_no;
    private String user_no;
    private String dti_num;
    private String token_num;
    private String add_time;
    private String remarks;
    private String dti_tax;
    private String dti_total;

    @Override
    public TokenBean newObject() {
        return new TokenBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        id = jsonUtils.getString("id");
        order_no = jsonUtils.getString("order_no");
        user_no = jsonUtils.getString("user_no");
        dti_num = jsonUtils.getString("dti_num");
        token_num = jsonUtils.getString("token_num");
        add_time = jsonUtils.getString("add_time");
        remarks = jsonUtils.getString("remarks");
        dti_tax = jsonUtils.getString("dti_tax");
        dti_total = jsonUtils.getString("dti_total");
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

    public String getUser_no() {
        return user_no;
    }

    public void setUser_no(String user_no) {
        this.user_no = user_no;
    }

    public String getDti_num() {
        return dti_num;
    }

    public void setDti_num(String dti_num) {
        this.dti_num = dti_num;
    }

    public String getDti_tax() {
        return dti_tax;
    }

    public void setDti_tax(String dti_tax) {
        this.dti_tax = dti_tax;
    }

    public String getDti_total() {
        return dti_total;
    }

    public void setDti_total(String dti_total) {
        this.dti_total = dti_total;
    }

    public String getToken_num() {
        return token_num;
    }

    public void setToken_num(String token_num) {
        this.token_num = token_num;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
