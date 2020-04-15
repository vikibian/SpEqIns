package com.neu.test.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.neu.test.R;
import com.neu.test.adapter.DropDownMenuAdapter;
import com.neu.test.adapter.ListViewAdapter1;
import com.neu.test.entity.DetectionItem1;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.fragment.ShowSearchedResultFragment;
import com.neu.test.layout.MyListView;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListDetectionResultCallBack;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.SearchUtil;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

public class ShowSearchedResultActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ShowSearchedResult";
    private TextView textView_time;
    private TextView textView_deviceType;
    private TextView textView_qualify;
    private TextView textView_checked;
    private TextView textView_user;
    private TextView textView_address;
    private TextView textView_title;
    private Button bt_goback;
    private Toolbar mToolbar;
    private TextView toolbar_title;
    private Button button_back;
    private MaterialSpinner sp_fliter;

    private MyListView listView;
    private ListViewAdapter1 listViewAdapter1;

    private CircleImageView circleImageView;

    private DropDownMenu dropDownMenu;
    private String headers[] = {"选择过滤项"};

    private DropDownMenuAdapter dropDownMenuAdapter;
    private List<View> popupViews = new ArrayList<>();
    private int posFlag=0;
    private List<DetectionItem1> listDatas;

    private List<Task> tasks;
    private Task task;
    private List<DetectionResult> listResult = new ArrayList<>();
    private String taskID;
    private String devID;
    private SearchUtil searchUtil = new SearchUtil();
    private List<DetectionResult> listDatas_qualified = new ArrayList<>();
    private List<DetectionResult> listDatas_unqualified = new ArrayList<>();
    private List<DetectionResult> listDatas_undecied = new ArrayList<>();//代表的是整改合格
    private PromptDialog promptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_list);
        promptDialog = new PromptDialog(this);
        task = (Task) getIntent().getSerializableExtra("tasks");
        Log.e(TAG,"获取数据");
        Log.e(TAG,"获取数据"+task.toString());
        Log.e(TAG,"获取数据"+task.getTASKID());
        taskID = task.getTASKID();
        devID = task.getDEVID();

        listDatas = new ArrayList<DetectionItem1>();

        initView();
        initToolbar();
        getTaskList();
        initContent();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: i "+ i);

                filiterResult();

                Intent intent = new Intent(ShowSearchedResultActivity.this, CheckDetailsActivity.class);

                DetectionResult detectionResult = new DetectionResult();
                if (posFlag == 1){
                    detectionResult = listDatas_qualified.get(i);
                }else if (posFlag == 2){
                    detectionResult = listDatas_unqualified.get(i);
                }else if(posFlag == 0){
                    detectionResult = listResult.get(i);
                }else if (posFlag == 3){
                    detectionResult = listDatas_undecied.get(i);
                }

                intent.putExtra("detectionResult",detectionResult);
                intent.putExtra("task",task);
                Log.e(TAG,"  测试显示： "+detectionResult.getJIANCHAXIANGTITLE());
                String string = new Gson().toJson(detectionResult);
                Log.e(TAG,"  测试显示结果： "+string);

                startActivity(intent);

            }
        });

        sp_fliter.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            /**
             * @param view
             * @param position 表示的是选择的向位于传入的list中的位置
             * @param id 表示的选择的项位于待选项中的位置
             * @param item 返回的是选择的spinner的文字
             */
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                posFlag = position;

                List<DetectionResult> listQualifed = new ArrayList<>();
                List<DetectionResult> listunQualifed = new ArrayList<>();
                List<DetectionResult> listundecied = new ArrayList<>();//代表的是整改合格
                for(int i=0;i<listResult.size();i++){
                    if(listResult.get(i).getSTATUS().equals(searchUtil.hegeText)
                            ||listResult.get(i).getSTATUS().equals(searchUtil.hege)){
                        listQualifed.add(listResult.get(i));
                    }
                    else if(listResult.get(i).getSTATUS().equals(searchUtil.nohegeText)
                            ||listResult.get(i).getSTATUS().equals(searchUtil.nohege)){
                        listunQualifed.add(listResult.get(i));
                    }else if (listResult.get(i).getSTATUS().equals(searchUtil.recifyQualifyText)
                            ||listResult.get(i).getSTATUS().equals(searchUtil.recifyQualify)){
                        listundecied.add(listResult.get(i));
                    }
                }
                if(position==0){
                    refresh(listResult);
                }else if (position==1){
                    refresh(listQualifed);
                }else if (position==2){
                    refresh(listunQualifed);
                }else if (position == 3){
                    refresh(listundecied);
                }
            }
        });
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar_searchResult);
        toolbar_title = findViewById(R.id.toolbar_searchResult_title);
        toolbar_title.setTextSize(18);

        toolbar_title.setText("查询结果显示");
        mToolbar.setTitle("");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getTaskList() {
        String url;
        url = BaseUrl.BaseUrl+"selectItemResultServlet";
        Log.d(TAG,"POST url: "+url);
        Map<String, String> map = new HashMap<>();
        map.put("username",LoginActivity.inputName);
//        map.put("taskID","1234567891011");//1affb4ca-1b34-4d99-9222-5ce1ed62afa5   taskID
        map.put("taskID",taskID);
        Log.e(TAG,"map: "+ map.toString());
//        map.put("DEVID","五号电梯");//123456  devID
        map.put("RUNWATERNUM",task.getRUNWATERNUM());
        map.put("DEVID",task.getDEVID());
        Log.e(TAG,"map: "+ map.toString());

        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new ListDetectionResultCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.toString());
                Log.e(TAG," 数据错误");
            }

            @Override
            public void onResponse(Result<List<DetectionResult>> reponse, int i) {
                if (reponse.getMessage().equals("获取成功")){
                    listResult  = reponse.getContent();
                    String result = new Gson().toJson(listResult);
                    Log.e(TAG,"测试结果："+result);
                    if (reponse.getMessage().length() == 0){
                        Log.e(TAG,"没有post数据");
                    }else {
                        //初始化listview应该在获取数据之后
                        if (listResult.size()!=0){
                            Log.e(TAG,"返回list的大小："+listResult.size());
                            initListViewAdapter1();
                        }
                    }
                }else {
                    Toasty.error(getApplicationContext(),"获取数据失败!",Toasty.LENGTH_SHORT,true).show();
                }
            }
        });
    }

    private void filiterResult() {
        for (int i=0;i<listResult.size();i++){
            if (listResult.get(i).getSTATUS().equals(searchUtil.hegeText)
                    ||listResult.get(i).getSTATUS().equals(searchUtil.hege)){
                listDatas_qualified.add(listResult.get(i));
            }
            if (listResult.get(i).getSTATUS().equals(searchUtil.nohegeText)
                    ||listResult.get(i).getSTATUS().equals(searchUtil.nohege)){
                listDatas_unqualified.add(listResult.get(i));
            }
            if (listResult.get(i).getSTATUS().equals(searchUtil.recifyQualifyText)
                    ||listResult.get(i).getSTATUS().equals(searchUtil.recifyQualify)){
                listDatas_undecied.add(listResult.get(i));
            }
        }
    }




    private void initContent() {
        if (task!=null){
            //时间
            textView_time.setText(task.getCHECKDATE());
            //增加下划线
//            textView_time.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            Log.e(TAG,"接收数据显示："+task.getCHECKDATE());
            Log.e(TAG,"接收数据显示："+task.toString());

            //设备种类
            //已经在前面把设备的编号转换成文字了
            textView_deviceType.setText(searchUtil.getMapdevclass().get(task.getDEVCLASS()));
//            textView_deviceType.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            //合格情况
            textView_qualify.setText(searchUtil.getHelpMapForResult().get(task.getRESULT()));
//            textView_qualify.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            //是否已检查
//            textView_checked.setText(mResultBean.getIschecked());
//            textView_checked.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            //检察人员
            textView_user.setText(LoginActivity.user.getUSERNAME());
//            textView_user.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            //地址
            textView_address.setText(task.getPLACE());
//            textView_address.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            textView_title.setText(task.getDEVID()+"检查情况");
        }

    }

    private void refresh(List<DetectionResult> listRes) {
        listViewAdapter1 = new ListViewAdapter1(getApplicationContext(),listRes,searchUtil.choose[posFlag]);
        listView.setAdapter(listViewAdapter1);
    }

    //对ListViewAdapter1进行适配
    private void initListViewAdapter1() {
        promptDialog.showLoading("加载数据中...");
        Log.e(TAG, "getTaskList: promptDialog");
        listViewAdapter1 = new ListViewAdapter1(getApplicationContext(),listResult,searchUtil.choose[posFlag]);
        listView.setAdapter(listViewAdapter1);
        promptDialog.dismiss();

    }


    private void initView() {
        textView_title = findViewById(R.id.searched_result_title);
        textView_time = findViewById(R.id.searched_result_time);
        textView_deviceType = findViewById(R.id.searched_result_deviceType);
        textView_qualify = findViewById(R.id.searched_result_qualify);
//        textView_checked = view.findViewById(R.id.searched_result_checked);
        textView_user = findViewById(R.id.searched_result_user);
        textView_address = findViewById(R.id.searched_result_address);

        listView = findViewById(R.id.show_searched_result_listview);

        dropDownMenu = findViewById(R.id.dropdownmenu);

        button_back = findViewById(R.id.btn_searchResult_back);
        button_back.setOnClickListener(this);

        sp_fliter = findViewById(R.id.show_searched_result_spinner);
        sp_fliter.setItems(searchUtil.getChooseList());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_searchResult_back:
                setResult(RESULT_OK);
                this.finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        promptDialog.dismissImmediately();
    }

    //Menu的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                setResult(RESULT_OK);
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
