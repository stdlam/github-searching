package com.android.nldlam.sample.searchgithubuser.data.model

data class User(
    val name: String,
    val company: String,
    val location: String,
    val bio: String,
    val publicRepos: Int,
    val followers: Int,
    val following: Int
)
