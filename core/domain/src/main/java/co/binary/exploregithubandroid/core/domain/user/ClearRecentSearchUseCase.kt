package co.binary.exploregithubandroid.core.domain.user

import co.binary.exploregithubandroid.core.model.IoDispatcher
import co.binary.exploregithubandroid.core.repository.user.RecentSearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClearRecentSearchUseCase @Inject constructor(
    private val recentRepository: RecentSearchRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(dispatcher) {
        recentRepository.clearRecentSearches()
    }
}
