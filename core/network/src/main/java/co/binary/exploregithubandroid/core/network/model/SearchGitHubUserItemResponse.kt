package co.binary.exploregithubandroid.core.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchGitHubUserItemResponse(
    @SerialName("login")
    val login: String,
    @SerialName("id")
    val id: Int,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
)
