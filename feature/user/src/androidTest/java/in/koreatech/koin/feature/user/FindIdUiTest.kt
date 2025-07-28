package `in`.koreatech.koin.feature.user

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.user.model.VerificationCodeState
import `in`.koreatech.koin.feature.user.model.VerificationMethodState
import `in`.koreatech.koin.feature.user.ui.findid.verification.FindIdVerificationImpl
import org.junit.Rule
import org.junit.Test

class FindIdUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `휴대전화_인증이고_번호가_공란이면_이메일_안내를_표시한다`() {
        composeTestRule.setContent {
            KoinTheme {
                FindIdVerificationImpl(
                    isSms = true,
                    verificationMethod = "",
                    verificationMethodState = VerificationMethodState.None,
                    verificationCode = "",
                    verificationCodeState = VerificationCodeState.None,
                    verificationTimeLeft = 300
                )
            }
        }

        composeTestRule.onNodeWithText("휴대전화 등록을 안 하셨나요?").assertIsDisplayed()
    }

    @Test
    fun `휴대전화_인증이고_번호가_입력되었다면_이메일_안내를_숨긴다`() {
        composeTestRule.setContent {
            KoinTheme {
                FindIdVerificationImpl(
                    isSms = true,
                    verificationMethod = "01000000000",
                    verificationMethodState = VerificationMethodState.None,
                    verificationCode = "",
                    verificationCodeState = VerificationCodeState.None,
                    verificationTimeLeft = 300
                )
            }
        }

        composeTestRule.onNodeWithText("휴대전화 등록을 안 하셨나요?").assertIsNotDisplayed()
    }

    @Test
    fun `이메일_인증이면_이메일_안내를_숨긴다`() {
        composeTestRule.setContent {
            KoinTheme {
                FindIdVerificationImpl(
                    isSms = false,
                    verificationMethod = "",
                    verificationMethodState = VerificationMethodState.None,
                    verificationCode = "",
                    verificationCodeState = VerificationCodeState.None,
                    verificationTimeLeft = 300
                )
            }
        }

        composeTestRule.onNodeWithText("휴대전화 등록을 안 하셨나요?").assertIsNotDisplayed()
    }

    @Test
    fun `인증수단이_공란이면_인증번호_발송_버튼을_비활성화_한다`() {
        composeTestRule.setContent {
            KoinTheme {
                FindIdVerificationImpl(
                    isSms = true,
                    verificationMethod = "",
                    verificationMethodState = VerificationMethodState.None,
                    verificationCode = "",
                    verificationCodeState = VerificationCodeState.None,
                    verificationTimeLeft = 300
                )
            }
        }

        composeTestRule.onNodeWithText("인증번호 발송").assertIsNotEnabled()
    }

    @Test
    fun `인증수단이_입력되었다면_인증번호_발송_버튼을_활성화_한다`() {
        composeTestRule.setContent {
            KoinTheme {
                FindIdVerificationImpl(
                    isSms = true,
                    verificationMethod = "01000000000",
                    verificationMethodState = VerificationMethodState.None,
                    verificationCode = "",
                    verificationCodeState = VerificationCodeState.None,
                    verificationTimeLeft = 300
                )
            }
        }

        composeTestRule.onNodeWithText("인증번호 발송").assertIsEnabled()
    }

    @Test
    fun `인증번호가_발송되면_시간과_안내를_표시한다`() {
        composeTestRule.setContent {
            KoinTheme {
                FindIdVerificationImpl(
                    isSms = true,
                    verificationMethod = "01000000000",
                    verificationMethodState = VerificationMethodState.Sent(1, 5, 4),
                    verificationCode = "",
                    verificationCodeState = VerificationCodeState.None,
                    verificationTimeLeft = 300
                )
            }
        }
        composeTestRule.onNodeWithText("05:00").assertIsDisplayed()
        composeTestRule.onNodeWithText("인증번호가 발송되었습니다.", true).assertIsDisplayed()
    }

    @Test
    fun `인증번호_발송_한도를_초과하면_안내를_표시하고_발송_버튼을_비활성화_한다`() {
        composeTestRule.setContent {
            KoinTheme {
                FindIdVerificationImpl(
                    isSms = true,
                    verificationMethod = "01000000000",
                    verificationMethodState = VerificationMethodState.CountExceeded,
                    verificationCode = "",
                    verificationCodeState = VerificationCodeState.None,
                    verificationTimeLeft = 300
                )
            }
        }

        composeTestRule.onNodeWithText("1일 발송 한도를 초과했습니다. 24시간 이후 재시도 바랍니다.").assertIsDisplayed()
        composeTestRule.onNodeWithText("인증번호 발송").assertIsNotEnabled()
    }

    @Test
    fun `인증번호_입력란이_공란이면_확인_버튼을_비활성화한다`() {
        composeTestRule.setContent {
            KoinTheme {
                FindIdVerificationImpl(
                    isSms = true,
                    verificationMethod = "01000000000",
                    verificationMethodState = VerificationMethodState.Sent(1, 5, 4),
                    verificationCode = "",
                    verificationCodeState = VerificationCodeState.None,
                    verificationTimeLeft = 300
                )
            }
        }

        composeTestRule.onNodeWithText("인증번호 확인").assertIsNotEnabled()
    }

    @Test
    fun `인증번호_입력란이_입력되었다면_확인_버튼을_활성화한다`() {
        composeTestRule.setContent {
            KoinTheme {
                FindIdVerificationImpl(
                    isSms = true,
                    verificationMethod = "01000000000",
                    verificationMethodState = VerificationMethodState.Sent(1, 5, 4),
                    verificationCode = "123456",
                    verificationCodeState = VerificationCodeState.None,
                    verificationTimeLeft = 300
                )
            }
        }

        composeTestRule.onNodeWithText("인증번호 확인").assertIsEnabled()
    }

    @Test
    fun `인증번호가_일치하면_일치_안내를_표시한다`() {
        composeTestRule.setContent {
            KoinTheme {
                FindIdVerificationImpl(
                    isSms = true,
                    verificationMethod = "01000000000",
                    verificationMethodState = VerificationMethodState.Sent(1, 5, 4),
                    verificationCode = "123456",
                    verificationCodeState = VerificationCodeState.Valid,
                    verificationTimeLeft = 300
                )
            }
        }

        composeTestRule.onNodeWithText("인증번호가 일치합니다.").assertIsDisplayed()
    }

    @Test
    fun `인증번호가_일치하지_않으면_불일치_안내를_표시한다`() {
        composeTestRule.setContent {
            KoinTheme {
                FindIdVerificationImpl(
                    isSms = true,
                    verificationMethod = "01000000000",
                    verificationMethodState = VerificationMethodState.Sent(1, 5, 4),
                    verificationCode = "123456",
                    verificationCodeState = VerificationCodeState.NotValid,
                    verificationTimeLeft = 300
                )
            }
        }

        composeTestRule.onNodeWithText("인증번호가 일치하지 않습니다. 다시 입력해 주세요.").assertIsDisplayed()
    }

    @Test
    fun `인증번호_시간이_만료되었으면_안내를_표시하고_확인_버튼을_비활성화한다`() {
        composeTestRule.setContent {
            KoinTheme {
                FindIdVerificationImpl(
                    isSms = true,
                    verificationMethod = "01000000000",
                    verificationMethodState = VerificationMethodState.Sent(1, 5, 4),
                    verificationCode = "123456",
                    verificationCodeState = VerificationCodeState.Expired,
                    verificationTimeLeft = 0
                )
            }
        }

        composeTestRule.onNodeWithText("유효시간이 지났습니다. 인증번호를 재발송 해주세요.").assertIsDisplayed()
        composeTestRule.onNodeWithText("인증번호 확인").assertIsNotEnabled()
    }

    @Test
    fun `모든_단계가_마무리_되었으면_다음_버튼을_활성화한다`() {
        composeTestRule.setContent {
            KoinTheme {
                FindIdVerificationImpl(
                    isSms = true,
                    verificationMethod = "01000000000",
                    verificationMethodState = VerificationMethodState.Sent(1, 5, 4),
                    verificationCode = "123456",
                    verificationCodeState = VerificationCodeState.Valid,
                    verificationTimeLeft = 300
                )
            }
        }

        composeTestRule.onNodeWithText("다음").assertIsEnabled()
    }
}
