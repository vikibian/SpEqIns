package com.neu.test.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.neu.test.R;
import com.neu.test.activity.DetctionActivity;
import com.neu.test.activity.LoginActivity;
import com.neu.test.activity.ReDetectionActivity;
import com.neu.test.activity.RectifyListActivity;
import com.neu.test.activity.RectifyResultActivity;
import com.neu.test.entity.DetailTask;
import com.neu.test.entity.DetectionItem;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.layout.BottomBarLayout;
import com.neu.test.layout.CircleRelativeLayout;
import com.neu.test.layout.MyTextView;
import com.neu.test.layout.SimpleToolbar;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListDetectionResultCallBack;
import com.neu.test.net.callback.ListIDetailTaskCallBack;
import com.neu.test.net.callback.ListItemsCallBack;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.SearchUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CheckFragment extends Fragment {

  private static String TAG = "CheckFragment";
  private int CHECKFRAGMENT =123;
  private int rectifylist =124;

  public ListView lv_check;
  private LinearLayout check_noitem;
  //    private Map<String,String> map_devclass;
  public CheckAdapter checkAdapter;
  List<Task> tasks = new ArrayList<Task>();
  BottomBarLayout mBottomBarLayout;
  private String taskType;

  private MaterialSpinner sp_devid;
  private MaterialSpinner sp_devclass;

  private List<String> devclassList;
  private List<String> devidList;

  public List<DetectionItem> testItem;

  private List<Task> devclassTasks;
  private SearchUtil searchUtil = new SearchUtil();

  private SharedPreferences sharedPreferences;
  private String data;//保存 缓存的字段名

  PromptDialog promptDialog;

  public CheckFragment(){

  }

  public CheckFragment(List<Task> tasks, BottomBarLayout bottomBarLayout){
    this.tasks = tasks;
    mBottomBarLayout = bottomBarLayout;
  }

  /**
   * @date : 2020/2/22
   * @time : 10:05
   * @author : viki
   * @description : 传入任务类型的参数
   */

  public CheckFragment(List<Task> tasks, BottomBarLayout bottomBarLayout, String s){
    this.tasks = tasks;
    mBottomBarLayout = bottomBarLayout;
    taskType = s;
  }


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_check, null);
    AppCompatActivity activity = (AppCompatActivity) getActivity();
    SimpleToolbar simple_toolbar = activity.findViewById(R.id.simple_toolbar);
    simple_toolbar.setVisibility(View.VISIBLE);
    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    lv_check = view.findViewById(R.id.lv_check);
//        sp_devid = view.findViewById(R.id.sp_devid);
    sp_devclass = view.findViewById(R.id.sp_devclass);
    check_noitem = view.findViewById(R.id.check_noitem);

    promptDialog = new PromptDialog(getActivity());

    devclassList = new ArrayList<>();
    devidList = new ArrayList<>();
//        map_devclass = new HashMap<>();
    devclassTasks = new ArrayList<>();
    //准备BaseAdapter
    checkAdapter = new CheckAdapter();
    devclassTasks.addAll(tasks);
    devclassList.add("全部");
    devidList.add("设备类别");
//        map_devclass.put("3000","电梯");
//        map_devclass.put("8000","压力管道");
    //设置Adapter显示列表
    lv_check.setAdapter(checkAdapter);

    if(devclassTasks.size() == 0){
      check_noitem.setVisibility(View.VISIBLE);
      lv_check.setVisibility(View.GONE);
    }else{
      check_noitem.setVisibility(View.GONE);
      lv_check.setVisibility(View.VISIBLE);
    }

    for(int i=0;i<tasks.size();i++){
      if(!(devclassList.contains(searchUtil.getMapdevclass().get(tasks.get(i).getDEVCLASS())))){
        devclassList.add(searchUtil.getMapdevclass().get(tasks.get(i).getDEVCLASS()));
      }
    }

    sp_devclass.setItems(devclassList);
