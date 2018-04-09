package com.example.baselibrary.utils.net;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * 描述：判断网络情况utils
 * 
 * @author lidagang
 * 
 * @ClassName:  com.frame.library.common.net.NetUtils
 * 
 * @date 16/01/13 14:22
 *
 */
public class NetUtils {


    /**
     * 检测wifi是否链接
     */
    public static Boolean IsWifiConnected(Activity mActivity){

        ConnectivityManager connectivityManager = (ConnectivityManager)mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
            return true ;
        }

        return false;
    }

    /**
     * 检测网络是否可用
     * @return
     */
    public static boolean isNetworkConnected(Context mActivity) {
        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    public static int getNetworkType(Context mActivity) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if(!TextUtils.isEmpty(extraInfo)){
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }


}
