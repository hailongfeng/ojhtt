package cn.ouju.htt.bean;


import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

/**
 * Created by Administrator on 2018/1/25.
 */

public class AddressBean implements EntityImp {


    /**
     * id : 地址id
     * receiver : 收货人
     * phone : 手机号
     * province : 省级
     * city : 市级
     * zone : 特色
     * address : 区级
     * is_default : 是否默认地址（0：常规地址；1：默认地址）
     */

    private String id;
    private String receiver;
    private String phone;
    private String province;
    private String city;
    private String zone;
    private String address;
    private String is_default;

    @Override
    public AddressBean newObject() {
        return new AddressBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        id = jsonUtils.getString("id");
        receiver = jsonUtils.getString("receiver");
        phone = jsonUtils.getString("phone");
        province = jsonUtils.getString("province");
        city = jsonUtils.getString("city");
        zone = jsonUtils.getString("zone");
        address = jsonUtils.getString("address");
        is_default = jsonUtils.getString("is_default");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "id='" + id + '\'' +
                ", receiver='" + receiver + '\'' +
                ", phone='" + phone + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", zone='" + zone + '\'' +
                ", address='" + address + '\'' +
                ", is_default='" + is_default + '\'' +
                '}';
    }
}
