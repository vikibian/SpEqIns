package com.neu.test.fragment;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.neu.test.R;
import com.neu.test.activity.LoginActivity;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.layout.SimpleToolbar;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.SearchUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

public class YinhuanFragment extends Fragment {

  private MaterialSpinner draft_yinhuan_spinner_devtype;
  private LinearLayout ll_zhili;
  private LinearLayout ll_devid_yinhuan;
  private EditText draft_yinhuan_edittext_devID;
  private EditText draft_yinhuan_edittext_content;
  private EditText draft_yinhuan_edittext_fenxi;
  private EditText draft_yinhuan_edittext_zhilicuoshi;
  private Button draft_yinhuan_button_zhili;
  private Button draft_yinhuan_button_upload;
  private Button draft_yinhuan_button_shangbao;
  private DetectionResult detectionResult;
  private Task task;
  private String faxianDate;
  private SimpleDateFormat sm;
  private PromptDialog promptDialog;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_yinhuanshangbao, null);
    AppCompatActivity activity = (AppCompatActivity) getActivity();
    SimpleToolbar simple_toolbar = activity.findViewById(R.id.simple_toolbar);
    simple_toolbar.setVisibility(View.VISIBLE);
    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    promptDialog = new PromptDialog(getActivity());
    initView(view);
    initListener();
    return view;
  }
  public void initView(View view){
    draft_yinhuan_spinner_devtype = view.findViewById(R.id.draft_yinhuan_spinner_devtype);
    draft_yinhuan_edittext_devID = view.findViewById(R.id.draft_yinhuan_edittext_devID);
    ll_zhili = view.findViewById(R.id.ll_zhili);
    ll_devid_yinhuan = view.findViewById(R.id.ll_devid_yinhuan);
    draft_yinhuan_edittext_content = view.findViewById(R.id.draft_yinhuan_edittext_content);
    draft_yinhuan_edittext_fenxi = view.findViewById(R.id.draft_yinhuan_edittext_fenxi);
    draft_yinhuan_edittext_zhilicuoshi = view.findViewById(R.id.draft_yinhuan_edittext_zhilicuoshi);
    draft_yinhuan_button_zhili = view.findViewById(R.id.draft_yinhuan_button_zhili);
    draft_yinhuan_button_upload = view.findViewById(R.id.draft_yinhuan_button_upload);
    draft_yinhuan_button_shangbao = view.findViewById(R.id.draft_yinhuan_button_shangbao);
    detectionResult = new DetectionResult();
    task = new Task();
    draft_yinhuan_spinner_devtype.setItems(new SearchUtil().deviceTypeForDraft);

  }


  public void initListener(){

    draft_yinhuan_spinner_devtype.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {//选择设备类型
      @Override
      public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
          if(item.equals("通用")){
            ll_devid_yinhuan.setVisibility(View.GONE);
          }else{
            draft_yinhuan_edittext_devID.setText("");
            ll_devid_yinhuan.setVisibility(View.VISIBLE);
          }
      }
    });



    View.OnClickListener onClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(draft_yinhuan_spinner_devtype.getText().toString().equals("通用")){
            draft_yinhuan_edittext_devID.setText("1000010000");
        }
        if(draft_yinhuan_edittext_devID.getText().toString().equals("")||(!isNumeric(draft_yinhuan_edittext_devID.getText().toString()))){
            Toasty.error(getContext(),"请输入有效的设备号").show();
            return;
        }
        if(draft_yinhuan_edittext_content.getText().toString().replace(" ","").equals("")){
          Toasty.error(getContext(),"请输入有效的隐患内容").show();
          return;
        }
        if(draft_yinhuan_edittext_fenxi.getText().toString().replace(" ","").equals("")){
          Toasty.error(getContext(),"请输入有效的隐患原因分析").show();
          return;
        }
        Date date = new Date();
        SimpleDateFormat sDF = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String taskid = sDF.format(date);
        String run = taskid;
        task.setTASKNAME(faxianDate+"隐患上报");
        task.setRUNWATERNUM(run);
        task.setLOGINNAME(LoginActivity.user.getUSERNAME());
        task.setTASKTYPE("隐患上报");
        task.setDEVID(Long.parseLong(draft_yinhuan_edittext_devID.getText().toString()));
        task.setTASKID(Long.parseLong(taskid));
        task.setRESULT("1");
        task.setUSEUNITNAME(LoginActivity.user.getUSEUNITNAME());
        //task.setDEVCLASS("");

        detectionResult.setDEVID(Long.parseLong(draft_yinhuan_edittext_devID.getText().toString()));
        detectionResult.setTASKID(Long.parseLong(taskid));
        detectionResult.setQIYEMINGCHENG(LoginActivity.user.getUSEUNITNAME());
        detectionResult.setLOGINNAME(LoginActivity.user.getUSERNAME());
        detectionResult.setRUNWATERNUM(run);
        detectionResult.setDEVCLASS(new SearchUtil().getTypeToDevclass(draft_yinhuan_spinner_devtype.getText().toString()));
        detectionResult.setJIANCHAXIANGTITLE(draft_yinhuan_edittext_fenxi.getText().toString());
        detectionResult.setCHECKCONTENT(draft_yinhuan_edittext_content.getText().toString());
        detectionResult.setJIANCHAXIANGBIANHAO("add");
        detectionResult.setQUZHENG("一律取证");
        detectionResult.setSUGGESTION("");
        switch (v.getId()){
          case R.id.draft_yinhuan_button_zhili://隐患治理
            draft_yinhuan_button_upload.setVisibility(View.VISIBLE);
            draft_yinhuan_button_zhili.setVisibility(View.GONE);
            ll_zhili.setVisibility(View.VISIBLE);
            break;
          case R.id.draft_yinhuan_button_upload://隐患结果上报
            if(draft_yinhuan_edittext_zhilicuoshi.getText().toString().replace(" ","").equals("")){
              Toasty.error(getContext(),"请输入有效的隐患治理措施").show();
              return;
            }
            promptDialog.showLoading("隐患治理结果上报中");
            if(!draft_yinhuan_edittext_zhilicuoshi.getText().toString().contains("不")&&!draft_yinhuan_edittext_zhilicuoshi.getText().toString().contains("未")
              &&!draft_yinhuan_edittext_zhilicuoshi.getText().toString().contains("没")){
              detectionResult.setSTATUS("0");
            }else{
              detectionResult.setSTATUS("1");
            }
            detectionResult.setSUGGESTION(draft_yinhuan_edittext_zhilicuoshi.getText().toString());
            String finishTime = simpleDateFormat.format(date);
            detectionResult.setCHECKDATE(finishTime);
            submitData(task,detectionResult);
            break;
          case R.id.draft_yinhuan_button_shangbao://隐患上报
            promptDialog.showLoading("隐患上报中");
            detectionResult.setSTATUS("1");
            detectionResult.setCHECKDATE(faxianDate);
            submitData(task,detectionResult);
            break;

        }
      }
    };

    draft_yinhuan_button_zhili.setOnClickListener(onClickListener);
    draft_yinhuan_button_upload.setOnClickListener(onClickListener);
    draft_yinhuan_button_shangbao.setOnClickListener(onClickListener);


  }


  private void submitData(Task task, DetectionResult detectionResult) {
    List<DetectionResult> list = new ArrayList<>();
    list.clear();
    list.add(detectionResult);
    Map<String, Object> map = new HashMap<>();
    map.put("task",task);
    map.put("DetectionResults",list);
    String url = BaseUrl.BaseUrl+"customerDraftYinhuan";
    OkHttp okHttp=new OkHttp();
    okHttp.postBypostString(url, new Gson().toJson(map), new ListTaskCallBack() {
      @Override
      public void onError(Call call, Exception e, int i) {
        promptDialog.dismissImmediately();
        Toasty.warning(getActivity(),"客官，网络不给力!", Toast.LENGTH_LONG,true).show();
      }

      @Override
      public void onResponse(Result<List<Task>> response, int id) {
        if(response.getMessage().equals("任务提交成功")){
          promptDialog.dismissImmediately();
          clearAll();
          Toasty.success(getActivity(),"上报成功").show();
        }else{
          promptDialog.dismissImmediately();
          Toasty.error(getActivity(),"操作失败，请稍后重试").show();
        }

      }
    });
  }

  public void clearAll(){
    draft_yinhuan_edittext_devID.setText("");
    draft_yinhuan_edittext_content.setText("");
    draft_yinhuan_edittext_fenxi.setText("");
    draft_yinhuan_edittext_zhilicuoshi.setText("");
    draft_yinhuan_button_upload.setVisibility(View.GONE);
    ll_zhili.setVisibility(View.GONE);
    draft_yinhuan_button_zhili.setVisibility(View.VISIBLE);
  }

  @Override
  public void onResume() {
    super.onResume();
    Date date1 = new Date();
    sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    faxianDate = sm.format(date1);
  }



  /**
   * 利用正则表达式判断字符串是否是数字
   * @param str
   * @return
   */
  public boolean isNumeric(String str){
    Pattern pattern = Pattern.compile("^[0-9]{1,18}");
    Matcher isNum = pattern.matcher(str);
    if( !isNum.matches() ){
      return false;
    }
    return true;
  }



}
