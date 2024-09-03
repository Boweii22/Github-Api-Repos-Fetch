package com.example.githubapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RepositoryContentAdapter(
    private val contents: List<GitHubRepoContent>,
    private val onItemClick: (GitHubRepoContent) -> Unit
) : RecyclerView.Adapter<RepositoryContentAdapter.ContentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_repo_content, parent, false)
        return ContentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val content = contents[position]
        holder.itemNameTextView.text = content.name
        holder.itemView.setOnClickListener { onItemClick(content) }
    }

    override fun getItemCount(): Int = contents.size

    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
    }
}
