package cn.ouju.htt.bean;

import java.util.List;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class TeamListBean implements EntityImp {

    /**
     * user_no : M726
     * phone :
     * realname : M726
     * recount : 3
     */

    private String user_no;
    private String phone;
    private String realname;
    private String recount;

    /**
     * user_no : 用户编号
     * user_level_name : 用户等级名称
     * children : [{"user_no":"用户编号","user_level_name":"用户等级名称","children":[]}]
     */


    @Override
    public TeamListBean newObject() {
        return new TeamListBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        user_no = jsonUtils.getString("user_no");
        realname = jsonUtils.getString("realname");
        recount = jsonUtils.getString("recount");
        phone = jsonUtils.getString("phone");
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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getRecount() {
        return recount;
    }

    public void setRecount(String recount) {
        this.recount = recount;
    }

}
