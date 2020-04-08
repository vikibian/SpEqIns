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
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.FileResultCallBack;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.ReloadImageAndVideo;
import com.neu.test.util.SearchUtil;
import com.neu.test.util.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import q.rorbin.badgeview.QBadgeView;

public class RectifyListActivity extends AppCompatActivity  {
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
    private TextView tv_totalitem;
    private RectifyListAdapter rectifyListAdapter;
    private SearchUtil searchUtil = new SearchUtil();

    private List<DetectionResult> detectionResults = new ArrayList<DetectionResult>();//检查结果list
    final int RectifyResultCode = 119;
    private static Map<Integer,Boolean> isDone = new HashMap<>();
    Dialog dialogMenu;
    Dialog dialogContent;
    Dialog dlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectify_list);
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
                List<DetectionResult> list = new ArrayList<>();
                for (int i=0;i<detectionResults.size();i++){
                    if (detectionResults.get(i).getSTATUS().equals("2")){
                        list.add(detectionResults.get(i));
                    }
                }

                if (list.size() != 0) {
                    Toasty.warning(RectifyListActivity.this, "有未操作项，无法提交", Toast.LENGTH_LONG).show();
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
                            Toasty.info(RectifyListActivity.this, "提交成功！", Toast.LENGTH_LONG).show();
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

            //测试
            if (position == 0){
                detectionResults.get(position).setREFJIM("image_202031610246.jpg,image_202031662445.jpg");
            }
//            //测试
            if (position == 1){
                detectionResults.get(position).setREFJIM("image_202031610246.jpg,image_202031662445.jpg");
                detectionResults.get(position).setREFJVI("video_1584540967093.mp4,video_1584582023465.mp4");
            }


            rectifyListViewHolder.recify_imageview_attach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    View.OnClickListener clickListener = new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            switch (v.getId()){
//                                case R.id.menu_icon_iv:
//                                    ToastUtil.showNumber(RectifyListActivity.this,"文字");
//                                    dialogMenu.dismiss();
//                                    View view = View.inflate(RectifyListActivity.this,R.layout.show_text_layout,null);
//                                    TextView textView = view.findViewById(R.id.tv_show_text);
//                                    textView.setText(detectionResults.get(position).getSUGGESTION()+"");
//                                    dialogContent = new AlertDialog.Builder(RectifyListActivity.this).setView(view).create();
//                                    dialogContent.setCanceledOnTouchOutside(true);
//                                    dialogContent.show();
//                                    break;
//                                case R.id.photo:
//                                    //ToastUtil.showNumber(RectifyListActivity.this,"图片");
//                                    if (detectionResults.get(position).getREFJIM().equals("")){
//                                        dialogMenu.dismiss();
//                                        Toast.makeText(RectifyListActivity.this, "无图片", Toast.LENGTH_SHORT).show();
//                                    }else {
//                                        dialogMenu.dismiss();
//                                        Intent intent = new Intent(RectifyListActivity.this,ShowImageActivity.class);
//                                        List<String> photopath = new ArrayList<>();
//                                        ReloadImageAndVideo reloadImageAndVideo = new ReloadImageAndVideo();
//                                        photopath = reloadImageAndVideo.decodeImagePath(detectionResults.get(position).getREFJIM()
//                                                ,detectionResults.get(position).getLOGINNAME());
//                                        intent.putExtra("photo", (Serializable) photopath);
//                                        startActivity(intent);
//                                    }
//                                    break;
//                                case R.id.video:
//                                    //ToastUtil.showNumber(RectifyListActivity.this,"视频");
//                                    if (detectionResults.get(position).getREFJVI().equals("")){
//                                        dialogMenu.dismiss();
//                                        Toast.makeText(RectifyListActivity.this, "无视频", Toast.LENGTH_SHORT).show();
//                                    }else {
//                                        dialogMenu.dismiss();
//                                        Intent intent2 = new Intent(RectifyListActivity.this,ShowVideoActivity.class);
//                                        List<String> videopath = new ArrayList<>();
//                                        ReloadImageAndVideo reloadImageAndVideo = new ReloadImageAndVideo();
//                                        videopath = reloadImageAndVideo.decodeVideoPath(detectionResults.get(position).getREFJVI()
//                                                ,detectionResults.get(position).getLOGINNAME());
//                                        Log.e(TAG, "onClick: 视频地址   "+videopath);
//                                        intent2.putExtra("video", (Serializable) videopath);
//                                        startActivity(intent2);
//                                    }
//                                    break;
//                                case R.id.file:
//                                    //ToastUtil.showNumber(RectifyListActivity.this,"文件");
//                                    dialogMenu.dismiss();
//                                    Intent intent3 = new Intent(RectifyListActivity.this,FileReadActivity.class);
//                                    intent3.putExtra("name","带时间窗的冷链物流车辆路径优化问题研究.pdf");
//                                    startActivity(intent3);
//
//                                    break;
//                            }
//                        }
//                    };
//                    View view = View.inflate(RectifyListActivity.this,R.layout.layout_zhenggai_menu,null);
//                    ImageView menu_icon_iv = view.findViewById(R.id.menu_icon_iv);
//                    ImageView photo = view.findViewById(R.id.photo);
//                    ImageView video = view.findViewById(R.id.video);
//                    ImageView file = view.findViewById(R.id.file);
//                    LinearLayout text_lin = view.findViewById(R.id.menu_text_lin);
//                    LinearLayout photo_lin = view.findViewById(R.id.menu_photo_lin);
//                    LinearLayout video_lin = view.findViewById(R.id.menu_video_lin);
//                    LinearLayout file_lin = view.findViewById(R.id.menu_file_lin);
//
//                    menu_icon_iv.setOnClickListener(clickListener);
//                    photo.setOnClickListener(clickListener);
//                    video.setOnClickListener(clickListener);
//                    file.setOnClickListener(clickListener);
//
//                    ReloadImageAndVideo reloadImageAndVideo = new ReloadImageAndVideo();
//                    int photonumber = 0;
//                    int videonumber = 0;
//                    photonumber = reloadImageAndVideo.getPhotoNumber(detectionResults.get(position).getREFJIM());
//                    videonumber = reloadImageAndVideo.getVideoNumber(detectionResults.get(position).getREFJVI());
//
//                    if (photonumber == 0){
//                        photo_lin.setVisibility(View.GONE);
//                    }else {
//                        new QBadgeView(getApplicationContext()).bindTarget(photo).setBadgeNumber(photonumber);
//                    }
//                    if (videonumber == 0){
//                        video_lin.setVisibility(View.GONE);
//                    }else {
//                        new QBadgeView(getApplicationContext()).bindTarget(video).setBadgeNumber(videonumber);
//                    }
//
//                    //BadgeView
//                    new QBadgeView(getApplicationContext()).bindTarget(menu_icon_iv).setBadgeNumber(-1);
//
//
//
//                    dialogMenu = new AlertDialog.Builder(RectifyListActivity.this).setView(view).create();
//                    Window window = dialogMenu.getWindow();
//                    window.setGravity(Gravity.BOTTOM);
//                    WindowManager m = getWindowManager();
//                    Display d = m.getDefaultDisplay(); //为获取屏幕宽、高
//                    WindowManager.LayoutParams p = dialogMenu.getWindow().getAttributes(); //获取对话框当前的参数值
//                    Log.e("eeeeee",p.width+"");
//                    Log.e("eeeeee",d.getWidth()+"");
//                    p.width = WindowManager.LayoutParams.MATCH_PARENT;
//                    dialogMenu.getWindow().setAttributes(p); //设置生效
//                    dialogMenu.show();

                    goToCheckMediaActivity(position);


                }
            });

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

            rectifyListViewHolder.recify_checkbox_unqualified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "onClick: 不合格 ");
                    rectifyListViewHolder.recify_checkbox_unqualified.setChecked(false);
                    if (detectionResults.get(position).getISCHANGED().equals(searchUtil.changed)
                    &&detectionResults.get(position).getSTATUS().equals(searchUtil.nohege)){
                        Toasty.warning(RectifyListActivity.this,"已选择，无法重复操作");
                        rectifyListViewHolder.recify_checkbox_unqualified.setChecked(true);
                        rectifyListViewHolder.recify_checkbox_recifyqualified.setChecked(false);
                    }else {
                        goRectifyResultActivity(position,searchUtil.nohege);
                    }
