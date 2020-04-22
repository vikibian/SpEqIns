package com.neu.test.net.callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neu.test.entity.EmailInfo;
import com.neu.test.entity.Result;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * created by Viki on 2020/4/21
 * system login name : lg
 * created time : 18:18
 * email : 710256138@qq.com
 */
public abstract class EmailInfoCallBack extends Callback<Result<EmailInfo>> {
    @Override
    public Result<EmailInfo> parseNetworkResponse(Response response, int i) throws Exception {
        String string = response.body().string();
        Result<EmailInfo> items = new Gson().fromJson(string, new TypeToken<Result<EmailInfo>>(){}.getType());
        return items;
    }
}
