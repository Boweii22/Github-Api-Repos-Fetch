package com.example.githubapi

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var githubApiService: GitHubApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val searchButton: Button = findViewById(R.id.searchButton)
        val profileImageView: ImageView = findViewById(R.id.profileImageView)
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val bioTextView: TextView = findViewById(R.id.bioTextView)
        val reposTextView: TextView = findViewById(R.id.reposTextView)

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

        // Handle the search button click
        searchButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            if (username.isNotEmpty()) {
                searchUser(username)
            } else {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchUser(username: String) {
        githubApiService.getUser(username).enqueue(object : Callback<GitHubUser> {
            override fun onResponse(call: Call<GitHubUser>, response: Response<GitHubUser>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        displayUserInfo(it)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GitHubUser>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
            }
        })

        githubApiService.getUserRepos(username).enqueue(object : Callback<List<GitHubRepo>> {
            override fun onResponse(call: Call<List<GitHubRepo>>, response: Response<List<GitHubRepo>>) {
                if (response.isSuccessful) {
                    val repos = response.body()
                    repos?.let {
                        displayUserRepos(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<GitHubRepo>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to retrieve repos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayUserInfo(user: GitHubUser) {
        val profileImageView: ImageView = findViewById(R.id.profileImageView)
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val bioTextView: TextView = findViewById(R.id.bioTextView)

        nameTextView.text = user.name ?: user.login
        bioTextView.text = user.bio ?: "No bio available"
        Glide.with(this).load(user.avatar_url).into(profileImageView)
    }

    private fun displayUserRepos(repos: List<GitHubRepo>) {
        val reposTextView: TextView = findViewById(R.id.reposTextView)
        val reposText = StringBuilder()
        for (repo in repos) {
            reposText.append("${repo.name}: ${repo.html_url}\n\n")
        }
        reposTextView.text = reposText.toString()
    }
}
