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
     *  @throws IllegalArgumentException 버전 코드가 5자리가 아닌 경우
     *  @throws IllegalArgumentException 버전 코드가 기존 코드 보다 작은 경우
     *  @since 2024-10-07
     */
    suspend operator fun invoke(versionCode: Int) = runCatching {
        if (versionCode > 99999 || versionCode < 10000) throw IllegalArgumentException("VersionCode must be in 10000 to 99999| input: $versionCode")

        versionRepository.getLatestVersionCode()?.let { latestVersionCode ->
            if (versionCode < latestVersionCode) throw IllegalArgumentException("VersionCode must be higher than before VersionCode| input: $versionCode, origin: ${latestVersionCode}")
        }

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