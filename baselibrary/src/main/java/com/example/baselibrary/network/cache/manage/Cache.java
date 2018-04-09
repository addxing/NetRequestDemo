package com.example.baselibrary.network.cache.manage;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.baselibrary.network.cache.disklrucache.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * DiskLruCache会根据我们在调用open()方法时设定的缓存最大值来自动删除多余的缓存。只有你确定某个key对应的缓存内容已经过期，
 * 需要从网络获取最新数据的时候才应该调用remove()方法来移除缓存。
 */
public class Cache {

    private static Cache instance;
    private final int maxSize = 50;//缓存大小 单位M
    private DiskLruCache diskLruCache;
    private Context context;

    private Cache(Context context){
        this.context = context;
        if(diskLruCache==null){
            try {
                File directory = getCacheDirectory(context);
                int appVersion =  getAppVersion(context);
                diskLruCache = DiskLruCache.open(directory, appVersion, 1, maxSize * 1024 * 1024);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Cache getInstance(Context context){
        if(instance==null){
            synchronized (Cache.class){
                if(instance==null){
                    instance = new Cache(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * 保存Object对象，Object要实现Serializable
     * @param key
     * @param value
     */
    public void put(String key, Object value){
        try {
            key =  toMd5Key(key);
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            if (editor != null) {
                OutputStream os = editor.newOutputStream(0);
                if ( writeObject(os, value)) {
                    editor.commit();
                } else {
                    editor.abort();
                }
                diskLruCache.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存Bitmap
     * @param key
     * @param bitmap
     */
    public void putBitmap(String key, Bitmap bitmap) {
        put(key,  bitmap2Bytes(bitmap));
    }

    /**
     * 保存Drawable
     * @param key
     * @param value
     */
    public void putDrawable(String key, Drawable value) {
        putBitmap(key,  drawable2Bitmap(value));
    }

    /**
     * 根据key获取保存对象
     * @param key
     * @param <T>
     * @return
     */
    public <T> T get(String key){
        try {
            key =  toMd5Key(key);
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);

            if (snapshot != null) {
                InputStream inputStream = snapshot.getInputStream(0);
                return (T) readObject(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Bitmap
     * @param key
     * @return
     */
    public Bitmap getBitmap(String key) {
        byte[] bytes = (byte[]) get(key);
        if (bytes == null) return null;
        return  bytes2Bitmap(bytes);
    }

    /**
     * 获取Drawable
     * @param key
     * @return
     */
    public Drawable getDrawable(String key) {
        byte[] bytes = (byte[]) get(key);
        if (bytes == null) {
            return null;
        }
        return  bitmap2Drawable(context,  bytes2Bitmap(bytes));
    }

    public long size() {
        return diskLruCache.size();
    }

    public void setMaxSize(int maxSize) {
        diskLruCache.setMaxSize(maxSize * 1024 * 1024);
    }

    public File getDirectory() {
        return diskLruCache.getDirectory();
    }

    public long getMaxSize() {
        return diskLruCache.getMaxSize();
    }

    public boolean remove(String key) {
        try {
            key =  toMd5Key(key);
            return diskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void delete(){
        try {
            diskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush(){
        try {
            diskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            diskLruCache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        return diskLruCache.isClosed();
    }
    /**
     * 获取应用版本
     * @param context
     * @return
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取缓存路径
     * @param context
     * @return
     */
    public static File getCacheDirectory(Context context) {
        File cacheDir = new File(context.getExternalCacheDir(), "diskcache");
        if (cacheDir == null) {
            return null;
        }
        if (!cacheDir.mkdirs() && (!cacheDir.exists() || !cacheDir.isDirectory())) {
            return null;
        }
        return cacheDir;
    }

    /**
     * 把key用MD5加密
     * @param key
     * @return
     */
    public static String toMd5Key(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static boolean writeObject(OutputStream fos, Object object) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }

    public static Object readObject(InputStream inputStream) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(inputStream);
            Object object = ois.readObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Bitmap -> byte[]
     * @param bm
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte[] -> Bitmap
     * @param bytes
     * @return
     */
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Drawable -> Bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap -> Drawable
     * @param bm
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Drawable bitmap2Drawable(Context context, Bitmap bm) {
        if (bm == null) {
            return null;
        }
        BitmapDrawable bd = new BitmapDrawable(bm);
        bd.setTargetDensity(bm.getDensity());
        return new BitmapDrawable(context.getResources(), bm);
    }
}
