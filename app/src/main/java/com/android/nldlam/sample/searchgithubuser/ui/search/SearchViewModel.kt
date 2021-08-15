package com.android.nldlam.sample.searchgithubuser.ui.search

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.*
import com.android.nldlam.sample.searchgithubuser.R
import com.android.nldlam.sample.searchgithubuser.SearchGithubApp
import com.android.nldlam.sample.searchgithubuser.data.ResultCallback
import com.android.nldlam.sample.searchgithubuser.data.repository.SearchRepository
import com.android.nldlam.sample.searchgithubuser.data.model.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.lang.Exception
import java.net.HttpURLConnection

class SearchViewModel : ViewModel() {
    companion object {
        val TAG = SearchViewModel::class.simpleName
        private const val FAKE_LOADING_INTERVAL = 2000L
        private const val QUERY_TIMEOUT = 5000L
    }

    private val mUIStateLiveData: MutableLiveData<UIState> = MutableLiveData()
    private val mSearchRepository = SearchRepository.getInstance()
    private val mHandler = Handler(Looper.getMainLooper())
    private val mApp = SearchGithubApp.instance

    fun getUIState() = mUIStateLiveData

    fun searchUser(user: String, page: Int) {
        emitData(UIState(isLoading = true))
        mHandler.postDelayed({
            executeQuery(user, page)
        }, FAKE_LOADING_INTERVAL)
    }

    private fun executeQuery(user: String, page: Int) {
        Log.d(TAG, "executeQuery - user=$user, page=$page")
        viewModelScope.launch {
            try {
                withTimeout(QUERY_TIMEOUT) {
                    withContext(Dispatchers.IO) {
                        mSearchRepository.queryUser(user, page, object : ResultCallback<MutableList<SearchResult>> {
                            override fun onSuccess(result: MutableList<SearchResult>) {
                                Log.d(TAG, "executeQuery - result size=${result.size}")
                                emitData(UIState(isLoading = false, response = result))
                            }

                            override fun onError(errorCode: Int) {
                                Log.d(TAG, "executeQuery - onError=$errorCode")
                                when (errorCode) {
                                    HttpURLConnection.HTTP_NOT_FOUND -> {
                                        emitData(UIState(isLoading = false, errorMessage = mApp.getString(R.string.data_not_found)))
                                    }
                                    HttpURLConnection.HTTP_BAD_REQUEST -> {
                                        emitData(UIState(isLoading = false, errorMessage = mApp.getString(R.string.no_internet)))
                                    }
                                    HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> {
                                        emitData(UIState(isLoading = false, errorMessage = mApp.getString(R.string.timeout)))
                                    }
                                    else -> {
                                        emitData(UIState(isLoading = false, errorMessage = mApp.getString(R.string.something_wrong)))
                                    }
                                }
                            }
                        })
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "executeQuery - TIMEOUT!!!")
                emitData(UIState(isLoading = false, errorMessage = mApp.getString(R.string.timeout)))
            }
        }
    }

    private fun emitData(uiState: UIState) {
        mUIStateLiveData.postValue(uiState)
    }

    data class UIState(var isLoading: Boolean? = null,
                       var response: MutableList<SearchResult>? = null,
                       var errorMessage: String? = null)
}