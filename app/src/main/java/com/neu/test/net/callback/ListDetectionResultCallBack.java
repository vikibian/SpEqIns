package com.neu.test.net.callback;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neu.test.entity.DetectionItem;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Result;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Response;

/**
 * created by Viki on 2020/2/27
 * system login name : lg
 * created time : 16:20
 * email : 710256138@qq.com
 */
public abstract class ListDetectionResultCallBack extends Callback<Result<List<DetectionResult>>> {

    @Override
    public Result<List<DetectionResult>> parseNetworkResponse(Response response, int i) throws Exception {
        String string = response.body().string();
        Log.e("ListDetectionallBack","  "+string.toString());
        Result<List<DetectionResult>> items = new Gson().fromJson(string, new TypeToken< Result<List<DetectionResult>>>(){}.getType());
        return items;
    }
}
