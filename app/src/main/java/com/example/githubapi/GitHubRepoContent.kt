package com.example.githubapi

data class GitHubRepoContent(
    val name: String,
    val path: String,
    val type: String,
    val download_url: String?
)
