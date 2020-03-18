package com.neu.test.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.FilePathResult;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.layout.SlideLayout;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.FileResultCallBack;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseUrl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class ReDetectionActivity extends AppCompatActivity {

    private static String TAG = "ReDetectionActivity";

    private ListView lv_detection;
    private Button btn_add_detection;
    private Button btn_save_detection;
    private Button btn_sure_detection;
    private TextView tv_totalitem;
    private ReDetectionActivity.ReDetectionAdapter detectionAdapter;

    public List<DetectionResult> detectionResults = new ArrayList<DetectionResult>();//检查结果list

    private String userName;
    private String url;
    private Task task = new Task();
    private String taskType ;
    private String title ;
    final int RequestCor = 521;
    final int RectifyCode = 600;
    private int pposition;


    DetctionActivity.ViewHolder viewHolder=null;
    View view1;

    private Toolbar toolbar;
    private TextView toolbar_textView;
    private static boolean isSave = false;

    private TextView toolbar_title;
    private TextView toolbar_subtitleLeft;
    private TextView toolabr_subtitleRight;
    private  Intent intent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detction);

        intent1 = getIntent();
        task = (Task) getIntent().getSerializableExtra("task");
        pposition =  getIntent().getIntExtra("position",0);
        title = task.getUSEUNITNAME();
        userName = task.getLOGINNAME();
        taskType =  task.getTASKTYPE();//getIntent().getStringExtra("tasktype");
        initToolbar();
        //初始化
        lv_detection = findViewById(R.id.lv_detection);
        btn_add_detection = findViewById(R.id.btn_add_detection);
        btn_sure_detection = findViewById(R.id.btn_sure_detection);
        btn_save_detection = findViewById(R.id.btn_save_detection);
        tv_totalitem = findViewById(R.id.tv_totalitem);

        detectionResults = (List<DetectionResult>) getIntent().getSerializableExtra("items");