//        sp_devid.setItems(devidList);


    sp_devclass.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
      @Override
      public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

        if(item.equals("全部")){
          devclassTasks.clear();
          devclassTasks.addAll(tasks);
        }else{
          devclassTasks.clear();
          for(int i=0;i<tasks.size();i++){
            if(tasks.get(i).getDEVCLASS().equals(getKeyByValue(searchUtil.getMapdevclass(),item.toString()))){
              devclassTasks.add(tasks.get(i));
            }
          }
        }
        if(devclassTasks.size() == 0){
          check_noitem.setVisibility(View.VISIBLE);
          lv_check.setVisibility(View.GONE);
        }else{
          check_noitem.setVisibility(View.GONE);
          lv_check.setVisibility(View.VISIBLE);
          lv_check.setAdapter(checkAdapter);
        }
      }
    });


    lv_check.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        promptDialog.showLoading("任务获取中 ... ");
        Task task = tasks.get(position);
        String s = task.getUSEUNITNAME();
        String DEVCLASS = task.getDEVCLASS();

        data = task.getTASKID()+task.getDEVID()+task.getTASKTYPE()+task.getLOGINNAME()+task.getRUNWATERNUM();
        sharedPreferences = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
        if(task.getRESULT().equals("2")&&!taskType.equals("整改")){
          String detectionResultList =  sharedPreferences.getString("detectionResultList",null);
          if(detectionResultList == null){
            getSaveData(task,position);
          }else{
            goNextActivity(detectionResultList,position,task);
          }
        }
        else if(taskType.equals("整改")){
            if(task.getRESULT().equals("2")){
              getSaveReData(task,position);
            }
            else{
              getReDetctionData(task,position);
            }
          Log.e(TAG," 整改："+"getReDetctionData");
        }else{
          String detectionResultList =  sharedPreferences.getString("detectionResultList",null);
          if(detectionResultList == null){
            String listDetailTask = sharedPreferences.getString("listDetailTask",null);
            if(listDetailTask == null){
              getDetctionData(DEVCLASS,s,task,position);
            }else{
              List<DetailTask> list = getList(new ArrayList<DetailTask>(),listDetailTask);
              List<DetectionResult> detectionResults = creatDetectionResultList(list,task);
              editorSharedPreferences(detectionResults,"detectionResultList");
              goReDetectionActivity(detectionResults,position,task);
            }
          }else{
            goNextActivity(detectionResultList,position,task);
          }
        }

      }
    });


    return view;
  }

