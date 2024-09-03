package com.example.githubapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
//probably create this in java

interface GitHubApiService {
    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Call<GitHubUser>

    @GET("users/{username}/repos")
    fun getUserRepos(@Path("username") username: String): Call<List<GitHubRepo>>
}
