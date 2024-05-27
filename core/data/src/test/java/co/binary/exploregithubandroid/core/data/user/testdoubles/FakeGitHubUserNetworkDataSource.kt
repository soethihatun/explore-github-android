package co.binary.exploregithubandroid.core.data.user.testdoubles

import co.binary.exploregithubandroid.core.network.user.datasource.GitHubUserNetworkDataSource
import co.binary.exploregithubandroid.core.network.user.model.GitHubUserDetailResponse
import co.binary.exploregithubandroid.core.network.user.model.GitHubUserRepoResponse
import co.binary.exploregithubandroid.core.network.user.model.SearchGitHubUserItemResponse
import co.binary.exploregithubandroid.core.network.user.model.SearchGitHubUserResponse
import org.jetbrains.annotations.TestOnly

class FakeGitHubUserNetworkDataSource(
    private val searchGitHubUserItemList: MutableList<SearchGitHubUserItemResponse> = mutableListOf(),
    private val gitHubUserDetailList: MutableList<GitHubUserDetailResponse> = mutableListOf(),
    private val gitHubUserRepoList: MutableList<GitHubUserRepoResponse> = mutableListOf(),
) : GitHubUserNetworkDataSource {

    override suspend fun searchGitHubUsers(query: String, page: Int): Result<SearchGitHubUserResponse> {
        if (query.isEmpty() || page < 1) return Result.failure(Exception("Error"))
        return searchGitHubUserItemList.filter {
            it.login.contains(query, ignoreCase = true)
        }.let { list ->
            Result.success(SearchGitHubUserResponse(totalCount = list.size, incompleteResults = false, items = list))
        }
    }

    override suspend fun getGitHubUserDetail(username: String): Result<GitHubUserDetailResponse> {
        if (username.isEmpty()) return Result.failure(Exception("Error"))
        return gitHubUserDetailList.find {
            it.login.equals(username, ignoreCase = true)
        }?.let { Result.success(it) } ?: Result.failure(Exception("Error"))
    }

    override suspend fun getGitHubUserRepos(username: String, page: Int): Result<List<GitHubUserRepoResponse>> {
        if (username.isEmpty() || page < 1) return Result.failure(Exception("Error"))
        return Result.success(gitHubUserRepoList)
    }

    @TestOnly
    fun setSearchGitHubUserItemList(list: List<SearchGitHubUserItemResponse>) {
        searchGitHubUserItemList.clear()
        searchGitHubUserItemList.addAll(list)
    }

    @TestOnly
    fun setGitHubUserDetailList(list: List<GitHubUserDetailResponse>) {
        gitHubUserDetailList.clear()
        gitHubUserDetailList.addAll(list)
    }

    @TestOnly
    fun setGitHubUserRepoList(list: List<GitHubUserRepoResponse>) {
        gitHubUserRepoList.clear()
        gitHubUserRepoList.addAll(list)
    }
}
