package com.example.baselibrary.utils.db;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float, Long类型的参数
 * 同样调用getParam就能获取到保存在手机里面的数据（SharedPreference不适合存储大量数据）
 */
public class SharedPreferencesUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "spf_data";


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context , String key, Object object){

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }

        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }

        return null;
    }

    /**
     * 保存图片对象到SharedPreferences
     * 数据量太大不推荐使用
     * @param key
     * @param bitmap
     */
    public static void setBitmap(String key,Bitmap bitmap,Activity activity){
        //创建preferences对象
        SharedPreferences preferences = activity.getSharedPreferences("base64", activity.MODE_PRIVATE);

        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

            byte[] b = baos.toByteArray();

            // 将字节流编码成base64的字符窜
            String oAuth_Base64 = new String(Base64.encode(b, Base64.DEFAULT));

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, oAuth_Base64);

            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取SharedPreference中的图片对象
     * 数据量太大不推荐使用
     * @param key
     */
    public static Bitmap getBitmap(String key,Activity activity){
        Bitmap bitmap = null;
        //创建preferences对象
        SharedPreferences preferences = activity.getSharedPreferences("base64", activity.MODE_PRIVATE);

        String productBase64 = preferences.getString(key, "");

        //读取字节
        byte[] base64 = Base64.decode(productBase64, Base64.DEFAULT);

        bitmap = BitmapFactory.decodeByteArray(base64, 0, base64.length);

        return bitmap;
    }

    /**
     *  针对复杂对象类型存储,对象要继承自Serializable
     *  数据量太大不推荐使用
     * @param
     * @return
     * @throws
     * @date 16/01/13 14:48
     */
    public static void setSerializable(String key, Object object,Activity activity) {
        if(!(object instanceof Serializable))
        {
            new Exception("传入的对象不是Serializable类型！");
        }

        SharedPreferences preferences = activity.getSharedPreferences("base64", activity.MODE_PRIVATE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {

            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, objectVal);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取SharedPreference中的Serializable对象
     *  数据量太大不推荐使用
     * @param key
     */
    public static Object getSerializable(String key,Activity activity){

        Object obj = null;

        //创建preferences对象
        SharedPreferences preferences = activity.getSharedPreferences("base64", activity.MODE_PRIVATE);

        String productBase64 = preferences.getString(key, "");

        // 对Base64格式的字符串进行解码
        byte[] base64Bytes = Base64.decode(productBase64, Base64.DEFAULT);;

        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);

            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 从ObjectInputStream中读取Product对象
        return obj;
    }
}
