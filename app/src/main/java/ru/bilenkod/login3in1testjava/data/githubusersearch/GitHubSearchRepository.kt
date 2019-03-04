package ru.bilenkod.login3in1testjava.data.githubusersearch

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import ru.bilenkod.login3in1testjava.data.githubusersearch.model.GitHubUser
import ru.bilenkod.login3in1testjava.data.githubusersearch.source.SearchDataSource
import ru.bilenkod.login3in1testjava.data.githubusersearch.source.SearchDataSourceFactory
import java.util.*

class GitHubSearchRepository {

    companion object {
        const val SEARCH_DELAY: Long = 600
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private var searchString: String = ""
    private val factory = SearchDataSourceFactory()

    var usersList: LiveData<PagedList<GitHubUser>> = object :
            LiveData<PagedList<GitHubUser>>() {}

    init {
        buildUsersListForKeyword(searchString)
    }

    fun initSearchTask(searchString: String, withDelay: Boolean) {
        this.searchString = searchString
        val delay: Long = if (withDelay) SEARCH_DELAY else 0

        if (!timerTaskIsActive()) {

            timerTask = object : TimerTask() {
                override fun run() {
                    searchGitHubTimerTask()
                }
            }
            timer = Timer()
            timer?.schedule(timerTask, delay)

        } else if (!withDelay) {
            nullTimerAndTask()
            initSearchTask(searchString, withDelay)
        }
    }

    fun searchGitHubTimerTask() {
        buildUsersListForKeyword(searchString)
        nullTimerAndTask()
    }

    private fun buildUsersListForKeyword(keyword: String) {
        factory.invalidateDataSourceIfKeywordIsUpdated(keyword)

        val config = PagedList.Config.Builder()
                .setPageSize(SearchDataSource.PAGE_SIZE)
                .setEnablePlaceholders(false)
                .build()

        usersList = LivePagedListBuilder(factory, config).build()
    }

    private fun timerTaskIsActive() = timer != null && timerTask != null

    private fun nullTimerAndTask() {
        timerTask?.cancel()
        timer?.cancel()
        timer?.purge()
        timerTask = null
        timer = null
    }
}