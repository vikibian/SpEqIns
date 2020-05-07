package com.neu.test.activity;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.FilePathResult;
import com.neu.test.entity.Task;
import com.neu.test.layout.MyTextView;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.FileResultCallBack;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.ClickUtil;
import com.neu.test.util.PermissionUtils;
import com.neu.test.util.ReloadImageAndVideo;
import com.neu.test.util.SearchUtil;
import com.neu.test.util.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import q.rorbin.badgeview.QBadgeView;

public class RectifyListActivity extends BaseActivity  {
    private static final String TAG = "RectifyListActivity";
    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView toolbar_subtitleLeft;
    private TextView toolabr_subtitleRight;
    private Button button_submit;
    private Intent intentall;
    private Task task = new Task();
    private String taskType = "" ;
    private String title = "";
    private int pposition;
    private String userName;
    private ListView listView;
    private LinearLayout rectify_list_nocontent;
    private LinearLayout rectify_list_hascontent;
    private MyTextView tv_totalitem;
    private RectifyListAdapter rectifyListAdapter;
    private SearchUtil searchUtil = new SearchUtil();

    private List<DetectionResult> detectionResults = new ArrayList<DetectionResult>();//检查结果list
    final int RectifyResultCode = 119;
    private static Map<Integer,Boolean> isDone = new HashMap<>();
    Dialog dialogMenu;
    Dialog dialogContent;
    Dialog dlg;
    private PromptDialog promptDialog;
    private PermissionUtils permissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectify_list);
        promptDialog = new PromptDialog(this);
        permissionUtils = new PermissionUtils(this,RectifyListActivity.this,null,null);
        initView();

        Log.e(TAG, "onCreate: " );
        intentall = getIntent();
        task = (Task) getIntent().getSerializableExtra("task");
        pposition =  getIntent().getIntExtra("position",0);
        title = task.getUSEUNITNAME();
        userName = task.getLOGINNAME();
        taskType =  task.getTASKTYPE();
        detectionResults = (List<DetectionResult>) getIntent().getSerializableExtra("items");
        Log.e(TAG," size: "+detectionResults.size());

        tv_totalitem.setText("--------设备："+task.getDEVID()+"  共 "+detectionResults.size()+" 项待操作--------");

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

        button_submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
