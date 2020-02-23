package com.neu.test.net.callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

public abstract class ListTaskCallBack extends Callback<Result<List<Task>>>
{

    @Override
    public Result<List<Task>> parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        Result<List<Task>> tasks = new Gson().fromJson(string, new TypeToken<Result<List<Task>>>(){}.getType());
        return tasks;
    }//new TypeToken<Result<List<Task>>>(){}.getType()


}
