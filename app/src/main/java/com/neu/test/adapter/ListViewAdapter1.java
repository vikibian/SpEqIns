package com.neu.test.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neu.test.R;
import com.neu.test.entity.DetectionItem;
import com.neu.test.entity.DetectionItem1;
import com.neu.test.entity.DetectionResult;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListViewAdapter1 extends BaseAdapter {
    private final String TAG ="ListViewAdapter1";
    private Context context;
    private List<DetectionItem1> listDatas;
    private List<DetectionResult> listResult;
    private String stringFlag;


//
//    public ListViewAdapter1(Context context,List<DetectionItem> listData,String str){
//        this.context = context;
//        this.listDatas = listData;
//        Log.d(TAG,"构造器");
//        this.stringFlag = str;
//
//
//    }

//    public ListViewAdapter1(Context context, List<DetectionItem1> listData, String str){
//        this.context = context;
//        this.listDatas = listData;
//        Log.d(TAG,"构造器");
//        this.stringFlag = str;
//    }

    public ListViewAdapter1(Context context, List<DetectionResult> listResult, String str){
        this.context = context;
        this.listResult = listResult;
        Log.d(TAG,"构造器");
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
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.show_searched_result_listview_item,null);
            viewHolder = new ViewHolder(convertView,position);

            if (listResult.get(position).getSTATUS().equals("1")){
                viewHolder.mTextView_name.setText(listResult.get(position).getCHECKCONTENT());
                //viewHolder.mTextView_name.setText(listDatas.get(position).getCHECKCONTENT());
                viewHolder.mTextView_status.setText("合格");
                int color = Color.parseColor("#008000");
//                viewHolder.circleImageView.setBackgroundColor(color);
                viewHolder.circleImageView.setImageResource(R.drawable.circleimageview_green);
            }else if (listResult.get(position).getSTATUS().equals("-1")){
                viewHolder.mTextView_name.setText(listResult.get(position).getCHECKCONTENT());
                //viewHolder.mTextView_name.setText(listDatas.get(position).getCHECKCONTENT());
                viewHolder.mTextView_status.setText("不合格");
                int color = Color.parseColor("#ff3333");
//                viewHolder.circleImageView.setBackgroundColor(color);
                viewHolder.circleImageView.setImageResource(R.drawable.circleimageview_red);
            }else {
                viewHolder.mTextView_name.setText(listResult.get(position).getCHECKCONTENT());
            }

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder{
        TextView mTextView_name;
        TextView mTextView_status;
        CircleImageView circleImageView;

        public ViewHolder(View convertView,int pos){
            mTextView_name = convertView.findViewById(R.id.show_searched_result_listview_item_textview_name);
            mTextView_status = convertView.findViewById(R.id.show_searched_result_listview_item_textview_status);
            circleImageView = convertView.findViewById(R.id.show_searched_result_listview_item_circleimage);

        }



    }
}


