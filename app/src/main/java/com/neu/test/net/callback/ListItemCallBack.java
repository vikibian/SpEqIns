package com.neu.test.net.callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neu.test.entity.DetectionItem;
import com.neu.test.entity.Result;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

public abstract class ListItemCallBack extends Callback<Result<List<DetectionItem>>>
{

    @Override
    public Result<List<DetectionItem>> parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();
            Result<List<DetectionItem>> items = new Gson().fromJson(string, new TypeToken<Result<List<DetectionItem>>>(){}.getType());
            return items;
    }


}
