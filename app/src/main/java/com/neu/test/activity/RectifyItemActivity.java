package com.neu.test.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import com.neu.test.adapter.SuggestionGridViewAdapter1;
import com.neu.test.entity.DetectionResult;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.SidebarUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RectifyItemActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RectifyItemActivity";
    private static final int REQUEST_CODE_CHOOSE = 23;
    //复选框 整改情况
    private CheckBox rectify_item_status_unrectify;
    private CheckBox rectify_item_status_rectified;
    private CheckBox rectify_item_status_rectifyliving;

    private EditText editText;
    private GridView gridView;
    private SuggestionGridViewAdapter1 suggestionGridViewAdapter1;
    public List<String> pathlistOfPhoto = new ArrayList<>();
    private int deleteIndex;
    private Intent intent;
    private DetectionResult detectionResult;
    private TextView textView_selectTime;
    private TextView textView_rectifyTime;
    private Button rectify_item_save;

    private int hourOfDay, minute,position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectify_item);

        //初始化视图
        initView();
        //初始化参数
        initParams();
        //依据参数修改视图
        changeView();
        //绑定监听
        setListener();
    }

    public void initView(){
        rectify_item_status_unrectify = findViewById(R.id.rectify_item_status_unrectify);
        rectify_item_status_rectified = findViewById(R.id.rectify_item_status_rectified);
        rectify_item_status_rectifyliving = findViewById(R.id.rectify_item_status_rectifyliving);
        editText = findViewById(R.id.rectify_item_rectifyaction);
        editText.setBackground(getResources().getDrawable(R.drawable.text_border_gray));
        gridView = findViewById(R.id.suggestion_gridview);
        textView_rectifyTime = findViewById(R.id.rectify_item_rectifytime);
        rectify_item_save = findViewById(R.id.rectify_item_save);
        textView_selectTime = findViewById(R.id.rectify_item_rectifiedtime);
    }

    public void initParams(){
        deleteIndex = -1;
        intent = getIntent();
        position = intent.getIntExtra("position",0);
        detectionResult = (DetectionResult) intent.getSerializableExtra("errorResult");
        suggestionGridViewAdapter1 = new SuggestionGridViewAdapter1(getApplicationContext(), pathlistOfPhoto,0);
        gridView.setAdapter(suggestionGridViewAdapter1);
    }

    public void changeView(){
        //设置是否整改
        if(detectionResult.getISCHANGED().equals("1")){
            rectify_item_status_rectified.setChecked(true);
            rectify_item_status_rectifyliving.setChecked(false);
            rectify_item_status_unrectify.setChecked(false);
        }
        else if(detectionResult.getISCHANGED().equals("0")){
            rectify_item_status_rectified.setChecked(false);
            rectify_item_status_rectifyliving.setChecked(false);
            rectify_item_status_unrectify.setChecked(true);
        }else {
            rectify_item_status_rectified.setChecked(false);
            rectify_item_status_rectifyliving.setChecked(true);
            rectify_item_status_unrectify.setChecked(false);
        }
    }

    public void setListener(){
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
                menu.add(0,1,0,"相册");
                menu.add(0,2,0,"相机");
                menu.add(0,3,0,"取消");
                menu.add(0,4,0,"删除");
            }
        });
        textView_selectTime.setOnClickListener(this);
//        new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//          Toast.makeText(getApplicationContext()," 点击了",Toast.LENGTH_SHORT).show();
//          SidebarUtils.initSelectStartTime(RectifyItemActivity.this,textView_selectTime);
//        }
//      });

        textView_rectifyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SidebarUtils.initSelectStartTime(RectifyItemActivity.this,textView_rectifyTime);
            }
        });

        rectify_item_status_unrectify.setOnClickListener(this);
        rectify_item_status_rectifyliving.setOnClickListener(this);
        rectify_item_status_rectified.setOnClickListener(this);
        rectify_item_save.setOnClickListener(this);

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
//                Intent intentall = new Intent(RectifyItemActivity.this,PhotoVideoActivity.class);
//                startActivityForResult(intentall,REQUEST_TEST);
                break;
            case 3:
                break;
            case 4:
                if (deleteIndex>=0&&deleteIndex<pathlistOfPhoto.size()){
                    pathlistOfPhoto.remove(deleteIndex);
                    suggestionGridViewAdapter1 = new SuggestionGridViewAdapter1(getApplicationContext(), pathlistOfPhoto,1);
                    gridView.setAdapter(suggestionGridViewAdapter1);
                }else {
                    Toast.makeText(getApplicationContext(),"此图片不能删除",Toast.LENGTH_SHORT).show();
                }
                //pathlistOfPhoto
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

        Matisse.from(RectifyItemActivity.this)
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


        //Masstise返回的图片数据
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            Log.d("Matisse", "Uris: " + Matisse.obtainResult(data));
            Log.d("Matisse", "Paths: " + Matisse.obtainPathResult(data));
            Log.e("Matisse", "Use the selected photos with original: "+String.valueOf(Matisse.obtainOriginalState(data)));
            for (int i = 0;i<Matisse.obtainPathResult(data).size();i++){
                pathlistOfPhoto.add(Matisse.obtainPathResult(data).get(i));
            }

            suggestionGridViewAdapter1 = new SuggestionGridViewAdapter1(getApplicationContext(), pathlistOfPhoto,0);
            gridView.setAdapter(suggestionGridViewAdapter1);
//            List<Uri> result = Matisse.obtainResult(data);
//            textView.setText(result.toString());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rectify_item_rectifiedtime:
                Toast.makeText(getApplicationContext()," 点击了",Toast.LENGTH_SHORT).show();
                SidebarUtils.initSelectStartTime(RectifyItemActivity.this,textView_selectTime);
                break;
            case R.id.rectify_item_status_rectified:
                rectify_item_status_rectified.setChecked(true);
                rectify_item_status_rectifyliving.setChecked(false);
                rectify_item_status_unrectify.setChecked(false);
                detectionResult.setISCHANGED("1");
                break;
            case R.id.rectify_item_status_rectifyliving:
                rectify_item_status_rectifyliving.setChecked(true);
                rectify_item_status_rectified.setChecked(false);
                rectify_item_status_unrectify.setChecked(false);
                detectionResult.setISCHANGED("2");
                break;
            case R.id.rectify_item_status_unrectify:
                rectify_item_status_unrectify.setChecked(true);
                rectify_item_status_rectified.setChecked(false);
                rectify_item_status_rectifyliving.setChecked(false);
                detectionResult.setISCHANGED("0");
                break;
            case R.id.rectify_item_save:
                intent.putExtra("errorResult",(Serializable) detectionResult);
                setResult(RESULT_OK,intent);
                finish();

        }
    }
}
