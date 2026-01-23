package `in`.koreatech.koin.feature.lostandfound.ui.modify

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.EditArticleDoneButton
import `in`.koreatech.koin.feature.lostandfound.component.EditArticleHeader
import `in`.koreatech.koin.feature.lostandfound.component.EditArticleItemDetail
import `in`.koreatech.koin.feature.lostandfound.component.EditArticleItemType
import `in`.koreatech.koin.feature.lostandfound.component.EditArticleUploadImage
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory.Companion.getCategoryKoreanWord
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import java.time.LocalDate
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LostAndFoundModify(
    onBackClick: () -> Unit,
    onComplete: (Int) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.top_container_text),
                        style = KoinTheme.typography.medium18,
                        color = KoinTheme.colors.neutral800
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                            contentDescription = stringResource(R.string.top_container_icon),
                            tint = KoinTheme.colors.neutral800
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KoinTheme.colors.neutral0
                )
            )
        }
    ) { innerPadding ->
        LostAndFoundWriteArticleImpl(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            onBackClick = onBackClick,
            onComplete = onComplete
        )
    }
}

@Composable
fun LostAndFoundWriteArticleImpl(
    modifier: Modifier = Modifier,
    viewModel: LostAndFoundModifyViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onComplete: (Int) -> Unit
) {
    val context = LocalContext.current

    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect {
        handleSideEffect(
            sideEffect = it,
            context = context,
            onComplete = {
                onComplete(uiState.articleId)
            },
            onBackClick = onBackClick
        )
    }

    KoinTheme {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(WindowInsets.navigationBars)
                .imePadding(),
            containerColor = KoinTheme.colors.neutral0,
            bottomBar = {
                EditArticleDoneButton(
                    text = stringResource(R.string.modify_done)
                ) {
                    when (uiState.lostOrFoundType) {
                        LostOrFoundType.FOUND ->
                            EventLogger.logCampusClickEvent(
                                AnalyticsConstant.Label.LostAndFound.FIND_USER_WRITE_CONFIRM,
                                "작성 완료"
                            )

                        LostOrFoundType.LOST ->
                            EventLogger.logCampusClickEvent(
                                AnalyticsConstant.Label.LostAndFound.LOST_ITEM_WRITE_CONFIRM,
                                "작성 완료"
                            )
                    }
                    viewModel.checkAllFieldValidAndModify()
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { contentPadding ->
            val lazyColumnState = rememberLazyListState()

            val isScrolling = lazyColumnState.isScrollInProgress

            LazyColumn(
                state = lazyColumnState,
                modifier = Modifier
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
            ) {
                item {
                    EditArticleHeader(type = uiState.lostOrFoundType)
                }

                item {
                    ModifyFoundItemArticleImpl(
                        articleData = uiState,
                        lostOrFoundType = uiState.lostOrFoundType,
                        isScrolling = isScrolling,
                        refreshDatePicker = uiState.refreshDatePicker,
                        onAddImageClick = { uri ->
                            val cursor = context.contentResolver.query(uri, null, null, null, null)
                            cursor.use {
                                if (cursor != null && cursor.moveToFirst()) {
                                    val fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                                    val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                                    if (fileNameIndex != -1 && fileSizeIndex != -1) {
                                        val fileName = cursor.getString(fileNameIndex)
                                        val fileSize = cursor.getLong(fileSizeIndex)
                                        val fileType =
                                            context.contentResolver.getType(uri)
                                                ?: "image/${fileName.split(".").last()}"

                                        viewModel.uploadImage(
                                            fileName,
                                            fileType,
                                            fileSize,
                                            uri
                                        )
                                    }
                                }
                            }
                        },
                        onRemoveImageClick = { index ->
                            viewModel.removeImage(index)
                        },
                        onChangeItemType = { itemType ->
                            EventLogger.logCampusClickEvent(
                                when (uiState.lostOrFoundType) {
                                    LostOrFoundType.FOUND -> AnalyticsConstant.Label.LostAndFound.FIND_USER_CATEGORY
                                    LostOrFoundType.LOST -> AnalyticsConstant.Label.LostAndFound.LOST_ITEM_CATEGORY
                                },
                                itemType.getCategoryKoreanWord()
                            )
                            viewModel.updateItemType(itemType)
                        },
                        onUpdateDescription = { description ->
                            viewModel.updateDescription(description)
                        },
                        onUpdateLocation = { foundPlace ->
                            viewModel.updateLocation(foundPlace)
                        },
                        onDateChange = { date ->
                            viewModel.updateDate(date)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ModifyFoundItemArticleImpl(
    modifier: Modifier = Modifier,
    articleData: LostAndFoundModifyState,
    lostOrFoundType: LostOrFoundType,
    isScrolling: Boolean,
    refreshDatePicker: Boolean,
    onAddImageClick: (uri: Uri) -> Unit = {},
    onRemoveImageClick: (index: Int) -> Unit = {},
    onChangeItemType: (itemType: LostItemCategory) -> Unit = {},
    onUpdateDescription: (description: String) -> Unit = {},
    onUpdateLocation: (location: String) -> Unit = {},
    onDateChange: (date: LocalDate) -> Unit = {}
) {
    val pickMultipleMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
            if (uris.isNotEmpty()) {
                uris.forEach {
                    onAddImageClick(it)
                }
            }
        }

    HorizontalDivider(thickness = 6.dp, color = KoinTheme.colors.neutral100)

    Column(
        modifier = modifier.padding(vertical = 16.dp, horizontal = 24.dp)
    ) {
        EditArticleUploadImage(
            type = lostOrFoundType,
            imageList = articleData.images,
            uploadedImageCount = articleData.images.size,
            onUploadImage = {
                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onRemoveImage = { index ->
                onRemoveImageClick(index)
            }
        )

        EditArticleItemType(
            selectedChipIndex = articleData.category.id,
            itemTypeRequired = articleData.itemTypeRequired
        ) {
            onChangeItemType(LostItemCategory.entries[it])
        }

        key(refreshDatePicker) {
            EditArticleItemDetail(
                type = lostOrFoundType,
                moreDescription = articleData.content ?: "",
                onMoreDescriptionChange = { onUpdateDescription(it) },
                location = articleData.foundPlace,
                locationRequired = if (lostOrFoundType == LostOrFoundType.FOUND) articleData.locationRequired else false, // Lost item doesn't require location
                onLocationChange = { onUpdateLocation(it) },
                date = articleData.foundDate,
                dateRequired = false, // Modify doesn't have null date
                shouldCollapse = isScrolling,
                onDateChange = { onDateChange(it) }
            )
        }
    }
}

fun handleSideEffect(
    sideEffect: LostAndFoundModifySideEffect,
    context: Context,
    onComplete: () -> Unit,
    onBackClick: () -> Unit
) {
    when (sideEffect) {
        is LostAndFoundModifySideEffect.LostAndFoundModifyArticle -> {
            Toast.makeText(
                context,
                context.getString(R.string.modify_success),
                Toast.LENGTH_SHORT
            ).show()
            onComplete()
        }

        LostAndFoundModifySideEffect.DeletedArticle -> {
            Toast.makeText(
                context,
                context.getString(R.string.detail_deleted_article),
                Toast.LENGTH_SHORT
            ).show()
            onBackClick()
        }

        LostAndFoundModifySideEffect.UploadedMaxImage -> {
            Toast.makeText(
                context,
                context.getString(R.string.upload_image_failed_by_max),
                Toast.LENGTH_SHORT
            ).show()
        }

        LostAndFoundModifySideEffect.FailedToUploadImage -> {
            Toast.makeText(
                context,
                context.getString(R.string.upload_image_failed),
                Toast.LENGTH_SHORT
            ).show()
        }

        LostAndFoundModifySideEffect.LostAndFoundModifyArticleFailed -> {
            Toast.makeText(context, context.getString(R.string.modify_failed), Toast.LENGTH_SHORT)
                .show()
        }
    }
}
