package `in`.koreatech.koin.data.constant

/**
 * KOIN API URL
 */
object URLConstant {
    const val CONTACT_URL = "https://open.kakao.com/o/sgiYx4Qg"
    const val BASE_URL_PRODUCTION = "https://api.koreatech.in" // release server
    const val BASE_URL_STAGE = "https://api.stage.koreatech.in" // development server
    const val URL_PLAYSTORE = "https://play.google.com/store/apps/details?id=in.koreatech.koin"

    const val OWNER_URL_STAGE = "https://owner.stage.koreatech.in/"
    const val OWNER_URL_PRODUCTION = "https://owner.koreatech.in/"

    object USER {
        const val EMAIL = "email"
        const val PW = "password"
        const val LOGIN_ID = "login_id"
        const val LOGIN_PW = "login_pw"
    }

    object OWNER {
        const val OWNERS = "owners"
        const val VERIFICATION = "verification"
        const val EMAIL = "$OWNERS/$VERIFICATION/email"
    }
}
