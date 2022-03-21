package com.gojek.demo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gojek.demo.R
import com.gojek.demo.data.model.RepoItem
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class RepoListAdapter @Inject constructor(@ActivityContext var context: Context) : RecyclerView.Adapter<RepoListAdapter.RepoViewHolder>() {

    var mRepoList:List<RepoItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item, parent, false)
        return RepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.let { view ->
            val repoItem = mRepoList?.get(position)
                repoItem?.run {

                    //set views data
                    view.textViewOwner.text = owner?.login ?: ""
                    view.textViewRepo.text = description
                    view.html_url.text = html_url
                    view.tv_language.text = language
                    view.tv_fork_count.text = forks_count.toString()
                    view.tv_watcher_count.text = watchers_count

                    //Load image for avatar
                    Glide.with(context)
                        .load(avatar_url)
                        .into(view.imageViewAvatar);
                }
        }
    }

    override fun getItemCount(): Int {
        return mRepoList?.size ?: 0
    }

    fun setDataList(repoList: List<RepoItem>) {
        mRepoList = repoList
    }

    class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewAvatar: ImageView
        val textViewOwner: AppCompatTextView
        val textViewRepo: AppCompatTextView
        val html_url: AppCompatTextView
        val tv_language: AppCompatTextView
        val tv_fork_count: AppCompatTextView
        val tv_watcher_count: AppCompatTextView

        init {
            imageViewAvatar = view.findViewById(R.id.imageViewAvatar)
            textViewOwner = view.findViewById(R.id.textViewOwner)
            textViewRepo = view.findViewById(R.id.textViewRepo)
            html_url = view.findViewById(R.id.html_url)
            tv_language = view.findViewById(R.id.tv_language)
            tv_fork_count = view.findViewById(R.id.tv_fork_count)
            tv_watcher_count = view.findViewById(R.id.tv_watcher_count)
        }
    }

}