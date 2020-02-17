package cn.ouju.htt.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.MeAddressAdapter;
import cn.ouju.htt.bean.AddressBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.UserUtils;

/**
 * Created by Administrator on 2018/1/23.
 */

public class MeAddressActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.me_address_lv)
    ListView meAddressLv;
    @BindView(R.id.me_address_ll)
    LinearLayout meAddressLl;
    private List<AddressBean> addressBeans;
    private MeAddressAdapter adapter;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_me_address);
        ButterKnife.bind(this);
        showToolBar(android.R.color.white);
        setTitle(getString(R.string.me_grid_address), android.R.color.black);
        setRightMenu(getString(R.string.new_address), android.R.color.black, 0, this);
        meAddressLv.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        addressBeans = new ArrayList<>();
        adapter = new MeAddressAdapter(addressBeans, this);
        adapter.setCall(new MeAddressAdapter.CallBack() {
            @Override
            public void setPosition(final int position, int flag) {
                //删除
                if (flag == 0) {
                    new AlertDialog.Builder(MeAddressActivity.this).setMessage(getString(R.string.confirm_delete)).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Map<String, Object> maps = new TreeMap<>();
                            maps.put("address_id", addressBeans.get(position).getId());
                            maps.put("user_no", UserUtils.getString("user_no", null));
                            CommonService.query("Mall/deleteAddress", maps, null, MeAddressActivity.this, handler);
                        }
                    }).create().show();

                   /* if (addressBeans.get(position).getIs_default().equals("1")) {
                        Intent intent = new Intent();
                        intent.setAction("cn.ouju.del");
                        intent.putExtra("isRefresh", true);
                        sendBroadcast(intent);
                    }*/
                    //更新
                } else if (flag == 1) {
                    Intent intent = new Intent();
                    intent.putExtra("flag", "update");
                    intent.putExtra("address", addressBeans.get(position));
                    intent.setClass(MeAddressActivity.this, MeAddAddressActivity.class);
                    startActivityForResult(intent, 10);
                    //设置默认
                } else if (flag == 2) {
                    if (!addressBeans.get(position).getIs_default().equals("1")) {
                        Map<String, Object> maps = new TreeMap<>();
                        maps.put("address_id", addressBeans.get(position).getId());
                        maps.put("user_no", UserUtils.getString("user_no", null));
                        CommonService.query("Mall/setAddress", maps, null, MeAddressActivity.this, handler);
                    }
                }
            }
        });
        meAddressLv.setAdapter(adapter);
        startInternet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_menu:
                if (!TextUtils.isEmpty(limit) && Integer.parseInt(limit) > addressBeans.size()) {
                    Intent intent = new Intent();
                    intent.putExtra("flag", "add");
                    intent.setClass(this, MeAddAddressActivity.class);
                    startActivityForResult(intent, 10);
                } else {
                    showShortToast(getString(R.string.address_limit_start) + limit + getString(R.string.address_limit_end));
                }
                break;
        }
    }

    private String limit;

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            JsonUtils jsonUtils = (JsonUtils) t[1];
            if (jsonUtils.getCode().equals(JsonUtils.SUCCESS)) {
                if (t[0].equals("Mall/address")) {
                    limit = jsonUtils.getString("limit", "data");
                    List<AddressBean> lists = jsonUtils.getEntityList("data", "data_list", new AddressBean());
                    addressBeans.clear();
                    if (lists != null && lists.size() > 0) {
                        meAddressLl.setVisibility(View.GONE);
                        addressBeans.addAll(lists);
                        adapter.notifyDataSetChanged();
                    } else {
                        meAddressLl.setVisibility(View.VISIBLE);
                    }
                } else if (t[0].equals("Mall/deleteAddress")) {
                    showShortToast(getString(R.string.delete_success));
                    startInternet();

                } else if (t[0].equals("Mall/setAddress")) {
                    showShortToast(getString(R.string.set_psw_success));
                    startInternet();
                }
            } else {
                AlertDialogUtils.showMsg(this, jsonUtils.getMsg());
            }
        }
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }

    private void startInternet() {
        Map<String, Object> maps = new TreeMap<>();
        maps.put("user_no", UserUtils.getString("user_no", null));
        CommonService.query("Mall/address", maps, null, this, handler);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (resultCode == 20) {
                startInternet();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = getIntent().getStringExtra("isStart");
        if (!TextUtils.isEmpty(str) && str.equals("yes")) {
           /* Map<String, Object> maps = new TreeMap<>();
            maps.put("address_id", addressBeans.get(position).getId());
            maps.put("user_no", UserUtils.getString("user_no", null));
            CommonService.query("Mall/setAddress", maps, null, MeAddressActivity.this, handler);*/
            Intent intent = new Intent();
            intent.putExtra("address", addressBeans.get(position));
            setResult(20, intent);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(20);
        }
        return super.onKeyDown(keyCode, event);
    }
}
