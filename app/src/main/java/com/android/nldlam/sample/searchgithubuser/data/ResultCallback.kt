package com.android.nldlam.sample.searchgithubuser.data

interface ResultCallback <T> {
    fun onSuccess(result: T)
    fun onError(errorCode: Int)
}