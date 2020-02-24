package in.koreatech.koin.constant;

/**
 * KOIN API URL
 */
public class URLConstant {

    public static final String ADMIN = "admin/";
    public static final String VERSION = "versions";

    public static final class USER {
        public static final String USER = "user";
        public static final String LOGIN = USER + "/login";
        public static final String LOGOUT = USER + "/logout";
        public static final String REGISTER = USER + "/register";
        public static final String FINDPASSWORD = USER + "/find/password";
        public static final String ME = USER + "/me";
        public static final String REFRESH = USER + "/refresh";
        public static final String CHECKNICKNAME = USER + "/check/nickname";
        public static final String PROFILEUPLOAD = USER + "/profile/upload";


        public static final String ID = "portal_account";
        public static final String PW = "password";
    }

    public static final String BUS = "buses";
    public static final String DINING = "dinings";
    public static final String SHOPS = "shops";
    public static final String FAQ = "faqs";
    public static final String LECTURE = "lectures";
    public static final String TIMETABLE = "timetable";
    public static final String TIMETABLES = "timetables";
    public static final String SEMESTERS = "semesters";
    public static final String LAND = "lands";
    public static final String TERM = "term";


    public static final class CALLVANS {
        public static final String CALLVAN = "callvan";
        public static final String ROOMS = CALLVAN + "/rooms";
        public static final String COMPANIES = CALLVAN + "/companies";
        public static final String PARTICIPANT = ROOMS + "/participant";
    }

    public static final class HOUSE {
        public static final String HOUSES = "houses";
    }

    public static final class COMMUNITY {
        public static final String BOARDS = "boards";
        public static final String ARTICLES = "articles";
        public static final String TEMPBOARD = "temp";
        public static final String COMMENTS = "comments";
        public static final String GRANTCHECK = ARTICLES + "/grant/check";

        public static final int ID_FREE = 1;

        public static final int ID_RECRUIT = 2;

        public static final int ID_ANONYMOUS = 3;
    }

    public static final class MARKET {
        public static final String MARKET = "market";
        public static final String ITEMS = MARKET + "/items";
        public static final String GRANTCHECK = ITEMS + "/grant/check";

    }

    public static final class CIRCLE {
        public static final String CIRCLE = "circles";
    }

    public static final class LOSTANDFOUND {
        public static final String LOST = "lost";
        public static final String LOSTITEMS = LOST + "/lostItems";
        public static final String GRANTCHECK = LOSTITEMS + "/grant/check";
    }

    public static final class SEARCH {
        public static final String SEARCH = "search";
        public static final String ARTICLESEARCH = "articles/" + SEARCH;
    }

    public static final class TEMP{
        public static final String TEMP = "/temp";
        public static final String TEMP_IMAGE_UPLOAD = TEMP + "/items/image/upload";

    }
}
