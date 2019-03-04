package ru.bilenkod.login3in1testjava.data.githubusersearch.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GitHubUserSearchResponse {
    @SerializedName("total_count")
    @Expose
    var totalCount: Int = 0

    @SerializedName("items")
    @Expose
    var gitHubUserList: List<GitHubUser>? = null
}

class GitHubUser {
    @SerializedName("login")
    @Expose
    var login: String = ""

    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("avatar_url")
    @Expose
    var avatarUrl: String = ""
}