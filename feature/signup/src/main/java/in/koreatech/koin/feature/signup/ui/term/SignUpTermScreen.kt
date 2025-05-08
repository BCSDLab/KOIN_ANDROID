package `in`.koreatech.koin.feature.signup.ui.term

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.feature.signup.R
import `in`.koreatech.koin.feature.signup.component.KoinSignUpCheckAllBox
import `in`.koreatech.koin.feature.signup.component.KoinSignUpCheckBox
import `in`.koreatech.koin.feature.signup.component.KoinSignUpProgressHeader
import `in`.koreatech.koin.feature.signup.component.KoinSignUpProgressIndicator
import `in`.koreatech.koin.feature.signup.component.KoinSignUpTerm
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SignUpTermScreen(
    viewModel: SignUpTermViewModel = hiltViewModel(),
    navigateToNextScreen: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()

    val context = LocalContext.current

    viewModel.collectSideEffect {
        handleSideEffect(it, context)
    }

    SignUpTermScreenImpl(
        privacyTerm = uiState.privacyTerm,
        koinTerm = uiState.koinTerm,
        marketingTerm = uiState.marketingTerm,
        privacyTermChecked = uiState.isPrivacyTermChecked,
        koinTermChecked = uiState.isKoinTermChecked,
        marketingTermChecked = uiState.isMarketingTermChecked,
        onPrivacyTermCheckChange = { viewModel.setPrivacyTermState(it) },
        onKoinTermCheckChange = { viewModel.setKoinTermState(it) },
        onMarketingTermCheckChange = { viewModel.setMarketingTermState(it) },
        navigateToNextScreen = navigateToNextScreen
    )
}

@Composable
private fun SignUpTermScreenImpl(
    privacyTerm: String,
    koinTerm: String,
    marketingTerm: String,
    privacyTermChecked: Boolean,
    koinTermChecked: Boolean,
    marketingTermChecked: Boolean,
    modifier: Modifier = Modifier,
    onPrivacyTermCheckChange: (Boolean) -> Unit = {},
    onKoinTermCheckChange: (Boolean) -> Unit = {},
    onMarketingTermCheckChange: (Boolean) -> Unit = {},
    navigateToNextScreen: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 40.dp)
    ) {
        KoinSignUpProgressHeader(
            text = stringResource(R.string.sign_up_agree_terms),
            currentStep = 1,
            maxStep = 4
        )

        Spacer(modifier = Modifier.height(8.dp))

        KoinSignUpProgressIndicator(
            currentStep = 1,
            maxStep = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        KoinSignUpCheckAllBox(
            checked = privacyTermChecked && koinTermChecked && marketingTermChecked,
            trailingText = stringResource(R.string.sign_up_agree_all),
            onCheckedChange = {
                onKoinTermCheckChange(it)
                onPrivacyTermCheckChange(it)
                onMarketingTermCheckChange(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        KoinSignUpCheckBox(
            checked = privacyTermChecked,
            trailingText = stringResource(R.string.sign_up_private_info_term),
            onCheckedChange = {
                onPrivacyTermCheckChange(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        KoinSignUpTerm(
            text = privacyTerm
        )

        Spacer(modifier = Modifier.height(16.dp))

        KoinSignUpCheckBox(
            checked = koinTermChecked,
            trailingText = stringResource(R.string.sign_up_koin_term),
            onCheckedChange = {
                onKoinTermCheckChange(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        KoinSignUpTerm(
            text = koinTerm
        )

        Spacer(modifier = Modifier.height(16.dp))

        KoinSignUpCheckBox(
            checked = marketingTermChecked,
            trailingText = stringResource(R.string.sign_up_marketing_term),
            onCheckedChange = {
                onMarketingTermCheckChange(it)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        KoinSignUpTerm(
            text = marketingTerm
        )

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.weight(1f))

        FilledButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.sign_up_next),
            contentPadding = PaddingValues(12.dp),
            onClick = { navigateToNextScreen() },
            enabled = privacyTermChecked && koinTermChecked
        )
    }
}

fun handleSideEffect(sideEffect: SignUpTermSideEffect, context: Context) {
    when (sideEffect) {
        SignUpTermSideEffect.FailedToFetchTerm -> {
            ToastUtil.getInstance().makeShort(context.getString(R.string.sign_up_failed_to_fetch_terms))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpTermScreenPreview() {
    SignUpTermScreenImpl(
        privacyTerm = "개인정보 처리방침",
        koinTerm = "코인 이용약관",
        marketingTerm = "마케팅 정보 수신 동의",
        privacyTermChecked = false,
        koinTermChecked = false,
        marketingTermChecked = false
    )
}
