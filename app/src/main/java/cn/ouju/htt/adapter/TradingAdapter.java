package cn.ouju.htt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.BuySellBean;

public class TradingAdapter extends MyAdapter<BuySellBean> {


    public TradingAdapter(List data, Context context) {
        super(data, context);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = getContentView(convertView, R.layout.adapter_trading_item1);
            vh.title = convertView.findViewById(R.id.trading_item_tv_title);
            vh.btn = convertView.findViewById(R.id.trading_item_tv_btn);
            vh.name = convertView.findViewById(R.id.trading_item_tv_name);
            vh.time = convertView.findViewById(R.id.trading_item_tv_time);
            vh.unit = convertView.findViewById(R.id.trading_item_tv_unit);
            vh.total = convertView.findViewById(R.id.trading_item_tv_total);
            vh.num = convertView.findViewById(R.id.trading_item_tv_num);
            vh.me = convertView.findViewById(R.id.trading_item_tv_me);
            vh.take = convertView.findViewById(R.id.trading_item_tv_take);
            convertView.setTag(vh);
        } else
            vh = (ViewHolder) convertView.getTag();
        BuySellBean bean = data.get(position);
        vh.name.setText(bean.getUser_no());
        vh.time.setText(bean.getAdd_time());
        vh.unit.setText(bean.getPrice());
        vh.total.setText(bean.getAmount());
        vh.num.setText(bean.getNumber());
        String type = bean.getType();
        if (type.equals("0")) {
            vh.take.setText(context.getString(R.string.buy));
            vh.title.setText(context.getString(R.string.sell_num));
        } else if (type.equals("1")) {
            vh.take.setText(context.getString(R.string.home_sell));
            vh.title.setText(context.getString(R.string.buy_num));
        }
        vh.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.sellAndBuy(position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView title, name, time, unit, total, num, me, take;
        LinearLayout btn;
    }

    private CallBack back;

    public void setBack(CallBack back) {
        this.back = back;
    }

    public interface CallBack {
        void sellAndBuy(int position);
    }


}
