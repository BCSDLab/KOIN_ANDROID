package `in`.koreatech.koin.feature.user.ui.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import `in`.koreatech.koin.feature.user.ui.signup.complete.SignUpCompleteScreen
import `in`.koreatech.koin.feature.user.ui.signup.term.SignUpTermScreen
import `in`.koreatech.koin.feature.user.ui.signup.userinfo.general.SignUpGeneralUserInfo
import `in`.koreatech.koin.feature.user.ui.signup.userinfo.student.SignUpStudentUserInfo
import `in`.koreatech.koin.feature.user.ui.signup.usertype.SignUpUserType
import `in`.koreatech.koin.feature.user.ui.signup.verification.SignUpVerification

fun NavGraphBuilder.koinUserGraph(
    navController: NavController,
    finish: () -> Unit = {}
) {
    composable<SignUpNavType.Term> { entry ->
        val args = entry.toRoute<SignUpNavType.Term>()
        SignUpTermScreen(sessionId = args.sessionId) {
            navController.navigate(SignUpNavType.Verification(sessionId = args.sessionId))
        }
    }
    composable<SignUpNavType.Verification> { entry ->
        val args = entry.toRoute<SignUpNavType.Verification>()
        SignUpVerification(sessionId = args.sessionId) { name, phoneNumber, gender ->
            navController.navigate(
                SignUpNavType.UserType(
                    name = name,
                    phoneNumber = phoneNumber,
                    gender = gender,
                    sessionId = args.sessionId
                )
            )
        }
    }
    composable<SignUpNavType.UserType> { entry ->
        val args = entry.toRoute<SignUpNavType.UserType>()
        SignUpUserType(
            sessionId = args.sessionId,
            navigateToStudentScreen = {
                navController.navigate(
                    SignUpNavType.StudentUserInfo(
                        name = args.name,
                        phoneNumber = args.phoneNumber,
                        gender = args.gender,
                        sessionId = args.sessionId
                    )
                )
            },
            navigateToGeneralScreen = {
                navController.navigate(
                    SignUpNavType.GeneralUserInfo(
                        name = args.name,
                        phoneNumber = args.phoneNumber,
                        gender = args.gender,
                        sessionId = args.sessionId
                    )
                )
            }
        )
    }
    composable<SignUpNavType.GeneralUserInfo> { entry ->
        val args = entry.toRoute<SignUpNavType.GeneralUserInfo>()
        SignUpGeneralUserInfo(sessionId = args.sessionId) {
            navController.navigate(SignUpNavType.SignUpComplete)
        }
    }

    composable<SignUpNavType.StudentUserInfo> { entry ->
        val args = entry.toRoute<SignUpNavType.StudentUserInfo>()
        SignUpStudentUserInfo(sessionId = args.sessionId) {
            navController.navigate(SignUpNavType.SignUpComplete)
        }
    }

    composable<SignUpNavType.SignUpComplete> {
        SignUpCompleteScreen {
            finish()
        }
    }
}

const val PHONE_NUMBER = "phoneNumber"
const val NAME = "name"
const val GENDER = "gender"
const val SESSION_ID = "sessionId"
