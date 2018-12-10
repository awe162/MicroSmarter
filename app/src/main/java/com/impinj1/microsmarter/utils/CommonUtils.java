package com.impinj1.microsmarter.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CommonUtils {


    public static Executor mThreadPoolExecutor = Executors.newCachedThreadPool();
        //子线程
    public static void runInThread (Runnable r){
        mThreadPoolExecutor.execute(r);
    }
            //handler
    public static Handler handler =new Handler(Looper.getMainLooper());
    public static Handler getHandler (){
        return  handler;
    }

        //主线程
    public  static void runOnMainThread(Runnable r){
        handler.post(r);
    }

    public static Toast toast= null;
    public static Toast longtoast= null;
    public static  void showToast(final Context context , final String text){
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (toast==null){

                   toast= Toast.makeText(context,"", Toast.LENGTH_SHORT);
                }
                toast.setText(text);
                toast.show();
            }
        });
    }


    public static  void LongToast(final Context context , final String text){
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (longtoast==null){

                    longtoast= Toast.makeText(context,"", Toast.LENGTH_LONG);
                }
                longtoast.setText(text);
                longtoast.show();
            }
        });
    }


    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }


    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }


    public static void put(Context context, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        Editor edit = sp.edit();

        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        }

        edit.commit();
    }


    public static void startActivity(Context context, Class clazz) {
        context.startActivity(new Intent(context, clazz));
    }

    public static void putExtraActivity(Context context , Class clazz , String Key , String Value){
        Intent intent = new Intent(context, clazz);
        intent.putExtra(Key,Value);
        context.startActivity(intent);
    }


}
