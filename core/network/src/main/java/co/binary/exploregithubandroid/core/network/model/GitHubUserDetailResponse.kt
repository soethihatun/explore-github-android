package co.binary.exploregithubandroid.core.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubUserDetailResponse(
    @SerialName("login")
    val login: String,
    @SerialName("id")
    val id: Int,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("url")
    val url: String,
    @SerialName("repos_url")
    val reposUrl: String,
    @SerialName("name")
    val name: String?,
    @SerialName("bio")
    val bio: String?,
    @SerialName("followers")
    val followers: Int,
    @SerialName("following")
    val following: Int,
)