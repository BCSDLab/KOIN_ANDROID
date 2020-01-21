package in.koreatech.koin.core.network;

import android.annotation.SuppressLint;
import android.content.Context;

import java.lang.reflect.Field;
import java.net.ConnectException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import in.koreatech.koin.core.BuildConfig;
import in.koreatech.koin.core.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hyerim on 2018. 5. 28....
 */
public class RetrofitManager {
    private static final String TAG = RetrofitManager.class.getSimpleName();

    private static RetrofitManager instance = null;

    public static final String BASE_URL_PRODUCTION = "https://api.koreatech.in"; //release server
    public static final String BASE_URL_STAGE = "http://stage.api.koreatech.in"; //development server

    private Retrofit retrofit;

    private RetrofitManager() {
    }

    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    public void init() {
        retrofit = getDefaultRetrofitSetting();
    }

    private Retrofit getDefaultRetrofitSetting() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        try {
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());
            HostnameVerifier hv = (hostname, session) -> true;
            String workerClassName = "okhttp3.OkHttpClient";
            try {
                Class workerClass = Class.forName(workerClassName);
                Field hostnameVerifier = workerClass.getDeclaredField("hostnameVerifier");
                hostnameVerifier.setAccessible(true);
                hostnameVerifier.set(httpClient, hv);
                Field sslSocketFactory = workerClass.getDeclaredField("sslSocketFactory");
                sslSocketFactory.setAccessible(true);
                sslSocketFactory.set(httpClient, sslContext.getSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.IS_DEBUG) {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL_STAGE)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        } else {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL_PRODUCTION)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
    }

    public Retrofit getRetrofit() {
        if (retrofit != null)
            return retrofit;
        else {
            init();
            return retrofit;
        }
    }

    public static String addAuthorizationBearer(String token) {
        return "Bearer " + token;
    }
}