//                List<DetectionResult> list = detectionResults.stream().filter(dete -> dete.getSTATUS().equals("2")).collect(Collectors.toList());
                promptDialog.showLoading("提交中...");
                List<DetectionResult> list = new ArrayList<>();
                for (int i=0;i<detectionResults.size();i++){
                    if (detectionResults.get(i).getSTATUS().equals("2")){
                        list.add(detectionResults.get(i));
                    }
                }

                if (list.size() != 0) {
                    promptDialog.dismiss();
                    Toasty.warning(RectifyListActivity.this, "有未操作项，无法提交", Toast.LENGTH_LONG).show();
                } else {
                    String string = new Gson().toJson(detectionResults);
                    Log.e(TAG,"提交时："+string);
                    String url = BaseUrl.BaseUrl+"setItemResult";
                    OkHttp okHttp = new OkHttp();
                    okHttp.postBypostString(url, string, new FileResultCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            Log.e("error", e.toString());
                            promptDialog.dismiss();
                        }

                        @Override
                        public void onResponse(FilePathResult filePathResult, int i) {
                            Log.e("message", filePathResult.getMessage());
                            if (filePathResult.getMessage().equals("任务提交成功")){
                                Toasty.info(RectifyListActivity.this, "提交成功！", Toast.LENGTH_LONG).show();
                                Intent intent = getIntent();
                                intent.putExtra("position",pposition);
                                setResult(RESULT_OK,intent);
                                finish();
                            }else {
                                promptDialog.dismiss();
                                Toasty.error(RectifyListActivity.this, "提交失败！", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
            }
        });

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_rectify_list);
        toolbar_title = findViewById(R.id.toolbar_rectify_list_title);
        toolbar_subtitleLeft = findViewById(R.id.toolbar_rectify_list_subtitle_left);
        toolabr_subtitleRight = findViewById(R.id.toolbar_rectify_list_subtitle_right);

        toolbar_title.setTextSize(18);
        toolbar_subtitleLeft.setTextSize(13);
        toolabr_subtitleRight.setTextSize(13);

        toolbar_title.setText(taskType+"               ");//加空格的原因是让其显示居中
        toolbar_subtitleLeft.setText(title);
        toolabr_subtitleRight.setText(taskType);
        toolabr_subtitleRight.setTextColor(getResources().getColor(R.color.yellow));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initView(){
        listView = findViewById(R.id.rectify_list_lv);
        rectify_list_nocontent = findViewById(R.id.rectify_list_nocontent);
        rectify_list_hascontent = findViewById(R.id.rectify_list_hascontent);
        tv_totalitem = findViewById(R.id.rectify_list_tv_totalitem);
        button_submit = findViewById(R.id.rectify_list_submit_button);
    }

    //Menu的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);//不设置RESULT_OK的原因是会出现bug
                this.finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
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
//            rectifyListViewHolder.recify_textview_attach_num = convertView.findViewById(R.id.recify_item_attach_num);
            rectifyListViewHolder.recify_imageview_arrowright = convertView.findViewById(R.id.recify_item_arrowRight);
            rectifyListViewHolder.recify_imageview_error = convertView.findViewById(R.id.recify_list_item_error);


            rectifyListViewHolder.recify_textview_leftnum.setText((position+1)+"");
            rectifyListViewHolder.recify_textview_context.setText(detectionResults.get(position).getJIANCHAXIANGTITLE());


            //设置按钮状态
            if (detectionResults.get(position).getISCHANGED().equals(searchUtil.changed)){
                if (detectionResults.get(position).getSTATUS().equals(searchUtil.recifyQualify)){
                    rectifyListViewHolder.recify_checkbox_unqualified.setChecked(false);
                    rectifyListViewHolder.recify_checkbox_recifyqualified.setChecked(true);
                }else if (detectionResults.get(position).getSTATUS().equals(searchUtil.nohege)){
                    rectifyListViewHolder.recify_checkbox_unqualified.setChecked(true);
                    rectifyListViewHolder.recify_checkbox_recifyqualified.setChecked(false);
                }
            }

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ClickUtil.isFastClick()){
                        return;
                    }
                    switch (v.getId()){
                        case R.id.recify_item_attach:
                            promptDialog.showLoading("加载中...");
                            goToCheckMediaActivity(position);
                            break;
                        case R.id.recify_item_status_unqualified:
                            Log.e(TAG, "onClick: 不合格 ");
                            rectifyListViewHolder.recify_checkbox_unqualified.setChecked(false);
                            if (detectionResults.get(position).getISCHANGED().equals(searchUtil.changed)
                                    &&detectionResults.get(position).getSTATUS().equals(searchUtil.nohege)){
                                Toasty.warning(RectifyListActivity.this,"已选择，无法重复操作");
                                rectifyListViewHolder.recify_checkbox_unqualified.setChecked(true);
                                rectifyListViewHolder.recify_checkbox_recifyqualified.setChecked(false);
                            }else {
                                promptDialog.showLoading("加载中...");
                                goRectifyResultActivity(position,searchUtil.nohege);
                            }
                            break;

                        case  R.id.recify_item_status_recifyqualified:
                            Log.e(TAG, "onClick: 整改合格 ");
                            rectifyListViewHolder.recify_checkbox_recifyqualified.setChecked(false);
                            if (detectionResults.get(position).getISCHANGED().equals(searchUtil.changed)
                                    &&detectionResults.get(position).getSTATUS().equals(searchUtil.recifyQualify)){
                                Toasty.warning(RectifyListActivity.this,"已选择，无法重复操作");
                                rectifyListViewHolder.recify_checkbox_recifyqualified.setChecked(true);
                                rectifyListViewHolder.recify_checkbox_unqualified.setChecked(false);
                            }else {
                                promptDialog.showLoading("加载中...");
                                goRectifyResultActivity(position,searchUtil.recifyQualify);
                            }
                            break;

                        case R.id.recify_list_item_error:
                            dlg = new Dialog(RectifyListActivity.this,R.style.FullScreen);
                            View textEntryView = View.inflate(RectifyListActivity.this,R.layout.show_law_and_other, null);
                            TextView tv_paichaxize = textEntryView.findViewById(R.id.tv_paichaxize);
                            TextView tv_laws = textEntryView.findViewById(R.id.tv_laws);
                            Button btn_cancel = textEntryView.findViewById(R.id.btn_cancel);
                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dlg.dismiss();
                                }
                            });
                            tv_paichaxize.setText(detectionResults.get(position).getCHECKCONTENT());
                            tv_laws.setText(detectionResults.get(position).getLAW());
                            dlg.setContentView(textEntryView);
                            dlg.show();
                            break;

                        case R.id.recify_item_arrowRight:
                            if (rectifyListViewHolder.recify_checkbox_recifyqualified.isChecked()){
                                promptDialog.showLoading("加载中...");
                                goRectifyResultActivity(position,searchUtil.recifyQualify);
                            }else if (rectifyListViewHolder.recify_checkbox_unqualified.isChecked()){
                                promptDialog.showLoading("加载中...");
                                goRectifyResultActivity(position,searchUtil.nohege);
                            }
                            break;
                    }
                }
            };

            rectifyListViewHolder.recify_imageview_attach.setOnClickListener(clickListener);
            rectifyListViewHolder.recify_checkbox_unqualified.setOnClickListener(clickListener);
            rectifyListViewHolder.recify_checkbox_recifyqualified.setOnClickListener(clickListener);
            rectifyListViewHolder.recify_imageview_error.setOnClickListener(clickListener);

            if (detectionResults.get(position).getISCHANGED().trim().equals(searchUtil.changed)){
                rectifyListViewHolder.recify_imageview_arrowright.setVisibility(View.VISIBLE);
                rectifyListViewHolder.recify_imageview_arrowright.setOnClickListener(clickListener);
            }else if (detectionResults.get(position).getISCHANGED().isEmpty()
                    || detectionResults.get(position).getISCHANGED().trim().equals(searchUtil.unchanged)){
                rectifyListViewHolder.recify_imageview_arrowright.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }
    }


    static class RectifyListViewHolder{
        public TextView recify_textview_leftnum;
        public MyTextView recify_textview_context;
        public CheckBox recify_checkbox_unqualified;
        public CheckBox recify_checkbox_recifyqualified;
        public ImageView recify_imageview_attach;
        public TextView recify_textview_attach_num;
        public ImageView recify_imageview_arrowright;

        public ImageView recify_imageview_error;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == RectifyResultCode){
                DetectionResult returnResult = (DetectionResult) data.getSerializableExtra("detectionResult");
                int pos  = 0;
                pos = data.getIntExtra("position",0);
                detectionResults.set(pos,returnResult);
                listView.setAdapter(rectifyListAdapter);
                Log.e(TAG, "onActivityResult: "+detectionResults.get(pos).getSTATUS());
                Log.e(TAG, "onActivityResult: pos  "+pos);
                Log.e(TAG, "onClick: status  "+detectionResults.get(pos).getSTATUS() );
                Log.e(TAG, "onClick: changed  "+detectionResults.get(pos).getISCHANGED() );
                String string = new Gson().toJson(detectionResults.get(pos));
                Log.e(TAG, "onActivityResult: detectionResults:"+string);
            }

        }
    }


    private void goRectifyResultActivity(int position, String isHege) {
        if (permissionUtils.canGoNextstep()){
            Intent intent = new Intent(RectifyListActivity.this,RectifyActivity.class);
            intent.putExtra("detectionResult",detectionResults.get(position));
            intent.putExtra("toolbarTitle",title);
            intent.putExtra("toolbarTaskType",taskType);
            intent.putExtra("position",position);
            intent.putExtra("isHege",isHege);
            startActivityForResult(intent,RectifyResultCode);
        }else {
            promptDialog.dismiss();
            Toast.makeText(this, "没有获取相应权限！", Toast.LENGTH_SHORT).show();
        }

    }



    private void goToCheckMediaActivity(int position) {
        Intent intent = new Intent(RectifyListActivity.this,CheckMediaForRectifyActivity.class);
        intent.putExtra("detectionResult",detectionResults.get(position));
        intent.putExtra("title",title);
        intent.putExtra("taskType",taskType);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        promptDialog.dismissImmediately();
    }
}
