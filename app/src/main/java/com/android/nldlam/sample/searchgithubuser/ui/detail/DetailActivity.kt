package com.android.nldlam.sample.searchgithubuser.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.nldlam.sample.searchgithubuser.R
import com.android.nldlam.sample.searchgithubuser.data.model.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_USER_NAME = "EXTRA_USER_NAME"
        private const val EXTRA_AVATAR_URL = "EXTRA_AVATAR_URL"
        fun start(context: Context, userName: String, avatarUrl: String) {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(EXTRA_USER_NAME, userName)
                putExtra(EXTRA_AVATAR_URL, avatarUrl)
            }
            context.startActivity(intent)
        }
    }

    private var mAvatarUrl = ""
    private lateinit var mViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        mViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        observeData()
        intent?.let {
            val userName = it.getStringExtra(EXTRA_USER_NAME) ?: ""
            mAvatarUrl = it.getStringExtra(EXTRA_AVATAR_URL) ?: ""
            if (userName.isNotBlank()) {
                mViewModel.getRemoteUser(userName)
            } else {
                Toast.makeText(this, getString(R.string.something_wrong), Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun observeData() {
        mViewModel.getUIState().observe(this, {
            it.errorMessage?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
            it.isLoading?.let { isLoading ->
                when {
                    isLoading -> {
                        Glide.with(this).load(R.drawable.loading).into(ivAvatar)
                    }
                    mAvatarUrl.isNotBlank() -> {
                        Glide.with(this).load(mAvatarUrl).into(ivAvatar)
                    }
                    else -> {
                        Glide.with(this).load(R.drawable.ic_account_circle).into(ivAvatar)
                    }
                }
            }
            it.response?.let { user ->
                loadUserInfo(user)
            }
        })
    }

    private fun loadUserInfo(user: User) {
        tvBio.text = user.bio
        tvCompany.text = user.company
        tvFollowers.text = user.followers.toString()
        tvFollowing.text = user.following.toString()
        tvName.text = user.name
        tvRepos.text = String.format(getString(R.string.repos), user.publicRepos)
    }

    override fun onBackPressed() {
        finish()
    }
}