package cn.ouju.htt.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.StoreAdapter;
import cn.ouju.htt.bean.CategoryBean;
import cn.ouju.htt.bean.StoreBean;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.http.HttpListener;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.popupwindow.StoreWindow;
import cn.ouju.htt.ui.activity.BaseActivity;
import cn.ouju.htt.ui.activity.LoginActivity;
import cn.ouju.htt.ui.activity.SearchStoreActivity;
import cn.ouju.htt.ui.activity.StoreDetailsActivity;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.UserUtils;

public class StoresFragment extends BaseFragment implements OnRefreshLoadMoreListener, AdapterView.OnItemClickListener {
    private static HttpListener listeners;
    private static BaseActivity.MyHandler handlers;
    @BindView(R.id.television_search_ll_content)
    LinearLayout televisionSearchLlContent;
    @BindView(R.id.search_tv_cancel)
    TextView searchTvCancel;
    @BindView(R.id.television_iv_sanjiao)
    ImageView televisionIvSanjiao;
    @BindView(R.id.television_ll)
    LinearLayout televisionLl;
    @BindView(R.id.common_ll_data_null)
    LinearLayout commonLlDataNull;
    @BindView(R.id.common_ll_failure)
    LinearLayout commonLlFailure;
    @BindView(R.id.search_lv)
    ListView searchLv;
    @BindView(R.id.main_srf)
    SmartRefreshLayout mainSrf;
    @BindView(R.id.television_divider)
    View televisionDivider;
    private List<StoreBean> lists;
    private StoreAdapter adapter;
    private StoreWindow window;
    private List<CategoryBean> categoryBeans;
    private int isEnd;
    private int page = 1;

    public static StoresFragment getInstance(HttpListener listener, BaseActivity.MyHandler handler) {
        StoresFragment storesFragment = new StoresFragment();
        listeners = listener;
        handlers = handler;
        return storesFragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            lists.clear();
            categoryBeans.clear();
            CommonService.query("Mall/category", null, null, listeners, handlers);
        }
    }

    @Override
    protected void initView() {
        setContentView(R.layout.fragment_store);
        CommonService.query("Mall/category", null, null, listeners, handlers);
        searchLv.setOnItemClickListener(this);
        categoryBeans = new ArrayList<>();
        window = new StoreWindow(getContext(), categoryBeans);
        window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.setFocusable(true);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                televisionIvSanjiao.setPivotX(televisionIvSanjiao.getWidth() / 2);
                televisionIvSanjiao.setPivotY(televisionIvSanjiao.getHeight() / 2);//支点在图片中心
                televisionIvSanjiao.setRotation(360);
            }
        });
        window.setBack(new StoreWindow.CallBack() {
            @Override
            public void setPosition(int position) {
                categoryId = categoryBeans.get(position).getId();
                page = 1;
                visitNet();
                for (int i = 0; i < categoryBeans.size(); i++) {
                    categoryBeans.get(i).setFlag(0);
                }
                categoryBeans.get(position).setFlag(1);
                window.setData(categoryBeans);
                window.dismiss();
            }
        });
        mainSrf.setOnRefreshLoadMoreListener(this);
    }

    private String categoryId;

    @Override

    protected void initData() {
        lists = new ArrayList<>();
        adapter = new StoreAdapter(lists, getContext());
        searchLv.setAdapter(adapter);
        adapter.setCall(new StoreAdapter.CallBack() {
            @Override
            public void setInt(int position, int num) {
                lists.get(position).setNum(num);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void join(int position) {
                if (!TextUtils.isEmpty(UserUtils.getString("user_no", null))) {
                    Map<String, Object> params = new TreeMap<>();
                    params.put("user_no", UserUtils.getString("user_no", null));
                    params.put("goods_id", lists.get(position).getId());
                    if (lists.get(position).getNum() <= 1) {
                        params.put("quantity", 1);
                    } else {
                        params.put("quantity", lists.get(position).getNum());
                    }
                    CommonService.query("Mall/addCart", params, null, listeners, handlers);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    @OnClick({R.id.television_ll, R.id.television_search_ll_content,R.id.common_ll_failure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.television_ll:
                if (window != null) {
                    if (window.isShowing()) {
                        window.dismiss();
                    } else {
                        window.showAsDropDown(televisionDivider);
                        televisionIvSanjiao.setPivotX(televisionIvSanjiao.getWidth() / 2);
                        televisionIvSanjiao.setPivotY(televisionIvSanjiao.getHeight() / 2);//支点在图片中心
                        televisionIvSanjiao.setRotation(180);
                    }
                }
                break;
            case R.id.television_search_ll_content:
                Intent intent = new Intent();
                intent.setClass(getContext(), SearchStoreActivity.class);
                startActivity(intent);
                break;
            case R.id.common_ll_failure:
                CommonService.query("Mall/category", null, null, listeners, handlers);
                break;
        }
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals("200")) {
                    if (t[0].equals("Mall/category")) {
                        visitNet();
                        if (categoryBeans != null) {
                            List<CategoryBean> beans = jsonUtils.getEntityList("data", new CategoryBean());
                            if (beans != null && beans.size() > 0) {
                                categoryBeans.addAll(beans);
                                window.setData(categoryBeans);
                            }
                        }
                    } else if (t[0].equals("Mall/goods")) {
                        lists.clear();
                        commonLlDataNull.setVisibility(View.GONE);
                        commonLlFailure.setVisibility(View.GONE);
                        isEnd = jsonUtils.getInt("data", "is_end");
                        List<StoreBean> beans = jsonUtils.getEntityList("data", "data_list", new StoreBean());
                        if (beans != null && beans.size() > 0) {
                            lists.addAll(beans);
                        }
                        if (lists == null || lists.size() <= 0) {
                            commonLlDataNull.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                        if (mainSrf.getState() == RefreshState.Refreshing) {
                            mainSrf.finishRefresh();
                        }
                        if (mainSrf.getState() == RefreshState.Loading) {
                            mainSrf.finishLoadMore();
                        }
                        if (isEnd == 1) {
                            mainSrf.finishLoadMoreWithNoMoreData();
                        }
                    } else if (t[0].equals("Mall/addCart")) {
                        Toast.makeText(getContext(), getString(R.string.store_add_success), Toast.LENGTH_SHORT).show();
                    }
                } else
                    AlertDialogUtils.showMsg(getContext(), jsonUtils.getMsg());
            }
        }
    }

    @Override
    public void onNetWorkedFail(Object... strings) {
        if (mainSrf.getState() == RefreshState.Refreshing) {
            mainSrf.finishRefresh();
        }
        if (mainSrf.getState() == RefreshState.Loading) {
            mainSrf.finishLoadMore();
        }
        commonLlDataNull.setVisibility(View.GONE);
        commonLlFailure.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        lists.clear();
        visitNet();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void visitNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        if (!TextUtils.isEmpty(categoryId)) {
            params.put("category_id", categoryId);
        }
        CommonService.query("Mall/goods", params, null, listeners, handlers);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        page++;
        if (isEnd != 1) {
            visitNet();
        } else {
            mainSrf.finishLoadMoreWithNoMoreData();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(getContext(), StoreDetailsActivity.class);
        intent.putExtra("id", lists.get(position).getId());
        startActivity(intent);
    }
}
