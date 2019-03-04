package ru.bilenkod.login3in1testjava.data.githubusersearch.client

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.bilenkod.login3in1testjava.data.githubusersearch.api.GitHubApi

class RetrofitClient private constructor() {

    private val retrofit: Retrofit
    private val mGitHubApi: GitHubApi
        get() = retrofit.create(GitHubApi::class.java)

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    companion object {
        const val BASE_URL = "https://api.github.com/search/"
        val instance = RetrofitClient()
        val gitHubApi = instance.mGitHubApi
    }
}