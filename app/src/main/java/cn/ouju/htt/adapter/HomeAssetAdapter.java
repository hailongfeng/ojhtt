package cn.ouju.htt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.TradeBean;
import cn.ouju.htt.utils.UserUtils;

public class HomeAssetAdapter extends MyAdapter<TradeBean> {
    public HomeAssetAdapter(List data, Context context) {
        super(data, context);
    }

      @Override
     public int getItemViewType(int position) {
      return data.get(position).getType();
     }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        TradeBean bean = data.get(i);
        switch (getItemViewType(i)) {
            case 0:
                if (view == null) {
                    view = getContentView(view, R.layout.adapter_asset_item0);
                    viewHolder = new ViewHolder();
                    common(viewHolder, view);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                setUser(viewHolder, bean, 0, i);
                viewHolder.amount.setText(bean.getCny_amount());
                break;
            case 1:
                if (view == null) {
                    view = getContentView(view, R.layout.adapter_asset_item1);
                    viewHolder = new ViewHolder();
                    viewHolder.confirm = view.findViewById(R.id.asset_item_tv_confirm);
                    viewHolder.no_yes = view.findViewById(R.id.asset_item_tv_no_yes);
                    common(viewHolder, view);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                setUser(viewHolder, bean, 0, i);
                viewHolder.amount.setText(bean.getCny_amount());
                viewHolder.confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        back.setPosition(i, 2);
                    }
                });
                viewHolder.no_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        back.setPosition(i, 3);
                    }
                });
                break;
            case 2:
                if (view == null) {
                    view = getContentView(view, R.layout.adapter_asset_item2);
                    viewHolder = new ViewHolder();
                    common(viewHolder, view);
                    viewHolder.confirm = view.findViewById(R.id.asset_item_tv_confirm);
                    viewHolder.no_yes = view.findViewById(R.id.asset_item_tv_no_yes);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                setUser(viewHolder, bean, 0, i);
                viewHolder.amount.setText(bean.getCny_amount());
                viewHolder.confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        back.setPosition(i, 1);
                    }
                });
                viewHolder.no_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        back.setPosition(i, 4);
                    }
                });
                break;
            case 3:
                if (view == null) {
                    view = getContentView(view, R.layout.adapter_asset_item3);
                    viewHolder = new ViewHolder();
                    viewHolder.no_yes = view.findViewById(R.id.asset_item_tv_no_yes);
                    common(viewHolder, view);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                setUser(viewHolder, bean, 0, i);
                viewHolder.amount.setText(bean.getCny_amount());

                viewHolder.no_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        back.setPosition(i, 3);
                    }
                });
                break;
            case 4:
                if (view == null) {
                    view = getContentView(view, R.layout.adapter_asset_item4);
                    viewHolder = new ViewHolder();
                    viewHolder.title = view.findViewById(R.id.asset_item_tv_title);
                    viewHolder.btn = view.findViewById(R.id.asset_item_tv_btn);
                    common(viewHolder, view);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                setUser(viewHolder, bean, 1, i);
                viewHolder.amount.setText(bean.getAmount());
                int bs = bean.getBuy_sell();
                if (bs == 1) {
                    viewHolder.title.setText(context.getString(R.string.buyer));
                } else if (bs == 0) {
                    viewHolder.title.setText(context.getString(R.string.seller));
                }
                String status = bean.getStatus();
                if (status.equals("0")) {
                    viewHolder.no_yes.setText(context.getString(R.string.progress));
                    viewHolder.btn.setVisibility(View.VISIBLE);
                    viewHolder.no_yes.setVisibility(View.GONE);
                } else if (status.equals("1")) {
                    viewHolder.no_yes.setText(context.getString(R.string.finish_intent));
                    viewHolder.btn.setVisibility(View.GONE);
                    viewHolder.no_yes.setVisibility(View.VISIBLE);
                } else if (status.equals("2")) {
                    viewHolder.btn.setVisibility(View.GONE);
                    viewHolder.no_yes.setText(context.getString(R.string.sponsor_cancel));
                    viewHolder.no_yes.setVisibility(View.VISIBLE);
                } else if (status.equals("3")) {
                    viewHolder.btn.setVisibility(View.GONE);
                    viewHolder.no_yes.setText(context.getString(R.string.manager_cancel));
                    viewHolder.no_yes.setVisibility(View.VISIBLE);
                }
                viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        back.setPosition(i, 1);
                    }
                });
                break;
            case 5:
                ViewHolder1 vh;
                if (view == null) {
                    vh = new ViewHolder1();
                    view = getContentView(view, R.layout.adapter_asset_item5);
                    vh.title = view.findViewById(R.id.asset_item_tv_title);
                    vh.name = view.findViewById(R.id.asset_item_tv_user_no);
                    vh.check = view.findViewById(R.id.asset_item_tv_user_check);
                    vh.time = view.findViewById(R.id.asset_item_tv_time);
                    vh.cny = view.findViewById(R.id.asset_item_tv_cny);
                    vh.usd = view.findViewById(R.id.asset_item_tv_usd);
                    vh.s_time = view.findViewById(R.id.asset_item_tv_s_time);
                    view.setTag(vh);
                } else {
                    vh = (ViewHolder1) view.getTag();
                }
                if (bean.getSale_uno().equals(UserUtils.getString("user_no", null))) {
                    vh.title.setText(context.getString(R.string.buyer));
                    vh.name.setText(bean.getBuy_uno());
                } else {
                    vh.name.setText(bean.getSale_uno());
                    vh.title.setText(context.getString(R.string.seller));
                }

                vh.time.setText(bean.getOperate_time());
                vh.cny.setText("￥"+bean.getCny_amount());
                vh.usd.setText("$"+bean.getUsd_amount());
                vh.s_time.setText(bean.getPay_time());
                vh.check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        back.setPosition(i, 0);
                    }
                });
                break;
        }

        return view;
    }

    private void setUser(ViewHolder viewHolder, TradeBean bean, final int flag, final int position) {
        if (flag == 0) {
            if (bean.getSale_uno().equals(UserUtils.getString("user_no", null))) {
                viewHolder.name.setText(bean.getBuy_uno());
            } else {
                viewHolder.name.setText(bean.getSale_uno());
            }
        } else if (flag == 1) {
            viewHolder.name.setText(bean.getUser_no());
        }

        if (!TextUtils.isEmpty(bean.getPay_time()) && !bean.getPay_time().equals("null")) {
            viewHolder.time.setText(bean.getPay_time());
        } else {
            viewHolder.time.setText(bean.getAdd_time());
        }
        viewHolder.num.setText(bean.getNumber());
        viewHolder.price.setText(bean.getPrice());

        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    back.setPosition(position, 0);
                }
            }
        });
    }

    private void common(ViewHolder viewHolder, View view) {
        viewHolder.name = view.findViewById(R.id.asset_item_tv_user_no);
        viewHolder.check = view.findViewById(R.id.asset_item_tv_user_check);
        viewHolder.time = view.findViewById(R.id.asset_item_tv_time);
        viewHolder.num = view.findViewById(R.id.asset_item_tv_qc);
        viewHolder.price = view.findViewById(R.id.asset_item_tv_price);
        viewHolder.amount = view.findViewById(R.id.asset_item_tv_amount);
        viewHolder.no_yes = view.findViewById(R.id.asset_item_tv_no_yes);
        view.setTag(viewHolder);
    }

    private class ViewHolder {
        TextView title, name, check, time, num, price, amount, confirm, no_yes, btn;
    }

    private class ViewHolder1 {
        TextView title, name, check, time, cny, usd, s_time;
    }

    public interface CallBack {
        /**
         * @param position item 位置
         * @param flag     类型 :0表示查看信息，1表示取消, 2表示确认收款,3表示查看凭证，4表示上传凭证
         */
        void setPosition(int position, int flag);
    }

    private CallBack back;

    public void setBack(CallBack back) {
        this.back = back;
    }
}