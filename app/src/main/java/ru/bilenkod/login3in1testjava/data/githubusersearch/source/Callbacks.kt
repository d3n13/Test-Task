package ru.bilenkod.login3in1testjava.data.githubusersearch.source

import androidx.paging.PageKeyedDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.bilenkod.login3in1testjava.data.githubusersearch.model.GitHubUser
import ru.bilenkod.login3in1testjava.data.githubusersearch.model.GitHubUserSearchResponse
import ru.bilenkod.login3in1testjava.ui.ToastAt

fun responseIsValid(response: Response<GitHubUserSearchResponse>): Boolean =
        response.body() != null && response.body()?.gitHubUserList != null

class InitialCallback(
        private var callback: PageKeyedDataSource.LoadInitialCallback<Int, GitHubUser>)
    : Callback<GitHubUserSearchResponse> {

    override fun onFailure(call: Call<GitHubUserSearchResponse>, t: Throwable) {
        ToastAt().githubUserLoadingError(t.localizedMessage)
    }

    override fun onResponse(call: Call<GitHubUserSearchResponse>,
                            response: Response<GitHubUserSearchResponse>) {
        if (responseIsValid(response)) {
            callback.onResult(
                    response.body()?.gitHubUserList!!,
                    null,
                    SearchDataSource.FIRST_PAGE + 1)
        }
    }
}

class Callback(private val type: Int,
               private val callback: PageKeyedDataSource.LoadCallback<Int, GitHubUser>,
               private val params: PageKeyedDataSource.LoadParams<Int>)
    : Callback<GitHubUserSearchResponse> {

    companion object {
        const val LOAD_BEFORE = 1
        const val LOAD_AFTER = 2
    }

    override fun onFailure(call: Call<GitHubUserSearchResponse>, t: Throwable) {
        ToastAt().githubUserLoadingError(t.localizedMessage)
    }

    override fun onResponse(call: Call<GitHubUserSearchResponse>,
                            response: Response<GitHubUserSearchResponse>) {
        if (responseIsValid(response)) {
            callOnResult(response)
        }
    }

    private fun callOnResult(response: Response<GitHubUserSearchResponse>) {
        val adjacentPageKey = when (type) {
            LOAD_BEFORE ->
                loadBeforeKeyOrNull(params.key)
            LOAD_AFTER ->
                loadAfterKeyOrNull(params.key, response.body()!!.totalCount.toFloat())
            else ->
                throw IllegalArgumentException("Callback type $type is not " +
                        "specified")
        }
        callback.onResult(response.body()?.gitHubUserList!!, adjacentPageKey)
    }

    private fun loadBeforeKeyOrNull(paramsKey: Int): Int? {
        return if (paramsKey > 1) paramsKey - 1 else null
    }

    private fun loadAfterKeyOrNull(paramsKey: Int, totalCount: Float): Int? {
        val pageSize = SearchDataSource.PAGE_SIZE.toFloat()
        val maxAvailableItems = if (totalCount <= SearchDataSource.MAX_ITEMS)
                    totalCount
                else
                    SearchDataSource.MAX_ITEMS.toFloat()
        val totalPages = Math.ceil((maxAvailableItems / pageSize).toDouble())
        return if (paramsKey < totalPages) paramsKey + 1 else null
    }
}