package com.neu.test.net.callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neu.test.entity.ResultForTaskAndUser;
import com.neu.test.entity.Task;
import com.neu.test.entity.User;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

public abstract class ListTaskAndUserCallBack extends Callback<ResultForTaskAndUser<List<Task>, User>>
{

    @Override
    public ResultForTaskAndUser<List<Task>, User> parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        ResultForTaskAndUser<List<Task>, User> tasks = new Gson().fromJson(string, new TypeToken<ResultForTaskAndUser<List<Task>, User>>(){}.getType());
        return tasks;
    }


}
