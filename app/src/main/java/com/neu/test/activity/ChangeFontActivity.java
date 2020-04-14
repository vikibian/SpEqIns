package com.neu.test.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;
import com.neu.test.R;
import com.neu.test.layout.RaeSeekBar;
import com.neu.test.util.BaseActivity;

public class ChangeFontActivity extends BaseActivity {

    RaeSeekBar mseekBar;
    TextView tv_message;
    TextView tv_message2;
    TextView tv_message3;
    Toolbar change_font_toolbar;
    int Size;
    SharedPreferences sp;
    private boolean mFontChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fontsizechange);

        initToolbar();

        mseekBar = findViewById(R.id.seekBar);
        tv_message = findViewById(R.id.tv_message);
        tv_message2 = findViewById(R.id.tv_message2);
        tv_message3 = findViewById(R.id.tv_message3);

        sp = getSharedPreferences("Theme", Context.MODE_PRIVATE);

        mseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Size = mseekBar.getRawTextSize(progress);
                Log.e("ssssssize:",Size+"?");
                tv_message.setTextSize(Size);
                tv_message2.setTextSize(Size);
                tv_message3.setTextSize(Size);
                mFontChanged = true;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initToolbar() {
        change_font_toolbar = findViewById(R.id.change_font_toolbar);
        change_font_toolbar.setTitle(" ");
        setSupportActionBar(change_font_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示箭头
    }

    @Override
    public void onBackPressed() {
        int historySize = sp.getInt("fontSize",-1);
        if(mFontChanged){
            if(historySize == Size){
                super.onBackPressed();
            }else{
                SharedPreferences sp = getSharedPreferences("Theme", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                Log.e("sssssssssssize",Size+"");
                switch (Size){
                    case 24:
                        editor.putInt("SizeTheme",R.style.Theme_Large_Media);
                        editor.putInt("fontSize",24);
                        break;
                    case 22:
                        editor.putInt("SizeTheme",R.style.Theme_Large_Small);
                        editor.putInt("fontSize",22);
                        break;
                    case 20:
                        editor.putInt("SizeTheme",R.style.Theme_Large);
                        editor.putInt("fontSize",20);
                        break;
                    case 18:
                        editor.putInt("SizeTheme",R.style.Theme_Medium);
                        editor.putInt("fontSize",18);
                        break;
                    case 16:
                        editor.putInt("SizeTheme",R.style.Theme_Small);
                        editor.putInt("fontSize",16);
                        break;
                }
                editor.commit();
                MessageDialog.build((ChangeFontActivity.this))
                        .setStyle(DialogSettings.STYLE.STYLE_IOS)
                        .setTitle("提示")
                        .setMessage("新的字体大小，需要重启才能生效，是否重启")
                        .setOkButton("确定", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                Intent intent =getPackageManager().getLaunchIntentForPackage(getPackageName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                return false;
                            }
                        })
                        .setCancelButton("取消", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                ChangeFontActivity.super.onBackPressed();
                                return false;
                            }
                        })
                        .show();
            }
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();//back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
