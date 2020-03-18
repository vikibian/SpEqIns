package com.neu.test.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.test.R;
import com.neu.test.adapter.SuggestionGridViewAdapter;
import com.neu.test.entity.DetectionResult;
import com.neu.test.util.SearchUtil;
import com.neu.test.util.SidebarUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class RectifyResultActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RectifyResultActivity";
    private static final int REQUEST_CODE_CHOOSE = 23;
    private GridView gridView;
    private SuggestionGridViewAdapter suggestionGridViewAdapter;
    public List<String> pathlistOfPhoto = new ArrayList<>();

    private  List<String> testpathlistOfPhoto = new ArrayList<>();

    private TextView textView_item_title;
    private EditText editText_rectify_way;
    private EditText editText_rectify_action;
    private TextView textView_finish_time;
    private EditText editText_rectify_result;
    private Button button_submit;
    private CheckBox recify_result_qualified;
    private CheckBox recify_result_unqualified;
    private int deleteIndex;

    private String testvideoPath = " ";

    final int RequestCor = 521;
    final int maxNum = 500;
    final int REQUEST_TEST = 66;
    public int position = 0;

    private String ImagePath = new String();
    private String VideoPath = new String();
    public DetectionResult detectionResult;
    private Intent intentByPreviousActivity;
    private int positionSelected;//获取从DetctionActivity中被点击的DetectionResult，当结束整改后将position返回 并更新

    private Toolbar toolbar;
    private TextView toolbar_textView;
    private TextView toolbar_title;
    private TextView toolbar_subtitleLeft;
    private TextView toolabr_subtitleRight;

    private String toolbarTitle;
    private String toolbarTaskType;
    //将status的固定值设置在一个固定的位置
    private SearchUtil searchUtil = new SearchUtil();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectify_result);

        deleteIndex = -1;

        initview();

        intentByPreviousActivity = getIntent();
        toolbarTitle = getIntent().getStringExtra("toolbarTitle");
        toolbarTaskType = getIntent().getStringExtra("toolbarTaskType");
        positionSelected = getIntent().getIntExtra("position",-1);
        detectionResult = (DetectionResult) getIntent().getSerializableExtra("detectionResult");


        initToolbar();

        textView_item_title.setText(detectionResult.getJIANCHAXIANGTITLE());//detectionResult.getJIANCHAXIANGTITLE()

        suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto,0);
        gridView.setAdapter(suggestionGridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteIndex = position;
                if (pathlistOfPhoto.size()==position){
                    view.showContextMenu();
                }else {
                    view.showContextMenu();
                }


            }
        });

        gridView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                menu.add(0,1,0,"相册");
                menu.add(0,2,0,"相机");
                menu.add(0,3,0,"取消");
                menu.add(0,4,0,"删除");
            }
        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_rectify);

        toolbar_title = findViewById(R.id.toolbar_rectify_title);
        toolbar_subtitleLeft = findViewById(R.id.toolbar_rectify_subtitle_left);
        toolabr_subtitleRight = findViewById(R.id.toolbar_rectify_subtitle_right);

        toolbar_title.setTextSize(20);
        toolbar_subtitleLeft.setTextSize(13);
        toolabr_subtitleRight.setTextSize(13);

        toolbar_title.setText(getResources().getString(R.string.app_name));
        toolbar_subtitleLeft.setText(toolbarTitle);
        toolabr_subtitleRight.setText(toolbarTaskType);
        toolabr_subtitleRight.setTextColor(getResources().getColor(R.color.yellow));
    }

    private void initview() {
        gridView = findViewById(R.id.rectify_result_gridview);
        textView_item_title = findViewById(R.id.rectify_result_item_title_textview);
        editText_rectify_way = findViewById(R.id.rectify_result_item_way_edittext);
        editText_rectify_action = findViewById(R.id.rectify_result_item_action_edittext);
        textView_finish_time = findViewById(R.id.rectify_result_item_finishtime_textview);
        editText_rectify_result = findViewById(R.id.rectify_result_item_rectifyresult);
        button_submit = findViewById(R.id.rectify_result_item_submit_button);
        recify_result_qualified = findViewById(R.id.rectify_result_qualified);
        recify_result_unqualified = findViewById(R.id.rectify_result_unqualified);

        textView_finish_time.setOnClickListener(this);
        button_submit.setOnClickListener(this);
        recify_result_qualified.setOnClickListener(this);
        recify_result_unqualified.setOnClickListener(this);


        //默认合格
        recify_result_qualified.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rectify_result_item_finishtime_textview:
                SidebarUtils.initSelectRecityTime(RectifyResultActivity.this,textView_finish_time);
                break;
            case R.id.rectify_result_item_submit_button:
                //在点击提交之前把整改的内容 添加到选中的detectionResult的相应属性中取

                if (!textView_finish_time.getText().equals(" ")){
                        setDetectionResult();
                        intentByPreviousActivity.putExtra("detectionResult",detectionResult);
                        intentByPreviousActivity.putExtra("position",positionSelected);
                        setResult(RESULT_OK,intentByPreviousActivity);
                        finish();

                }else {
                    Toasty.info(getApplicationContext(),"对不起，您没有选择时间！",Toasty.LENGTH_SHORT).show();
                }

                break;
            case R.id.rectify_result_qualified:
                recify_result_unqualified.setChecked(false);
                break;
            case R.id.rectify_result_unqualified:
                recify_result_qualified.setChecked(false);
                break;
            default:
                break;
        }
    }

    private void setDetectionResult() {
        detectionResult.setCHANGEDWAY(editText_rectify_way.getText().toString());
        detectionResult.setCHANGEDACTION(editText_rectify_action.getText().toString());
        detectionResult.setCHANGEDFINISHTIME(textView_finish_time.getText().toString());
        detectionResult.setCHANGEDRESULT(editText_rectify_result.getText().toString());
        if (recify_result_qualified.isChecked()){
            detectionResult.setISCHANGED(searchUtil.changed);
            detectionResult.setSTATUS(searchUtil.recifyQualify);
            detectionResult.setCHANGEDIMAGE(ImagePath);
            detectionResult.setCHANGEDVIDEO(VideoPath);
            Log.e(TAG,"imagepath  changed: "+ImagePath);
            Log.e(TAG,"VideoPath  changed: "+VideoPath);
        }else if (recify_result_unqualified.isChecked()){
            detectionResult.setISCHANGED(searchUtil.unchanged);
            detectionResult.setSTATUS(searchUtil.nohege);
            detectionResult.setREFJIM(ImagePath);
            detectionResult.setREFJVI(VideoPath);
            Log.e(TAG,"imagepath  refjim: "+ImagePath);
            Log.e(TAG,"VideoPath  refjvm: "+VideoPath);
        }

    }

    /**
     * 虽然menu选项在SuggestionGridViewAdapter.java文件中但是一样可以使用这里的选择事件按钮
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case 1:
                Log.e(TAG," item  "+item.getTitle());
                startPhotoGallery();
                break;
            case 2:
                Intent intentall = new Intent(RectifyResultActivity.this,PhotoVideoActivity.class);
                intentall.putExtra("username",detectionResult.getLOGINNAME());
                startActivityForResult(intentall,REQUEST_TEST);
                break;
            case 3:
                break;
            case 4:
                if (deleteIndex>=0&&deleteIndex<pathlistOfPhoto.size()){
                    pathlistOfPhoto.remove(deleteIndex);
                    suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto,1);
                    gridView.setAdapter(suggestionGridViewAdapter);
                }else {
                    Toast.makeText(getApplicationContext(),"此图片不能删除",Toast.LENGTH_SHORT).show();
                }
                break;


        }
        return super.onContextItemSelected(item);
    }


    /**
     * @date : 2020/2/23
     * @time : 20:55
     * @author : viki
     * @description :获取相册
     */

    private void startPhotoGallery() {

        Matisse.from(RectifyResultActivity.this)
                .choose(MimeType.ofAll())//图片类型
                .countable(true)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(5)//可选的最大数
                //.capture(true)//选择照片时，是否显示拍照
                //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；
                // 参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                //"com.neu.testimageload.fileprovider"
                .captureStrategy(new CaptureStrategy(true, "com.neu.test.fileprovider"))
                .imageEngine(new GlideEngine())//图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE);//
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //测试多张图片显示
        String imgString = new String();
        String videoString = new String();

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_TEST) {

                imgString = data.getStringExtra("ImagePath");
                testvideoPath = data.getStringExtra("VideoPath");
                videoString = data.getStringExtra("VideoPath");
                //不需要旋转90度  需要在设置图片的时候进行判断

                //在此处需要更新图片数组
                if (!(imgString.equals(""))) {
                    ImagePath += imgString + ",";
                    imgString = Environment.getExternalStorageDirectory() + "/DCIM/" + detectionResult.getLOGINNAME() + "/Photo/" + imgString;
                    pathlistOfPhoto.add(imgString);
                }
                if (!(testvideoPath.equals(""))) {
                    VideoPath += videoString + ",";
                    videoString = Environment.getExternalStorageDirectory() + "/DCIM/" + detectionResult.getLOGINNAME() + "/Video/" + videoString;
                    pathlistOfPhoto.add(videoString);
                }
                suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto, 1);
                gridView.setAdapter(suggestionGridViewAdapter);

            }

            //Masstise返回的图片数据
            if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
                Log.d("Matisse", "Uris: " + Matisse.obtainResult(data));
                Log.d("Matisse", "Paths: " + Matisse.obtainPathResult(data));
                Log.e("Matisse", "Use the selected photos with original: "+String.valueOf(Matisse.obtainOriginalState(data)));
                for (int i = 0;i<Matisse.obtainPathResult(data).size();i++){
                    pathlistOfPhoto.add(Matisse.obtainPathResult(data).get(i));
                }

                suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto,0);
                gridView.setAdapter(suggestionGridViewAdapter);
            }
        }

    }
}
