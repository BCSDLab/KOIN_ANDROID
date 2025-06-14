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

    const val ADMIN = "admin/"
    const val VERSION = "version"
    const val FAQ = "faqs"
    const val LECTURE = "lectures"
    const val TIMETABLE = "timetable"
    const val TIMETABLES = "timetables"
    const val SEMESTERS = "semesters"
    const val LAND = "lands"
    const val TERM = "term"
    const val COOPSHOP = "coopshop"

    object DINING {
        const val DINING = "dining"
        const val DININGS = "dinings"
        const val LIKE = "$DINING/like"
        const val UNLIKE = "$LIKE/cancel"
    }

    // external url
    const val UNIBUS = "koreatech.unibus.kr"

    object SHOPS {
        const val OWNERSHOPS = "owner/shops"
        const val SHOPS = "shops"
        const val SHOPS_V2 = "/v2/shops"
        const val EVENTS = "$SHOPS/events"
        const val CATERGORIES = "$SHOPS/categories"
    }

    object BUS {
        const val BUS = "bus"
        const val COURSES = "$BUS/courses"
        const val TIMETABLE = "$BUS/timetable"
        const val TIMETABLE_V2 = "$BUS/timetable/v2"
        const val CITY = "$BUS/timetable/city"
        const val SEARCH = "$BUS/search"
        const val BUSES = "/buses"
    }

    object USER {
        const val USER = "user"
        const val LOGIN: String = "$USER/login"
        const val LOGOUT: String = "$USER/logout"
        const val REGISTER: String = "$USER/register"
        const val FINDPASSWORD: String = "$USER/find/password"
        const val ME: String = "$USER/student/me"
        const val REFRESH: String = "$USER/refresh"
        const val CHECKNICKNAME: String = "$USER/check/nickname"
        const val CHECKEMAIL: String = "$USER/check/email"
        const val CHECKPASSWORD: String = "$USER/check/password"
        const val PROFILEUPLOAD: String = "$USER/profile/upload"
        const val CHECKNICKNAME_V2: String = "$USER/check/nickname"
        const val CHECKPHONE: String = "$USER/check/phone"
        const val CHECKUSERID: String = "$USER/check/id"
        const val AUTH: String = "$USER/auth"
        const val ID = "portal_account"
        const val EMAIL = "email"
        const val PW = "password"
        const val LOGIN_ID = "login_id"
        const val LOGIN_PW = "login_pw"
        const val ID_EXISTS = "$USER/id/exists"

        object STUDENT {
            const val STUDENT = "student"
            const val REGISTER: String = "$USER/$STUDENT/register"
        }

        object EXISTS {
            const val EXISTS = "exists"
            const val EMAIL = "email/$EXISTS"
            const val PHONE = "phone/$EXISTS"
        }
    }

    object USERS {
        const val USERS = "users"
        const val CHECKLOGINID: String = "$USERS/check/id"

        const val SMSSEND: String = "$USERS/verification/sms/send"
        const val SMSVERIFY: String = "$USERS/verification/sms/verify"
        const val EMAILSEND: String = "$USERS/verification/email/send"
        const val EMAILVERIFY: String = "$USERS/verification/email/verify"

        const val VERSION: String = "v2"

        const val SIGNIN_V2: String = "$VERSION/$USERS/login"

        const val PASSWORD_RESET_BY_EMAIL: String = "$USERS/password/reset/email"
        const val PASSWORD_RESET_BY_SMS: String = "$USERS/password/reset/sms"

        const val ID_MATCH_EMAIL: String = "$USERS/id/match/email"
        const val ID_MATCH_PHONE: String = "$USERS/id/match/phone"

        object FINDID {
            const val FINDID = "id/find"
            const val EMAIL = "$USERS/$FINDID/email"
            const val SMS = "$USERS/$FINDID/sms"
        }

        object STUDENTS {
            const val STUDENTS = "students"
            const val ME: String = "$VERSION/$USERS/$STUDENTS/me"
            const val REGISTER_V2: String = "$VERSION/$USERS/$STUDENTS/register"
        }
        object GENERAL {
            const val REGISTER: String = "$VERSION/$USERS/register"
            const val ME: String = "$VERSION/$USERS/me"
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
        const val PW = "password"
        const val SHOPS = "$OWNER/shops"
        const val EXISTS_ACCOUNT = "$OWNERS/exists/account"
    }

    object CALLVANS {
        const val CALLVAN = "callvan"
        const val ROOMS: String = "$CALLVAN/rooms"
        const val COMPANIES: String = "$CALLVAN/companies"
        const val PARTICIPANT: String = "$ROOMS/participant"
    }

    object HOUSE {
        const val HOUSES = "houses"
    }

    object COMMUNITY {
        const val BOARDS = "boards"
        const val ARTICLES = "articles"
        const val TEMPBOARD = "temp"
        const val COMMENTS = "comments"
        const val GRANTCHECK: String = "$ARTICLES/grant/check"
        const val ID_FREE = 1
        const val ID_RECRUIT = 2
        const val ID_ANONYMOUS = 3
    }

    object MARKET {
        const val MARKET = "market"
        const val ITEMS: String = "$MARKET/items"
        const val GRANTCHECK: String = "$ITEMS/grant/check"
    }

    object CIRCLE {
        const val CIRCLE = "circles"
    }

    object LOSTANDFOUND {
        const val LOST = "lost"
        const val LOSTITEMS: String = "$LOST/lostItems"
        const val GRANTCHECK: String = "$LOSTITEMS/grant/check"
    }

    object SEARCH {
        const val SEARCH = "search"
        const val ARTICLESEARCH = "articles/$SEARCH"
    }

    object TEMP {
        const val TEMP = "/temp"
        const val TEMP_IMAGE_UPLOAD: String = "$TEMP/items/image/upload"
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
    }
}