//                    goRectifyResultActivity(position,searchUtil.nohege);
                }
            });

            rectifyListViewHolder.recify_checkbox_recifyqualified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "onClick: 整改合格 ");
                    rectifyListViewHolder.recify_checkbox_recifyqualified.setChecked(false);
                    if (detectionResults.get(position).getISCHANGED().equals(searchUtil.changed)
                    &&detectionResults.get(position).getSTATUS().equals(searchUtil.recifyQualify)){
                        Toasty.warning(RectifyListActivity.this,"已选择，无法重复操作");
                        rectifyListViewHolder.recify_checkbox_recifyqualified.setChecked(true);
                        rectifyListViewHolder.recify_checkbox_unqualified.setChecked(false);
                    }else {

                        goRectifyResultActivity(position,searchUtil.recifyQualify);
                    }
//                    goRectifyResultActivity(position,searchUtil.recifyQualify);
                }
            });

            rectifyListViewHolder.recify_imageview_error.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                }
            });

//            rectifyListViewHolder.recify_textview_context.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    dlg = new Dialog(RectifyListActivity.this,R.style.FullScreen);
//                    View textEntryView = View.inflate(RectifyListActivity.this,R.layout.show_law_and_other, null);
//                    TextView tv_paichaxize = textEntryView.findViewById(R.id.tv_paichaxize);
//                    TextView tv_laws = textEntryView.findViewById(R.id.tv_laws);
//                    Button btn_cancel = textEntryView.findViewById(R.id.btn_cancel);
//                    btn_cancel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dlg.dismiss();
//                        }
//                    });
//                    tv_paichaxize.setText(detectionResults.get(position).getCHECKCONTENT());
//                    tv_laws.setText(detectionResults.get(position).getLAW());
//                    dlg.setContentView(textEntryView);
//                    dlg.show();
//                    return true;
//                }
//            });

            //默认ISCHANGED为empty  默认是havedetail
            if (detectionResults.get(position).getISCHANGED().equals(searchUtil.changed)){
                rectifyListViewHolder.recify_imageview_arrowright.setVisibility(View.VISIBLE);
                rectifyListViewHolder.recify_imageview_arrowright.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rectifyListViewHolder.recify_checkbox_recifyqualified.isChecked()){
                            goRectifyResultActivity(position,searchUtil.recifyQualify);
                        }else if (rectifyListViewHolder.recify_checkbox_unqualified.isChecked()){
                            goRectifyResultActivity(position,searchUtil.nohege);
                        }
                    }
                });

            }else if (detectionResults.get(position).getISCHANGED().isEmpty()
                    || detectionResults.get(position).getISCHANGED().equals(searchUtil.unchanged)){
                rectifyListViewHolder.recify_imageview_arrowright.setVisibility(View.INVISIBLE);
            }

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
            }

        }
    }


    private void goRectifyResultActivity(int position, String isHege) {
        Intent intent = new Intent(RectifyListActivity.this,RectifyActivity.class);
        intent.putExtra("detectionResult",detectionResults.get(position));
        intent.putExtra("toolbarTitle",title);
        intent.putExtra("toolbarTaskType",taskType);
        intent.putExtra("position",position);
        intent.putExtra("isHege",isHege);
        startActivityForResult(intent,RectifyResultCode);
    }



    private void goToCheckMediaActivity(int position) {
        Intent intent = new Intent(RectifyListActivity.this,CheckMediaForRectifyActivity.class);
        intent.putExtra("detectionResult",detectionResults.get(position));
        intent.putExtra("title",title);
        intent.putExtra("taskType",taskType);
        startActivity(intent);
    }