//        for(int i=0;i<detectionResults.size();i++){
//            detectionResults.get(i).setTASKID(task.getTASKID());
//            //暂时默认 都有证据
//            detectionResults.get(i).setHaveDetail(true);
//        }
        Log.e(TAG," detectionResults: "+detectionResults.size());


        tv_totalitem.setText("------------共"+detectionResults.size()+"项------------");
        detectionAdapter = new ReDetectionActivity.ReDetectionAdapter();
        lv_detection.setAdapter(detectionAdapter);

        btn_add_detection.setOnClickListener(new View.OnClickListener() {
            String itemString;
            @Override
            public void onClick(View v) {

                Log.e(TAG," 保存");

                LayoutInflater layoutInflater = LayoutInflater.from(ReDetectionActivity.this);
                final View textEntryView = layoutInflater.inflate(R.layout.detection_dialog, null);
                AlertDialog dlg = new AlertDialog.Builder(ReDetectionActivity.this)
                        .setTitle("输入你要添加的检测项")
                        .setView(textEntryView)
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText item = (EditText) textEntryView.findViewById(R.id.et_add_item);
                                itemString = item.getText().toString();
                                DetectionResult detectionResult = new DetectionResult();
                                detectionResult.setRUNWATERNUM(detectionResults.get(0).getRUNWATERNUM());
                                detectionResult.setJIANCHAXIANGTITLE(itemString);
                                detectionResult.setTASKID(detectionResults.get(0).getTASKID());
                                detectionResult.setDEVID(detectionResults.get(0).getDEVID());
                                detectionResult.setDEVCLASS(detectionResults.get(0).getDEVCLASS());
                                detectionResult.setLOGINNAME(detectionResults.get(0).getLOGINNAME());
                                detectionResults.add(detectionResult);
                                //更新
                                detectionAdapter.notifyDataSetChanged();
                                lv_detection.invalidate();

                                //postTaskAndGetResult();
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                List<DetectionResult> list = detectionResults.stream().filter(dete -> dete.getSTATUS().equals("2")).collect(Collectors.toList());
                if (list.size() != 0) {
                    Toasty.warning(ReDetectionActivity.this, "有未操作项，无法提交", Toast.LENGTH_LONG).show();
                } else {
                    String string = new Gson().toJson(detectionResults);
                    String url = BaseUrl.BaseUrl+"setItemResult";
                    OkHttp okHttp = new OkHttp();
                    okHttp.postBypostString(url, string, new FileResultCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            Log.e("error", e.toString());
                        }

                        @Override
                        public void onResponse(FilePathResult filePathResult, int i) {
                            Log.e("message", filePathResult.getMessage());
                            Intent intent = new Intent(ReDetectionActivity.this, PDFActivity.class);
                            intent.putExtra("listData", (Serializable) detectionResults);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        btn_save_detection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = new Gson().toJson(detectionResults);
                String url = BaseUrl.BaseUrl+"setItemResult";
                OkHttp okHttp = new OkHttp();
                okHttp.postBypostString(url, string, new FileResultCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Log.e("error",e.toString());
                    }

                    @Override
                    public void onResponse(FilePathResult filePathResult, int i) {
                        Log.e("message",filePathResult.getMessage());
                        intent1.putExtra("position",pposition);
                        setResult(RESULT_OK,intent1);
                        finish();
                    }
                });
            }
        });
    }

    /**
     * @date : 2020/2/22
     * @time : 9:51
     * @author : viki
     * @description : 设置toolbar让中间显示文字
     */

  public void initToolbar() {
    toolbar = (Toolbar) findViewById(R.id.toolbar_detction);
      toolbar_title = findViewById(R.id.toolbar_detction_title);
      toolbar_subtitleLeft = findViewById(R.id.toolbar_detction_subtitle_left);
      toolabr_subtitleRight = findViewById(R.id.toolbar_detction_subtitle_right);

      toolbar_title.setTextSize(20);
      toolbar_subtitleLeft.setTextSize(13);
      toolabr_subtitleRight.setTextSize(13);

      toolbar_title.setText(getResources().getString(R.string.app_name));
      toolbar_subtitleLeft.setText(title);
      toolabr_subtitleRight.setText(taskType);
      toolabr_subtitleRight.setTextColor(getResources().getColor(R.color.yellow));

  }



    class ReDetectionAdapter extends BaseAdapter {

        private Map<Integer,Integer> hashMap = new HashMap<>();// key封装的是它爹的tag值，value封装儿子radiobutton

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
        public View getView(final int position, View convertView, ViewGroup parent) {

            //加载item的布局
            convertView = View.inflate(ReDetectionActivity.this, R.layout.test_list_item2, null);
            viewHolder = new DetctionActivity.ViewHolder();
            viewHolder.detction_item_text_leftnum= (TextView) convertView.findViewById(R.id.detction_item_text_leftnum_2);
            viewHolder.detction_item_text_context= (TextView) convertView.findViewById(R.id.detction_item_text_context_2);
            viewHolder.rectify_item_status_rectified = convertView.findViewById(R.id.rectify_item_status_rectified_2);
            viewHolder.rectify_item_status_rectifyliving = convertView.findViewById(R.id.rectify_item_status_rectifyliving_2);
            viewHolder.rectify_item_status_unrectify = convertView.findViewById(R.id.rectify_item_status_unrectify_2);
            viewHolder.detction_item_image_right = convertView.findViewById(R.id.detction_item_image_right_2);
            viewHolder.ll_test = convertView.findViewById(R.id.ll_ttt);
            convertView.setTag(viewHolder);
            //检查项标题
            viewHolder.detction_item_text_context.setText(detectionResults.get(position).getJIANCHAXIANGTITLE());

            //检查任务的number设置
            viewHolder.detction_item_text_leftnum.setText(position+1+"");

            //详细按钮的显示问题
            if(detectionResults.get(position).getISHAVEDETAIL().equals("1")){
                viewHolder.detction_item_image_right.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.detction_item_image_right.setVisibility(View.INVISIBLE);
            }



            //初始化选择状态
            if(detectionResults.get(position).getSTATUS().equals("0")){
                viewHolder.rectify_item_status_unrectify.setChecked(true);
                viewHolder.rectify_item_status_rectified.setChecked(false);
                viewHolder.rectify_item_status_rectifyliving.setChecked(false);
            }

            else if(detectionResults.get(position).getSTATUS().equals("1")){
                viewHolder.rectify_item_status_unrectify.setChecked(false);
                viewHolder.rectify_item_status_rectified.setChecked(true);
                viewHolder.rectify_item_status_rectifyliving.setChecked(false);
            }
            else if (detectionResults.get(position).getSTATUS().equals("3")){
                viewHolder.rectify_item_status_unrectify.setChecked(false);
                viewHolder.rectify_item_status_rectified.setChecked(false);
                viewHolder.rectify_item_status_rectifyliving.setChecked(true);
            }


            viewHolder.rectify_item_status_unrectify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//          viewHolder.rectify_item_status_unrectify.setChecked(true);
//          viewHolder.rectify_item_status_rectified.setChecked(false);
//          viewHolder.rectify_item_status_rectifyliving.setChecked(false);
                    if(detectionResults.get(position).getSTATUS().equals("0")){
                        viewHolder.rectify_item_status_unrectify.setChecked(true);
                        viewHolder.rectify_item_status_rectified.setChecked(false);
                        viewHolder.rectify_item_status_rectifyliving.setChecked(false);
                        //Toasty.warning(DetctionActivity.this,"已选择，无法重复操作");
                        lv_detection.setAdapter(detectionAdapter);
                    }else{
                        jumpToSuggesstionActivity(position,"0");
                    }
                }
            });


            viewHolder.rectify_item_status_rectified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(detectionResults.get(position).getSTATUS().equals("1")){
                        viewHolder.rectify_item_status_unrectify.setChecked(false);
                        viewHolder.rectify_item_status_rectified.setChecked(true);
                        viewHolder.rectify_item_status_rectifyliving.setChecked(false);
                        Toasty.warning(ReDetectionActivity.this,"已选择，无法重复操作");
                        lv_detection.setAdapter(detectionAdapter);
                    }else{
                        //页面跳转
                        //detectionResults.get(position).setSTATUS("1");
                        jumpToSuggesstionActivity(position,"1");
                    }
                }
            });

            viewHolder.rectify_item_status_rectifyliving.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//          viewHolder.rectify_item_status_unrectify.setChecked(false);
