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
        const val ID = "{id}"
        const val MENUID = "{menuId}"
        const val SHOPID = "{shopId}"
        const val SHOPS = "shops"
        const val REVIEWID = "{reviewId}"
        const val SHOPS_V2 = "/v2/shops"
        const val EVENTS = "$SHOPS/events"
        const val CATERGORIES = "$SHOPS/categories"
        const val ID_REVIEWS = "$SHOPS/$ID/reviews"
        const val REVIEWS = "$SHOPS/$SHOPID/reviews"
        const val REVIEWS_REVIEWID = "$SHOPS/$SHOPID/reviews/$REVIEWID"
        const val REPORTS = "$SHOPS/$SHOPID/reviews/$REVIEWID/reports"
        const val NOTIFICATION = "$SHOPS/$SHOPID/call-notification"
        object OWNERSHOPS {
            const val OWNERSHOPS = "owner/shops"
            const val SHOP_ID = "${OWNERSHOPS}/$ID"
            const val MENUS = "${OWNERSHOPS}/menus"
            const val ID_MENUS = "${OWNERSHOPS}/$ID/menus"
            const val MENU_MENUID = "${OWNERSHOPS}/menus/$MENUID"
            const val SHOPS_SHOPID = "${OWNERSHOPS}/$SHOPID"
            const val EVENT = "${OWNERSHOPS}/$SHOPID/event"
            const val EVENTS_EVENTID = "${OWNERSHOPS}/$SHOPID/events/{eventId}"
            const val ID_EVENT = "${OWNERSHOPS}/$ID/event"
        }
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
        const val NICKNAME = "{nickname}"
        const val AUTH_CHECKNICKNAME = "$CHECKNICKNAME/$NICKNAME"
        const val NOTIFICATION = "/notification"
        const val SUBSCRIBE = "$NOTIFICATION/subscribe"
        const val DETAIL = "$NOTIFICATION/subscribe/detail"

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
        const val CLUBID = "{clubId}"
        const val CATEGORIES = "$CLUBS/categories"
        const val HOT = "$CLUBS/hot"
        const val QNA = "$CLUBS/$CLUBID/qna"
        const val DELETE_QNA = "$QNA/{qnaId}"
        const val DETAILS = "$CLUBS/$CLUBID"
        const val MODIFY = "$CLUBS/$CLUBID"
        const val LIKE = "$CLUBS/$CLUBID/like"
        const val CANCEL_LIKE = "$LIKE/cancel"
        const val EMPOWERMENT = "$CLUBS/empowerment"
    }

    object ARTICLES {
        const val ARTICLES = "articles"
        const val ID = "{id}"
        const val KEYWORD = "$ARTICLES/keyword"
        const val KEYWORD_ME = "$KEYWORD/me"
        const val KEYWORD_ID = "$KEYWORD/$ID"
        const val SUGGESTIONS = "$KEYWORD/suggestions"
        const val LOSTANDFOUND = "$ARTICLES/lost-item"
        const val LOSTANDFOUND_ID = "$LOSTANDFOUND/$ID"
        const val LOSTANDFOUND_REPORTS = "$LOSTANDFOUND/$ID/reports"
    }

    object CHAT {
        const val CHATROOM = "chatroom/lost-item"
        const val ARTICLEID = "{articleId}"
        const val CHATROOMID = "{chat_room_id}"
        const val CHATROOM_ARTICLEID = "$CHATROOM/$ARTICLEID"
        const val CHATROOM_ARTICLEID_ROOMID = "$CHATROOM/$ARTICLEID/$CHATROOMID"
        const val MESSAGES = "$CHATROOM/$ARTICLEID/$CHATROOMID/messages"
        const val BLOCK = "$CHATROOM/$ARTICLEID/$CHATROOMID/block"
    }

    object TIMETABLE {
        const val TIMETABLE = "timetables"
        const val ID = "{id}"
        const val FRAMEID = "{frameId}"
        const val LECTUREID = "{lectureId}"

        object V2 {
            const val V2 = "v2"
            const val LECTURE = "/${V2}/$TIMETABLE/lecture"
            const val FRAME_ID = "/${V2}/$TIMETABLE/frame/$ID"
            const val FRAME = "/${V2}/$TIMETABLE/frame"
            const val ALL_FRAMES = "/${V2}/all/$TIMETABLE/frame"
            const val FRAMES = "/${V2}/$TIMETABLE/frames"
            const val ROLLBACK = "/${V2}/$TIMETABLE/frame/rollback"
            const val LECTURE_ID = "/${V2}/$TIMETABLE/lecture/$ID"
            const val FRAME_LECTURE = "/${V2}/$TIMETABLE/frame/$FRAMEID/lecture/$LECTUREID"
            const val LECTURES = "/${V2}/$TIMETABLE/lectures"
        }
        object V3 {
            const val V3 = "v3"
            const val SEMESTERS = "/${V3}/semesters/check"
            const val LECTURE = "/${V3}/$TIMETABLE/lecture"
            const val REGULER = "/${V3}/$TIMETABLE/lecture/regular"
            const val CUSTOM = "/${V3}/$TIMETABLE/lecture/custom"
            const val FRAME_ID = "/${V3}/$TIMETABLE/frame/$ID"
            const val FRAMES = "/${V3}/$TIMETABLE/frames"
            const val FRAME = "/${V3}/$TIMETABLE/frame"
            const val ROLLBACK = "/${V3}/$TIMETABLE/frame/rollback"
        }
    }

    object ABTEST {
        const val ABTEST = "abtest"
        const val UPDATE = "$ABTEST/assign/token"
        const val ASSIGN = "$ABTEST/assign"
    }
}
