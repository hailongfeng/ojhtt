package cn.ouju.htt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.TokenBean;

public class TokenAdapter extends MyAdapter<TokenBean> {
    public TokenAdapter(List data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = getContentView(convertView, R.layout.adapter_me_token_item);
            vh.odd = convertView.findViewById(R.id.fcc_item_tv_odd);
            vh.time = convertView.findViewById(R.id.fcc_item_tv_time);
            vh.fcc = convertView.findViewById(R.id.fcc_item_tv_fcc);
            vh.gfc = convertView.findViewById(R.id.fcc_item_tv_gfc);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        TokenBean bean = data.get(position);
        vh.odd.setText(bean.getOrder_no());
        vh.time.setText(bean.getAdd_time());
        vh.fcc.setText("+" + bean.getToken_num() + "TOKEN");
        vh.gfc.setText("-" + bean.getDti_num() + "DTI");
        return convertView;
    }

    class ViewHolder {
        TextView odd, time, fcc, gfc;
    }
}
