package co.binary.exploregithubandroid.core.network.user.service

import co.binary.exploregithubandroid.core.network.user.model.GitHubUserDetailResponse
import co.binary.exploregithubandroid.core.network.user.model.GitHubUserRepoResponse
import co.binary.exploregithubandroid.core.network.user.model.SearchGitHubUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("search/users")
    suspend fun searchGitHubUsers(
        @Header("Authorization") accessToken: String,
        @Query("q") query: String,
        @Query("page") page: Int,
    ): Response<SearchGitHubUserResponse>

    @GET("users/{username}")
    suspend fun getGitHubUserDetail(
        @Header("Authorization") accessToken: String,
        @Path("username") username: String,
    ): Response<GitHubUserDetailResponse>

    @GET("users/{username}/repos")
    suspend fun getGitHubUserRepos(
        @Header("Authorization") accessToken: String,
        @Path("username") username: String,
        @Query("page") page: Int,
    ): Response<List<GitHubUserRepoResponse>>
}
