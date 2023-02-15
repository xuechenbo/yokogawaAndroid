package com.yokogawa.xc.worknet.unretrofit;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 使用这个类可以快速的创建一个全局的Retrofit引用
 * Created by YichenZ on 2016/4/13 14:41.
 * @version 2.0 由单例模式更换为静态类方式
 */
public class APIServiceManager {

    private static Retrofit sRetrofit;

    public static Retrofit build(String url,OkHttpClient okHttpClient){
        if(url == null) {
            new IllegalAccessException("url is not null");
        }
        if(okHttpClient == null){
            new IllegalAccessException("okHttpClient is not null");
        }
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return sRetrofit;
        } else {
            return getRetrofit();
        }
    }

    public static Retrofit getRetrofit() {
        if(sRetrofit==null) {
            new IllegalAccessException("sRetrofit is null");
        }
        return sRetrofit;
    }

    /**
     * 可以设置自己定义的Retrofit
     * @param ret
     */
    public static void setRetrofit(Retrofit ret){
        sRetrofit = ret;
    }

}
