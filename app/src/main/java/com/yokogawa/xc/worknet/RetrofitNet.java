package com.yokogawa.xc.worknet;

import com.yokogawa.xc.BuildConfig;
import com.yokogawa.xc.MyApplication;
import com.yokogawa.xc.utils.Constant;
import com.yokogawa.xc.utils.LogUtil;
import com.yokogawa.xc.utils.Retrofit2ConverterFactory;
import com.yokogawa.xc.utils.SpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitNet {

    //HTTP FAILED: java.net.SocketTimeoutException: failed to connect to /47.117.119.186 (port 8888) from
    private static final int DEFAULT_TIME_OUT = 20;
    private static final int DEFAULT_READ_TIME_OUT = 25;
    private final Retrofit build;
    private static RetrofitNet retrofitServiceManager;

    public static RetrofitNet getInstance() {
        if (retrofitServiceManager == null) {
            synchronized (RetrofitNet.class) {
                if (retrofitServiceManager == null) {
                    retrofitServiceManager = new RetrofitNet();
                }
            }
        }
        return retrofitServiceManager;
    }


    public void clearRetrofit() {
        retrofitServiceManager = null;
    }

    public RetrofitNet() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(false);//默认重试一次
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间
        builder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);
        //最大连接数
        builder.connectionPool(new ConnectionPool(100,8,TimeUnit.MINUTES));
//        builder.addInterceptor(new RetryIntercepter(1));
//        builder.addInterceptor(new RetryAndFollowUpInterceptor());

        /*HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder() //拦截器
                .build();
        builder.addInterceptor(commonInterceptor);*/
        if (BuildConfig.DEBUG) {//发布版本不再打印  debugb版本打印   日志拦截器
            // 日志显示级别
            HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
            //新建log拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    LogUtil.e("http", "OkHttp====Message:" + message);
                }
            });
            loggingInterceptor.setLevel(level);
            //OkHttp进行添加拦截器loggingInterceptor
            builder.addInterceptor(loggingInterceptor);
        }

        //创建Retrofit
        build = new Retrofit.Builder().client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(new Retrofit2ConverterFactory())
                .baseUrl(SpUtils.getInstance(MyApplication.getInstance()).getBoolean("isNetWorkType", true) ? Constant.BASE_URL : Constant.BASE_URL_TEST)
                .build();
    }

    public GsonImp getApi() {
        return build.create(GsonImp.class);
    }
}
