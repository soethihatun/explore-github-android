package co.binary.exploregithubandroid.core.data.repository

import co.binary.exploregithubandroid.core.data.mapper.asExternalModel
import co.binary.exploregithubandroid.core.data.testdoubles.FakeGitHubUserNetworkDataSource
import co.binary.exploregithubandroid.core.network.model.GitHubUserDetailResponse
import co.binary.exploregithubandroid.core.network.model.GitHubUserRepoResponse
import co.binary.exploregithubandroid.core.network.model.SearchGitHubUserItemResponse
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GitHubUserRepositoryImplTest {

    private val searchGitHubUserItemResponse = SearchGitHubUserItemResponse(
        login = "login",
        id = 1,
        avatarUrl = "avatarUrl",
        nodeId = "nodeid",
    )

    private val gitHubUserRepoResponse = GitHubUserRepoResponse(
        id = 1,
        name = "name",
        nodeId = "nodeid",
        htmlUrl = "htmlUrl",
        description = "description",
        stargazersCount = 1,
        topics = listOf("topic"),
        language = "language",
        fullName = "full_name",
    )

    private val gitHubUserDetailResponse = GitHubUserDetailResponse(
        login = "login",
        id = 1,
        name = "name",
        avatarUrl = "avatarUrl",
        followers = 1,
        following = 1,
        bio = null,
        url = "url",
        reposUrl = "reposUrl",
    )

    private lateinit var fakeNetwork: FakeGitHubUserNetworkDataSource

    private lateinit var repository: GitHubUserRepositoryImpl

    @Before
    fun setUp() {
        fakeNetwork = FakeGitHubUserNetworkDataSource()
        repository = GitHubUserRepositoryImpl(fakeNetwork)
    }

    @Test
    fun `#searchGitHubUsers, given invalid query & page, when network returns error, then it should return error`() =
        runTest {
            // given
            val query = ""
            val page = -1

            // when
            val actual = repository.searchGitHubUsers(query, page)

            // then
            Truth.assertThat(actual.isFailure).isTrue()
            Truth.assertThat(actual.exceptionOrNull()).isInstanceOf(Exception::class.java)
        }

    @Test
    fun `#searchGitHubUsers, given valid query & page, when network has no match data, then it should return empty`() =
        runTest {
            val query = "query"
            val otherQuery = "other"
            fakeNetwork.setSearchGitHubUserItemList(listOf(searchGitHubUserItemResponse.copy(login = query)))
            val page = 1

            val actual = repository.searchGitHubUsers(otherQuery, page)

            Truth.assertThat(actual.isSuccess).isTrue()
            Truth.assertThat(actual.getOrNull()).isEmpty()
        }

    @Test
    fun `#searchGitHubUsers, given valid query & page, when network has match data, then it should return data`() =
        runTest {
            fakeNetwork.setSearchGitHubUserItemList(listOf(searchGitHubUserItemResponse))
            val expected = searchGitHubUserItemResponse.asExternalModel()
            val query = searchGitHubUserItemResponse.login
            val page = 1

            val actual = repository.searchGitHubUsers(query, page)

            Truth.assertThat(actual.isSuccess).isTrue()
            Truth.assertThat(actual.getOrNull()).hasSize(1)
            Truth.assertThat(actual.getOrNull()?.first()).isEqualTo(expected)
        }

    @Test
    fun `#getGitHubUserDetail, given invalid username & page, when network returns error, then it should return error`() =
        runTest {
            // given
            val username = ""
            val page = -1

            // when
            val actual = repository.getGitHubUserDetail(username, page)

            // then
            Truth.assertThat(actual.isFailure).isTrue()
            Truth.assertThat(actual.exceptionOrNull()).isInstanceOf(Exception::class.java)
        }

    @Test
    fun `#getGitHubUserDetail, given valid username & page, when network has no match data, then it should return error`() =
        runTest {
            val username = "username"
            val other = "other"
            fakeNetwork.setGitHubUserDetailList(listOf(gitHubUserDetailResponse.copy(login = username)))
            val page = 1

            val actual = repository.getGitHubUserDetail(other, page)

            Truth.assertThat(actual.isFailure).isTrue()
            Truth.assertThat(actual.exceptionOrNull()).isInstanceOf(Exception::class.java)
        }

    @Test
    fun `#getGitHubUserDetail, given valid username & page, when network has match data, then it should return data`() =
        runTest {
            fakeNetwork.setGitHubUserDetailList(listOf(gitHubUserDetailResponse))

            val expected = gitHubUserDetailResponse.asExternalModel(repos = emptyList())
            val username = searchGitHubUserItemResponse.login
            val page = 1

            val actual = repository.getGitHubUserDetail(username, page)

            Truth.assertThat(actual.isSuccess).isTrue()
            Truth.assertThat(actual.getOrNull()).isEqualTo(expected)
        }

    @Test
    fun `#getGitHubUserRepos, given invalid username & page, when network returns error, then it should return error`() =
        runTest {
            // given
            val username = ""
            val page = -1

            // when
            val actual = repository.getGitHubUserRepos(username, page)

            // then
            Truth.assertThat(actual.isFailure).isTrue()
            Truth.assertThat(actual.exceptionOrNull()).isInstanceOf(Exception::class.java)
        }

    @Test
    fun `#getGitHubUserRepos, given valid username & page, when network has no data, then it should return empty`() =
        runTest {
            val username = "username"
            fakeNetwork.setGitHubUserRepoList(emptyList())
            val page = 1

            val actual = repository.getGitHubUserRepos(username, page)

            Truth.assertThat(actual.isSuccess).isTrue()
            Truth.assertThat(actual.getOrNull()).isEmpty()
        }

    @Test
    fun `#getGitHubUserRepos, given valid username & page, when network has match data, then it should return data`() =
        runTest {
            fakeNetwork.setGitHubUserRepoList(listOf(gitHubUserRepoResponse))
            val expected = gitHubUserRepoResponse.asExternalModel()
            val username = "username"
            val page = 1

            val actual = repository.getGitHubUserRepos(username, page)

            Truth.assertThat(actual.isSuccess).isTrue()
            Truth.assertThat(actual.getOrNull()).hasSize(1)
            Truth.assertThat(actual.getOrNull()?.first()).isEqualTo(expected)
        }
}
