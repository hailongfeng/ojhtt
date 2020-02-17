package cn.ouju.htt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.MiningBean;

public class MiningAdapter extends MyAdapter<MiningBean> {
    public MiningAdapter(List<MiningBean> data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = getContentView(convertView, R.layout.adapter_mining_item);
            vh.price = convertView.findViewById(R.id.mining_item_tv_price);
            vh.time = convertView.findViewById(R.id.mining_item_tv_time);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        MiningBean bean = data.get(position);
        vh.price.setText("+" + bean.getNumber());
        vh.time.setText(bean.getAdd_time());
        return convertView;
    }

    class ViewHolder {
        TextView time, price;
    }
}
