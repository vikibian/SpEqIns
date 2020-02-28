package com.neu.test.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.neu.test.R;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;

public class SelectCompanyActivity extends AppCompatActivity {

    // 1. 初始化搜索框变量
    private SearchView searchView;
    private ListView listView;
    private String data[] = {"aa","bb","cc","dd","aa","bb","cc","dd","aa","bb","cc","dd","aa","bb","cc","dd"};//假数据


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
                Toast.makeText(SelectCompanyActivity.this,data[position]+" "+position,Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                intent.putExtra("company",data[position]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // 4. 设置点击搜索按键后的操作（通过回调接口）
        // 参数 = 搜索框输入的内容
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
               init();
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

    private void init() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item,data);//新建并配置ArrayAapeter
        listView.setAdapter(adapter);
    }
}
