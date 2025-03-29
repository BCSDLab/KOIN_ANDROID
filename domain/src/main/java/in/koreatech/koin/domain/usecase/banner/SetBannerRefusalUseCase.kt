package `in`.koreatech.koin.domain.usecase.banner

import `in`.koreatech.koin.domain.repository.BannerRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SetBannerRefusalUseCase @Inject constructor(
    private val bannerRepository: BannerRepository
) {
    suspend operator fun invoke() {
        bannerRepository.saveBannerRefusalDate(
            LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE).toInt()
        )
    }
}
