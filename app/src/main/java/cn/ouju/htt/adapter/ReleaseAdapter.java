package cn.ouju.htt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.ReleaseBean;

public class ReleaseAdapter extends MyAdapter<ReleaseBean> {
    public ReleaseAdapter(List data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = getContentView(convertView, R.layout.adapter_me_release_item);
            vh.total = convertView.findViewById(R.id.release_item_tv_total);
            vh.time = convertView.findViewById(R.id.release_item_tv_time);
            vh.dsf_int = convertView.findViewById(R.id.release_item_tv_dsf_int);
            vh.dsf_float = convertView.findViewById(R.id.release_item_tv_dsf_float);
            vh.ysf_int = convertView.findViewById(R.id.release_item_tv_ysf_int);
            vh.ysf_float = convertView.findViewById(R.id.release_item_tv_ysf_float);
            convertView.setTag(vh);
        } else
            vh = (ViewHolder) convertView.getTag();
        ReleaseBean bean = data.get(position);
        vh.total.setText(bean.getAmount());
        vh.time.setText(bean.getAdd_time());
        String ysf = "" + bean.getReturn_amount();
        vh.ysf_int.setText(ysf.substring(0, ysf.indexOf(".")));
        vh.ysf_float.setText(ysf.substring(ysf.indexOf("."), ysf.length()));
        String dsf = "" + bean.getMinus_amount();
        vh.dsf_int.setText(dsf.substring(0, dsf.indexOf(".")));
        vh.dsf_float.setText(dsf.substring(dsf.indexOf("."), dsf.length()));
        return convertView;
    }

    class ViewHolder {
        TextView total, time, dsf_int, dsf_float, ysf_int, ysf_float;
    }
}
