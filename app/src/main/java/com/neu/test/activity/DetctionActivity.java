package com.neu.test.activity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.DetectionItem;
import com.neu.test.layout.SlideLayout;
import com.neu.test.net.OkHttp;
import com.neu.test.net.netBeans.AddTaskBean;
import com.neu.test.net.netBeans.LoginBean;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;


public class DetctionActivity extends AppCompatActivity {
    private static String TAG = "DetctionActivity";

    private ListView lv_detection;
    private Button btn_add_detection;
    private Button btn_sure_detection;
    DetectionAdapter detectionAdapter;
    public List<DetectionItem> listData;//检测项数据
    int detctionPosition;
    int taskType ;
    int hegeFlag;

    private String[] taskTypes = {"自查","复查","上级","随机"};
    LayoutInflater layoutInflater ;
    View textEntryView ;


    //    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int selectedPosition = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
//        switch(item.getItemId()){
//            case 0:
//                listData.remove(selectedPosition);//选择行的位置
//                detectionAdapter.notifyDataSetChanged();
//                lv_detection.invalidate();
//
//                break;
//            case 1:
//                break;
//
//
//        }
//        return super.onContextItemSelected(item);
//    }
//



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detction);

        layoutInflater = LayoutInflater.from(DetctionActivity.this);
        textEntryView = layoutInflater.inflate(R.layout.detection_dialog, null);


        lv_detection = findViewById(R.id.lv_detection);
        btn_add_detection = findViewById(R.id.btn_add_detection);
        btn_sure_detection = findViewById(R.id.btn_sure_detection);
        //获取数据
        getData();
        String title = getIntent().getStringExtra("TITLE");
        setTitle(title);
        //String position = "position";
        detctionPosition = getIntent().getIntExtra("position",0);
        taskType =getIntent().getIntExtra("taskType",0);
        hegeFlag =1;
        detectionAdapter = new DetectionAdapter();
        lv_detection.setAdapter(detectionAdapter);


//        lv_detection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                DetectionItem test =listData.get(position);
////                String s = test.getItemContent();
//                String s = "test";
//                Toast.makeText(DetctionActivity.this,s,Toast.LENGTH_SHORT).show();
//            }
//        });



//        lv_detection.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                menu.setHeaderTitle("确定删除该检查项吗");
//                menu.add(0, 0, 0, "删除此项");
//                menu.add(0, 1, 0, "取消删除");
//            }
//        });

        btn_add_detection.setOnClickListener(new View.OnClickListener() {
            String itemString;
            @Override
            public void onClick(View v) {

//                LayoutInflater layoutInflater = LayoutInflater.from(DetctionActivity.this);
//                final View textEntryView = layoutInflater.inflate(R.layout.detection_dialog, null);
                AlertDialog dlg = new AlertDialog.Builder(DetctionActivity.this)
                        .setTitle("输入你要添加的检测项")
                        .setView(textEntryView)
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                EditText item = (EditText) textEntryView.findViewById(R.id.et_add_item);
//                                itemString = item.getText().toString();
//                                DetectionItem testItem = new DetectionItem(itemString);
//                                listData.add(testItem);
//                                detectionAdapter.notifyDataSetChanged();
//                                lv_detection.invalidate();

                                postTaskAndGetResult();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dlg.show();

            }
        });


        btn_sure_detection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(DetctionActivity.this,ResultActivity.class);
