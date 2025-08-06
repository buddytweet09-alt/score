package com.footballscore.liveapp.network;

import com.footballscore.liveapp.models.Match;
import com.footballscore.liveapp.models.MatchResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static final String BASE_URL = "https://livescore-real-time.p.rapidapi.com/";
    private static final String API_KEY = "0bc27d54f4msh5f440545a40f51bp14a63ajsn77b3a95243de";
    private static final String API_HOST = "livescore-real-time.p.rapidapi.com";

    private static Retrofit retrofit = null;
    private static ApiService apiService = null;

    public static ApiService getApiService() {
        if (apiService == null) {
            retrofit = getRetrofitInstance();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Create logging interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Create OkHttp client with headers
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("X-RapidAPI-Key", API_KEY)
                                .header("X-RapidAPI-Host", API_HOST)
                                .header("Content-Type", "application/json");
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    })
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            // Create custom Gson with deserializers
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(MatchResponse.class, new MatchResponse.MatchResponseDeserializer())
                    .registerTypeAdapter(Match.class, new Match.MatchDeserializer())
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}