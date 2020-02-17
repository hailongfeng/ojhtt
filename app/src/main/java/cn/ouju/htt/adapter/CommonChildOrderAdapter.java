package cn.ouju.htt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.GoodsListBean;

/**
 * Created by Administrator on 2018/2/3.
 */

public class CommonChildOrderAdapter extends MyAdapter<GoodsListBean> {
    public CommonChildOrderAdapter(List list, Context context) {
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
        GoodsListBean bean = data.get(position);
        Picasso.with(context).load(bean.getPic_url()).into(holder.pic);
        holder.title.setText(bean.getTitle());
        holder.price.setText(bean.getPrice());
        holder.num.setText("x" + bean.getQuantity());
        return convertView;
    }

    class ViewHolder {
        ImageView pic;
        TextView title;
        TextView price;
        TextView num;
    }
}
