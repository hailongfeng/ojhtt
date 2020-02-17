package cn.ouju.htt.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.GoodsListBean;
import cn.ouju.htt.bean.StoreBean;

/**
 * Created by Administrator on 2018/2/1.
 */

public class OrderDetailsAdapter extends MyAdapter<GoodsListBean> {
    public OrderDetailsAdapter(List list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getContentView(convertView, R.layout.order_details_item);
            holder.pic = convertView.findViewById(R.id.shopping_order_item_iv_pic);
            holder.title = convertView.findViewById(R.id.shopping_order_item_tv_title);
            holder.price = convertView.findViewById(R.id.shopping_order_item_tv_price);
            holder.num = convertView.findViewById(R.id.shopping_order_item_tv_num);
            holder.status = convertView.findViewById(R.id.shopping_order_item_tv_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GoodsListBean sb = data.get(position);
        Picasso.with(context).load(sb.getPic_url()).into(holder.pic);
        holder.price.setText(sb.getPrice());
        holder.title.setText(sb.getTitle());
        holder.num.setText("x" + sb.getQuantity());
        String status = sb.getStatus();
        if (status.equals("0")) {
            holder.status.setText(context.getString(R.string.manager_order));
            holder.status.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        } else if (status.equals("1")) {
            holder.status.setText(context.getString(R.string.manager_confirm));
            holder.status.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        } else if (status.equals("2")) {
            holder.status.setText(context.getString(R.string.manager_reject));
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
        }
        return convertView;
    }
    class ViewHolder {
        ImageView pic;
        TextView title;
        TextView price;
        TextView num;
        TextView status;
    }
}
