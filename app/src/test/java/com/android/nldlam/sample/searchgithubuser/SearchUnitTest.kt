package com.android.nldlam.sample.searchgithubuser

import com.android.nldlam.sample.searchgithubuser.data.model.SearchResult
import com.android.nldlam.sample.searchgithubuser.data.remote.ApiService
import okhttp3.ResponseBody
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUnitTest {
    @Test
    fun testSearchResponse_success() {
        val mockInterface = Mockito.mock(ApiService::class.java)
        val mockCall: Call<MutableList<SearchResult>> = Mockito.mock(Call::class.java) as Call<MutableList<SearchResult>>
        val mockCallResult: Callback<MutableList<SearchResult>> = Mockito.mock(Callback::class.java) as Callback<MutableList<SearchResult>>

        Mockito.`when`(mockInterface.searchUser("nldlam", 1)).thenReturn(mockCall)
        Mockito.doAnswer { invocation ->
            val callback: Callback<MutableList<SearchResult>> = invocation.getArgument(0, Callback::class.java) as Callback<MutableList<SearchResult>>
            callback.onResponse(mockCall, Response.success(mutableListOf()))
            null
        }.`when`(mockCall).enqueue(mockCallResult)
    }

    @Test
    fun testSearchResponse_not_found() {
        val mockInterface = Mockito.mock(ApiService::class.java)
        val mockCall: Call<MutableList<SearchResult>> = Mockito.mock(Call::class.java) as Call<MutableList<SearchResult>>
        val mockCallResult: Callback<MutableList<SearchResult>> = Mockito.mock(Callback::class.java) as Callback<MutableList<SearchResult>>
        val mockBody: Response<MutableList<SearchResult>> = Mockito.mock(Response::class.java) as Response<MutableList<SearchResult>>

        Mockito.`when`(mockInterface.searchUser("    ", 1)).thenReturn(mockCall)
        Mockito.doAnswer { invocation ->
            val callback: Callback<MutableList<SearchResult>> = invocation.getArgument(0, Callback::class.java) as Callback<MutableList<SearchResult>>
            callback.onResponse(mockCall, Response.error(404, mockBody.body() as ResponseBody))
            null
        }.`when`(mockCall).enqueue(mockCallResult)
    }
}