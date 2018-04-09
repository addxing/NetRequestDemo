package com.example.baselibrary.network.cookie;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;



import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 使用
 *  builder.cookieJar(new CookieJarImpl(new SPCookieStore(getContext())));
 *  builder.cookieJar(new SaCookieManger(getContext()));
 *
 */

public class SaCookieManger implements CookieJar {

    private static Context mContext;

    public SaCookieManger(Context context) {
        mContext = context;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        loadCookie(cookies,url.host());
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return new ArrayList<>();
    }
    /**
     * 获取cookie
     * @param cookies
     * @param url
     */
    public static void loadCookie(List<Cookie> cookies, String url){
        List<String> convertCookies  = new ArrayList<>();

        for (int i = 0; i < cookies.size(); i++) {
            String temp = cookies.get(i).toString();
            convertCookies.add(temp);
        }
        CookieManager cookieManager= CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        for (String aCookiesArray : convertCookies) {
            cookieManager.setCookie(url, aCookiesArray);
        }
        if (Build.VERSION.SDK_INT <21){
            CookieSyncManager.getInstance().sync();
        }else {
            CookieManager.getInstance().flush();
        }
    }
}
