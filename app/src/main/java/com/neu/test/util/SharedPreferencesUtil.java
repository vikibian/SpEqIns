package com.neu.test.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.kongzue.dialog.v3.TipDialog;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Task;

import java.lang.reflect.Type;
import java.util.List;

/**
 * created by Viki on 2020/5/6
 * system login name : lg
 * created time : 15:51
 * email : 710256138@qq.com
 */
public class SharedPreferencesUtil {

    private static final String TAG = "SharedPreferencesUtil";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    public static final String TASKSHARE = "ReDraftActivityForTask";
    public static final String DETECTIONRESULTSHARE = "ReDraftActivityForDetectionResult";

    public static void saveTasks(Context context, List<Task> tasksList){
        sharedPreferences = context.getSharedPreferences(TASKSHARE,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.remove("tasks");
        String string = new Gson().toJson(tasksList);
        Log.e(TAG, "onClick: string"+string );
        editor.putString("tasks",string);
        editor.commit();
    }

    public static void saveDetectionResults(Context context,  List<List<DetectionResult>> detectionResults){
        sharedPreferences = context.getSharedPreferences(DETECTIONRESULTSHARE,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String detectionString =  new Gson().toJson(detectionResults);
        editor.putString("detectionResultsLists",detectionString);
        editor.commit();
    }

    public static List<Task> getTaskList(Context context, Type type){
        List<Task> taskList;
        sharedPreferences = context.getSharedPreferences(TASKSHARE,Context.MODE_PRIVATE);
        String taskstring = sharedPreferences.getString("tasks", "[]");
        taskList = new Gson().fromJson(taskstring, type);

        return taskList;
    }

    public static List<List<DetectionResult>> getDetectionResultLists(Context context,Type type){
        List<List<DetectionResult>> detectionResultLists;
        sharedPreferences = context.getSharedPreferences(DETECTIONRESULTSHARE,Context.MODE_PRIVATE);
        String string = sharedPreferences.getString("detectionResultsLists","[]");
        detectionResultLists = new Gson().fromJson(string, type);

        return detectionResultLists;
    }
}
