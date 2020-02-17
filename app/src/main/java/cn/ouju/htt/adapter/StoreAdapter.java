package cn.ouju.htt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.StoreBean;


/**
 * Created by pc on 2018/1/26.
 */

public class StoreAdapter extends MyAdapter<StoreBean> {

    public StoreAdapter(List list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder vh;
        if (view == null) {
            vh = new ViewHolder();
            view = getContentView(view, R.layout.adapter_store_item);
            vh.plus = view.findViewById(R.id.shopping_item_btn_plus);
            vh.minus = view.findViewById(R.id.shopping_item_btn_minus);
            vh.num = view.findViewById(R.id.shopping_item_tv_num);
            vh.type = view.findViewById(R.id.shopping_item_tv_type);
            vh.name = view.findViewById(R.id.shopping_item_tv_name);
            vh.kc = view.findViewById(R.id.shopping_item_tv_kc);
            vh.xg = view.findViewById(R.id.shopping_item_tv_xg);
            vh.pic = view.findViewById(R.id.shopping_item_iv_pic);
            vh.price = view.findViewById(R.id.shopping_item_tv_price);
            vh.ys = view.findViewById(R.id.shopping_item_tv_ys);
            vh.join = view.findViewById(R.id.shopping_tv_join);
            vh.xgll = view.findViewById(R.id.shopping_item_ll_xg);
            view.setTag(vh);
        } else
            vh = (ViewHolder) view.getTag();
        final StoreBean bean = data.get(i);
        vh.type.setText(bean.getCate_name());
        vh.name.setText(bean.getTitle());
        if (bean.getNum() <= 1) {
            vh.num.setText("1");
        } else {
            vh.num.setText("" + bean.getNum());
        }
        vh.ys.setText(bean.getSell_number());
        vh.kc.setText(bean.getInventory_number());
        String num = bean.getLimit_number();
        if (!TextUtils.isEmpty(num)) {
            if (!num.equals("0")) {
                vh.xgll.setVisibility(View.VISIBLE);
                vh.xg.setText(bean.getLimit_number());
            } else {
                vh.xgll.setVisibility(View.GONE);
                vh.xg.setText(context.getString(R.string.limit_no));
            }
        }
        vh.price.setText(bean.getPrice());
        vh.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setInt(i, Integer.parseInt(vh.num.getText().toString()) + 1);
            }
        });
        vh.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(vh.num.getText().toString()) > 1) {
                    back.setInt(i, Integer.parseInt(vh.num.getText().toString()) - 1);
                }
            }
        });
        Picasso.with(context).load(bean.getPic_url()).error(R.drawable.default_pic).into(vh.pic);
        vh.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.join(i);
            }
        });
        return view;
    }

    class ViewHolder {
        Button plus;
        Button minus;
        TextView num;
        TextView type;
        TextView name;
        TextView kc;
        TextView xg;
        TextView ys;
        ImageView pic;
        TextView price;
        TextView join;
        TextView xgll;
    }

    private CallBack back;

    public void setCall(CallBack back) {
        this.back = back;
    }

    public interface CallBack {
        void setInt(int position, int num);

        void join(int position);
    }
}
