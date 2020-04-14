package com.neu.test.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.neu.test.R;
import com.neu.test.entity.DetectionItem;
import com.neu.test.util.BaseActivity;

import java.util.List;

public class ResultActivity extends BaseActivity {

    List<DetectionItem> resultData;
    private ScrollView sv_result;
    private Button btn_finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        sv_result = findViewById(R.id.sv_result);
        btn_finish = findViewById(R.id.btn_finish);

        resultData = (List<DetectionItem>) getIntent().getSerializableExtra("listData");
        new Thread(new Runnable() {
            @Override
            public void run() {
                sv_result.post(new Runnable() {
                    @Override
                    public void run() {
                        for ( int i = 0 ;i<resultData.size();i++){
                            //System.out.println(resultData.get(i).getItemContent());
                            TextView tv = new TextView(ResultActivity.this);
                            tv.setText(resultData.get(i).getItemContent()+"\n"+resultData.get(i).getResultStatus());
//                            tv.setText(resultData.get(i).getCHECKCONTENT()+"\n"); //+resultData.get(i).getResultStatus());
                            sv_result.addView(tv);
                        }
                    }
                });

            }
        }).start();

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }
}
