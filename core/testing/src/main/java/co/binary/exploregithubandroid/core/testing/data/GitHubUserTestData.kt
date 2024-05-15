package co.binary.exploregithubandroid.core.testing.data

import co.binary.exploregithubandroid.core.model.user.GitHubUser
import co.binary.exploregithubandroid.core.model.user.GitHubUserDetail

val gitHubUserTestData = listOf(
    GitHubUser(
        id = 1,
        username = "john",
        avatarUrl = "https://avatars.githubusercontent.com/u/878437?s=200&v=4",
    ),
    GitHubUser(
        id = 2,
        username = "jane",
        avatarUrl = "https://avatars.githubusercontent.com/u/878437?s=200&v=4",
    ),
    GitHubUser(
        id = 3,
        username = "tony",
        avatarUrl = "https://avatars.githubusercontent.com/u/878437?s=200&v=4",
    ),
)

fun GitHubUserDetail.asUser(): GitHubUser = GitHubUser(
    id = id,
    username = username,
    avatarUrl = avatarUrl,
)
