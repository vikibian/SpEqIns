package com.neu.test.entity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Type;

public  class TypeResultDeserializer implements JsonDeserializer<List<BaseTshcedule>> {
  @Override
  public List<BaseTshcedule> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

    JsonArray array = json.getAsJsonArray();
    List<BaseTshcedule> list = new ArrayList<>();
    for (JsonElement jsonElement : array) {
      String type = jsonElement.getAsJsonObject().get("TASKTYPE").getAsString();
      if (type.equals("日常")) {
        list.add(new Gson().fromJson(jsonElement, DailySchedule.class));
      } else if (type.equals("专项")) {
        list.add(new Gson().fromJson(jsonElement, SpecialSchedule.class));
      }
    }
      return list;
  }
}
