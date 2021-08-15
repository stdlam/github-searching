package com.android.nldlam.sample.searchgithubuser.utils

import com.android.nldlam.sample.searchgithubuser.SearchGithubApp
import com.android.nldlam.sample.searchgithubuser.constant.Constant
import com.android.nldlam.sample.searchgithubuser.data.remote.ApiService
import com.android.nldlam.sample.searchgithubuser.data.remote.RetrofitClient

object ApiUtils {
    fun getApiServiceInstance(): ApiService? {
        return RetrofitClient.getRetrofitClient(SearchGithubApp.instance, Constant.BASE_GITHUB_API)?.create(ApiService::class.java)
    }
}