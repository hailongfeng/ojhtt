package cn.ouju.htt.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.AddressBean;

/**
 * Created by Administrator on 2018/1/25.
 */

public class MeAddressAdapter extends MyAdapter<AddressBean> {
    public MeAddressAdapter(List<AddressBean> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = getContentView(convertView, R.layout.adapter_me_address_item);
            vh.name = convertView.findViewById(R.id.address_item_tv_name);
            vh.details = convertView.findViewById(R.id.address_item_tv_details);
            vh.del = convertView.findViewById(R.id.address_item_ll_del);
            vh.edit = convertView.findViewById(R.id.address_item_ll_edit);
            vh.iv = convertView.findViewById(R.id.address_item_iv);
            vh.isDef = convertView.findViewById(R.id.address_item_tv_text);
            vh.set = convertView.findViewById(R.id.address_item_ll_set);
            convertView.setTag(vh);
        } else
            vh = (ViewHolder) convertView.getTag();
        AddressBean bean = data.get(position);
        if (bean.getIs_default().equals("1")) {
            vh.iv.setImageResource(R.drawable.select_is_yes);
            vh.isDef.setText(context.getString(R.string.done_set_default));
            vh.isDef.setTextColor(ContextCompat.getColor(context, R.color.colorBtn));
        } else {
            vh.iv.setImageResource(R.drawable.select_is_no);
            vh.isDef.setText(context.getString(R.string.set_default));
            vh.isDef.setTextColor(ContextCompat.getColor(context, R.color.colorGrayText));
        }
        vh.name.setText(bean.getReceiver() + "," + bean.getPhone());
        vh.details.setText(bean.getAddress());
        vh.set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.setPosition(position, 2);
            }
        });
        vh.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.setPosition(position, 0);
            }
        });
        vh.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.setPosition(position, 1);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView details;
        ImageView iv;
        TextView isDef;
        LinearLayout del;
        LinearLayout edit;
        LinearLayout set;
    }

    private CallBack callback;

    public void setCall(CallBack back) {
        this.callback = back;
    }

    public interface CallBack {
        /**
         * @param position item
         * @param flag     标记0为删除1为编辑2为设为默认
         */
        void setPosition(int position, int flag);
    }
}
