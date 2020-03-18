package com.neu.test.net.callback;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.FileBean;
import com.neu.test.entity.Result;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import okhttp3.Response;

/**
 * created by Viki on 2020/3/11
 * system login name : lg
 * created time : 18:38
 * email : 710256138@qq.com
 */
public abstract class FileBeanCallBack extends Callback<Result<List<FileBean>>> {

    @Override
    public Result<List<FileBean>> parseNetworkResponse(Response response, int i) throws Exception {
        String string = response.body().string();
        Result<List<FileBean>> items = new Gson().fromJson(string, new TypeToken< Result<List<FileBean>>>(){}.getType());
        return items;
    }
}
