package com.neu.test.net.callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neu.test.entity.DetailTask;
import com.neu.test.entity.Result;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

public abstract class ListItemsCallBack extends Callback<Result<List<DetailTask>>>
{

    @Override
    public Result<List<DetailTask>> parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
            Result<List<DetailTask>> items = new Gson().fromJson(string, new TypeToken<Result<List<DetailTask>>>(){}.getType());
            return items;
    }


}
