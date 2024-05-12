package co.binary.exploregithubandroid.core.network.service

import co.binary.exploregithubandroid.core.network.model.SearchGitHubUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GitHubService {
    @GET("search/users")
    suspend fun searchUsers(
        @Header("Authorization") accessToken: String,
        @Query("q") query: String,
    ): Response<SearchGitHubUserResponse>
}
