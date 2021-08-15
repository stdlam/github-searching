package com.android.nldlam.sample.searchgithubuser.ui.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.nldlam.sample.searchgithubuser.R
import com.android.nldlam.sample.searchgithubuser.SearchGithubApp
import com.android.nldlam.sample.searchgithubuser.data.ResultCallback
import com.android.nldlam.sample.searchgithubuser.data.model.User
import com.android.nldlam.sample.searchgithubuser.data.repository.UserDetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.lang.Exception
import java.net.HttpURLConnection

class DetailViewModel : ViewModel() {
    companion object {
        val TAG = DetailViewModel::class.simpleName
        private const val QUERY_TIMEOUT = 5000L
    }

    private val mUIStateLiveData: MutableLiveData<UIState> = MutableLiveData()
    private val mUserDetailRepository = UserDetailRepository.getInstance()
    private val mApp = SearchGithubApp.instance

    fun getUIState() = mUIStateLiveData

    fun getRemoteUser(url: String) {
        viewModelScope.launch {
            emitData(UIState(isLoading = true))
            try {
                withTimeout(QUERY_TIMEOUT) {
                    withContext(Dispatchers.IO) {
                        mUserDetailRepository.getRemoteUser(url, object : ResultCallback<User?> {
                            override fun onError(errorCode: Int) {
                                when (errorCode) {
                                    HttpURLConnection.HTTP_NOT_FOUND -> {
                                        emitData(
                                            UIState(
                                                isLoading = false,
                                                errorMessage = mApp.getString(R.string.data_not_found)
                                            )
                                        )
                                    }
                                    HttpURLConnection.HTTP_BAD_REQUEST -> {
                                        emitData(
                                            UIState(
                                                isLoading = false,
                                                errorMessage = mApp.getString(R.string.no_internet)
                                            )
                                        )
                                    }
                                    HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> {
                                        emitData(
                                            UIState(
                                                isLoading = false,
                                                errorMessage = mApp.getString(R.string.timeout)
                                            )
                                        )
                                    }
                                    else -> {
                                        emitData(
                                            UIState(
                                                isLoading = false,
                                                errorMessage = mApp.getString(R.string.something_wrong)
                                            )
                                        )
                                    }
                                }
                            }

                            override fun onSuccess(result: User?) {
                                Log.d(TAG, "getRemoteUser - user=$result")
                                emitData(UIState(isLoading = false, response = result))
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
                       var response: User? = null,
                       var errorMessage: String? = null)
}