package cn.ouju.htt.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.OrderBean;
import cn.ouju.htt.ui.view.MyListView;

/**
 * Created by Administrator on 2018/2/3.
 */

public class CommonOrderAdapter extends MyAdapter<OrderBean> {


    public CommonOrderAdapter(List<OrderBean> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getContentView(convertView, R.layout.adapter_common_order_item);
            holder.num = convertView.findViewById(R.id.common_order_tv_num);
            holder.status = convertView.findViewById(R.id.common_order_tv_status);
            holder.listView = convertView.findViewById(R.id.common_order_mlv);
            holder.time = convertView.findViewById(R.id.common_order_tv_time);
            holder.count = convertView.findViewById(R.id.common_order_tv_count);
            holder.price = convertView.findViewById(R.id.common_order_tv_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrderBean bean = data.get(position);
        holder.num.setText(bean.getOrder_no());
        String str = bean.getStatus();
        if (str.equals("0")) {
            holder.status.setText(context.getString(R.string.manager_order));
            holder.status.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        } else if (str.equals("1")) {
            holder.status.setText(context.getString(R.string.manager_confirm));
            holder.status.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        } else if (str.equals("2")) {
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
            holder.status.setText(context.getString(R.string.manager_reject));
        }

        holder.time.setText(bean.getAdd_time());
        holder.count.setVisibility(View.GONE);
        holder.count.setText(context.getString(R.string.all) + bean.getGoods_list().size() + context.getString(R.string.all_store));
        holder.price.setText(bean.getTotal() + "DTI");
        holder.listView.setClickable(false);
        holder.listView.setPressed(false);
        holder.listView.setEnabled(false);
        holder.listView.setFocusable(false);
        holder.listView.setAdapter(new CommonChildOrderAdapter(bean.getGoods_list(), context));
        return convertView;
    }

    class ViewHolder {
        TextView num;
        TextView status;
        MyListView listView;
        TextView time;
        TextView count;
        TextView price;
    }
}
