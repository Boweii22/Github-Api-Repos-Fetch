package com.example.githubapi

data class GitHubRepo(
    val id: Int,
    val name: String,
    val html_url: String,
    val description: String?
)
