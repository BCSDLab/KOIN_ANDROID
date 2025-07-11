package `in`.koreatech.koin.feature.club.ui.clubdetail.recruit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.model.ParcelizeClubRecruitment
import `in`.koreatech.koin.feature.club.model.RecruitmentStatus

@Composable
fun ClubDetailRecruit(
    recruitment: ParcelizeClubRecruitment?,
    modifier: Modifier = Modifier,
    showProgressBar: Boolean = false,
    onImageClick: (String) -> Unit = {},
    onRecruitCreateClick: () -> Unit = {},
    isManager: Boolean = false
) {
    val context = LocalContext.current
    Box {
        if (showProgressBar) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                )
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(
                        top = 16.dp,
                        start = 24.dp,
                        end = 24.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (recruitment == null || recruitment.status == RecruitmentStatus.NONE) {
                    if (!isManager) {
                        Spacer(Modifier.height(200.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.detail_recruit_end),
                            style = KoinTheme.typography.medium18,
                            color = KoinTheme.colors.neutral500,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            Arrangement.End
                        ) {
                            FilledButton(
                                text = stringResource(R.string.detail_recruit_create),
                                onClick = { onRecruitCreateClick() },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                    }
                } else {
                    Column {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.detail_recruit_deadline_header),
                            style = KoinTheme.typography.medium16,
                            color = KoinTheme.colors.primary600
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement
                                .spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            var backgroungColor = KoinTheme.colors.primary400
                            var innerText = ""
                            when (recruitment.status) {
                                RecruitmentStatus.RECRUITING -> {
                                    innerText = stringResource(R.string.club_recruit_dday, recruitment.dday ?: -1)
                                }
                                RecruitmentStatus.ALWAYS -> {
                                    backgroungColor = KoinTheme.colors.primary500
                                    innerText = stringResource(R.string.club_recruit_always)
                                }
                                RecruitmentStatus.CLOSED -> {
                                    backgroungColor = KoinTheme.colors.primary700
                                    innerText = stringResource(R.string.club_recruit_closed)
                                }
                                RecruitmentStatus.BEFORE -> {
                                    innerText = stringResource(R.string.club_recruit_before)
                                }
                                else -> {}
                            }
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = backgroungColor,
                                        shape = KoinTheme.shapes.extraLarge
                                    )
                                    .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = innerText,
                                    style = KoinTheme.typography.medium14,
                                    color = KoinTheme.colors.neutral0
                                )
                            }
                            if (recruitment.status != RecruitmentStatus.ALWAYS) {
                                Text(
                                    text = stringResource(
                                        R.string.club_recruit_period,
                                        recruitment.startDate,
                                        recruitment.endDate
                                    ),
                                    style = KoinTheme.typography.medium15
                                )
                            }
                        }
                    }
                    if (!recruitment.imageUrl.isNullOrBlank()) {
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(4f / 5f)
                                .clickable {
                                    onImageClick(recruitment.imageUrl)
                                },
                            model = ImageRequest.Builder(context)
                                .data(recruitment.imageUrl)
                                .size(400)
                                .build(),
                            contentDescription = "Recruit Image",
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            loading = {
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            },
                            error = {
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(stringResource(R.string.detail_club_image_error))
                                }
                            }
                        )
                    }
                }
                Text(
                    text = recruitment?.content ?: "",
                    style = KoinTheme.typography.regular16
                )
            }
        }
    }
}

@Preview
@Composable
fun ClubDetailRecruitNone() {
    ClubDetailRecruit(
        recruitment = ParcelizeClubRecruitment(
            id = 0,
            status = RecruitmentStatus.NONE,
            dday = null,
            startDate = "",
            endDate = "",
            imageUrl = null,
            content = "",
            isManager = false
        ),
        modifier = Modifier.background(color = KoinTheme.colors.neutral0)
    )
}

@Preview
@Composable
fun ClubDetailRecruitNoneManager() {
    ClubDetailRecruit(
        recruitment = ParcelizeClubRecruitment(
            id = 0,
            status = RecruitmentStatus.NONE,
            dday = null,
            startDate = "",
            endDate = "",
            imageUrl = null,
            content = "",
            isManager = false
        ),
        isManager = true,
        modifier = Modifier.background(color = KoinTheme.colors.neutral0)
    )
}

@Preview
@Composable
fun ClubDetailRecruitDday() {
    ClubDetailRecruit(
        recruitment = ParcelizeClubRecruitment(
            id = 0,
            status = RecruitmentStatus.RECRUITING,
            dday = 4,
            startDate = "2025.07.04",
            endDate = "2025.07.13",
            imageUrl = "https://bcsdlab.com/static/img/logo.d89d9cc.png",
            content = "BCSD LAB 모집",
            isManager = false
        ),
        modifier = Modifier.background(color = KoinTheme.colors.neutral0)
    )
}

@Preview
@Composable
fun ClubDetailRecruitNoImage() {
    ClubDetailRecruit(
        recruitment = ParcelizeClubRecruitment(
            id = 0,
            status = RecruitmentStatus.RECRUITING,
            dday = 4,
            startDate = "2025.07.04",
            endDate = "2025.07.13",
            imageUrl = null,
            content = "BCSD LAB 모집",
            isManager = false
        ),
        modifier = Modifier.background(color = KoinTheme.colors.neutral0)
    )
}
