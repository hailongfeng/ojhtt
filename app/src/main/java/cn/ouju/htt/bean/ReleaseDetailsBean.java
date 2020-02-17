package cn.ouju.htt.bean;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class ReleaseDetailsBean implements EntityImp {

    /**
     * id : 790
     * market_record_id : 46
     * user_no : M287
     * amount : 9.80100000
     * add_time : 2018-12-13 01:00:01
     */

    private String id;
    private String market_record_id;
    private String user_no;
    private String amount;
    private String add_time;

    @Override
    public ReleaseDetailsBean newObject() {
        return new ReleaseDetailsBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        id = jsonUtils.getString("id");
        market_record_id = jsonUtils.getString("market_record_id");
        user_no = jsonUtils.getString("user_no");
        amount = jsonUtils.getString("amount");
        add_time = jsonUtils.getString("add_time");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarket_record_id() {
        return market_record_id;
    }

    public void setMarket_record_id(String market_record_id) {
        this.market_record_id = market_record_id;
    }

    public String getUser_no() {
        return user_no;
    }

    public void setUser_no(String user_no) {
        this.user_no = user_no;
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
}
