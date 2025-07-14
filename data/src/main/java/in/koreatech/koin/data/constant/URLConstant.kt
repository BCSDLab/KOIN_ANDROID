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

    object VERSION {
        const val VERSION = "version"
        const val TYPE = "$VERSION/{type}"
    }
    object LAND {
        const val LAND = "lands"
        object PATH {
            const val ID = "{id}"
        }
        const val ID = "$LAND/${PATH.ID}"
    }
    object COOPSHOP {
        const val COOPSHOP = "coopshop"
        object PATH {
            const val COOPSHOPID = "{coopShopId}"
        }
        const val COOPSHOPID = "$COOPSHOP/${PATH.COOPSHOPID}"
    }

    object DINING {
        const val DINING = "dining"
        const val DININGS = "dinings"
        const val LIKE = "$DINING/like"
        const val UNLIKE = "$LIKE/cancel"
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
        const val EVENTS = "$SHOPS/events"
        const val CATERGORIES = "$SHOPS/categories"
        object ID {
            const val ID = "$SHOPS/${PATH.ID}"
            const val MENUS = "$ID/menus"
            const val EVENTS = "$ID/events"
            const val REVIEWS = "$ID/reviews"
        }
        object SHOPID {
            const val SHOPID = "$SHOPS/${PATH.SHOPID}"
            const val NOTIFICATION = "$SHOPID}/call-notification"
            object MENUS {
                const val CATEGORIES = "$SHOPID/menus/categories"
            }
            object REVIEWS {
                const val REVIEWS = "$SHOPID/reviews"
                object REVIEWID {
                    const val REVIEWID = "$REVIEWS/${PATH.REVIEWID}"
                    const val REPORTS = "$REVIEWID/reports"
                }
            }
        }
        const val QUERY = "/$SHOPS/search/related/{query}"
        object OWNERSHOPS {
            const val OWNERSHOPS = "owner/shops"
            object ID {
                const val ID = "$OWNERSHOPS/${PATH.ID}"
                const val MENUS = "$ID/menus"
                const val EVENT = "$ID/event"
            }
            object SHOPID {
                const val SHOPID = "$OWNERSHOPS/${PATH.SHOPID}"
                object EVENT {
                    const val EVENT = "$SHOPID/event"
                    const val EVENTID = "$SHOPID/events/{eventId}"
                }
            }
            object MENUS {
                const val MENUS = "$OWNERSHOPS/menus"
                const val MENUID = "$MENUS/${PATH.MENUID}"
            }
        }
    }
    object BENEFIT {
        const val BENEFIT = "benefit"
        const val SHOPS = "$BENEFIT/{id}/shops"
        const val CATEGORIES = "$BENEFIT/categories"
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
        const val AUTH: String = "$USER/auth"
        const val EMAIL = "email"
        const val PW = "password"
        object NOTIFICATION {
            const val NOTIFICATION = "/notification"
            object SUBSCRIBE {
                const val SUBSCRIBE = "$NOTIFICATION/subscribe"
                const val DETAIL = "$SUBSCRIBE/detail"
            }
        }
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
        const val COMPANYNUMBER = "$OWNERS/exists/company-number"
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
        object CLUBID {
            const val CLUBID = "$CLUBS/${PATH.CLUBID}"
            object QNA {
                const val QNA = "$CLUBID/qna"
                const val QNAID = "$QNA/{qnaId}"
            }
            object LIKE {
                const val LIKE = "$CLUBID/like"
                const val CANCEL = "$LIKE/cancel"
            }
        }
        const val EMPOWERMENT = "$CLUBS/empowerment"
    }

    object ARTICLES {
        const val ARTICLES = "articles"
        object PATH {
            const val ID = "{id}"
        }
        const val ID = "$ARTICLES/${PATH.ID}"
        const val SEARCH = "$ARTICLES/search"
        object HOT {
            const val HOT = "$ARTICLES/hot"
            const val KEYWORD = "$HOT/keyword"
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
            const val SEARCH = "$LOSTITEM/search"
        }
    }

    object CHAT {
        const val CHATROOM = "chatroom/lost-item"
        object PATH {
            const val ARTICLEID = "{articleId}"
            const val CHATROOMID = "{chat_room_id}"
        }
        object ARTICLEID {
            const val ARTICLEID = "$CHATROOM/${PATH.ARTICLEID}"
            object ROOMID {
                const val ROOMID = "$CHATROOM/${PATH.ARTICLEID}/${PATH.CHATROOMID}"
                const val MESSAGES = "$CHATROOM/${PATH.ARTICLEID}/${PATH.CHATROOMID}/messages"
                const val BLOCK = "$CHATROOM/${PATH.ARTICLEID}/${PATH.CHATROOMID}/block"
            }
        }
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
            object SEMESTERS {
                const val SEMESTERS = "/$V3/semesters"
                const val CHECK = "$SEMESTERS/check"
            }
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

    object BANNER {
        object PATH {
            const val CATEGORYID = "{categoryId}"
        }
        const val CATEGORYID = "banners/${PATH.CATEGORYID}"
        const val CATEGORYS = "banner-categories"
    }

    object BUS {
        const val BUS = "bus"
        object PATH {
            const val ID = "{id}"
        }
        const val NOTICE = "$BUS/notice"
        const val SHUTTLE = "$BUS/courses/shuttle"
        const val ROUTE = "$BUS/route"
        object TIMETABLE {
            const val TIMETABLE = "$BUS/timetable"
            const val SHUTTLE_ID = "$TIMETABLE/shuttle/${PATH.ID}"
            const val EXPRESS = "$TIMETABLE/v2?bus_type=EXPRESS&region=null"
            const val CITY = "$TIMETABLE/city"
        }
    }
    object LECTURES {
        const val LECTURES = "/lectures"
        const val V3_LECTURES = "/v3/lectures"
    }

    object ABTEST {
        const val ABTEST = "abtest"
        object ASSIGN {
            const val ASSIGN = "$ABTEST/assign"
            const val TOKEN = "$ASSIGN/token"
        }
    }
}
