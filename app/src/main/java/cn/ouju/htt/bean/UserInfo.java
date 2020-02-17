package cn.ouju.htt.bean;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class UserInfo implements EntityImp {

    /**
     * user_no : C12
     * realname : Jay
     * phone : 18955825202
     * user_level_name : 普通影视链商
     * id_card :
     * alipay :
     * wechat :
     * bank :
     * sub_bank :
     * bank_no :
     */

    private String user_no;
    private String realname;
    private String phone;
    private String sms;
    private String id_card;
    private String alipay;
    private String wechat;
    private String bank;
    private String sub_bank;
    private String bank_no;
    //0需要保存，1不允许保存
    private String cont_save;

    @Override
    public UserInfo newObject() {
        return new UserInfo();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        user_no = jsonUtils.getString("user_no");
        realname = jsonUtils.getString("realname");
        phone = jsonUtils.getString("phone");
        sms = jsonUtils.getString("sms");
        id_card = jsonUtils.getString("id_card");
        alipay = jsonUtils.getString("alipay");
        wechat = jsonUtils.getString("wechat");
        bank = jsonUtils.getString("bank");
        sub_bank = jsonUtils.getString("sub_bank");
        bank_no = jsonUtils.getString("bank_no");
        cont_save = jsonUtils.getString("cont_save");
    }

    public String getCont_save() {
        return cont_save;
    }

    public void setCont_save(String cont_save) {
        this.cont_save = cont_save;
    }

    public String getUser_no() {
        return user_no;
    }

    public void setUser_no(String user_no) {
        this.user_no = user_no;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getSub_bank() {
        return sub_bank;
    }

    public void setSub_bank(String sub_bank) {
        this.sub_bank = sub_bank;
    }

    public String getBank_no() {
        return bank_no;
    }

    public void setBank_no(String bank_no) {
        this.bank_no = bank_no;
    }
}
