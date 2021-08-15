package com.android.nldlam.sample.searchgithubuser.data.remote

import android.content.Context
import com.android.nldlam.sample.searchgithubuser.data.SearchResultDeserializer
import com.android.nldlam.sample.searchgithubuser.data.UserDeserializer
import com.android.nldlam.sample.searchgithubuser.data.model.SearchResult
import com.android.nldlam.sample.searchgithubuser.data.model.User
import com.android.nldlam.sample.searchgithubuser.utils.NetworkUtils.hasNetworkAvailable
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
    companion object {
        private var retrofit: Retrofit? = null

        @Synchronized
        fun getRetrofitClient(context: Context, baseUrl: String): Retrofit? {
            if (retrofit == null) {
                val cacheSize = (5 * 1024 * 1024).toLong()      // 5 MB
                val cache = Cache(context.cacheDir, cacheSize)
                val okHttpClient = OkHttpClient.Builder()
                    .cache(cache)
                    .addNetworkInterceptor { chain ->
                        val response = chain.proceed(chain.request())
                        val cacheControl = CacheControl.Builder()
                            .maxAge(10, TimeUnit.DAYS)
                            .build()
                        response.newBuilder()
                            .header("Cache-Control", cacheControl.toString())
                            .build()
                    }
                    .addInterceptor { chain ->
                        val builder = chain.request().newBuilder()
                        if (!context.hasNetworkAvailable()) {
                            builder.cacheControl(CacheControl.FORCE_CACHE)
                        }
                        chain.proceed(builder.build())
                    }
                    .build()

                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setLenient()
                        .registerTypeAdapter(object : TypeToken<MutableList<SearchResult>>() {}.type, SearchResultDeserializer())
                        .create()))
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setLenient()
                        .registerTypeAdapter(object : TypeToken<User?>() {}.type, UserDeserializer())
                        .create()))
                    .client(okHttpClient)
                    .build()
            }
            return retrofit
        }
    }
}