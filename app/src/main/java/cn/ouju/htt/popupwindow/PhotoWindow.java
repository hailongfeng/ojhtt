package cn.ouju.htt.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import cn.ouju.htt.R;


/**
 * Created by Lenovo on 2016/5/31.
 */
public class PhotoWindow extends PopupWindow {
    public PhotoWindow(Context context, View.OnClickListener onClickListener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View photoView = inflater.inflate(R.layout.photo_popupwindow, null);
        setContentView(photoView);
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
        Button cancel = photoView.findViewById(R.id.photo_cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        photoView.findViewById(R.id.photo_take_btn).setOnClickListener(onClickListener);
        photoView.findViewById(R.id.photo_pick_btn).setOnClickListener(onClickListener);
        setPopupWindowAttribute();
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
