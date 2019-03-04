package ru.bilenkod.login3in1testjava.data.githubusersearch.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.bilenkod.login3in1testjava.data.githubusersearch.model.GitHubUserSearchResponse

interface GitHubApi {
    @GET("users")
    fun getUsers(@Query("q") qQuery: String,
                 @Query("page") page: Int,
                 @Query("per_page") perPage: Int,
                 @Query("access_token") accessToken: String): Call<GitHubUserSearchResponse>
}