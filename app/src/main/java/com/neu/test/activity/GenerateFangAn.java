package com.neu.test.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.neu.test.R;
import com.neu.test.entity.CheckLists;
import com.neu.test.entity.DetailTask;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListCheckListCallBack;
import com.neu.test.net.callback.ListIDetailTaskCallBack;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.tree.TreeNode;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.SearchUtil;
import com.neu.test.util.SidebarUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

public class GenerateFangAn extends AppCompatActivity implements View.OnClickListener {

  private static final String TAG = "GenerateFangAn";
  private static final int JUMP_TREELIST = 12;

  private EditText generate_edittext_fanganname;
  private EditText generate_edittext_fangan;
  private MaterialSpinner generate_spinner_devtype;
  private TextView generate_textview_companyname;
  private TextView generate_textview_person;

  private Button generate_button_sure;
  private Button generate_button_addCheckItem;

  private SearchUtil searchUtil;
  private Toolbar generate_toolbar;//顶部导航
  private TextView generate_toolbar_textview;//顶部导航
  private String task_devclass = "10000";//设备种类

  private List<CheckLists> checkLists;

  private PromptDialog promptDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_generate_fang_an);

    searchUtil = new SearchUtil();
    promptDialog = new PromptDialog(this);

    initView();
    initToolbar();

    generate_spinner_devtype.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
      @Override
      public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
        Log.e(TAG," 测试materialSpinner_devType："+searchUtil.classofdevForDraft[position]+" position:"+position+"  text:"+generate_spinner_devtype.getText());
        task_devclass = searchUtil.classofdevForDraft[position];
      }
    });

  }

  private void initToolbar() {
    generate_toolbar = findViewById(R.id.generate_toolbar);
    generate_toolbar.setTitle(" ");
    generate_toolbar.setTitleTextColor(Color.WHITE);

    generate_toolbar_textview = findViewById(R.id.generate_toolbar_textview);
    generate_toolbar_textview.setText("制定方案");

    setSupportActionBar(generate_toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示箭头
  }

  private void initView() {
    generate_edittext_fanganname = findViewById(R.id.generate_edittext_fanganname);
    generate_edittext_fangan = findViewById(R.id.generate_edittext_fangan);
    generate_spinner_devtype = findViewById(R.id.generate_spinner_devtype);
    generate_textview_companyname = findViewById(R.id.generate_textview_companyname);
    generate_textview_person = findViewById(R.id.generate_textview_person);
    generate_button_sure = findViewById(R.id.generate_button_sure);
    generate_button_addCheckItem = findViewById(R.id.generate_button_addCheckItem);
    generate_button_sure.setOnClickListener(this);
    generate_button_addCheckItem.setOnClickListener(this);

    List<String> list_devtype = new ArrayList<>();
    list_devtype = Arrays.asList(searchUtil.deviceTypeForDraft);
    generate_spinner_devtype.setItems(list_devtype);
    generate_textview_companyname.setText(LoginActivity.user.getUSEUNITNAME());
    generate_textview_person.setText(LoginActivity.user.getUSERNAME());
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.generate_button_sure:
        promptDialog.showLoading("方案生成中 ...");
        draftTask();
        break;
      case R.id.generate_button_addCheckItem:
        promptDialog.showLoading("检查项获取中 ...");
        jumpToGetItems();
        break;

    }
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


  //提交任务
  public void draftTask(){
    submitData();
  }
  public void jumpToGetItems(){
    Date date = new Date();
    SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String time = sd2.format(date);
    getDetctionData(time);
  }




  private void getDetctionData(String time) {
    Map<String, Object> map = new HashMap<>();
    map.put("DEVCLASS",searchUtil.getTypeToDevclass(generate_spinner_devtype.getText().toString()));
    map.put("UNIT",LoginActivity.user.getUSEUNITNAME());

    String url = BaseUrl.BaseUrl+"getCheckList";
    OkHttp okHttp=new OkHttp();
    okHttp.postBypostString(url, new Gson().toJson(map), new ListCheckListCallBack() {
      @Override
      public void onError(Call call, Exception e, int i) {
        promptDialog.dismissImmediately();
        Log.e("tttttttttttt",e.getMessage());
        Log.e("tttttttttttt",e.toString());
        Toasty.warning(GenerateFangAn.this,"客官，网络不给力!", Toast.LENGTH_LONG,true).show();
      }

      @Override
      public void onResponse(Result<List<CheckLists>> response, int id) {
        if(response.getMessage().equals("操作成功")) {
          List<CheckLists> items = response.getContent();
          List<CheckLists> cc = new ArrayList<>();

          if(items.size() >0) {
            List<CheckLists> listsList = creatCheckList(items,time);
            cc.addAll(listsList);
            Intent intent = new Intent(GenerateFangAn.this,TreeListActivity2.class);
            intent.putExtra("CheckLists", (Serializable) cc);
            startActivityForResult(intent,JUMP_TREELIST);
            promptDialog.dismissImmediately();
          } else{
            promptDialog.dismissImmediately();
            Toasty.error(GenerateFangAn.this,"暂无可选项").show();
          }
        }else{
          promptDialog.dismissImmediately();
          Toasty.error(GenerateFangAn.this,"获取检查项失败，请稍后重试").show();
        }
      }
    });
  }


  public List<CheckLists> creatCheckList(List<CheckLists> items,String time){
    //父节点

    CheckLists checkLists1 = new CheckLists(0, TreeNode.ROOT_PARENT_ID,true);
    checkLists1.setMONITORCONTENT("通用");
    CheckLists checkLists2 = new CheckLists(1, TreeNode.ROOT_PARENT_ID,true);
    checkLists2.setMONITORCONTENT(generate_spinner_devtype.getText().toString());
    //子节点
    List<CheckLists> checkLists = new ArrayList<>();
    for (int position=0;position<items.size();position++){
      CheckLists cl = items.get(position);
      cl.setId(position+2);
      cl.setGroup(false);
      cl.setCRATETIME(time);
      cl.setCRATETPERSON(LoginActivity.user.getUSERNAME());
      cl.setSHENGXIAOTIME(time);
      cl.setUNITNAME(LoginActivity.user.getUSEUNITNAME());
      cl.setSTARANDMIAOSHU(generate_edittext_fangan.getText().toString().replace(" ",""));
      cl.setSTARANDNAME(generate_edittext_fanganname.getText().toString().replace(" ",""));
      if(cl.getDEVCLASS().equals("10000")){//通用
        if (!checkLists.contains(checkLists1)){
          checkLists.add(checkLists1);
        }
        cl.setParentId(0);
        checkLists.add(cl);
      }else{
        if (!checkLists.contains(checkLists2)){
          checkLists.add(checkLists2);
        }
        cl.setParentId(1);
        checkLists.add(cl);
      }

    }
    return  checkLists;
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode == RESULT_OK){
      checkLists = (List<CheckLists>) data.getSerializableExtra("CList");
    }
  }




  private void submitData() {

    Map<String, Object> map = new HashMap<>();
    map.put("CHECKLIST",checkLists);
    String url = BaseUrl.BaseUrl+"makeFangAn";
    OkHttp okHttp=new OkHttp();
    okHttp.postBypostString(url, new Gson().toJson(map), new ListTaskCallBack() {
      @Override
      public void onError(Call call, Exception e, int i) {
        promptDialog.dismissImmediately();
        Toasty.warning(GenerateFangAn.this,"客官，网络不给力!", Toast.LENGTH_LONG,true).show();
      }

      @Override
      public void onResponse(Result<List<Task>> response, int id) {

        if(response.getMessage().equals("操作成功")){
          Toasty.success(GenerateFangAn.this,"成功").show();
          promptDialog = null;
          finish();
        }else{
          promptDialog = null;
          Toasty.error(GenerateFangAn.this,"操作失败，请稍后重试").show();
        }

      }
    });
  }

}
