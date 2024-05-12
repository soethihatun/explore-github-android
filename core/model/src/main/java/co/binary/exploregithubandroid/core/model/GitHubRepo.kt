package co.binary.exploregithubandroid.core.model

data class GitHubRepo(
    val id: Int,
    val name: String,
    val description: String,
    val primaryLanguage: String,
    val url: String,
    val stargazersCount: Int,
)

// TODO: delete later
val dummyRepo = GitHubRepo(
    id = 1,
    name = "Lorem Ipsum Repo",
    description = "Lorem Ipsum description",
    primaryLanguage = "Kotlin",
    url = "https://github.com/JetBrains/kotlin",
    stargazersCount = 1,
)

val dummyRepos = listOf(
    dummyRepo,
    dummyRepo.copy(id = 2),
    dummyRepo.copy(id = 3),
)