//    public List<String> decodeImagePath(String imagePath,String name) {
////        detectionResult.setCHANGEDIMAGE("image_202031610246.jpg,image_202031662445.jpg,image_202031610246.jpg,image_202031662445.jpg");//image_202031610246.jpg,image_202031662445.jpg  image_202031610246.jpg,image_202031662445.jpg,image_202031610246.jpg,image_202031662445.jpg
//        List<String> paths = new ArrayList<>();
////        String imagePath = "";
////        imagePath = detectionResult.getCHANGEDIMAGE();
//        if ((!imagePath.equals(""))&&(!imagePath.equals(null))){
//            String[] splitpath = imagePath.split(",");
//            for (int i=0;i<splitpath.length;i++){
//                String httppath = BaseUrl.pathOfPhotoAndVideo+ name +"/"+splitpath[i];
//                paths.add(httppath);
//            }
//        }else {
//
//        }
//        return paths;
//    }
//
//    private List<String> decodeVideoPath(String videoPath,String name) {
////        detectionResult.setCHANGEDVIDEO("video_1584540967093.mp4,video_1584582023465.mp4,video_1584540967093.mp4,video_1584582023465.mp4");
//        List<String> paths = new ArrayList<>();
////        String videoPath = "";
////        videoPath = detectionResult.getCHANGEDVIDEO();
//        if ((!videoPath.equals(""))&&(!videoPath.equals(null))){
//            String[] splitpath = videoPath.split(",");
//            for (int i=0;i<splitpath.length;i++){
//                String httppath = BaseUrl.pathOfPhotoAndVideo+ name +"/"+splitpath[i];
//                paths.add(httppath);
//                Log.e(TAG, "decodeChaneVideoPath: httppath  "+httppath );
//            }
//        }else {
//
//        }
//        return paths;
//    }



}
