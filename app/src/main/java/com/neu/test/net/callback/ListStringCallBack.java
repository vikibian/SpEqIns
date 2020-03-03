package com.neu.test.net.callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neu.test.entity.DetectionItem;
import com.neu.test.entity.Result;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import okhttp3.Response;

/**
 * created by Viki on 2020/3/3
 * system login name : lg
 * created time : 13:01
 * email : 710256138@qq.com
 */
public abstract class ListStringCallBack extends Callback<Result<List<String>>> {
    @Override
    public Result<List<String>> parseNetworkResponse(Response response, int i) throws Exception {
        String string = response.body().string();
        Result<List<String>> items = new Gson().fromJson(string, new TypeToken<Result<List<String>>>(){}.getType());
        return items;
    }
}
