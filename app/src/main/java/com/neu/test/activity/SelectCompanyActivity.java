package com.neu.test.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.Result;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListStringCallBack;
import com.neu.test.util.BaseUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;

public class SelectCompanyActivity extends AppCompatActivity {
    private static String TAG = "SelectCompany";

    // 1. 初始化搜索框变量
    private SearchView searchView;
    private ListView listView;
//    private String data[] = {"aa","bb","cc","dd","aa","bb","cc","dd","aa","bb","cc","dd","aa","bb","cc","dd"};//假数据
    private String url;
    private List<String> unitname = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_company);

        // 3. 绑定组件
        searchView = (SearchView) findViewById(R.id.search_view);
        listView = findViewById(R.id.select_company_listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SelectCompanyActivity.this,unitname.get(position)+" "+position,Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                intent.putExtra("company",unitname.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // 4. 设置点击搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                Log.e(TAG," "+string);
               getUnitName(string);
//               init(unitname);
            }
        });

        // 5. 设置点击返回按键后的操作（通过回调接口）
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });
    }

    private void getUnitName(String string) {
        url = BaseUrl.BaseUrl+"getUnitName";
        Log.d(TAG,"POST url: "+url);
        Map<String, String> map = new HashMap<>();
        map.put("unitname",string);
        Log.e(TAG,"map: "+ map.toString());


        String gson = new Gson().toJson(map);
        Log.e(TAG,"gson: "+ gson);

        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, gson, new ListStringCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(Result<List<String>> reponse, int i) {
                if (reponse.getMessage().equals("操作成功")){
                    unitname = reponse.getContent();
                    init(unitname);
                }
            }
        });
    }

    private void init(List<String> unitname) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, unitname);//新建并配置ArrayAapeter
        listView.setAdapter(adapter);
    }
}
