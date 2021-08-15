package com.android.nldlam.sample.searchgithubuser.data.repository

import android.util.Log
import com.android.nldlam.sample.searchgithubuser.data.ResultCallback
import com.android.nldlam.sample.searchgithubuser.data.model.User
import com.android.nldlam.sample.searchgithubuser.data.remote.ApiService
import com.android.nldlam.sample.searchgithubuser.ui.detail.DetailViewModel
import com.android.nldlam.sample.searchgithubuser.utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class UserDetailRepository {
    companion object {
        @Volatile
        private var instance: UserDetailRepository? = null
        fun getInstance(): UserDetailRepository {
            instance?.let {
                return it
            }
            return synchronized(this) {
                var i2 = instance
                if (i2 != null) {
                    i2
                } else {
                    i2 = UserDetailRepository()
                    instance = i2
                    i2
                }
            }
        }

        private val TAG = UserDetailRepository::class.java.simpleName
    }

    private val apiService: ApiService? = ApiUtils.getApiServiceInstance()

    fun getRemoteUser(userName: String, callback: ResultCallback<User?>) {
        apiService?.getUser(userName)?.let {
            it.enqueue(object : Callback<User?> {
                override fun onResponse(call: Call<User?>, response: Response<User?>) {
                    val responseCode = response.code()
                    Log.d(TAG, "getRemoteUser - responseCode=$responseCode")
                    response.body()?.let { result ->
                        Log.d(TAG, "getRemoteUser - user=$result")
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            callback.onSuccess(result)
                        } else {
                            callback.onError(responseCode)
                        }
                    } ?: kotlin.run {
                        Log.e(TAG, "getRemoteUser - NO RESULT!!!")
                        callback.onError(responseCode)
                    }
                }

                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Log.e(DetailViewModel.TAG, "executeQuery - onFailure - message=${t.message}")
                    callback.onError(HttpURLConnection.HTTP_UNAVAILABLE)
                }

            })
        } ?: kotlin.run {
            callback.onError(HttpURLConnection.HTTP_UNAVAILABLE)
        }
    }
}