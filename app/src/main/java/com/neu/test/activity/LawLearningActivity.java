package com.neu.test.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.adapter.ListLawAdaper;
import com.neu.test.entity.FileBean;
import com.neu.test.entity.Result;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.FileBeanCallBack;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.BaseUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

public class LawLearningActivity extends BaseActivity {
    private static String TAG = "LawLearningActivity";

    private Toolbar law_toolbar;
    private TextView law_toolbar_textview;
    private ListView listView_law;

    private String url;
    private List<FileBean> fileBeans = new ArrayList<>();
    private ArrayAdapter<String> adapter ;
    private ListLawAdaper listLawAdaper ;
    private String[] datas ;
    private PromptDialog promptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law_learning);
        promptDialog = new PromptDialog(this);
        initToolbar();
        init();
        getLawFiles();

//        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1);
//        listView_law.setAdapter(adapter);

        listLawAdaper = new ListLawAdaper(getApplicationContext(),fileBeans);
        listView_law.setAdapter(listLawAdaper);

        listView_law.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LawLearningActivity.this,FileReadActivity.class);
//                Toast.makeText(getApplicationContext(),position+" ",Toast.LENGTH_SHORT).show();
                if (fileBeans.size()>0){
                    intent.putExtra("name",fileBeans.get(position).getPDFURL());
                }
                startActivity(intent);
            }
        });
    }


    private void init() {
        law_toolbar_textview = findViewById(R.id.me_law_toolbar_textview);
        listView_law = findViewById(R.id.law_learning_listview);


        law_toolbar_textview.setText("法律法规学习");
    }

    private void initToolbar() {
        law_toolbar = findViewById(R.id.me_law_toolbar);
        law_toolbar.setTitle(" ");//不能去掉 需要把toolbar中心的文字设置为空 才能显示设置好的
        law_toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(law_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示箭头
    }

    private void getLawFiles() {
        promptDialog.showLoading("获取数据中...");
        url = BaseUrl.BaseUrl+"getPDFFile";
        Log.d(TAG,"POST url: "+url);
        Map<String, String> map = new HashMap<>();
        map.put("key","file");
        map.put("key1","file1");

        Log.e(TAG,"map: "+ map.toString());

        String gson = new Gson().toJson(map);
        Log.d(TAG,"gson: "+gson);

        OkHttp okHttp = new OkHttp();
        okHttp.postByget(url, new FileBeanCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Log.e(TAG," "+e.toString());
                promptDialog.dismiss();
            }

            @Override
            public void onResponse(Result<List<FileBean>> response, int id) {

                Log.e(TAG," message: "+response.getMessage());

               if (response.getMessage().equals("获取文件成功")){
                   fileBeans = response.getContent();
                   Log.e(TAG," files: "+ fileBeans.size());
                   datas = new String[fileBeans.size()];
                   for (int i=0;i<fileBeans.size();i++){
                       datas[i] = fileBeans.get(i).getPDFTITLE();
                   }
//                   adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,datas);
//                   listView_law.setAdapter(adapter);

                   listLawAdaper = new ListLawAdaper(getApplicationContext(),fileBeans);
                   listView_law.setAdapter(listLawAdaper);
                   promptDialog.dismiss();

               }else {
                   promptDialog.dismiss();
                   Toast.makeText(getApplicationContext(), "对不起，网络有问题！", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(RESULT_OK);
                this.finish();//back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
