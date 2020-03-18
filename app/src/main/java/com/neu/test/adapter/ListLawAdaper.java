package com.neu.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neu.test.R;
import com.neu.test.entity.FileBean;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Viki on 2020/3/11
 * system login name : lg
 * created time : 19:26
 * email : 710256138@qq.com
 */
public class ListLawAdaper extends BaseAdapter {

    private Context mContext;
    private List<FileBean> laws = new ArrayList<>();


    public ListLawAdaper(Context context, List<FileBean> lawPaths){
        this.mContext = context;
        this.laws = lawPaths;
    }

    @Override
    public int getCount() {
        return laws.size();
    }

    @Override
    public Object getItem(int position) {
        return laws.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_law_item,null);
            viewHolder = new ViewHolder(convertView);

            viewHolder.law_title.setText(laws.get(position).getPDFTITLE());
            viewHolder.law_subdtitle.setText(laws.get(position).getPDFDETAIL());
            viewHolder.law_publishtime.setText(laws.get(position).getPUBLISHTIME());

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder{
        public TextView law_title;
        public TextView law_subdtitle;
        public TextView law_publishtime;

        public ViewHolder(View view){
            law_title = view.findViewById(R.id.list_law_item_title);
            law_subdtitle = view.findViewById(R.id.list_law_item_subtitle);
            law_publishtime = view.findViewById(R.id.list_law_item_publishtime);
        }
    }
}
