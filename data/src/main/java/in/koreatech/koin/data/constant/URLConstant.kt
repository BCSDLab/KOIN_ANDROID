package `in`.koreatech.koin.data.constant

/**
 * KOIN API URL
 */
object URLConstant {
    const val CONTACT_URL = "https://open.kakao.com/o/sgiYx4Qg"
    const val BASE_URL_PRODUCTION = "https://api.koreatech.in" // release server
    const val BASE_URL_STAGE = "https://api.stage.koreatech.in" // development server
    const val URL_PLAYSTORE = "https://play.google.com/store/apps/details?id=in.koreatech.koin"
    // const val BASE_URL_STAGE = "https://api.koreatech.in" //development server

    const val OWNER_URL_STAGE = "https://owner.stage.koreatech.in/"
    const val OWNER_URL_PRODUCTION = "https://owner.koreatech.in/"

    const val VERSION = "version"
    const val LAND = "lands"
    const val COOPSHOP = "coopshop"

    object DINING {
        const val DINING = "dining"
        const val DININGS = "dinings"
        const val LIKE = "$DINING/like"
        const val UNLIKE = "$LIKE/cancel"
    }

    // external url

    object SHOPS {
        const val OWNERSHOPS = "owner/shops"
        const val ID = "{id}"
        const val MENUID = "{menuId}"
        const val SHOPID = "{shopId}"
        const val INFO = "$OWNERSHOPS/$ID"
        const val MENUS = "$OWNERSHOPS/menus"
        const val POST_MENU = "$OWNERSHOPS/$ID/menus"
        const val MODIFY_MENU = "$OWNERSHOPS/menus/$MENUID"
        const val MENU_INFO = "$OWNERSHOPS/menus/$MENUID"
        const val MODIFY_INFO = "$OWNERSHOPS/$SHOPID"
        const val GET_EVENTS = "$OWNERSHOPS/$SHOPID/event"
        const val DELETE_EVENTS = "$OWNERSHOPS/$SHOPID/events/{eventId}"
        const val POST_EVENT = "$OWNERSHOPS/$ID/event"
        const val SHOPS = "shops"
        const val REVIEWID = "{reviewId}"
        const val SHOPS_V2 = "/v2/shops"
        const val EVENTS = "$SHOPS/events"
        const val CATERGORIES = "$SHOPS/categories"
        const val GET_REVIEWS = "$SHOPS/$ID/reviews"
        const val WRITE_REVIEWS = "$SHOPS/$SHOPID/reviews"
        const val DELETE_REVIEWS = "$SHOPS/$SHOPID/reviews/$REVIEWID"
        const val MODIFY_REVIEWS = "$SHOPS/$SHOPID/reviews/$REVIEWID"
        const val REPORTS = "$SHOPS/$SHOPID/reviews/$REVIEWID/reports"
        const val SHOP_NOTIFICATION = "$SHOPS/$SHOPID/call-notification"
    }

    object USER {
        const val USER = "user"
        const val LOGIN: String = "$USER/login"
        const val FINDPASSWORD: String = "$USER/find/password"
        const val ME: String = "$USER/student/me"
        const val REFRESH: String = "$USER/refresh"
        const val CHECKNICKNAME: String = "$USER/check/nickname"
        const val CHECKEMAIL: String = "$USER/check/email"
        const val CHECKPASSWORD: String = "$USER/check/password"
        const val CHECKPHONE: String = "$USER/check/phone"
        const val CHECKNICKNAME_V2: String = "$USER/check/nickname"
        const val SMSSEND: String = "$USER/verification/send"
        const val SMSVERIFY: String = "$USER/verification/verify"
        const val SMSCOUNT: String = "$USER/verification/count"
        const val AUTH: String = "$USER/auth"
        const val EMAIL = "email"
        const val PW = "password"

        const val VERSION: String = "v2"
        object STUDENT {
            const val STUDENT = "student"
            const val REGISTER: String = "$USER/$STUDENT/register"
            const val REGISTER_V2: String = "$VERSION/$USER/$STUDENT/register"
        }
        object GENERAL {
            const val GENERAL = "general"
            const val REGISTER: String = "$VERSION/$USER/$GENERAL/register"
        }
    }

    object OWNER {
        const val OWNER = "owner"
        const val OWNERS = "owners"
        const val VERIFICATION = "verification"
        const val SIGNIN = "$OWNER/login"
        const val REGISTER: String = "$OWNERS/register"
        const val REGISTER_PHONE: String = "$OWNERS/register/phone"
        const val CODE = "$OWNERS/$VERIFICATION/code"
        const val EMAIL = "$OWNERS/$VERIFICATION/email"
        const val PASSWORD = "password"
        const val RESET = "reset"
        const val CHANGEPASSWORDEMAIL = "$OWNERS/$PASSWORD/$RESET/$VERIFICATION"
        const val CHANGEPASSWORDESENDSMS = "$OWNERS/$PASSWORD/$RESET/$VERIFICATION/sms"
        const val CHANGEPASSWORDSMSCODE = "$OWNERS/$PASSWORD/$RESET/send/sms"
        const val CHANGEPASSWORDCODE = "$OWNERS/$PASSWORD/$RESET/send"
        const val CHANGEPASSWORD = "$OWNERS/$PASSWORD/$RESET"
        const val CHANGEPASSWORDSMS = "$OWNERS/$PASSWORD/$RESET/sms"
        const val CODE_SMS = "$OWNERS/$VERIFICATION/code/sms"
        const val SMS = "$OWNERS/$VERIFICATION/sms"
        const val SHOPS = "$OWNER/shops"
        const val EXISTS_ACCOUNT = "$OWNERS/exists/account"
    }

    object DEPT {
        const val DEPT = "/dept"
        const val DEPTS = "/depts"
    }

    object UPLOAD {
        const val url = "/{domain}/upload/url"
        const val OWNERURL = "/owners/upload/url"
        const val MARKETURL = "/market/upload/url"
        const val LOSTANDFOUNDURL = "/lost_items/upload/url"
        const val CLUB = "/club/upload/url"
    }

    object CLUBS {
        const val CLUBS = "clubs"
        const val CATEGORIES = "$CLUBS/categories"
        const val HOT = "$CLUBS/hot"
        const val CLUBID = "{clubId}"
        const val QNA = "$CLUBS/$CLUBID/qna"
        const val DELETE_QNA = "$QNA/{qnaId}"
        const val DETAILS = "$CLUBS/$CLUBID"
        const val MODIFY = "$CLUBS/$CLUBID"
        const val LIKE = "$CLUBS/$CLUBID/like"
        const val CANCEL_LIKE = "$LIKE/cancel"
        const val EMPOWERMENT = "$CLUBS/empowerment"
    }
}
