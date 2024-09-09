package `in`.koreatech.business.feature.signup.checkterm

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.Blue1
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.ColorUnarchived
import `in`.koreatech.business.ui.theme.Gray5
import `in`.koreatech.business.ui.theme.Gray6
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@Composable
fun CheckTermScreen(
    modifier: Modifier = Modifier,
    viewModel: CheckTermViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {}
) {
    val state = viewModel.collectAsState().value
    val scrollState = rememberScrollState()
    val scrollStatePrivacy = rememberScrollState()
    val scrollStateKoin = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                IconButton(
                    onClick = { viewModel.onBackButtonClicked() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = stringResource(id = R.string.back_icon),
                    )
                }

                Text(
                    text = stringResource(id = R.string.sign_up),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier,
                        color = ColorPrimary,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(id = R.string.check_terms)
                    )
                    Text(
                        text = stringResource(id = R.string.one_third),
                        color = ColorPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    drawLine(
                        color = ColorUnarchived,
                        start = Offset(-40f, 0f),
                        end = Offset(size.width + 35, size.height),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = ColorPrimary,
                        start = Offset(-40f, 0f),
                        end = Offset((size.width + 40) / 3, size.height),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clickable {
                        viewModel.onAllTermCheckedChanged()
                    }
                    .background(color = ColorTextField, shape = RoundedCornerShape(4.dp)),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Image(
                    painter = if (state.isAllTermChecked) painterResource(id = R.drawable.ic_check_selected) else painterResource(
                        id = R.drawable.ic_check
                    ),
                    contentDescription = stringResource(R.string.check),
                    modifier = modifier
                        .padding(horizontal = 8.dp)
                        .height(24.dp)
                        .width(24.dp)

                )

                Text(
                    text = stringResource(R.string.check_all),
                    color = ColorPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(22.dp)
                    .clickable {
                        viewModel.onPrivacyTermCheckedChanged()
                    }
            ) {
                Image(
                    painter = if (state.isCheckedPrivacyTerms || state.isAllTermChecked) painterResource(
                        id = R.drawable.ic_check_selected
                    ) else painterResource(
                        id = R.drawable.ic_check
                    ),
                    contentDescription = stringResource(R.string.check),
                    modifier = modifier
                        .padding(horizontal = 8.dp)
                        .height(24.dp)
                        .width(24.dp)

                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.private_info_term),
                    color = Gray6,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Box(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .border(width = 1.dp, color = Blue1, shape = RectangleShape)
                    .padding(10.dp)
                    .height(143.dp)
                    .verticalScroll(scrollStatePrivacy)
            ) {
                Text(text = stringResource(R.string.term_1), fontSize = 10.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(22.dp)
                    .clickable {
                        viewModel.onKoinTermCheckedChanged()
                    }
            ) {
                Image(
                    painter = if (state.isCheckedKoinTerms || state.isAllTermChecked) painterResource(
                        id = R.drawable.ic_check_selected
                    ) else painterResource(
                        id = R.drawable.ic_check
                    ),
                    contentDescription = stringResource(R.string.check),
                    modifier = modifier
                        .padding(horizontal = 8.dp)
                        .height(24.dp)
                        .width(24.dp)
                )

                Text(
                    text = stringResource(R.string.koin_essential_term),
                    color = Gray6,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .border(width = 1.dp, color = Blue1, shape = RectangleShape)
                    .padding(10.dp)
                    .height(143.dp)
                    .verticalScroll(scrollStateKoin)
            ) {
                Text(text = stringResource(R.string.term_2), fontSize = 10.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                modifier = modifier
                    .fillMaxWidth()
                    .height(44.dp),
                onClick = { viewModel.onNextButtonClicked() },
                shape = RoundedCornerShape(4.dp),
                colors = if (state.isAllTermChecked) ButtonDefaults.buttonColors(ColorPrimary) else ButtonDefaults.buttonColors(
                    Gray5
                ),

                ) {
                Text(text = stringResource(R.string.next))
            }

        }
    }
    viewModel.collectSideEffect {
        when (it) {
            CheckTermSideEffect.NavigateToNextScreen -> onNextClicked()
            CheckTermSideEffect.NavigateToBackScreen -> onBackClicked()
        }
    }
}

@Preview
@Composable
fun previewTermPage() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        CheckTermScreen()
    }
}