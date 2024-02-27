package `in`.koreatech.koin.data.constant

/**
 * KOIN API URL
 */
object URLConstant {

    const val BASE_URL_PRODUCTION = "https://api.koreatech.in" //release server
    const val BASE_URL_STAGE = "https://api.stage.koreatech.in" //development server
    //const val BASE_URL_STAGE = "https://api.koreatech.in" //development server

    const val OWNER_URL_STAGE = "https://owner.stage.koreatech.in/"
    const val OWNER_URL_PRODUCTION = "https://owner.koreatech.in/"

    const val ADMIN = "admin/"
    const val VERSION = "versions"
    const val DINING = "dinings"
    const val SHOPS = "shops"
    const val FAQ = "faqs"
    const val LECTURE = "lectures"
    const val TIMETABLE = "timetable"
    const val TIMETABLES = "timetables"
    const val SEMESTERS = "semesters"
    const val LAND = "lands"
    const val TERM = "term"

    object BUS {
        const val BUS = "bus"
        const val COURSES = "$BUS/courses"
        const val TIMETABLE = "$BUS/timetable"
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
        const val PROFILEUPLOAD: String = "$USER/profile/upload"
        const val ID = "portal_account"
        const val EMAIL = "email"
        const val PW = "password"

        object STUDENT{
            const val STUDENT = "student"
            const val REGISTER: String = "$USER/$STUDENT/register"
        }
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
}