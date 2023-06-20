package `in`.koreatech.koin.util.ext

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri


val Context.isKakaoTalkInstalled: Boolean
    get() {
        return try {
            packageManager.getPackageInfo("com.kakao.talk", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: NameNotFoundException) {
            false
        }
    }

fun Context.goToKakaoTalkBcsdlabFriend() {
    val link = if(isKakaoTalkInstalled) {
        "kakaoplus://plusfriend/friend/@bcsdlab"
    } else {
        "https://goto.kakao.com/@bcsdlab"
    }

    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
}