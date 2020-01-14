package com.neu.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neu.test.R;

import java.util.List;

public class DropDownMenuAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private int checkItemPosition = 0;

    public DropDownMenuAdapter(Context context, List<String> list1){
        this.context = context;
        this.list = list1;
    }

    public void setCheckItem(int position){
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_default_drop_down,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTextView.setText(list.get(position));
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                viewHolder.mTextView.setTextColor(context.getResources().getColor(R.color.drop_down_selected));
                viewHolder.mTextView.setBackgroundResource(R.color.check_bg);
            } else {
                viewHolder.mTextView.setTextColor(context.getResources().getColor(R.color.drop_down_unselected));
                viewHolder.mTextView.setBackgroundResource(R.color.white);
            }
        }

        return convertView;
    }

    class ViewHolder{
        TextView mTextView;

        public ViewHolder(View view){
            mTextView = view.findViewById(R.id.item_default_drop_down_text);
        }
    }
}
