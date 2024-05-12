package co.binary.exploregithubandroid.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubUserRepoResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("name")
    val name: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("description")
    val description: String?,
    @SerialName("url")
    val url: String,
    @SerialName("language")
    val language: String?,
    @SerialName("stargazers_count")
    val stargazersCount: Int,
    @SerialName("topics")
    val topics: List<String>,
)