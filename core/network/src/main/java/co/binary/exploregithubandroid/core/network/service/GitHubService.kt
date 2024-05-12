package co.binary.exploregithubandroid.core.network.service

import co.binary.exploregithubandroid.core.network.model.GitHubUserDetailResponse
import co.binary.exploregithubandroid.core.network.model.GitHubUserRepoResponse
import co.binary.exploregithubandroid.core.network.model.SearchGitHubUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("search/users")
    suspend fun searchUsers(
        @Header("Authorization") accessToken: String,
        @Query("q") query: String,
    ): Response<SearchGitHubUserResponse>

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Header("Authorization") accessToken: String,
        @Path("username") username: String,
    ): Response<GitHubUserDetailResponse>

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Header("Authorization") accessToken: String,
        @Path("username") username: String,
    ): Response<List<GitHubUserRepoResponse>>
}
