package cn.ouju.htt.ui.activity;


import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.bean.AddressBean;
import cn.ouju.htt.bean.CitySelectBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.UserUtils;

/**
 * Created by Administrator on 2018/1/23.
 */

public class MeAddAddressActivity extends BaseActivity {
    @BindView(R.id.me_add_address_et_name)
    EditText meAddAddressEtName;
    @BindView(R.id.me_add_address_et_tel)
    EditText meAddAddressEtTel;
    @BindView(R.id.me_add_address_rl_city)
    RelativeLayout meAddAddressRlCity;
    @BindView(R.id.me_add_address_tv_province)
    TextView meAddAddressTvProvince;
    @BindView(R.id.me_add_address_et_details)
    EditText meAddAddressEtDetails;
    private ArrayList<CitySelectBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private AddressBean addressBean;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_me_add_address);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        String title = getIntent().getStringExtra("flag");
        if (title.equals("update")) {
            setTitle(getString(R.string.edit_address), android.R.color.black);
            addressBean = (AddressBean) getIntent().getSerializableExtra("address");
            meAddAddressEtName.setText(addressBean.getReceiver());
            meAddAddressEtTel.setText(addressBean.getPhone());
            meAddAddressEtDetails.setText(addressBean.getAddress().substring((addressBean.getProvince() + addressBean.getCity() + addressBean.getZone()).length(), addressBean.getAddress().length()).trim());
            meAddAddressTvProvince.setText(addressBean.getProvince() + addressBean.getCity() + addressBean.getZone());
            province = addressBean.getProvince();
            city = addressBean.getCity();
            area = addressBean.getZone();
            flag = true;
        } else if (title.equals("add")) {
            setTitle(getString(R.string.add_store_address), android.R.color.black);
        }

        setRightMenu(getString(R.string.save), R.color.colorGrayText, 0, this);
        initJsonData();
    }

    private void initJsonData() {
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader bs = new BufferedReader(new InputStreamReader(this.getAssets().open("address.json")));
            String line;
            while ((line = bs.readLine()) != null) {
                sb.append(line);
            }
            JSONArray data = new JSONArray(sb.toString());
            ArrayList<CitySelectBean> arrayList = new ArrayList<>();
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                CitySelectBean bean = gson.fromJson(data.optJSONObject(i).toString(), CitySelectBean.class);
                arrayList.add(bean);
            }
            options1Items = arrayList;
            for (int i = 0; i < arrayList.size(); i++) {
                ArrayList<String> CityList = new ArrayList<>();
                ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();
                for (int j = 0; j < arrayList.get(i).getCityList().size(); j++) {
                    String CityName = arrayList.get(i).getCityList().get(j).getName();
                    CityList.add(CityName);
                    ArrayList<String> City_AreaList = new ArrayList<>();
                    if (arrayList.get(i).getCityList().get(j).getAreaList() == null || arrayList.get(i).getCityList().get(j).getAreaList().size() == 0) {
                        City_AreaList.add("");
                    } else {
                        for (int k = 0; k < arrayList.get(i).getCityList().get(j).getAreaList().size(); k++) {
                            String areaName = arrayList.get(i).getCityList().get(j).getAreaList().get(k);
                            City_AreaList.add(areaName);
                        }
                    }
                    Province_AreaList.add(City_AreaList);
                }
                options2Items.add(CityList);
                options3Items.add(Province_AreaList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    @OnClick({R.id.me_add_address_rl_city})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_add_address_rl_city:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                ShowPickerView();
                break;
            case R.id.toolbar_menu:
                String name = meAddAddressEtName.getText().toString().trim();
                String tel = meAddAddressEtTel.getText().toString().trim();
                String details = meAddAddressEtDetails.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    if (name.length() >= 2 && name.length() <= 20) {
                        if (!TextUtils.isEmpty(tel)) {
                            if (flag) {
                                if (!TextUtils.isEmpty(details)) {
                                    if (getIntent().getStringExtra("flag").equals("add")) {
                                        Map<String, Object> maps = new TreeMap<>();
                                        maps.put("user_no", UserUtils.getString("user_no", null));
                                        maps.put("receiver", name);
                                        maps.put("phone", tel);
                                        maps.put("province", province);
                                        maps.put("city", city);
                                        maps.put("zone", area);
                                        maps.put("address", details);
                                        CommonService.query("Mall/addAddress", maps, null, this, handler);
                                    } else {
                                        Map<String, Object> maps = new TreeMap<>();
                                        maps.put("address_id", addressBean.getId());
                                        maps.put("user_no", UserUtils.getString("user_no", null));
                                        maps.put("receiver", name);
                                        maps.put("phone", tel);
                                        maps.put("province", province);
                                        maps.put("city", city);
                                        maps.put("zone", area);
                                        maps.put("address", details);
                                        CommonService.query("Mall/modifyAddress", maps, null, this, handler);
                                    }
                                } else
                                    showShortToast(getString(R.string.input_address_details));
                            } else {
                                showShortToast(getString(R.string.select_area));
                            }

                        } else
                            showShortToast(getString(R.string.input_phone));
                    } else {
                        showShortToast(getString(R.string.name_character));
                    }
                } else
                    showShortToast(getString(R.string.input_name));
                break;
        }
    }

    private boolean flag;
    private String province;
    private String city;
    private String area;

    private void ShowPickerView() {
        OptionsPickerView pv = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                province = options1Items.get(options1).getPickerViewText();
                city = options2Items.get(options1).get(options2);
                area = options3Items.get(options1).get(options2).get(options3);
                meAddAddressTvProvince.setText(province + city + area);
                flag = true;
            }
        }).setDividerColor(Color.BLACK).setTextColorCenter(Color.BLACK).build();
        pv.setPicker(options1Items, options2Items, options3Items);
        pv.show();
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            JsonUtils jsonUtils = (JsonUtils) t[1];
            if (t[0].equals("Mall/addAddress")) {
                if (jsonUtils.getCode().equals(JsonUtils.SUCCESS)) {
                    showShortToast(getString(R.string.add_success));
                    setResult(20);
                    finish();
                }
            } else if (t[0].equals("Mall/modifyAddress")) {
                if (jsonUtils.getCode().equals(JsonUtils.SUCCESS)) {
                    showShortToast(getString(R.string.update_success));
                    setResult(20);
                    finish();
                }
            }
        }
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }
}
