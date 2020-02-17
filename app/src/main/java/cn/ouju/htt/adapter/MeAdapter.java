package cn.ouju.htt.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.MeMenuBean;


public class MeAdapter extends MyAdapter<MeMenuBean> {
    public MeAdapter(List data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = getContentView(convertView, R.layout.adapter_me_item);
            vh.title = convertView.findViewById(R.id.me_item_tv_title);
            vh.pic = convertView.findViewById(R.id.me_item_iv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        MeMenuBean bean = data.get(position);
        vh.title.setText(bean.getTabs());
        vh.pic.setImageResource(bean.getImg_res());
        return convertView;
    }

    class ViewHolder {
        TextView title;
        ImageView pic;
    }
}
