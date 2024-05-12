package co.binary.exploregithubandroid.core.model

data class GitHubUser(
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val name: String,
)

// TODO: delete later
val dummyUser = GitHubUser(
    id = 1,
    login = "johndoe",
    avatarUrl = "https://avatars.githubusercontent.com/u/878437?s=200&v=4",
    name = "John Doe"
)

val dummyUsers = listOf(
    dummyUser,
    dummyUser.copy(id = 2),
    dummyUser.copy(id = 3),
)
