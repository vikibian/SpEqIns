package com.neu.test.net;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;



public class OkHttp {
    private static String TAG = "OkHttp";


    /**
     * 使用okhttp-utils的post请求网络文本数据 使用postString函数
     */
    public void postBypostString(String url, JSONObject jsonObject, StringCallback stringCallback) {
        //String url = "http://www.zhiyun-tech.com/App/Rider-M/changelog-zh.txt";
        //url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";

        OkHttpUtils
                .postString()
                .url(url)
                .id(100)
                .content(jsonObject.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(stringCallback);
        Log.d(TAG,"Okhttp: --> postString");

    }


    /**
     * 使用okhttp-utils的post请求网络文本数据 使用post函数
     */
    public void postBypost(String url, JSONObject jsonObject, StringCallback stringCallback) {
        //String url = "http://www.zhiyun-tech.com/App/Rider-M/changelog-zh.txt";
        //url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";

        OkHttpUtils
                .post()
                .url(url)
                .id(100)
                .build()
                .execute(stringCallback);
        Log.d(TAG,"Okhttp: --> post");

    }


}
