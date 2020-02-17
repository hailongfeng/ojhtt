package cn.ouju.htt.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.ouju.htt.R;

public class LanguageWindow extends PopupWindow {
    public LanguageWindow(Context context, View.OnClickListener clickListener) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.language_popupwindow, null);
        TextView send = view.findViewById(R.id.language_cn);
        TextView write = view.findViewById(R.id.language_en_us);
        TextView receive = view.findViewById(R.id.language_th);
        send.setOnClickListener(clickListener);
        receive.setOnClickListener(clickListener);
        write.setOnClickListener(clickListener);
        setContentView(view);
        setPopupWindowAttribute();
    }

    private void setPopupWindowAttribute() {
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        setBackgroundDrawable(cd);
    }
}
