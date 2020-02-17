package cn.ouju.htt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.StoreBean;

/**
 * Created by Administrator on 2018/1/30.
 */

public class ShoppingCartAdapter extends MyAdapter<StoreBean> {
    public ShoppingCartAdapter(List<StoreBean> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getContentView(convertView, R.layout.adapter_me_store_bag_item);
            holder.pic = convertView.findViewById(R.id.shopping_cart_item_iv_pic);
            holder.plus = convertView.findViewById(R.id.shopping_cart_item_btn_plus);
            holder.minus = convertView.findViewById(R.id.shopping_cart_item_btn_minus);
            holder.num = convertView.findViewById(R.id.shopping_cart_item_tv_num);
            holder.name = convertView.findViewById(R.id.shopping_cart_item_tv_title);
            holder.price = convertView.findViewById(R.id.shopping_cart_item_tv_price);
            holder.isSelect = convertView.findViewById(R.id.shopping_cart_item_iv_check);
            holder.error = convertView.findViewById(R.id.shopping_cart_item_tv_error);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final StoreBean bean = data.get(position);
        Picasso.with(context).load(bean.getPic_url()).into(holder.pic);
        holder.name.setText(bean.getTitle());
        holder.price.setText(bean.getNew_price());
        holder.num.setText("" + bean.getQuantity());
        if (bean.getNum() == 10) {
            holder.isSelect.setImageResource(R.drawable.select_is_yes);
        } else {
            holder.isSelect.setImageResource(R.drawable.select_is_no);
        }
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setInt(position, Integer.parseInt(holder.num.getText().toString()) + 1);
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(holder.num.getText().toString()) > 1) {
                    back.setInt(position, Integer.parseInt(holder.num.getText().toString()) - 1);
                }
            }
        });
        holder.isSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setSelect(position);
            }
        });
        List<String> lists = bean.getErrors();
        if (lists != null && lists.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < lists.size(); i++) {
                sb.append(lists.get(i) + ",");
            }
            holder.error.setText(sb.toString().substring(0, sb.length() - 1));
        } else {
            holder.error.setText("");
        }
        return convertView;
    }

    class ViewHolder {
        Button plus;
        Button minus;
        TextView num;
        TextView name;
        ImageView pic;
        TextView price;
        ImageView isSelect;
        TextView error;
    }

    private CallBack back;

    public void setCall(CallBack back) {
        this.back = back;
    }

    public interface CallBack {
        void setInt(int position, int num);

        void setSelect(int position);
    }
}
