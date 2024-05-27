package co.binary.exploregithubandroid.core.testing.data

import co.binary.exploregithubandroid.core.model.user.GitHubUserRepo

val gitHubUserRepoTestData = listOf(
    GitHubUserRepo(
        id = 1,
        name = "Lorem Ipsum Repo",
        description = "Lorem Ipsum description",
        primaryLanguage = "Kotlin",
        htmlUrl = "https://github.com/JetBrains/kotlin",
        stargazersCount = 1,
    ),
    GitHubUserRepo(
        id = 2,
        name = "Lorem Ipsum Repo",
        description = "Lorem Ipsum description",
        primaryLanguage = "Kotlin",
        htmlUrl = "https://github.com/JetBrains/kotlin",
        stargazersCount = 1,
    ),
    GitHubUserRepo(
        id = 3,
        name = "Lorem Ipsum Repo",
        description = null,
        primaryLanguage = null,
        htmlUrl = "https://github.com/JetBrains/kotlin",
        stargazersCount = 1,
    ),
)
