package com.neu.test.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neu.test.R;
import com.neu.test.entity.DetectionItem1;
import com.neu.test.entity.DetectionResult;
import com.neu.test.util.SearchUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListViewAdapter1 extends BaseAdapter {
    private final String TAG ="ListViewAdapter1";
    private Context context;
    private List<DetectionResult> listResult = new ArrayList<>();
    private String stringFlag;
    private SearchUtil searchUtil = new SearchUtil();


    public ListViewAdapter1(Context context, List<DetectionResult> listResult, String str){
        this.context = context;
        this.listResult = listResult;
        Log.d(TAG,"构造器");
        Log.e(TAG,"listresult: "+listResult.toString());
        this.stringFlag = str;
    }


    @Override
    public int getCount() {
        return listResult.size();
    }

    @Override
    public Object getItem(int position) {
        return listResult.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        convertView = LayoutInflater.from(context).inflate(R.layout.show_searched_result_listview_item,null);
        viewHolder = new ViewHolder(convertView,position);
        convertView.setTag(viewHolder);

        if (listResult.get(position).getSTATUS().equals(searchUtil.hege)
                ||listResult.get(position).getSTATUS().equals(searchUtil.hegeText)){
            viewHolder.mTextView_name.setText(listResult.get(position).getJIANCHAXIANGTITLE());
            viewHolder.mTextView_status.setText(searchUtil.hegeText);
            viewHolder.imageView.setImageResource(R.drawable.ic_check_circle_green_800_24dp);
        }else if (listResult.get(position).getSTATUS().equals(searchUtil.nohege)
                ||listResult.get(position).getSTATUS().equals(searchUtil.nohegeText)){
            viewHolder.mTextView_name.setText(listResult.get(position).getJIANCHAXIANGTITLE());
            viewHolder.mTextView_status.setText(searchUtil.nohegeText);
            viewHolder.imageView.setImageResource(R.drawable.ic_highlight_off_red_600_24dp);
        }else if (listResult.get(position).getSTATUS().equals(searchUtil.undecided)
                ||listResult.get(position).getSTATUS().equals(searchUtil.undecidedText)){
            viewHolder.mTextView_name.setText(listResult.get(position).getJIANCHAXIANGTITLE());
            viewHolder.mTextView_status.setText(searchUtil.undecidedText);
            viewHolder.imageView.setImageResource(R.drawable.ic_help_outline_yellow_a200_24dp);
        } else if (listResult.get(position).getSTATUS().equals(searchUtil.recifyQualify)
                ||listResult.get(position).getSTATUS().equals(searchUtil.recifyQualifyText)){
            viewHolder.mTextView_name.setText(listResult.get(position).getJIANCHAXIANGTITLE());
            viewHolder.mTextView_status.setText(searchUtil.recifyQualifyText);
            viewHolder.imageView.setImageResource(R.drawable.ic_offline_pin_light_green_700_24dp);
        }

        return convertView;
    }

    class ViewHolder{
        TextView mTextView_name;
        TextView mTextView_status;
        CircleImageView circleImageView;
        ImageView imageView;

        public ViewHolder(View convertView,int pos){
            mTextView_name = convertView.findViewById(R.id.show_searched_result_listview_item_textview_name);
            mTextView_status = convertView.findViewById(R.id.show_searched_result_listview_item_textview_status);
//            circleImageView = convertView.findViewById(R.id.show_searched_result_listview_item_circleimage);
            imageView = convertView.findViewById(R.id.show_searched_result_listview_item_circleimage);

        }



    }
}


