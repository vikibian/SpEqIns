package com.neu.test.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.test.R;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Task;

import java.util.ArrayList;
import java.util.List;

public class RectifyListActivity extends AppCompatActivity  {
    private static final String TAG = "RectifyListActivity";
    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView toolbar_subtitleLeft;
    private TextView toolabr_subtitleRight;
    private Intent intentall;
    private Task task = new Task();
    private String taskType = "" ;
    private String title = "";
    private int pposition;
    private String userName;
    private ListView listView;
    private LinearLayout rectify_list_nocontent;
    private LinearLayout rectify_list_hascontent;
    private TextView tv_totalitem;
    private RectifyListAdapter rectifyListAdapter;

    private List<DetectionResult> detectionResults = new ArrayList<DetectionResult>();//检查结果list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectify_list);

        listView = findViewById(R.id.rectify_list_lv);
        rectify_list_nocontent = findViewById(R.id.rectify_list_nocontent);
        rectify_list_hascontent = findViewById(R.id.rectify_list_hascontent);
        tv_totalitem = findViewById(R.id.rectify_list_tv_totalitem);


        intentall = getIntent();
        task = (Task) getIntent().getSerializableExtra("task");
        pposition =  getIntent().getIntExtra("position",0);
        title = task.getUSEUNITNAME();
        userName = task.getLOGINNAME();
        taskType =  task.getTASKTYPE();
        detectionResults = (List<DetectionResult>) getIntent().getSerializableExtra("items");
        Log.e(TAG," size: "+detectionResults.size());

        tv_totalitem.setText("------------共"+detectionResults.size()+"项------------");

        int size = detectionResults.size();
        if(size == 0){
            rectify_list_nocontent.setVisibility(View.VISIBLE);
            rectify_list_hascontent.setVisibility(View.GONE);
        }else {
            rectify_list_nocontent.setVisibility(View.GONE);
            rectify_list_hascontent.setVisibility(View.VISIBLE);
        }

        rectifyListAdapter = new RectifyListAdapter();
        listView.setAdapter(rectifyListAdapter);

