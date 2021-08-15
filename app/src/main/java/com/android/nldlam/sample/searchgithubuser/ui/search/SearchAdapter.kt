package com.android.nldlam.sample.searchgithubuser.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.nldlam.sample.searchgithubuser.R
import com.android.nldlam.sample.searchgithubuser.data.model.SearchResult
import com.bumptech.glide.Glide

class SearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mData = mutableListOf<SearchResult>()
    private var mCallback: ItemClick? = null

    fun setData(data: MutableList<SearchResult>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    fun setCallback(callback: ItemClick) {
        mCallback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ResultViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (itemCount > position && position != RecyclerView.NO_POSITION) {
            (holder as ResultViewHolder).build(mData[position])
            holder.itemView.setOnClickListener {
                val item = mData[position]
                mCallback?.onClicked(item.login, item.avatarUrl)
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ResultViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        private val tvAccount: TextView = view.findViewById(R.id.tvAccount)
        private val context = view.context

        fun build(result: SearchResult) {
            Glide.with(context).load(result.avatarUrl).into(ivAvatar)
            tvAccount.text = result.login
        }
    }

    interface ItemClick {
        fun onClicked(userName: String, avatarUrl: String)
    }
}