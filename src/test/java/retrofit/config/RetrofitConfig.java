package retrofit.config;

import allure.AllureAttachment;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitConfig {
    public static Retrofit buildRetrofitClient() {
        return new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(new OkHttpClient().newBuilder()
                        .addInterceptor(new HttpLoggingInterceptor(s -> AllureAttachment.attachText("basic log", s))
                                .setLevel(HttpLoggingInterceptor.Level.BASIC))
                        .readTimeout(5, TimeUnit.SECONDS)
                        .writeTimeout(5, TimeUnit.SECONDS)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build())
                .build();
    }
}
