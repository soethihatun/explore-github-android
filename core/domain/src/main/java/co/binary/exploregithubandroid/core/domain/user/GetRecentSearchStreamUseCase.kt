package co.binary.exploregithubandroid.core.domain.user

import co.binary.exploregithubandroid.core.repository.user.RecentSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentSearchStreamUseCase @Inject constructor(
    private val recentRepository: RecentSearchRepository
) {
    operator fun invoke(limit: Int = 15): Flow<List<String>> =
        recentRepository.getRecentSearchStream(limit)
}
