package com.neu.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neu.test.R;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Task;

import java.util.ArrayList;
import java.util.List;


/**
 * created by Viki on 2020/3/14
 * system login name : lg
 * created time : 16:38
 * email : 710256138@qq.com
 */

public class RectifyListItemAdapter extends BaseAdapter {
    private static final String TAG = "RectifyListItemAdapter";
    private Context context;
    private String[] num;

    private List<DetectionResult> errorList = new ArrayList<>();

    public RectifyListItemAdapter(Context context,String[] num){
        this.context = context;
        this.num = num;
    }

    public RectifyListItemAdapter(Context context,String[] num,List<DetectionResult> list){
        this.context = context;
        this.num = num;
        this.errorList = list;
    }

    @Override
    public int getCount() {
        return errorList.size();
    }

    @Override
    public Object getItem(int position) {
        return errorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.test_rectify_list_item,null);
            viewHoler = new ViewHoler(convertView);

            convertView.setTag(viewHoler);
        }else {
            viewHoler = (ViewHoler) convertView.getTag();
        }

        viewHoler.question.setText("问题："+errorList.get(position).getSUGGESTION());
        viewHoler.item.setText("排查项目："+errorList.get(position).getJIANCHAXIANGTITLE());

        if (errorList.get(position).getISCHANGED().equals("1")){
            viewHoler.unrectify.setBackgroundColor(context.getResources().getColor(R.color.green));
            viewHoler.rectify_status.setText("已整改");
        }
        else if (errorList.get(position).getISCHANGED().equals("0")){
            viewHoler.unrectify.setBackgroundColor(context.getResources().getColor(R.color.red));
            viewHoler.rectify_status.setText("未整改");
        }
        else{
            viewHoler.unrectify.setBackgroundColor(context.getResources().getColor(R.color.greenyellow));
            viewHoler.rectify_status.setText("现场整改");
        }
        return convertView;
    }

    class ViewHoler{
        TextView question;
        TextView item;
        LinearLayout unrectify;
        TextView num1;
        TextView num2;
        TextView rectify_status;

        public ViewHoler(View view){
            question = view.findViewById(R.id.rectify_item_text_question);
            item = view.findViewById(R.id.rectify_item_text_item);
            unrectify = view.findViewById(R.id.rectity_linearlayout);
            num1 = view.findViewById(R.id.rectify_item_text_num1);
            num2 = view.findViewById(R.id.rectify_item_text_num2);
            rectify_status = view.findViewById(R.id.rectify_linearlayout_textview);
        }
    }
}
