package cn.ouju.htt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ouju.htt.R;
import cn.ouju.htt.bean.TeamListBean;


public class MeTeamAdapter extends MyAdapter<TeamListBean> {
    public MeTeamAdapter(List data, Context context) {
        super(data, context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 vh;
        if (convertView == null) {
            vh = new ViewHolder1();
            convertView = getContentView(convertView, R.layout.adapter_me_team_item1);
            vh.number = convertView.findViewById(R.id.team_item_tv_num);
            vh.person = convertView.findViewById(R.id.team_item_tv_person);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder1) convertView.getTag();
        }
        TeamListBean bean = data.get(position);
        vh.number.setText(bean.getUser_no());
        vh.person.setText(bean.getRecount());

        return convertView;
    }

    class ViewHolder1 {
        TextView number;
        TextView person;
    }
}
