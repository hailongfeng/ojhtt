package cn.ouju.htt.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.adapter.StorePopupAdapter;
import cn.ouju.htt.bean.CategoryBean;


public class StoreWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private StorePopupAdapter adapter;
    private Context context;
    private List<CategoryBean> datas = new ArrayList<>();

    public StoreWindow(Context context, List<CategoryBean> lists) {
        super(context);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.store_popupwindow, null);
        setContentView(view);
        final GridView televisionGv = view.findViewById(R.id.television_gv);
        datas.addAll(lists);
        adapter = new StorePopupAdapter(datas, context);
        televisionGv.setAdapter(adapter);
        televisionGv.setOnItemClickListener(this);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = televisionGv.getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        setPopupWindowAttribute();
    }

    private void setPopupWindowAttribute() {
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        ColorDrawable cd = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(cd);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        back.setPosition(position);
    }

    private CallBack back;

    public void setBack(CallBack back) {
        this.back = back;
    }

    public interface CallBack {
        void setPosition(int position);
    }

    public void setData(List<CategoryBean> lists) {
        datas.clear();
        datas.addAll(lists);
        adapter.notifyDataSetChanged();
    }
}
