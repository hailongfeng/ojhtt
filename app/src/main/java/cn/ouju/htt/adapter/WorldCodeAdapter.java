package cn.ouju.htt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.AreaCodeBean;

public class WorldCodeAdapter extends MyAdapter<AreaCodeBean> {
    public WorldCodeAdapter(List data, Context context) {
        super(data, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = getContentView(convertView, R.layout.popuwindow_world_code);
            vh=new ViewHolder();
            vh.content=convertView.findViewById(R.id.world_code_tv);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        AreaCodeBean bean= data.get(position);
        vh.content.setText(bean.getChinese_name()+" "+bean.getName());
        return convertView;
    }

    class ViewHolder {
        TextView content;
    }
}
