package co.binary.exploregithubandroid.core.testing.data

import co.binary.exploregithubandroid.core.model.user.GitHubUserDetail

val gitHubUserDetailTestData = listOf(
    GitHubUserDetail(
        id = 1,
        username = "john",
        avatarUrl = "https://avatars.githubusercontent.com/u/878437?s=200&v=4",
        name = "John",
        followers = 1,
        following = 1,
        repos = gitHubUserRepoTestData,
    ),
    GitHubUserDetail(
        id = 2,
        username = "jane",
        avatarUrl = "https://avatars.githubusercontent.com/u/878437?s=200&v=4",
        name = "Jane",
        followers = 1,
        following = 1,
        repos = gitHubUserRepoTestData,
    ),
    GitHubUserDetail(
        id = 3,
        username = "tony",
        avatarUrl = "https://avatars.githubusercontent.com/u/878437?s=200&v=4",
        name = "Tony",
        followers = 1,
        following = 1,
        repos = gitHubUserRepoTestData,
    ),
)