//          viewHolder.rectify_item_status_rectified.setChecked(false);
//          viewHolder.rectify_item_status_rectifyliving.setChecked(true);
                    if(detectionResults.get(position).getSTATUS().equals("3")){
                        viewHolder.rectify_item_status_unrectify.setChecked(false);
                        viewHolder.rectify_item_status_rectified.setChecked(false);
                        viewHolder.rectify_item_status_rectifyliving.setChecked(true);
                        Toasty.warning(ReDetectionActivity.this,"已选择，无法重复操作");
                        lv_detection.setAdapter(detectionAdapter);
                    }else{
                        //页面跳转  现场整改
                        //detectionResults.get(position).setSTATUS("3");
//                        Toasty.warning(ReDetectionActivity.this,"跳转至整改合格界面");
                        //jumpToSuggesstionActivity(position);
                        jumpToRectifyResultActivity(position);
                        lv_detection.setAdapter(detectionAdapter);
                    }
                }
            });
            //跳转至 已操作界面
            viewHolder.detction_item_image_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jumpToSuggesstionActivity(position,detectionResults.get(position).getSTATUS());
                }
            });

            viewHolder.detction_item_text_context.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            viewHolder.detction_item_text_context.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//          AlertDialog.Builder builder=new AlertDialog.Builder(DetctionActivity.this);
