package com.example.githubapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RepositoryAdapter(
    private val repositories: List<GitHubRepo>,
    private val onRepoClick: (GitHubRepo) -> Unit
) : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repo = repositories[position]
        holder.repoNameTextView.text = repo.name
        holder.itemView.setOnClickListener { onRepoClick(repo) }
    }

    override fun getItemCount(): Int = repositories.size

    class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val repoNameTextView: TextView = itemView.findViewById(android.R.id.text1)
    }
}
