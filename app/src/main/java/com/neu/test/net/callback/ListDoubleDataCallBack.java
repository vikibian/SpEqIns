package com.neu.test.net.callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neu.test.entity.DoubleData;
import com.neu.test.entity.ResultForTaskAndUser;
import com.neu.test.entity.Task;
import com.neu.test.entity.User;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public abstract class ListDoubleDataCallBack extends Callback<DoubleData<ArrayList<String>, ArrayList<String>>>
{

    @Override
    public DoubleData<ArrayList<String>, ArrayList<String>> parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
        DoubleData<ArrayList<String>, ArrayList<String>> tasks = new Gson().fromJson(string, new TypeToken<DoubleData<ArrayList<String>, ArrayList<String>>>(){}.getType());
        return tasks;
    }


}
