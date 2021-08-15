package com.android.nldlam.sample.searchgithubuser.data.remote

import com.android.nldlam.sample.searchgithubuser.data.model.SearchResult
import com.android.nldlam.sample.searchgithubuser.data.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun searchUser(@Query("q") user: String, @Query("page") page: Int) : Call<MutableList<SearchResult>>?

    @GET("users/{username}")
    fun getUser(@Path("username") userName: String) : Call<User?>?
}