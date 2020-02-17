package cn.ouju.htt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.EmailBean;

public class MeSendEmailAdapter extends MyAdapter<EmailBean> {
    public MeSendEmailAdapter(List data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = getContentView(convertView, R.layout.adapter_send_email_item);
            vh.time = convertView.findViewById(R.id.email_item_tv_time);
            vh.title = convertView.findViewById(R.id.email_item_tv_title);
            convertView.setTag(vh);
        } else
            vh = (ViewHolder) convertView.getTag();
        EmailBean bean = data.get(position);
        vh.title.setText(bean.getTitle());
        vh.time.setText(bean.getSend_time());
        return convertView;
    }

    class ViewHolder {
        TextView title, time;
    }
}
