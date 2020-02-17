package cn.ouju.htt.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.CategoryBean;


public class StorePopupAdapter extends MyAdapter<CategoryBean> {
    public StorePopupAdapter(List data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = getContentView(convertView, R.layout.adapter_store_popup_item);
            vh = new ViewHolder();
            vh.content = convertView.findViewById(R.id.television_item_tv);
            convertView.setTag(vh);
        } else
            vh = (ViewHolder) convertView.getTag();
        CategoryBean sortBean = data.get(position);
        vh.content.setText(sortBean.getTitle());
        if (sortBean.getFlag() == 1) {
            vh.content.setBackground(ContextCompat.getDrawable(context, R.drawable.category_margin_red));
            vh.content.setTextColor(context.getResources().getColor(R.color.colorRed));
        } else {
            vh.content.setBackground(ContextCompat.getDrawable(context, R.drawable.text_margin));
            vh.content.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }

        return convertView;
    }

    class ViewHolder {
        TextView content;
    }
}
