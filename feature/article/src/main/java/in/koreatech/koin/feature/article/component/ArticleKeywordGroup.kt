package `in`.koreatech.koin.feature.article.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.onboarding.ArrowDirection
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.core.onboarding.rememberOnboardingManager
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.util.horizontalFadingEdge

@Composable
fun ArticleKeywordGroup(
    keyWords: List<String>,
    selectedKeywordIndex: Int,
    modifier: Modifier = Modifier,
    navigateToKeywordFragment: () -> Unit = {},
    selectKeyword: (keyword: String) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val onboardingManager = rememberOnboardingManager()

    /**
     * TextStyle for keyword chip
     * for match design with xml based view
     */
    val textStyle = TextStyle(
        fontSize = 14.sp,
        fontStyle = FontStyle.Normal,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        ),
        letterSpacing = 0.2.sp,
        lineHeight = 20.sp
    )

    Row(
        modifier = modifier
            .padding(vertical = 16.dp, horizontal = 24.dp)
            .horizontalFadingEdge(scrollState, 24.dp, Color.White)
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(size = 999.dp)
                )
                .noRippleClickable {
                    EventLogger.logClickEvent(
                        EventAction.CAMPUS,
                        AnalyticsConstant.Label.MANAGE_KEYWORD,
                        "키워드관리"
                    )
                    navigateToKeywordFragment()
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.align(alignment = Alignment.Center),
                painter = painterResource(id = R.drawable.ic_go_to_keyword),
                contentDescription = "Keyword Setting"
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        ArticleTextChip(
            modifier = Modifier.defaultMinSize(
                minWidth = Dp.Unspecified,
                minHeight = 32.dp
            ),
            title = stringResource(R.string.keyword_see_all),
            isSelected = selectedKeywordIndex == 0,
            textStyle = textStyle,
            onSelect = {
                EventLogger.logClickEvent(
                    EventAction.CAMPUS,
                    AnalyticsConstant.Label.NOTICE_FILTER_ALL,
                    "모두보기"
                )
                selectKeyword("")
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        keyWords.forEachIndexed { index, it ->
            ArticleTextChip(
                modifier = Modifier.defaultMinSize(
                    minWidth = Dp.Unspecified,
                    minHeight = 32.dp
                ),
                title = "#$it",
                isSelected = selectedKeywordIndex == index + 1,
                textStyle = textStyle,
                onSelect = {
                    selectKeyword(it)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (keyWords.isEmpty()) {
            with(onboardingManager) {
                ShowOnboardingTooltipIfNeeded(
                    type = OnboardingType.ARTICLE_KEYWORD,
                    arrowDirection = ArrowDirection.TOP
                ) {
                    ArticleTextChip(
                        modifier = Modifier.defaultMinSize(
                            minWidth = Dp.Unspecified,
                            minHeight = 32.dp
                        ),
                        title = stringResource(R.string.keyword_add_new),
                        isSelected = false,
                        textStyle = textStyle,
                        onSelect = {
                            EventLogger.logClickEvent(
                                EventAction.CAMPUS,
                                AnalyticsConstant.Label.ADD_KEYWORD,
                                "키워드추가"
                            )
                            navigateToKeywordFragment()
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ArticleKeywordGroupPreview() {
    ArticleKeywordGroup(
        keyWords = listOf("키워드1", "키워드2", "키워드3"),
        navigateToKeywordFragment = {},
        selectKeyword = {},
        selectedKeywordIndex = 1
    )
}
