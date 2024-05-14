package co.binary.exploregithubandroid.core.model.user

data class GitHubUser(
    val id: Int,
    val username: String,
    val avatarUrl: String,
)

// TODO: delete later
val dummyUser = GitHubUser(
    id = 1,
    username = "johndoe",
    avatarUrl = "https://avatars.githubusercontent.com/u/878437?s=200&v=4",
)

val dummyUsers = listOf(
    dummyUser,
    dummyUser.copy(id = 2),
    dummyUser.copy(id = 3),
)
