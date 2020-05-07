package com.neu.test.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.neu.test.R;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Task;
import com.neu.test.layout.MyListView;
import com.neu.test.layout.MyTextView;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.SearchUtil;

import java.util.List;

public class DraftBoxDetailsActivity extends BaseActivity {
    private static final String TAG = "DraftBoxDetailsActivity";

    private Toolbar mToolbar;
    private TextView textView_toolbar;
    private Task task;
    private List<DetectionResult> detectionResults;
    private MyTextView textView_taskName;
    private MyTextView textView_taskID;
    private MyListView listView;
    private SearchUtil searchUtil;
    private DraftDetailAdapter draftDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_box_details);
        searchUtil = new SearchUtil();
        getData();
        initview();
        initToolbar();
    }

    private void initview() {
        textView_taskName = findViewById(R.id.draft_box_detail_textview_taskName);
        textView_taskID = findViewById(R.id.draft_box_detail_textview_taskID);

        listView = findViewById(R.id.draft_box_detail_listview);

        textView_taskName.setText("任务名："+task.getTASKNAME());
        textView_taskID.setText("任务ID："+task.getTASKID());

        draftDetailAdapter = new DraftDetailAdapter();
        listView.setAdapter(draftDetailAdapter);
    }

    private void getData() {
        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra("task");
        detectionResults = (List<DetectionResult>) intent.getSerializableExtra("detectionresults");
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.draft_box_detail_toolbar);
        mToolbar.setTitle(" ");
        mToolbar.setTitleTextColor(Color.WHITE);

        textView_toolbar = findViewById(R.id.draft_box_detail_toolbar_textview);
        textView_toolbar.setText("任务详情");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class DraftDetailAdapter extends BaseAdapter {

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
            ViewHolder viewHolder = new ViewHolder();;
            if (convertView == null){
                convertView = LayoutInflater.from(DraftBoxDetailsActivity.this).inflate(R.layout.redraft_listview_item,null);
                viewHolder.item_name = convertView.findViewById(R.id.redraft_listview_item_textview_name);
                viewHolder.item_status = convertView.findViewById(R.id.redraft_listview_item_textview_status);
                viewHolder.item_image = convertView.findViewById(R.id.redraft_listview_item_circleimage);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.item_name.setText(detectionResults.get(position).getJIANCHAXIANGTITLE());
            viewHolder.item_status.setText(searchUtil.getQualityFromNUm(detectionResults.get(position).getSTATUS()));
            viewHolder.item_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLaw(detectionResults.get(position));
                }
            });
            return convertView;
        }
    }

    private class ViewHolder{
        TextView item_name;
        TextView item_status;
        ImageView item_image;
    }

    private void showLaw(DetectionResult detectionResult) {
        Dialog dlg = new Dialog(DraftBoxDetailsActivity.this,R.style.FullScreen);
        View textEntryView = View.inflate(DraftBoxDetailsActivity.this,R.layout.show_law_and_other, null);
        TextView tv_paichaxize = textEntryView.findViewById(R.id.tv_paichaxize);
        TextView tv_laws = textEntryView.findViewById(R.id.tv_laws);
        Button btn_cancel = textEntryView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        tv_paichaxize.setText(detectionResult.getCHECKCONTENT());
        tv_laws.setText(detectionResult.getLAW());
        dlg.setContentView(textEntryView);
        dlg.show();
    }
}
