package co.binary.exploregithubandroid.core.model.user

data class GitHubUserRepo(
    val id: Int,
    val name: String,
    val description: String?,
    val primaryLanguage: String?,
    val htmlUrl: String,
    val stargazersCount: Int,
)

// TODO: delete later
val dummyRepo = GitHubUserRepo(
    id = 1,
    name = "Lorem Ipsum Repo",
    description = "Lorem Ipsum description",
    primaryLanguage = "Kotlin",
    htmlUrl = "https://github.com/JetBrains/kotlin",
    stargazersCount = 1,
)

val dummyRepos = listOf(
    dummyRepo,
    dummyRepo.copy(id = 2),
    dummyRepo.copy(id = 3),
)
