package cn.ouju.htt.bean;

import java.util.List;

import cn.ouju.htt.json.EntityImp;
import cn.ouju.htt.json.JsonUtils;

public class RangeBean implements EntityImp {

    /**
     * curr_price : 当前价格
     * max_price : 最高价格
     * min_price : 最低价格
     * price_range : 涨幅
     * curr_trade : 当日交易量
     * price_list : [{"price":"价格","add_time":"时间"}]
     */

    private String curr_price;
    private String max_price;
    private String min_price;
    private String price_range;
    private String curr_trade;
    private List<PriceListBean> price_list;

    @Override
    public RangeBean newObject() {
        return new RangeBean();
    }

    @Override
    public void paseFromJson(JsonUtils jsonUtils) {
        curr_price = jsonUtils.getString("curr_price");
        max_price = jsonUtils.getString("max_price");
        min_price = jsonUtils.getString("min_price");
        price_range = jsonUtils.getString("price_range");
        curr_trade = jsonUtils.getString("curr_trade");
        price_list = jsonUtils.getEntityList("price_list", new PriceListBean());
    }

    public String getCurr_price() {
        return curr_price;
    }

    public void setCurr_price(String curr_price) {
        this.curr_price = curr_price;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }

    public String getPrice_range() {
        return price_range;
    }

    public void setPrice_range(String price_range) {
        this.price_range = price_range;
    }

    public String getCurr_trade() {
        return curr_trade;
    }

    public void setCurr_trade(String curr_trade) {
        this.curr_trade = curr_trade;
    }

    public List<PriceListBean> getPrice_list() {
        return price_list;
    }

    public void setPrice_list(List<PriceListBean> price_list) {
        this.price_list = price_list;
    }

    public static class PriceListBean implements EntityImp {
        /**
         * price : 价格
         * add_time : 时间
         */

        private String price;
        private String add_time;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        @Override
        public PriceListBean newObject() {
            return new PriceListBean();
        }

        @Override
        public void paseFromJson(JsonUtils jsonUtils) {
            price = jsonUtils.getString("price");
            add_time = jsonUtils.getString("add_time");
        }
    }
}
