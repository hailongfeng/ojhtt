package cn.ouju.htt.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/19.
 */

public abstract class MyAdapter<T extends Serializable> extends BaseAdapter {
    List<T> data;
    protected Context context;
    protected LayoutInflater inflater;

    public MyAdapter(List<T> data, Context context) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return data != null ? data.get(position) : position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getContentView(View convertView, int layout_res) {
        convertView = inflater.inflate(layout_res, null);
        return convertView;
    }

    public void setData(List<T> lists) {
        data.clear();
        data.addAll(lists);
        notifyDataSetChanged();
    }
}
