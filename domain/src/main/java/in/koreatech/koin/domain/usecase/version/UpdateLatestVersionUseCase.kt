package `in`.koreatech.koin.domain.usecase.version

import `in`.koreatech.koin.domain.repository.VersionRepository
import javax.inject.Inject

class UpdateLatestVersionUseCase @Inject constructor(
    private val versionRepository: VersionRepository
) {
    /**
     *  VersionCode 형식
     *  Mmmpp
     *  M: Major
     *  m: Minor
     *  p: Path
     *
     *  @since 2024-10-07
     */
    suspend operator fun invoke(versionCode: Int) = runCatching {
        if (versionCode > 99999 || versionCode < 10000) throw IllegalArgumentException("VersionCode must be in 10000 to 99999| input: $versionCode")

        var vc = versionCode

        val path = vc % 100
        vc /= 100
        val minor = vc % 100
        vc /= 100
        val major = vc

        val versionName = listOf(major, minor, path).joinToString(separator = ".")

        versionRepository.updateLatestVersionCode(versionCode)
        versionRepository.updateLatestVersionName(versionName)
    }
}