//                intent.putExtra("listData",(Serializable)listData);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Intent intent = new Intent(DetctionActivity.this,PDFActivity.class);
                intent.putExtra("listData",(Serializable)listData);
                intent.putExtra("taskType",taskType);
                intent.putExtra("hegeFlag",hegeFlag);
                intent.putExtra("position",detctionPosition);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    private void postTaskAndGetResult() {

        String url = SplashActivity.baseurl+"/addTaskServlet";
        Log.d(TAG,"POST url: "+url);


        JSONObject tSchedule = new JSONObject();
        JSONObject jsonObject = null;
        try {
            tSchedule.put("TASKID","123461");
            tSchedule.put("DEVCLASS","3000");
            tSchedule.put("DEVID","12345");
            tSchedule.put("LOGINNAME",LoginActivity.testinputName);
            tSchedule.put("TASKTYPE",taskTypes[taskType].toString());

            jsonObject = new JSONObject();
            jsonObject.put("tSchedule", tSchedule);
            Log.e(TAG,"tSchedule: "+ jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, jsonObject, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Log.e(TAG,"error: "+e.toString());
                Log.e(TAG,"i: "+i);
            }

            @Override
            public void onResponse(String s, int i) {
                //测试Gson
                Gson gson = new Gson();
                AddTaskBean addTaskBean = gson.fromJson(s,AddTaskBean.class);
                AddTaskBean.ResultBean resultBean = addTaskBean.getResult();
                String message = resultBean.getMessage();


                if(message.equals("添加任务成功")){
                    String itemString;
                    EditText item = (EditText) textEntryView.findViewById(R.id.et_add_item);
                    itemString = item.getText().toString();
                    DetectionItem testItem = new DetectionItem(itemString);
                    listData.add(testItem);
                    detectionAdapter.notifyDataSetChanged();
                    lv_detection.invalidate();
                    Toasty.success(DetctionActivity.this,"添加任务成功！",Toast.LENGTH_SHORT,true).show();
                } else if(message.equals("操作失败")){
                    Toasty.error(DetctionActivity.this, "添加任务失败！", Toast.LENGTH_LONG,true).show();
                }

            }
        });

    }

    public void getData(){
        listData = new ArrayList<DetectionItem>();
        listData.add(new DetectionItem("现场人员是否具有有效证件。","合格"));
        listData.add(new DetectionItem("是否有使用登记标志，并按规定固定在电梯的显著位置，是否在下次检验期限内。","合格"));
        listData.add(new DetectionItem("安全注意事项和警示标志是否置于易于为乘客注意的显著位置。","合格"));
        listData.add(new DetectionItem("电梯内设置的报警装置是否可靠，联系是否畅通。","合格"));
        listData.add(new DetectionItem("抽查呼层、楼层等显示信号系统功能是否有效，指示是否正确。","合格"));
        listData.add(new DetectionItem("门防夹保护装置是否有效。","合格"));
        listData.add(new DetectionItem("自动扶梯和自动人行道入口处急停开关是否有效。","合格"));
        listData.add(new DetectionItem("限速器校验报告是否在有效期内。","合格"));
        listData.add(new DetectionItem("是否有有效的维保合同，维保资质及人员资质是否满足要求。","合格"));
        listData.add(new DetectionItem("是否有维保记录，并经安全管理人签字确认，维保周期是否符合规定。","合格"));


    }

    class DetectionAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            //如果没有复用的
            if(convertView == null){
                //加载item的布局
                convertView = View.inflate(DetctionActivity.this, R.layout.listview_detection, null);
                viewHolder = new ViewHolder();
                viewHolder.contentView= (TextView) convertView.findViewById(R.id.tv_detection_item);
                viewHolder.menuView = (TextView) convertView.findViewById(R.id.tv_detection_menu);
                //viewHolder.rg_detection = convertView.findViewById(R.id.rg_detection);
                viewHolder.rb_detection_1 = convertView.findViewById(R.id.rb_detection_1);
                viewHolder.rb_detection_2 = convertView.findViewById(R.id.rb_detection_2);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();

            }

            viewHolder.contentView.setText(listData.get(position).getItemContent());

            viewHolder.contentView.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    Toast.makeText(DetctionActivity.this, "click "+((TextView)v).getText(), Toast.LENGTH_SHORT).show();

                }

            });



            viewHolder.rb_detection_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String noItem = listData.get(position).getItemContent();
                    listData.get(position).setResultStatus("不合格");
                    hegeFlag =0;
                    Intent intent = new Intent(DetctionActivity.this, SuggestionActivity.class);
                    intent.putExtra("suggestions",noItem);
                    startActivity(intent);

                }
            });
            viewHolder.rb_detection_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String noItem = listData.get(position).getItemContent();
                    listData.get(position).setResultStatus("合格");
                    Intent intent = new Intent(DetctionActivity.this,SuggestionActivity.class);
                    intent.putExtra("suggestion1",noItem);
                    startActivity(intent);
                }
            });



            viewHolder.menuView.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    listData.remove(position);
                    detectionAdapter.notifyDataSetChanged();
                    lv_detection.invalidate();

                }

            });

            SlideLayout slideLayout = (SlideLayout) convertView;
            slideLayout.setOnStateChangeListener(new MyOnStateChangeListener());

            return convertView;
        }
    }








    public SlideLayout slideLayout = null;
    class MyOnStateChangeListener implements SlideLayout.OnStateChangeListener {

        @Override

        public void onOpen(SlideLayout layout) {

            slideLayout = layout;

        }


        @Override

        public void onMove(SlideLayout layout) {

            if (slideLayout != null && slideLayout !=layout)

            {

                slideLayout.closeMenu();

            }

        }


        @Override

        public void onClose(SlideLayout layout) {

            if (slideLayout == layout)

            {

                slideLayout = null;

            }

        }

    }



    static class ViewHolder

    {

        public TextView contentView;

        public TextView menuView;

        public RadioButton rb_detection_1;
        public RadioButton rb_detection_2;
       // public RadioButton rg_detection;


    }

}
