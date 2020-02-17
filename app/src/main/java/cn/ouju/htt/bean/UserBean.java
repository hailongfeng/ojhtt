package cn.ouju.htt.bean;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

/**
 * Created by Administrator on 2017/10/30.
 */

public class UserBean implements EntityImp {

    /**
     * user_no : 用户编号
     * realname : 真实姓名
     * user_level_name : 用户等级名称
     * recommend_number : 直推人数
     * team_number : 团队人数
     * team_power : 团队算力
     * gfc_total : GFC总资产
     * gfc_valid : GFC可用资产
     * gfc_frozen : GFC冻结资产
     * invitation_code : 我的邀请码
     * qrcode : 分享注册url
     */

    private String user_no;
    private String realname;
    private String recommend_number;
    private String team_number;
    private String invitation_code;
    private String qrcode;
    private String phone;
    /**
     * sms_code : 86
     * ccm_account : 0.00000000
     * ccm_frozen : 0.00000000
     * rb_account : 0.00000000
     */

    private String sms_code;
    private String dti_account;
    private String dti_frozen;
    private String rb_account;

    @Override
    public UserBean newObject() {
        return new UserBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        user_no = jsonUtils.getString("user_no");
        realname = jsonUtils.getString("realname");
        recommend_number = jsonUtils.getString("recommend_number");
        team_number = jsonUtils.getString("team_number");
        invitation_code = jsonUtils.getString("invitation_code");
        qrcode = jsonUtils.getString("qrcode");
        phone = jsonUtils.getString("phone");
        sms_code = jsonUtils.getString("sms_code");
        dti_account = jsonUtils.getString("dti_account");
        dti_frozen = jsonUtils.getString("dti_frozen");
        rb_account = jsonUtils.getString("rb_account");
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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


    public String getRecommend_number() {
        return recommend_number;
    }

    public void setRecommend_number(String recommend_number) {
        this.recommend_number = recommend_number;
    }

    public String getTeam_number() {
        return team_number;
    }

    public void setTeam_number(String team_number) {
        this.team_number = team_number;
    }


    public String getInvitation_code() {
        return invitation_code;
    }

    public void setInvitation_code(String invitation_code) {
        this.invitation_code = invitation_code;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getSms_code() {
        return sms_code;
    }

    public void setSms_code(String sms_code) {
        this.sms_code = sms_code;
    }

    public String getDti_account() {
        return dti_account;
    }

    public void setDti_account(String dti_account) {
        this.dti_account = dti_account;
    }

    public String getDti_frozen() {
        return dti_frozen;
    }

    public void setDti_frozen(String dti_frozen) {
        this.dti_frozen = dti_frozen;
    }

    public String getRb_account() {
        return rb_account;
    }

    public void setRb_account(String rb_account) {
        this.rb_account = rb_account;
    }
}
