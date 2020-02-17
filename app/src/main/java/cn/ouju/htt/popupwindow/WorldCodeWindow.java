package cn.ouju.htt.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.adapter.WorldCodeAdapter;
import cn.ouju.htt.bean.AreaCodeBean;


/**
 * Created by Lenovo on 2016/5/31.
 */
public class WorldCodeWindow extends PopupWindow {
    private WorldCodeAdapter adapter;
    private List<AreaCodeBean> lists;

    public WorldCodeWindow(Context context, AdapterView.OnItemClickListener onClickListener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View photoView = inflater.inflate(R.layout.world_code_popupwindow, null);
        setContentView(photoView);
        ListView listView = photoView.findViewById(R.id.pop_lv);
        lists = new ArrayList<>();
        adapter = new WorldCodeAdapter(lists, context);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onClickListener);
        photoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = photoView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        setPopupWindowAttribute();
    }

    public void setLists(List<AreaCodeBean> beans) {
        lists.addAll(beans);
        adapter.notifyDataSetChanged();
    }

    private void setPopupWindowAttribute() {
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable cd = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(cd);
        setAnimationStyle(R.style.AnimBottom);
    }
}
