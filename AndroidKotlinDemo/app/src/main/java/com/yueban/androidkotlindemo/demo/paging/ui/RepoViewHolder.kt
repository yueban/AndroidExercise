package com.yueban.androidkotlindemo.demo.paging.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yueban.androidkotlindemo.R
import com.yueban.androidkotlindemo.demo.paging.model.entity.Repo

/**
 * @author yueban
 * @date 2018/12/10
 * @email fbzhh007@gmail.com
 */
class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.repo_name)
    private val description: TextView = view.findViewById(R.id.repo_description)
    private val stars: TextView = view.findViewById(R.id.repo_stars)
    private val language: TextView = view.findViewById(R.id.repo_language)
    private val forks: TextView = view.findViewById(R.id.repo_forks)

    private var repo: Repo? = null

    init {
        view.setOnClickListener {
            repo?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(repo: Repo?) {
        if (repo == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            description.visibility = View.GONE
            language.visibility = View.GONE
            stars.text = resources.getString(R.string.unknown)
            forks.text = resources.getString(R.string.unknown)
        } else {
            showRepoData(repo)
        }
    }

    private fun showRepoData(repo: Repo) {
        this.repo = repo
        name.text = repo.fullName
        if (repo.description != null) {
            description.text = repo.description
            description.visibility = View.VISIBLE
        } else {
            description.visibility = View.GONE
        }

        stars.text = repo.stars.toString()
        forks.text = repo.forks.toString()

        if (repo.language.isNullOrEmpty()) {
            language.visibility = View.GONE
        } else {
            val resources = this.itemView.context.resources
            language.text = resources.getString(R.string.language, repo.language)
            language.visibility = View.VISIBLE
        }
    }

    companion object {
        fun create(parent: ViewGroup): RepoViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_view_item, parent, false)
            return RepoViewHolder(view)
        }
    }
}