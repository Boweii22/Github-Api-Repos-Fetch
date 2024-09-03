package com.example.githubapi

data class GitHubUser(
    val login: String,
    val id: Int,
    val avatar_url: String,
    val html_url: String,
    val name: String?,
    val bio: String?,
    val public_repos: Int
)
