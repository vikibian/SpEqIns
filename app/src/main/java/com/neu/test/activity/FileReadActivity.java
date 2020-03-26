package com.neu.test.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.barteksc.pdfviewer.PDFView;
import com.neu.test.R;
import com.neu.test.net.OkHttp;
import com.neu.test.util.BaseUrl;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

public class FileReadActivity extends AppCompatActivity {
    private static String TAG = "FileReadActivity";
    private PDFView pdfView;
    private String fileName;
    private String uri;
    private TextView loading_name;

    private NumberProgressBar numberProgressBar;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_read);

        Intent intent = getIntent();
        fileName = intent.getStringExtra("name");

        pdfView = findViewById(R.id.pdfView);
        numberProgressBar = findViewById(R.id.fileread_number_progress_bar);
        linearLayout = findViewById(R.id.linearlayout_fileread);
        loading_name = findViewById(R.id.file_read_loading_name);
        loading_name.setText(fileName);
        uri = BaseUrl.lawFilePath+fileName;

//        if (!fileUrl.equals(null)){
//            pdfView
//                    .fromUri(Uri.parse(uri))
//                    .load();
//        }

        String folderPath = BaseUrl.absolutePath+"/DCIM/"+LoginActivity.inputName+"/PDFDownloads";
        File folder = new File(folderPath);

        if (folder.exists()){
            File file = new File(folderPath+"/"+fileName);
            if (file.exists()){
                loadPdfFile(file);
                //如果文件存在不让加载条显示
                linearLayout.setVisibility(View.INVISIBLE);
            }else {
                downloadAndRead(uri,folderPath,fileName);
            }
        }else {
            folder.mkdir();
            downloadAndRead(uri, folderPath, fileName);
        }




    }

    private void downloadAndRead(String uri, String folderPath, String name) {
        OkHttp okHttp = new OkHttp();
        okHttp.downloadFile(uri, new FileCallBack(folderPath,name) {

            @Override
            public void inProgress(float progress, long total, int id) {
                numberProgressBar.setProgress((int) (progress*100));
                if (progress == 1){
                    linearLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                Log.e(TAG," error: "+e.toString());
            }

            @Override
            public void onResponse(File file, int i) {
                loadPdfFile(file);
            }
        });
    }

    private void loadPdfFile(File file) {
        pdfView.fromFile(file).load();
    }
}
