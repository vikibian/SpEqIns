package com.neu.test.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.neu.test.R;
import com.neu.test.adapter.RectifyListItemAdapter;
import com.neu.test.entity.DetectionResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RectifyActivity extends AppCompatActivity {
    private static final String TAG = "RectifyActivity";
    private final int RectifyItem = 1650;
    private ListView listView;
    private Button btn_retify_save;
    private RectifyListItemAdapter rectifyListItemAdapter;
    Intent intent ;
    String[] num = {"1","2","3","4","5"};
    private List<DetectionResult> detectionResultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectify);

        listView = findViewById(R.id.rectify_listview);
        btn_retify_save = findViewById(R.id.btn_retify_save);
        intent = getIntent();
        detectionResultList = (List<DetectionResult>) intent.getSerializableExtra("errorList");
        rectifyListItemAdapter = new RectifyListItemAdapter(getApplicationContext(), num, detectionResultList);
        listView.setAdapter(rectifyListItemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), position + " ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RectifyActivity.this, RectifyItemActivity.class);
                intent.putExtra("errorResult", (Serializable) detectionResultList.get(position));
                intent.putExtra("position", position);
                startActivityForResult(intent, RectifyItem);
            }
        });
        btn_retify_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("errorList", (Serializable) detectionResultList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK){
                if (requestCode == RectifyItem){
                    DetectionResult detectionResult = (DetectionResult) data.getSerializableExtra("errorResult");
                    int position = data.getIntExtra("position",0);
                    detectionResultList.get(position).setISCHANGED(detectionResult.getISCHANGED());
                    Log.e("tttest",detectionResultList.get(position).getISCHANGED());
                    rectifyListItemAdapter = new RectifyListItemAdapter(getApplicationContext(),num,detectionResultList);
                    listView.setAdapter(rectifyListItemAdapter);

                }
            }
        }
}
