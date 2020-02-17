package cn.ouju.htt.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class CitySelectBean implements IPickerViewData {
    private String name;
    private List<CityBean> cityList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityBean> cityList) {
        this.cityList = cityList;
    }

    @Override

    public String getPickerViewText() {
        return this.name;
    }

    public static class CityBean {
        private String name;
        private List<String> areaList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getAreaList() {
            return areaList;
        }

        public void setAreaList(List<String> areaList) {
            this.areaList = areaList;
        }
    }
}
