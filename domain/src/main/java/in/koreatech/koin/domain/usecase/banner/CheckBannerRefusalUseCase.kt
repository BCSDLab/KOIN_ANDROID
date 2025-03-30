package `in`.koreatech.koin.domain.usecase.banner

import `in`.koreatech.koin.domain.repository.BannerRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CheckBannerRefusalUseCase @Inject constructor(
    private val bannerRepository: BannerRepository
) {
    suspend operator fun invoke(): Boolean {
        return bannerRepository.getBannerRefusalDate().let {
            LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE).toInt() - it < 7
        }
    }
}
