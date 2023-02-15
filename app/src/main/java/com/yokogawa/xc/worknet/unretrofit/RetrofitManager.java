package com.yokogawa.xc.worknet.unretrofit;



import com.yokogawa.xc.BuildConfig;
import com.yokogawa.xc.utils.Constant;
import com.yokogawa.xc.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by android on 2017/3/30.
 */

public class RetrofitManager {
    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;
    private RetrofitManager(){

    }
    public static Retrofit getInstance() {
        if (APIServiceManager.getRetrofit() == null){
            if (okHttpClient == null)
            {
                synchronized (RetrofitManager.class)
                {
                    if (okHttpClient == null)
                    {
                        okHttpClient  = new OkHttpClient.Builder()
                                .addNetworkInterceptor(new Interceptor() {
                                    @Override
                                    public Response intercept(Chain chain) throws IOException {
                                        Request request = chain.request().newBuilder()
                                                .addHeader("Connection", "close")
                                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                                .build();
                                        return chain.proceed(request);
                                    }
                                })
                                .connectTimeout(30, TimeUnit.SECONDS)
                                .writeTimeout(30, TimeUnit.SECONDS)
                                .readTimeout(30, TimeUnit.SECONDS)
                                .build();
                    }
                }
            }
//            //处理网络请求的日志拦截输出
//            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor("logMsg");
//            httpLoggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
//            okHttpClient.newBuilder().addInterceptor(httpLoggingInterceptor);
            if (BuildConfig.DEBUG) {//发布版本不再打印  debugb版本打印   日志拦截器
                // 日志显示级别
                HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
                //新建log拦截器
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        LogUtil.e("test", "OkHttp====Message:" + message);
                    }
                });
                loggingInterceptor.setLevel(level);
                //OkHttp进行添加拦截器loggingInterceptor
                okHttpClient.newBuilder().addInterceptor(loggingInterceptor);
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        } else {
           retrofit =  APIServiceManager.getRetrofit();
        }
        return retrofit;

    }



}