//          builder.setTitle("排查要求&法律法规");
//          builder.setMessage(detectionResults.get(position).getCHECKCONTENT()+"/n"+detectionResults.get(position).getLAW());
////          builder.setPositiveButton("我知道了",
////            new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialogInterface, int i) {
////
////            }
////          });
//          AlertDialog dialog=builder.create();
//          dialog.show();


                    new MaterialStyledDialog.Builder(ReDetectionActivity.this)
                            .setTitle("排查要求&法律法规")
                            .setDescription("排查要求：\n"+detectionResults.get(position).getCHECKCONTENT()+
                                    "\n"+"法律法规：\n"+detectionResults.get(position).getLAW())
                            .setStyle(Style.HEADER_WITH_TITLE)
                            .setScrollable(true,20)
                            .setPositiveText("我知道了")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Log.d("MaterialStyledDialogs", "Do something!");
                                }
                            })
                            .show();
                    return true;
                }
            });
            return convertView;
        }
    }

    private void jumpToRectifyResultActivity(int position) {
        Intent intent = new Intent(ReDetectionActivity.this,RectifyResultActivity.class);
        DetectionResult test = new DetectionResult();
//        test.setJIANCHAXIANGTITLE("测试");//测试代码
//        test.setLOGINNAME(LoginActivity.inputName);//测试代码
//        detectionResults.add(test);
        intent.putExtra("detectionResult",  detectionResults.get(position));
        intent.putExtra("position",position);
        intent.putExtra("toolbarTitle",title);
        intent.putExtra("toolbarTaskType",taskType);
        startActivityForResult(intent,RectifyCode);
    }



    private void jumpToSuggesstionActivity(int position,String status) {
        //listData.get(position).setResultStatus(status);
        // String isHege = listData.get(position).getResultStatus();
        //String noItem = listData.get(position).getJIANCHAXIANGCONTENT();
        //String devclass = listData.get(position).getDEVCLASS();
        Intent intent = new Intent(ReDetectionActivity.this, SuggestionActivity.class);
        intent.putExtra("detectionResult",detectionResults.get(position));
        //intent.putExtra("suggestion",noItem);
        //intent.putExtra("path",task.getTASKID());
        intent.putExtra("position",position);
        intent.putExtra("title",title);//需要传递status
        intent.putExtra("taskType",taskType);
        intent.putExtra("status",status);
        Log.e("status1",status);
        //intent.putExtra("isHege",isHege);
        startActivityForResult(intent,RequestCor);
    }


    //接收图片视频的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == RequestCor){
                int position = data.getIntExtra("position",0);
                Log.e("position",position+"-");
                int imageNumber = data.getIntExtra("imageNumber",0);
                Log.e("imageNumber",imageNumber+"-");
                int videoNumber = data.getIntExtra("videoNumber",0);
                Log.e("videoNumber",videoNumber+"-");
                String content = data.getStringExtra("content");
                Log.e("content",content+"-");
                String status = data.getStringExtra("status");

                String imagePath = data.getStringExtra("ImagePath");
                String videoPath = data.getStringExtra("VideoPath");
                if(imageNumber > 0){
                    //detectionResults.get(position).setREFJIM(task.getTASKID() + position + "-" + imageNumber);
                    detectionResults.get(position).setREFJIM(imagePath);
                    detectionResults.get(position).setISHAVEDETAIL("1");
                }
                if(videoNumber > 0){
                    //detectionResults.get(position).setREFJVI(task.getTASKID() + position + "-" + videoNumber);
                    detectionResults.get(position).setREFJVI(videoPath);
                    detectionResults.get(position).setISHAVEDETAIL("1");
                }
                //if(!(content.trim().toString().equals(""))){
                detectionResults.get(position).setSUGGESTION(content);
                detectionResults.get(position).setISHAVEDETAIL("1");
                detectionResults.get(position).setSTATUS(status);
                Log.e("status6",detectionResults.get(position).getSTATUS());
                //}
                lv_detection.setAdapter(detectionAdapter);
            }

            if (requestCode == RectifyCode){
                int pos = data.getIntExtra("position",-1);
                DetectionResult getdata = (DetectionResult) data.getSerializableExtra("detectionResult");
                detectionResults.set(pos,getdata);
                Log.e(TAG," way: "+detectionResults.get(pos).getCHANGEDWAY());
                Log.e(TAG," action: "+detectionResults.get(pos).getCHANGEDACTION());
                Log.e(TAG," finish time: "+detectionResults.get(pos).getCHANGEDFINISHTIME());
                Log.e(TAG," result: "+detectionResults.get(pos).getCHANGEDRESULT());
                Log.e(TAG,"imagepath: "+detectionResults.get(pos).getCHANGEDIMAGE());
                Log.e(TAG,"videopath: "+detectionResults.get(pos).getCHANGEDVIDEO());

                lv_detection.setAdapter(detectionAdapter);

            }
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




    static class ViewHolder{
        public TextView detction_item_text_context;
        public TextView detction_item_text_leftnum;
        public CheckBox rectify_item_status_unrectify;
        public CheckBox rectify_item_status_rectified;
        public CheckBox rectify_item_status_rectifyliving;
        public ImageView detction_item_image_right;
        public LinearLayout ll_test;
    }



    @Override
    public void onResume() {
        super.onResume();
        lv_detection.setAdapter(detectionAdapter);
        Log.e(TAG," onResume");
    }
}
