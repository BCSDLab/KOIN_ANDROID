package `in`.koreatech.koin.core.analytics

object AccountAnalyticsConstant {
    object Label {
        object Login {
            const val CONFIRM = "account__login__login__confirm__click"
            const val ACCOUNT_RECOVERY_ID_ENTRY = "account__login__account_recovery__id_entry__click"
            const val ACCOUNT_RECOVERY_PASSWORD_ENTRY = "account__login__account_recovery__password_entry__click"
            const val SIGN_UP_ENTRY = "account__login__sign_up__entry__click"
        }

        object SignUp {
            const val MENU_ENTRY: String = "account__main_category__sign_up__menu_entry__click"
            const val TERMS_AGREE = "account__sign_up_policy__sign_up__terms_agree__click"
            const val USER_TYPE = "account__user_type_selection__sign_up__user_type__click"
            const val SEND_CODE = "account__identity_verification__sign_up__send_code__click"
            const val CODE_VERIFY_SUCCESS = "account__identity_verification__sign_up__code_verify_success__click"
            const val ID_AVAILABLE = "account__input_user_info__sign_up__id_available__click"
            const val NICKNAME_AVAILABLE = "account__input_user_info__sign_up__nickname_available__click"
            const val CONFIRM = "account__input_user_info__sign_up__confirm__click"
        }

        object ProfileEdit {
            const val CONFIRM = "account__edit_profile__profile_edit__confirm__click"
            const val PASSWORD_CONFIRM = "account__password_change__password_change__confirm__click"
            const val ENTRY_FROM_ACCOUNT_CONFIG = "account__account_config__profile_edit__entry__click"
        }

        object MainProfile {
            const val LOGIN_ENTRY = "account__main_profile__login__entry__click"
            const val LOGOUT = "account__main_profile__logout__click"
            const val ACCOUNT_CONFIG_ENTRY = "account__main_profile__account_config__entry__click"
            const val TIMETABLE_ENTRY = "timetable__main_profile__timetable_entry__entry__click"
        }
    }

    object Value {
        const val LOGIN_SUCCESS = "로그인 완료"
        const val LOGIN_FAILURE = "로그인 실패"
        const val FIND_ID = "아이디 찾기"
        const val FIND_PASSWORD = "비밀번호 찾기"
        const val SIGN_UP_START = "회원가입 시작"
        const val TERMS_AGREE = "약관동의"
        const val USER_TYPE_STUDENT = "학생"
        const val USER_TYPE_GENERAL = "외부인"
        const val SEND_VERIFICATION_CODE = "인증번호 발송"
        const val CODE_VERIFIED = "인증완료"
        const val ID_CREATED = "아이디생성"
        const val NICKNAME_CREATED = "닉네임생성"
        const val SIGN_UP_COMPLETED = "회원가입완료"
        const val PROFILE_EDIT_SUCCESS = "정보수정 완료"
        const val PROFILE_EDIT_FAILURE = "정보수정 실패"
        const val PROFILE_EDIT_ATTEMPT = "정보수정 시도"
        const val PASSWORD_CHANGE_SUCCESS = "비밀번호 수정 완료"
        const val LOGIN = "로그인"
        const val LOGOUT = "로그아웃"
        const val SETTINGS = "설정"
        const val TIMETABLE = "내 시간표"
    }
}
