package com.neu.test.net.callback;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neu.test.entity.CheckLists;
import com.neu.test.entity.Result;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import okhttp3.Response;

/**
 * created by Viki on 2020/2/27
 * system login name : lg
 * created time : 16:20
 * email : 710256138@qq.com
 */
public abstract class ListCheckListCallBack extends Callback<Result<List<CheckLists>>> {

    @Override
    public Result<List<CheckLists>> parseNetworkResponse(Response response, int i) throws Exception {
        String string = response.body().string();
        Log.e("ListDetectionallBack","  "+string.toString());
        Result<List<CheckLists>> items = new Gson().fromJson(string, new TypeToken<Result<List<CheckLists>>>(){}.getType());
        return items;
    }
}
