package com.neu.test.net.callback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.neu.test.entity.BaseTshcedule;
import com.neu.test.entity.Result;
import com.neu.test.entity.ResultForTaskAndUser;
import com.neu.test.entity.TypeResultDeserializer;
import com.neu.test.entity.User;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public abstract class ListTcheduleCallBack extends Callback<Result<List<BaseTshcedule>>>
{

    @Override
    public Result<List<BaseTshcedule>> parseNetworkResponse(Response response, int id) throws IOException
    {
        String string = response.body().string();

      GsonBuilder gsonb = new GsonBuilder();
      gsonb.registerTypeAdapter(new TypeToken<ArrayList<BaseTshcedule>>() {}.getType(), new TypeResultDeserializer());
      gsonb.serializeNulls();
      Gson gson = gsonb.create();
      Result<List<BaseTshcedule>> list = gson.fromJson(string, new TypeToken<Result<ArrayList<BaseTshcedule>>>() {}.getType());
      return list;
    }


}
