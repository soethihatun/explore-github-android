package co.binary.exploregithubandroid.core.domain.user

import co.binary.exploregithubandroid.core.model.IoDispatcher
import co.binary.exploregithubandroid.core.repository.user.RecentSearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InsertOrReplaceRecentSearchUseCase @Inject constructor(
    private val recentRepository: RecentSearchRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(query: String) = withContext(dispatcher) {
        recentRepository.insertOrReplaceRecentSearch(query)
    }
}
