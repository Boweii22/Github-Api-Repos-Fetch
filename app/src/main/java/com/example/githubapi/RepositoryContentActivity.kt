package com.example.githubapi

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RepositoryContentActivity : AppCompatActivity() {

    private lateinit var githubApiService: GitHubApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_content)

        val repoOwner = intent.getStringExtra("REPO_OWNER")
        val repoName = intent.getStringExtra("REPO_NAME")

        if (repoOwner != null && repoName != null) {
            // Set up Retrofit
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            githubApiService = retrofit.create(GitHubApiService::class.java)

            fetchRepoContents(repoOwner, repoName)
        } else {
            Toast.makeText(this, "Repository not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun fetchRepoContents(owner: String, repo: String) {
        githubApiService.getRepoContents(owner, repo, "").enqueue(object : Callback<List<GitHubRepoContent>> {
            override fun onResponse(call: Call<List<GitHubRepoContent>>, response: Response<List<GitHubRepoContent>>) {
                if (response.isSuccessful) {
                    val contents = response.body()
                    contents?.let {
                        displayRepoContents(it)
                    }
                } else {
                    Toast.makeText(this@RepositoryContentActivity, "Failed to load contents", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GitHubRepoContent>>, t: Throwable) {
                Toast.makeText(this@RepositoryContentActivity, "Failed to retrieve contents", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayRepoContents(contents: List<GitHubRepoContent>) {
        val contentTextView: TextView = findViewById(R.id.contentTextView)
        val contentText = StringBuilder()
        for (content in contents) {
            contentText.append("${content.name} (${content.type})\n")
        }
        contentTextView.text = contentText.toString()
    }
}

