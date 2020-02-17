package cn.ouju.htt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.NewsBean;


public class HomeNewsAdapter extends MyAdapter<NewsBean> {


    public HomeNewsAdapter(List data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            vh = new ViewHolder();
            view = getContentView(view, R.layout.adapter_home_news_item);
            vh.time = view.findViewById(R.id.news_time);
            vh.title = view.findViewById(R.id.news_title);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        NewsBean bean = data.get(i);
        vh.title.setText(bean.getTitle());
        vh.time.setText(bean.getAdd_time());
        return view;
    }

    class ViewHolder {
        TextView title, time;
    }
}
