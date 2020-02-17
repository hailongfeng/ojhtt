package cn.ouju.htt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.StoreBean;

/**
 * Created by Administrator on 2018/2/1.
 */

public class StoreOrderAdapter extends MyAdapter<StoreBean> {
    public StoreOrderAdapter(List list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getContentView(convertView, R.layout.store_order_item);
            holder.pic = convertView.findViewById(R.id.shopping_order_item_iv_pic);
            holder.title = convertView.findViewById(R.id.shopping_order_item_tv_title);
            holder.price = convertView.findViewById(R.id.shopping_order_item_tv_price);
            holder.num = convertView.findViewById(R.id.shopping_order_item_tv_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StoreBean sb = data.get(position);
        Picasso.with(context).load(sb.getPic_url()).into(holder.pic);
        holder.title.setText(sb.getTitle());
        holder.price.setText(sb.getNew_price());
        holder.num.setText("x" + sb.getQuantity());
        return convertView;
    }

    class ViewHolder {
        ImageView pic;
        TextView title;
        TextView price;
        TextView num;
    }
}
