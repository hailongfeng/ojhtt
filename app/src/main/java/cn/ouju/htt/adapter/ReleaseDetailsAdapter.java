package cn.ouju.htt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.ReleaseBean;
import cn.ouju.htt.bean.ReleaseDetailsBean;

public class ReleaseDetailsAdapter extends MyAdapter<ReleaseDetailsBean> {
    public ReleaseDetailsAdapter(List data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = getContentView(convertView, R.layout.adapter_release_details_item);
            vh.time = convertView.findViewById(R.id.release_details_tv_time);
            vh.price = convertView.findViewById(R.id.release_details_tv_price);
            convertView.setTag(vh);
        } else
            vh = (ViewHolder) convertView.getTag();
        ReleaseDetailsBean bean = data.get(position);
        vh.time.setText(bean.getAdd_time());
        vh.price.setText("+" + bean.getAmount());
        return convertView;
    }

    class ViewHolder {
        TextView time, price;
    }
}
