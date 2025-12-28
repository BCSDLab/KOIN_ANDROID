package `in`.koreatech.koin.data.constant

import `in`.koreatech.koin.data.constant.URLConstant.USER.USER

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
        const val DININGS = "dinings"
    }

    // external url

    object SHOPS {
        object PATH {
            const val ID = "{id}"
            const val MENUID = "{menuId}"
            const val SHOPID = "{shopId}"
            const val REVIEWID = "{reviewId}"
        }
        const val SHOPS = "shops"
        const val SHOPS_V2 = "/v2/shops"
        const val SHOPS_V3 = "/v3/shops"
        const val EVENTS = "$SHOPS/events"
        const val CATERGORIES = "$SHOPS/categories"
        const val NOTIFICATION = "$SHOPS/${PATH.SHOPID}/call-notification"
        object REVIEWS {
            const val ID_REVIEWS = "$SHOPS/${PATH.ID}/reviews"
            const val REVIEWS = "$SHOPS/${PATH.SHOPID}/reviews"
            const val REVIEWID = "$REVIEWS/${PATH.REVIEWID}"
            const val REPORTS = "$REVIEWS/${PATH.REVIEWID}/reports"
        }
        object OWNERSHOPS {
            const val OWNERSHOPS = "owner/shops"
            const val ID = "$OWNERSHOPS/${PATH.ID}"
            const val SHOPID = "$OWNERSHOPS/${PATH.SHOPID}"
            object MENUS {
                const val MENUS = "$OWNERSHOPS/menus"
                const val ID_MENUS = "$OWNERSHOPS/${PATH.ID}/menus"
                const val MENUID = "$MENUS/${PATH.MENUID}"
            }
            object EVENT {
                const val EVENT = "$OWNERSHOPS/${PATH.SHOPID}/event"
                const val EVENTID = "$OWNERSHOPS/${PATH.SHOPID}/events/{eventId}"
                const val ID_EVENT = "$OWNERSHOPS/${PATH.ID}/event"
            }
        }
    }

    object USER {
        const val USER = "user"
        object PATH {
            const val NICKNAME = "{nickname}"
        }
        const val LOGIN: String = "$USER/login"
        const val FINDPASSWORD: String = "$USER/find/password"
        const val ME: String = "$USER/student/me"
        const val REFRESH: String = "$USER/refresh"

        const val CHECKUSERID: String = "$USER/check/id"

        const val AUTH: String = "$USER/auth"
        const val EMAIL = "email"
        const val PW = "password"
        const val NOTIFICATION = "/notification"
        const val SUBSCRIBE = "$NOTIFICATION/subscribe"
        const val DETAIL = "$NOTIFICATION/subscribe/detail"
        const val LOGIN_ID = "login_id"
        const val LOGIN_PW = "login_pw"
        object CHECK {
            const val CHECK = "$USER/check"
            const val NICKNAME: String = "$CHECK/nickname"
            const val EMAIL: String = "$CHECK/email"
            const val PASSWORD: String = "$CHECK/password"
            const val PHONE: String = "$CHECK/phone"
            const val NICKNAME_V2: String = "$CHECK/nickname"
            const val BYNICKNAME = "$NICKNAME/${PATH.NICKNAME}"
        }
        object VERIFICATION {
            const val VERIFICATION = "$USER/verification"
            const val SMSSEND: String = "$VERIFICATION/send"
            const val SMSVERIFY: String = "$VERIFICATION/verify"
            const val SMSCOUNT: String = "$VERIFICATION/count"
        }

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
        object PATH {
            const val CLUBID = "{clubId}"
        }
        const val CATEGORIES = "$CLUBS/categories"
        const val HOT = "$CLUBS/hot"
        const val QNA = "$CLUBS/${PATH.CLUBID}/qna"
        const val DELETE_QNA = "$QNA/{qnaId}"
        const val DETAILS = "$CLUBS/${PATH.CLUBID}"
        const val MODIFY = "$CLUBS/${PATH.CLUBID}"
        const val LIKE = "$CLUBS/${PATH.CLUBID}/like"
        const val CANCEL_LIKE = "$LIKE/cancel"
        const val EMPOWERMENT = "$CLUBS/empowerment"
    }

    object ARTICLES {
        const val ARTICLES = "articles"
        object PATH {
            const val ID = "{id}"
        }
        object KEYWORD {
            const val KEYWORD = "$ARTICLES/keyword"
            const val ME = "$KEYWORD/me"
            const val ID = "$KEYWORD/${PATH.ID}"
            const val SUGGESTIONS = "$KEYWORD/suggestions"
        }
        object LOSTITEM {
            const val LOSTITEM = "$ARTICLES/lost-item"
            const val ID = "$LOSTITEM/${PATH.ID}"
            const val REPORTS = "$LOSTITEM/${PATH.ID}/reports"
        }
    }

    object CHAT {
        const val CHATROOM = "chatroom/lost-item"
        object PATH {
            const val ARTICLEID = "{articleId}"
            const val CHATROOMID = "{chat_room_id}"
        }
        const val ARTICLEID = "$CHATROOM/${PATH.ARTICLEID}"
        const val ARTICLEID_ROOMID = "$CHATROOM/${PATH.ARTICLEID}/${PATH.CHATROOMID}"
        const val MESSAGES = "$CHATROOM/${PATH.ARTICLEID}/${PATH.CHATROOMID}/messages"
        const val BLOCK = "$CHATROOM/${PATH.ARTICLEID}/${PATH.CHATROOMID}/block"
    }

    object TIMETABLE {
        const val TIMETABLE = "timetables"
        object PATH {
            const val ID = "{id}"
            const val FRAMEID = "{frameId}"
            const val LECTUREID = "{lectureId}"
        }

        object V2 {
            const val V2 = "v2"
            object FRAME {
                const val FRAME = "/$V2/$TIMETABLE/frame"
                const val ID = "$FRAME/${PATH.ID}"
                const val ALL = "/$V2/all/$TIMETABLE/frame"
                const val FRAMES = "/$V2/$TIMETABLE/frames"
                const val ROLLBACK = "$FRAME/rollback"
                const val LECTURE = "$FRAME/${PATH.FRAMEID}/lecture/${PATH.LECTUREID}"
            }
            object LECTURE {
                const val LECTURE = "/$V2/$TIMETABLE/lecture"
                const val LECTURES = "/$V2/$TIMETABLE/lectures"
                const val ID = "$LECTURE/${PATH.ID}"
            }
        }
        object V3 {
            const val V3 = "v3"
            const val CHECK = "/$V3/semesters/check"
            object FRAME {
                const val FRAME = "/$V3/$TIMETABLE/frame"
                const val FRAMES = "/$V3/$TIMETABLE/frames"
                const val ID = "$FRAME/${PATH.ID}"
                const val ROLLBACK = "$FRAME/rollback"
            }
            object LECTURE {
                const val LECTURE = "/$V3/$TIMETABLE/lecture"
                const val REGULER = "$LECTURE/regular"
                const val CUSTOM = "$LECTURE/custom"
            }
        }
    }

    object ABTEST {
        const val ABTEST = "abtest"
        const val UPDATE = "$ABTEST/assign/token"
        const val ASSIGN = "$ABTEST/assign"
    }
}
