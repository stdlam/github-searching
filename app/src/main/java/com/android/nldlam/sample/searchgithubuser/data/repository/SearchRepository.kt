package com.android.nldlam.sample.searchgithubuser.data.repository

import android.util.Log
import com.android.nldlam.sample.searchgithubuser.data.ResultCallback
import com.android.nldlam.sample.searchgithubuser.data.model.SearchResult
import com.android.nldlam.sample.searchgithubuser.data.remote.ApiService
import com.android.nldlam.sample.searchgithubuser.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class SearchRepository {
    companion object {
        @Volatile
        private var instance: SearchRepository? = null
        fun getInstance(): SearchRepository {
            instance?.let {
                return it
            }
            return synchronized(this) {
                var i2 = instance
                if (i2 != null) {
                    i2
                } else {
                    i2 = SearchRepository()
                    instance = i2
                    i2
                }
            }
        }

        private val TAG = SearchRepository::class.java.simpleName
    }

    private val apiService: ApiService? = ApiUtils.getApiServiceInstance()

    fun queryUser(user: String, page: Int, callback: ResultCallback<MutableList<SearchResult>>) {
        apiService?.let {
            it.searchUser(user, page)?.enqueue(object : Callback<MutableList<SearchResult>> {
                override fun onResponse(call: Call<MutableList<SearchResult>>, response: Response<MutableList<SearchResult>>) {
                    val responseCode = response.code()
                    Log.d(TAG, "queryUser - responseCode=$responseCode")
                    response.body()?.let { results ->
                        Log.d(TAG, "queryUser - result size=${results.size}")
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            callback.onSuccess(results)
                        } else {
                            callback.onError(responseCode)
                        }
                    } ?: kotlin.run {
                        Log.e(TAG, "queryUser - NO RESULT!!!")
                        callback.onError(responseCode)
                    }
                }

                override fun onFailure(call: Call<MutableList<SearchResult>>, t: Throwable) {
                    Log.e(TAG, "queryUser - onFailure - message=${t.message}")
                    callback.onError(HttpURLConnection.HTTP_UNAVAILABLE)
                }

            })
        } ?: kotlin.run {
            callback.onError(HttpURLConnection.HTTP_UNAVAILABLE)
        }
    }
}