//    private void judgeAdapterAndSet(){
//        if (taskType.equals(searchUtil.recify)){
//            lv_check.setAdapter(checkAdapterForRecify);
//        }else {
//            lv_check.setAdapter(checkAdapter);
//        }
//    }


  public void goNextActivity(String detectionResultList,int position,Task task){
    Gson gson = new Gson();
    Type type = new TypeToken<List<DetectionResult>>(){}.getType();
    List<DetectionResult> list = gson.fromJson(detectionResultList,type);
    goReDetectionActivity(list,position,task);
  }

  public void goReDetectionActivity(List<DetectionResult> list ,int position,Task task){
    Intent intent = new Intent(getActivity(), ReDetectionActivity.class);
    intent.putExtra("items", (Serializable) list);
    intent.putExtra("position",position);
    intent.putExtra("task", task);
    startActivityForResult(intent,CHECKFRAGMENT);
    promptDialog.dismiss();
  }

  public void goRectifyListActivity(List<DetectionResult> list ,int position,Task task){
    Intent intent = new Intent(getActivity(), RectifyListActivity.class);
    intent.putExtra("items", (Serializable) list);
    intent.putExtra("position",position);
    intent.putExtra("task", task);
//        startActivity(intent);
    startActivityForResult(intent,rectifylist);
    promptDialog.dismiss();
  }

  public List<DetectionResult> creatDetectionResultList(List<DetailTask> items,Task task){
    List<DetectionResult> detectionResults = new ArrayList<>();
    for (int position=0;position<items.size();position++){
      DetectionResult detectionResult = new DetectionResult();
      detectionResult.setCHECKCONTENT(items.get(position).getJIANCHAXIANGCONTENT());//检查内容
      detectionResult.setJIANCHAXIANGBIANHAO(items.get(position).getJIANCHAXIANGID());//检查编号
      Log.e("编号",items.get(position).getJIANCHAXIANGID());
      detectionResult.setLOGINNAME(items.get(position).getLOGINNAME());//检查人员
      detectionResult.setTASKID(task.getTASKID());
      detectionResult.setDEVID(task.getDEVID());
      detectionResult.setDEVCLASS(task.getDEVCLASS());
      detectionResult.setJIANCHAXIANGTITLE(items.get(position).getJIANCHAXIANGTITLE());
      detectionResult.setRUNWATERNUM(task.getRUNWATERNUM());
      detectionResult.setLAW(items.get(position).getLAW());
      detectionResult.setQIYEMINGCHENG(task.getUSEUNITNAME());
      detectionResults.add(detectionResult);
      if(!items.get(position).getSHIFOUHEGEQUZHENG().equals("1")){
        detectionResult.setSTATUS("0");
      }
    }
    return  detectionResults;
  }

  public <T> void editorSharedPreferences(T value,String key){
    String json2 = new Gson().toJson(value);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(key,json2);
    editor.commit();
  }

  public <T> T getList(T content,String key){

    Gson gson = new Gson();
    Type type = content.getClass();
    T list = gson.fromJson(key,type);
    return list;
  }

  private String getKeyByValue(Map map,String value){
    Set set = map.entrySet(); //通过entrySet()方法把map中的每个键值对变成对应成Set集合中的一个对象
    Iterator<Map.Entry<Object, Object>> iterator = set.iterator();
    while(iterator.hasNext()){
      //Map.Entry是一种类型，指向map中的一个键值对组成的对象
      Map.Entry<Object, Object> entry = iterator.next();
      if(entry.getValue().equals(value)){
        return entry.getKey().toString();
      }
    }
    return "";
  }

  private void getDetctionData(String devclass, final String title, final Task task, final int position) {
    Map<String, Object> map = new HashMap<>();
    map.put("username", LoginActivity.user.getUSERNAME());
    map.put("unitname", LoginActivity.user.getUSEUNITNAME());
    map.put("TASKID",task.getTASKID());
    map.put("DEVID",task.getDEVID());
    map.put("DEVCLASS",task.getDEVCLASS());
    map.put("RUNWATERNUM",task.getRUNWATERNUM());
    String url = BaseUrl.BaseUrl+"getFullCheckItems";
    Log.e(TAG,"url: "+url);
    OkHttp okHttp=new OkHttp();
    okHttp.postBypostString(url, new Gson().toJson(map), new ListIDetailTaskCallBack() {
      @Override
      public void onError(Call call, Exception e, int i) {
        promptDialog.dismiss();
        Toasty.warning(getActivity(),"客官，网络不给力!",Toast.LENGTH_LONG,true).show();
      }

      @Override
      public void onResponse(Result<List<DetailTask>> response, int id) {
        if(response.getMessage().equals("获取成功")) {
          List<DetailTask> items = response.getContent();
          List<DetectionResult> detectionResults = creatDetectionResultList(items,task);
          editorSharedPreferences(items,"listDetailTask");
          editorSharedPreferences(detectionResults,"detectionResultList");
          goReDetectionActivity(detectionResults,position,task);
        }else {
          promptDialog.dismiss();
          Toasty.error(getContext(),"获取失败！").show();
        }
      }
    });
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == CHECKFRAGMENT) {
        int position = data.getIntExtra("position", 0);
        boolean isDoing = data.getBooleanExtra("isDoing",false);
        String where = data.getStringExtra("where");
        if(where.equals("back")){
          if(isDoing){
          }
        }else{
          tasks.get(position).setRESULT("2");
        }
        lv_check.setAdapter(checkAdapter);
      }

      if (requestCode == rectifylist){
        int pos = data.getIntExtra("position",-1);

        tasks.remove(pos);
        devclassTasks.remove(pos);
        BottomBarLayout bottomBarLayout = getActivity().findViewById(R.id.bbl);
        bottomBarLayout.setUnread(1,tasks.size());
        checkAdapter.notifyDataSetChanged();
        lv_check.setAdapter(checkAdapter);
        if(devclassTasks.size() == 0){
          check_noitem.setVisibility(View.VISIBLE);
          lv_check.setVisibility(View.GONE);
        }else{
          check_noitem.setVisibility(View.GONE);
          lv_check.setVisibility(View.VISIBLE);
        }
      }
    }



  }

  private void getReDetctionData(final Task task, final int position) {
    Map<String, Object> map = new HashMap<>();
    map.put("taskID",task.getTASKID());
    map.put("DEVID",task.getDEVID());
    map.put("RUNWATERNUMBER",task.getRUNWATERNUM());
    map.put("USERNAME",LoginActivity.user.getUSERNAME());
    map.put("unitname",LoginActivity.user.getUSEUNITNAME());
    String url = BaseUrl.BaseUrl+"selectReItemResult";
    Log.e(TAG,"url: "+url);
    OkHttp okHttp=new OkHttp();
    okHttp.postBypostString(url, new Gson().toJson(map), new ListDetectionResultCallBack() {
      @Override
      public void onError(Call call, Exception e, int i) {
        promptDialog.dismiss();
        Toasty.warning(getActivity(),"客官，网络不给力!",Toast.LENGTH_LONG,true).show();
      }

      @Override
      public void onResponse(Result<List<DetectionResult>> listResult, int i) {
        if (listResult.getMessage().equals("获取成功")) {
          List<DetectionResult> list = listResult.getContent();
          Log.e("size",list.size()+"");
//                    Intent intent = new Intent(getActivity(), ReDetectionActivity.class);
          for (DetectionResult d:list) {
            d.setRUNWATERNUM(task.getRUNWATERNUM());
          }
          if(list.get(0).getRUNWATERNUM().equals(task.getRUNWATERNUM())){
            editorSharedPreferences(list,"detectionResultList");
            goRectifyListActivity(list,position,task);
          }
        }else {
          promptDialog.dismiss();
          Toasty.error(getContext(),"获取失败！").show();
        }
      }
    });
  }


  private void getSaveData(final Task task, final int position) {
    Map<String, Object> map = new HashMap<>();
    map.put("taskID",task.getTASKID());
    map.put("DEVID",task.getDEVID());
    map.put("USERNAME",LoginActivity.user.getUSERNAME());
    map.put("unitname",LoginActivity.user.getUSEUNITNAME());
    map.put("RUNWATERNUM",task.getRUNWATERNUM());
    String url = BaseUrl.BaseUrl+"getSaveResult";
    Log.e(TAG,"url: "+url);
    OkHttp okHttp=new OkHttp();
    okHttp.postBypostString(url, new Gson().toJson(map), new ListDetectionResultCallBack() {
      @Override
      public void onError(Call call, Exception e, int i) {
        Log.e(TAG,"onError: "+e.toString());
        promptDialog.dismiss();
        Toasty.warning(getActivity(),"客官，网络不给力!",Toast.LENGTH_LONG,true).show();
      }

      @Override
      public void onResponse(Result<List<DetectionResult>> listResult, int i) {
        if (listResult.getMessage().equals("获取成功")) {
          List<DetectionResult> list = listResult.getContent();
          Log.e("size",list.size()+"");
          Intent intent = new Intent(getActivity(), ReDetectionActivity.class);
          intent.putExtra("items", (Serializable) list);
          intent.putExtra("position",position);
          intent.putExtra("task", task);
          promptDialog.dismissImmediately();
          startActivityForResult(intent,CHECKFRAGMENT);
        }else {
          promptDialog.dismiss();
        }
      }
    });
  }


  private void getSaveReData(final Task task, final int position) {
    Map<String, Object> map = new HashMap<>();
    map.put("taskID",task.getTASKID());
    map.put("DEVID",task.getDEVID());
    map.put("USERNAME",LoginActivity.user.getUSERNAME());
    map.put("unitname",LoginActivity.user.getUSEUNITNAME());
    map.put("RUNWATERNUM",task.getRUNWATERNUM());
    String url = BaseUrl.BaseUrl+"getSaveResult";
    Log.e(TAG,"url: "+url);
    OkHttp okHttp=new OkHttp();
    okHttp.postBypostString(url, new Gson().toJson(map), new ListDetectionResultCallBack() {
      @Override
      public void onError(Call call, Exception e, int i) {
        Log.e(TAG,"onError: "+e.toString());
        promptDialog.dismiss();
        Toasty.warning(getActivity(),"客官，网络不给力!",Toast.LENGTH_LONG,true).show();
      }

      @Override
      public void onResponse(Result<List<DetectionResult>> listResult, int i) {
        if (listResult.getMessage().equals("获取成功")) {
          List<DetectionResult> list = listResult.getContent();
          Log.e("size",list.size()+"");
          Intent intent = new Intent(getActivity(), RectifyListActivity.class);
          intent.putExtra("items", (Serializable) list);
          intent.putExtra("position",position);
          intent.putExtra("task", task);
          promptDialog.dismissImmediately();
          startActivityForResult(intent,CHECKFRAGMENT);
        }else {
          promptDialog.dismiss();
        }
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    promptDialog.dismissImmediately();
  }

  class CheckAdapter extends BaseAdapter{


    //返回集合数据数量
    @Override
    public int getCount() {
      return devclassTasks.size();
    }

    //返回指定下标的数据对象
    @Override
    public Object getItem(int position) {
      return devclassTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    /**
     * 返回指定下表对应的item的View对象
     * @param position 下标
     * @param convertView 可复用的缓存Item试图对象，前n+1个为null
     * @param parent ListView对象
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


      //加载item的布局
      convertView = View.inflate(getActivity(), R.layout.fragment_listview_check_new, null);



      //根据position设置对应数据
      //获得当前数据对象
      Task task = devclassTasks.get(position);
      MyTextView tv_check_device = convertView.findViewById(R.id.tv_check_device);
      MyTextView tv_check_address = convertView.findViewById(R.id.tv_check_address);
      MyTextView tv_check_endtime = convertView.findViewById(R.id.tv_check_endtime);
      TextView tv_issave_device = convertView.findViewById(R.id.tv_issave_device);
      MyTextView tv_check_taskname = convertView.findViewById(R.id.tv_check_taskname);
//            CircleRelativeLayout circleRelativeLayout = convertView.findViewById(R.id.check_list_imagebutton);
//            LinearLayout ll_check_bg = convertView.findViewById(R.id.ll_check_bg);

      if(task.getRESULT().equals("2")){
        tv_issave_device.setVisibility(View.VISIBLE);
      }
      else{
        tv_issave_device.setVisibility(View.INVISIBLE);
      }

      int color = 0xFFFFFF00;
      if(position>15){
        color = 0xC606F600;
      }
      else{
        color = 0xC6F00000-(0x000F0000-0x00000F00)*position;

      }
      tv_check_taskname.setTextColor(color);
      tv_check_taskname.setText(task.getTASKNAME());
      tv_check_device.setText(String.valueOf(task.getDEVID()));
      tv_check_address.setText(task.getPLACE());
      tv_check_endtime.setText(task.getDEADLINE());
      return convertView;
    }
  }

}
