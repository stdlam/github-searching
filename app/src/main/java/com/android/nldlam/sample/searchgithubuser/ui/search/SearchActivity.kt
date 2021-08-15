package com.android.nldlam.sample.searchgithubuser.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.nldlam.sample.searchgithubuser.R
import com.android.nldlam.sample.searchgithubuser.ui.detail.DetailActivity
import com.android.nldlam.sample.searchgithubuser.utils.NetworkUtils.hasNetworkAvailable
import com.android.nldlam.sample.searchgithubuser.utils.disableButton
import com.android.nldlam.sample.searchgithubuser.utils.enableButton
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), SearchAdapter.ItemClick {
    companion object {
        private val TAG = SearchActivity::class.java.simpleName
    }

    private lateinit var mViewModel: SearchViewModel
    private var mInputText = ""
    private var mAdapter = SearchAdapter()
    private var mPageCounter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        mViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        mAdapter.setCallback(this)
        Log.d(TAG, "onCreate")

        initViews()
        observeData()
    }

    private fun initViews() {
        Glide.with(this).load(R.drawable.loading).into(ivLoading)

        btGet.setOnClickListener {
            if (mInputText.isNotBlank()) mViewModel.searchUser(mInputText, mPageCounter)
            else Toast.makeText(this, getString(R.string.invalid_input), Toast.LENGTH_LONG).show()
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // no need implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // no need implement
            }

            override fun afterTextChanged(s: Editable?) {
                mInputText = s.toString()
            }

        })

        rvResult.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }
    }

    private fun observeData() {
        mViewModel.getUIState().observe(this, {
            it?.errorMessage?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
            it?.isLoading?.let { loading ->
                ivLoading.visibility = if (loading) {
                    btGet.disableButton()
                    View.VISIBLE
                } else {
                    btGet.enableButton()
                    View.GONE
                }
            }
            it?.response?.let { data ->
                Log.d(TAG, "observeData - observed result size=${data.size}")
                mAdapter.setData(data)
            }
        })
    }

    override fun onClicked(userName: String, avatarUrl: String) {
        if (!this.hasNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_LONG).show()
            return
        }
        DetailActivity.start(this, userName, avatarUrl)
    }
}