package com.neu.test.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.neu.test.R;
import com.neu.test.entity.DetectionResult;

import java.util.ArrayList;
import java.util.List;

public class AnswerListActivity extends AppCompatActivity {
    private static final String TAG = "AnswerListActivity";
    private Button button_left;
    private Button button_right;
    private ListView lv_answer;
    private List<DetectionResult> detectionResults = new ArrayList<>();
    private Intent intent;
    private AnswerListActivity.AnswerListAdapter answerListAdapter;
    private ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_list);
        //初始化视图
        initView();
        //初始化参数
        initParams();
        //根据参数改变视图内容
        changeViewByParams();
        //设置监听
        setListener();
    }

    public void initView(){
        button_left = findViewById(R.id.button_answerlist_left);
        button_right = findViewById(R.id.button_answerlist_right);
        lv_answer = findViewById(R.id.lv_answer);
    }

    public void initParams(){
        intent = getIntent();
        detectionResults = (List<DetectionResult>) intent.getSerializableExtra("detectionResults");
        answerListAdapter = new AnswerListAdapter();
        viewHolder = new ViewHolder();
    }

    public void changeViewByParams(){
        lv_answer.setAdapter(answerListAdapter);
    }

    public void setListener(){
        button_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_left.setBackground(getResources().getDrawable(R.drawable.logincode_left_press));
                button_left.setTextColor(getResources().getColor(R.color.white));

                button_right.setBackground(getResources().getDrawable(R.drawable.logincode_right));
                button_right.setTextColor(getResources().getColor(R.color.black));
            }
        });

        button_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_right.setBackground(getResources().getDrawable(R.drawable.logincode_right_press));
                button_right.setTextColor(getResources().getColor(R.color.white));

                button_left.setBackground(getResources().getDrawable(R.drawable.logincode_left));
                button_left.setTextColor(getResources().getColor(R.color.black));
            }
        });

    }

    class AnswerListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return detectionResults.size();
        }

        @Override
        public Object getItem(int position) {
            return detectionResults.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //加载item的布局
            convertView = View.inflate(AnswerListActivity.this, R.layout.item_anwerslist, null);
            viewHolder = new ViewHolder();



            return convertView;
        }
    }




    static class ViewHolder{

    }
}
