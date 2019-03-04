package ru.bilenkod.login3in1testjava.data.githubusersearch.source

import androidx.paging.DataSource
import ru.bilenkod.login3in1testjava.data.githubusersearch.model.GitHubUser

class SearchDataSourceFactory(private var keyword: String = "") : DataSource.Factory<Int, GitHubUser>() {

    private lateinit var dataSource: SearchDataSource

    override fun create(): DataSource<Int, GitHubUser> {
        dataSource = SearchDataSource(keyword)
        return dataSource
    }

    fun invalidateDataSourceIfKeywordIsUpdated(keyword: String) {
        if (keyword != this.keyword) {
            this.keyword = keyword
            dataSource.currentSearchWord = this.keyword
            dataSource.invalidate()
        }
    }
}