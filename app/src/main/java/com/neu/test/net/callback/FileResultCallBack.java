package com.neu.test.net.callback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neu.test.entity.FilePathResult;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

public abstract class FileResultCallBack extends Callback<FilePathResult>
{

  @Override
  public FilePathResult parseNetworkResponse(Response response, int id) throws IOException
  {
    String string = response.body().string();
    FilePathResult items = new Gson().fromJson(string, new TypeToken<FilePathResult>(){}.getType());
    return items;
  }


}
