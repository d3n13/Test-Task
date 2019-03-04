package ru.bilenkod.login3in1testjava.data.githubusersearch.source

import androidx.paging.PageKeyedDataSource
import retrofit2.Callback
import ru.bilenkod.login3in1testjava.data.githubusersearch.client.RetrofitClient
import ru.bilenkod.login3in1testjava.data.githubusersearch.model.GitHubUser
import ru.bilenkod.login3in1testjava.data.githubusersearch.model.GitHubUserSearchResponse
import ru.bilenkod.login3in1testjava.data.githubusersearch.source.Callback.Companion.LOAD_AFTER
import ru.bilenkod.login3in1testjava.data.githubusersearch.source.Callback.Companion.LOAD_BEFORE

class SearchDataSource(var currentSearchWord: String = "")
    : PageKeyedDataSource<Int, GitHubUser>() {

    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>,
                             callback: PageKeyedDataSource.LoadInitialCallback<Int, GitHubUser>) {
        loadIfSearchWordIsNotEmpty(
                FIRST_PAGE,
                InitialCallback(callback))
    }

    override fun loadBefore(params: PageKeyedDataSource.LoadParams<Int>,
                            callback: PageKeyedDataSource.LoadCallback<Int, GitHubUser>) {
        loadIfSearchWordIsNotEmpty(
                params.key,
                Callback(LOAD_BEFORE, callback, params))

    }

    override fun loadAfter(params: PageKeyedDataSource.LoadParams<Int>,
                           callback: PageKeyedDataSource.LoadCallback<Int, GitHubUser>) {
        loadIfSearchWordIsNotEmpty(
                params.key,
                Callback(LOAD_AFTER, callback, params))
    }

    private fun loadIfSearchWordIsNotEmpty(key: Int, callbackResponse: Callback<GitHubUserSearchResponse>) {
        if (searchWordIsNotEmpty())
            RetrofitClient.gitHubApi
                    .getUsers("$currentSearchWord in:login", key, PAGE_SIZE, GITHUB_TOKEN)
                    .enqueue(callbackResponse)
    }

    private fun searchWordIsNotEmpty() = !currentSearchWord.isBlank()

    companion object {
        const val PAGE_SIZE = 30
        const val FIRST_PAGE = 1
        const val MAX_ITEMS = 1000
        const val GITHUB_TOKEN = "618b39977639d098250be7092bc4df3c28d3d364"
    }
}