        initToolbar();


    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_rectify_list);
        toolbar_title = findViewById(R.id.toolbar_rectify_list_title);
        toolbar_subtitleLeft = findViewById(R.id.toolbar_rectify_list_subtitle_left);
        toolabr_subtitleRight = findViewById(R.id.toolbar_rectify_list_subtitle_right);

        toolbar_title.setTextSize(20);
        toolbar_subtitleLeft.setTextSize(13);
        toolabr_subtitleRight.setTextSize(13);

        toolbar_title.setText(getResources().getString(R.string.app_name));
        toolbar_subtitleLeft.setText(title);
        toolabr_subtitleRight.setText(taskType);
        toolabr_subtitleRight.setTextColor(getResources().getColor(R.color.yellow));
    }

    public void initView(){

    }

    class RectifyListAdapter extends BaseAdapter {

        //返回集合数据数量
        @Override
        public int getCount() {
            return detectionResults.size();
        }

        //返回指定下标的数据对象
        @Override
        public Object getItem(int position) {
            return detectionResults.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RectifyListViewHolder rectifyListViewHolder;
            //如果没有复用的

            rectifyListViewHolder = new RectifyListViewHolder();
            //加载item的布局
            convertView = View.inflate(RectifyListActivity.this, R.layout.fragment_rectify_list_item, null);
            rectifyListViewHolder.recify_textview_leftnum = convertView.findViewById(R.id.recify_item_textview_leftnum);
            rectifyListViewHolder.recify_textview_context = convertView.findViewById(R.id.recify_item_text_context);
            rectifyListViewHolder.recify_checkbox_unqualified = convertView.findViewById(R.id.recify_item_status_unqualified);
            rectifyListViewHolder.recify_checkbox_recifyqualified = convertView.findViewById(R.id.recify_item_status_recifyqualified);
            rectifyListViewHolder.recify_imageview_attach = convertView.findViewById(R.id.recify_item_attach);
            rectifyListViewHolder.recify_textview_attach_num = convertView.findViewById(R.id.recify_item_attach_num);
            rectifyListViewHolder.recify_imageview_arrowright = convertView.findViewById(R.id.recify_item_arrowRight);

            rectifyListViewHolder.recify_textview_leftnum.setText((position+1)+"");
            rectifyListViewHolder.recify_textview_context.setText(detectionResults.get(position).getJIANCHAXIANGTITLE());

            //测试
            if (position == 0){
                detectionResults.get(position).setCHANGEDIMAGE("image_202031610246.jpg,image_202031662445.jpg");
            }
            //测试
            if (position == 1){
                detectionResults.get(position).setCHANGEDIMAGE("image_202031610246.jpg,image_202031662445.jpg");
                detectionResults.get(position).setCHANGEDVIDEO("video_1584540967093.mp4,video_1584582023465.mp4");
            }

            if (detectionResults.get(position).getCHANGEDIMAGE().equals("")&&
            detectionResults.get(position).getCHANGEDVIDEO().equals("")){
                rectifyListViewHolder.recify_imageview_attach.setVisibility(View.GONE);
                rectifyListViewHolder.recify_textview_attach_num.setVisibility(View.GONE);
            }else if (detectionResults.get(position).getCHANGEDIMAGE().equals("")){
                String[] videopath = detectionResults.get(position).getCHANGEDVIDEO().split(",");
                rectifyListViewHolder.recify_textview_attach_num.setText(videopath.length+"");
            } else if (detectionResults.get(position).getCHANGEDVIDEO().equals("")){
                String[] imagepath = detectionResults.get(position).getCHANGEDIMAGE().split(",");
                rectifyListViewHolder.recify_textview_attach_num.setText(imagepath.length+"");
            } else {
                String[] imagepath = detectionResults.get(position).getCHANGEDIMAGE().split(",");
                String[] videopath = detectionResults.get(position).getCHANGEDVIDEO().split(",");
                rectifyListViewHolder.recify_textview_attach_num.setText(imagepath.length+videopath.length+"");
            }

            rectifyListViewHolder.recify_imageview_attach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToCheckMediaActivity(position);
                }
            });

            rectifyListViewHolder.recify_checkbox_unqualified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rectifyListViewHolder.recify_checkbox_recifyqualified.setChecked(false);
                    goSuggestionActivity(position);
                }
            });

            rectifyListViewHolder.recify_checkbox_recifyqualified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rectifyListViewHolder.recify_checkbox_unqualified.setChecked(false);
                    goRectifyResultActivity(position);
                }
            });

            return convertView;
        }
    }


    static class RectifyListViewHolder{
        public TextView recify_textview_leftnum;
        public TextView recify_textview_context;
        public CheckBox recify_checkbox_unqualified;
        public CheckBox recify_checkbox_recifyqualified;
        public ImageView recify_imageview_attach;
        public TextView recify_textview_attach_num;
        public ImageView recify_imageview_arrowright;

    }

    private void goSuggestionActivity(int position) {
        Intent intent = new Intent(RectifyListActivity.this,SuggestionActivity.class);
        intent.putExtra("detectionResult",detectionResults.get(position));
        intent.putExtra("title",title);
        intent.putExtra("taskType",taskType);
        intent.putExtra("position",position);
        intent.putExtra("status",detectionResults.get(position).getSTATUS());
        startActivity(intent);
    }

    private void goRectifyResultActivity(int position) {
        Intent intent = new Intent(RectifyListActivity.this,RectifyResultActivity.class);
        intent.putExtra("detectionResult",detectionResults.get(position));
        intent.putExtra("toolbarTitle",title);
        intent.putExtra("toolbarTaskType",taskType);
        intent.putExtra("position",position);
        startActivity(intent);
    }



    private void goToCheckMediaActivity(int position) {
        Intent intent = new Intent(RectifyListActivity.this,CheckMediaForRectifyActivity.class);
        intent.putExtra("detectionResult",detectionResults.get(position));
        intent.putExtra("title",title);
        intent.putExtra("taskType",taskType);
        startActivity(intent);
    }





}
