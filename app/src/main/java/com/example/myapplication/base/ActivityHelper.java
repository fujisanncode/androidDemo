package com.example.myapplication.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityHelper {
    private static List<Activity> list = new ArrayList<>();

    public static void add(Activity activity){
        list.add(activity);
    }

    public static void remove(Activity activity){
        list.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity : list) {
            if(!activity.isFinishing()){
                activity.finish();
            }
        }

    }
}
