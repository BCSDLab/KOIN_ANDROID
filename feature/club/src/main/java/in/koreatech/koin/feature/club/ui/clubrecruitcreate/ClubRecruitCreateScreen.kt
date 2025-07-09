package `in`.koreatech.koin.feature.club.ui.clubrecruitcreate

import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.component.KoinClubBasicTextField
import `in`.koreatech.koin.feature.club.component.KoinClubDateSelectBox
import `in`.koreatech.koin.feature.club.utils.pickMedia

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ClubRecruitCreateScreen(
    modifier: Modifier = Modifier,
    onTopbarBackClick: () -> Unit = {}
    // viewModel: ClubRecruitCreateViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    // val uiState by viewModel.collectAsState()

    Scaffold(
        modifier = modifier.imePadding(),
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.club_recruit_create_title),
                onNavigationIconClick = onTopbarBackClick
            )
        },
        // Let's try to draw content behind the system bars
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { contentPadding ->
        ClubRecruitCreateScreenImpl(
            modifier = Modifier
                .padding(contentPadding)
        )
    }
}

@Composable
fun ClubRecruitCreateScreenImpl(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    uploadImage: (fileSize: Long, fileType: String, fileName: String, fileUri: Uri) -> Unit = { _, _, _, _ -> },
) {
    val permanentRecruit = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val pickMedia = pickMedia(
        context = context,
        onResult = uploadImage
    )
    var descriptionText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "모집 기한",
                    style = KoinTheme.typography.medium18
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "상시 모집",
                        style = KoinTheme.typography.medium18
                    )
                    Checkbox(
                        checked = permanentRecruit.value,
                        onCheckedChange = { permanentRecruit.value = it }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                KoinClubDateSelectBox(
                    text = "2025년\n12월 30일 (화)",
                    onClick = { }
                )
                Text(
                    text = "~",
                    style = KoinTheme.typography.bold20,
                    textAlign = TextAlign.Center
                )
                KoinClubDateSelectBox(
                    text = "2026년\n12월 30일 (목)",
                    onClick = { }
                )
            }
        }
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 5f)
                    .clip(KoinTheme.shapes.extraLarge)
                    .border(1.dp, Color.Unspecified, KoinTheme.shapes.extraLarge)
                    .background(KoinTheme.colors.neutral200)
                    .clickable {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (imageUrl.isEmpty()) {
                    Image(
                        modifier = Modifier,
                        imageVector = ImageVector.vectorResource(R.drawable.ic_club_create_upload_logo),
                        contentDescription = null
                    )
                } else {
                    SubcomposeAsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    )
                }
                Text(
                    text = stringResource(R.string.club_recruit_create_logo_description),
                    style = KoinTheme.typography.medium18,
                    color = KoinTheme.colors.neutral500
                )
            }
            Text(
                text = "4:5 비율로 업로드 해주세요.\n사진을 클릭하여 수정할 수 있습니다.",
                style = KoinTheme.typography.regular12,
                color = KoinTheme.colors.neutral500
            )
        }
        Column (
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "상세 설명",
                style = KoinTheme.typography.medium18
            )
            KoinClubBasicTextField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                modifier = Modifier
                    .fillMaxWidth(),
                minLines = 2,
                maxLength = 255,
                hint = "상세 내용을 적어주세요."
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FilledButton(
                text = "생성 취소",
                onClick = { }, //TODO create cancel
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 5.dp)
            )
            Spacer(Modifier.width(8.dp))
            FilledButton(
                text = "생성 하기",
                onClick = { }, //TODO create
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 5.dp)
            )
        }
    }
}

@Preview
@Composable
fun ClubRecruitCreateScreenImplPreview() {
    ClubRecruitCreateScreenImpl(
        modifier = Modifier.background(KoinTheme.colors.neutral0)
    )
}