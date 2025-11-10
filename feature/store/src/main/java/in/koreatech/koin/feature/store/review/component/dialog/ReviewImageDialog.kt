package `in`.koreatech.koin.feature.store.review.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewImageDialog(
    imageUrls: ImmutableList<String>,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
    onDismiss: () -> Unit = { }
) {
    val pagerState = rememberPagerState(
        initialPage = initialPage.coerceIn(0, imageUrls.size - 1)
    ) {
        imageUrls.size
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        BasicText(
                            text = stringResource(
                                R.string.review_image_page_indicator,
                                pagerState.currentPage + 1,
                                pagerState.pageCount
                            ),
                            style = RebrandKoinTheme.typography.medium18.copy(color = RebrandKoinTheme.colors.neutral0)
                        )
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_close_round),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .size(24.dp)
                                .clickable { onDismiss() }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = RebrandKoinTheme.colors.neutral800,
                        titleContentColor = RebrandKoinTheme.colors.neutral800,
                        navigationIconContentColor = RebrandKoinTheme.colors.neutral0
                    )
                )
            },
            containerColor = RebrandKoinTheme.colors.neutral800
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(RebrandKoinTheme.colors.neutral800),
                contentAlignment = Alignment.Center
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth(),
                    pageSpacing = 16.dp,
                    beyondViewportPageCount = 1
                ) { pageIndex ->
                    val painter = rememberAsyncImagePainter(imageUrls[pageIndex])
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = "",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun ReviewImageDialogPreview() {
    val imageUrls = persistentListOf("", "", "")
    ReviewImageDialog(
        imageUrls = imageUrls,
        initialPage = 1,
        onDismiss = { }
    )
}
