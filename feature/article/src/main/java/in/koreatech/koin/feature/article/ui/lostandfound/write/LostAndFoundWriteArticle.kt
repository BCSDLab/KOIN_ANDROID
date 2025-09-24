package `in`.koreatech.koin.feature.article.ui.lostandfound.write

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
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.article.MAX_ITEM_COUNT
import `in`.koreatech.koin.feature.article.R
import `in`.koreatech.koin.feature.article.enums.LostItemCategory
import `in`.koreatech.koin.feature.article.enums.LostItemCategory.Companion.getCategoryKoreanWord
import `in`.koreatech.koin.feature.article.enums.LostOrFoundType
import `in`.koreatech.koin.feature.article.ui.lostandfound.write.component.WriteArticleAddItemButton
import `in`.koreatech.koin.feature.article.ui.lostandfound.write.component.WriteArticleDoneButton
import `in`.koreatech.koin.feature.article.ui.lostandfound.write.component.WriteArticleHeader
import `in`.koreatech.koin.feature.article.ui.lostandfound.write.component.WriteArticleItemChip
import `in`.koreatech.koin.feature.article.ui.lostandfound.write.component.WriteArticleItemDetail
import `in`.koreatech.koin.feature.article.ui.lostandfound.write.component.WriteArticleItemType
import `in`.koreatech.koin.feature.article.ui.lostandfound.write.component.WriteArticleUploadImage
import java.time.LocalDate
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LostAndFoundWriteArticle(
    viewModel: LostAndFoundWriteArticleViewModel = hiltViewModel(),
    onWriteComplete: (articleId: Int) -> Unit = {}
) {
    val context = LocalContext.current
    viewModel.collectSideEffect {
        handleSideEffect(it, viewModel, context, onWriteComplete)
    }
    val uiState by viewModel.collectAsState()

    KoinTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(WindowInsets.navigationBars)
                .imePadding(),
            containerColor = KoinTheme.colors.neutral0,
            bottomBar = {
                WriteArticleDoneButton {
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
                    viewModel.checkAllFieldValid()
                }
            },
            // Fix wrong top padding value
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { contentPadding ->
            val itemList = uiState.itemList
            var shouldShowItemRemoveButton by remember { mutableStateOf(false) }
            var shouldShowItemAddButton by remember { mutableStateOf(true) }

            LaunchedEffect(itemList) {
                shouldShowItemRemoveButton = itemList.size > 1
                shouldShowItemAddButton = itemList.size < MAX_ITEM_COUNT
            }

            var shouldShowDatePicker by remember { mutableStateOf(false) }

            val lazyColumnState = rememberLazyListState()

            LaunchedEffect(lazyColumnState.isScrollInProgress) {
                if (lazyColumnState.isScrollInProgress) {
                    shouldShowDatePicker = false
                }
            }

            LazyColumn(
                state = lazyColumnState,
                modifier = Modifier
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
            ) {
                item {
                    WriteArticleHeader(type = uiState.lostOrFoundType)
                }

                itemsIndexed(itemList) { itemIndex, item ->
                    WriteFoundItemArticleImpl(
                        index = itemIndex,
                        shouldShowDelete = shouldShowItemRemoveButton,
                        articleData = item,
                        lostOrFoundType = item.lostOrFoundType,
                        showDatePicker = shouldShowDatePicker,
                        onAddImageClick = { uri ->
                            viewModel.addImage(itemIndex, uri)
                        },
                        onRemoveImageClick = { index ->
                            viewModel.removeImage(itemIndex, index)
                        },
                        onRemoveItemClick = { index ->
                            viewModel.removeItem(index)
                        },
                        onChangeItemType = { itemType ->
                            EventLogger.logCampusClickEvent(
                                when (uiState.lostOrFoundType) {
                                    LostOrFoundType.FOUND -> AnalyticsConstant.Label.LostAndFound.FIND_USER_CATEGORY
                                    LostOrFoundType.LOST -> AnalyticsConstant.Label.LostAndFound.LOST_ITEM_CATEGORY
                                },
                                itemType.getCategoryKoreanWord()
                            )
                            viewModel.updateItemType(itemIndex, itemType)
                        },
                        onUpdateDescription = { description ->
                            viewModel.updateDescription(itemIndex, description)
                        },
                        onUpdateLocation = { foundPlace ->
                            viewModel.updateLocation(itemIndex, foundPlace)
                        },
                        onShowDatePickerChange = { showDatePicker ->
                            shouldShowDatePicker = showDatePicker
                        },
                        onDateChange = { date ->
                            viewModel.updateDate(itemIndex, date)
                        }
                    )
                }

                if (shouldShowItemAddButton) {
                    item {
                        WriteArticleAddItemButton(
                            modifier = Modifier.padding(end = 24.dp, bottom = 16.dp)
                        ) {
                            EventLogger.logCampusClickEvent(
                                when (uiState.lostOrFoundType) {
                                    LostOrFoundType.FOUND -> AnalyticsConstant.Label.LostAndFound.FIND_USER_ADD_ITEM
                                    LostOrFoundType.LOST -> AnalyticsConstant.Label.LostAndFound.LOST_ITEM_ADD_ITEM
                                },
                                "물품 추가"
                            )
                            viewModel.addItem(
                                LostAndFoundWriteArticleItemState(
                                    lostOrFoundType = uiState.lostOrFoundType
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WriteFoundItemArticleImpl(
    index: Int,
    shouldShowDelete: Boolean = false,
    articleData: LostAndFoundWriteArticleItemState,
    lostOrFoundType: LostOrFoundType,
    showDatePicker: Boolean,
    modifier: Modifier = Modifier,
    onAddImageClick: (uri: Uri) -> Unit = {},
    onRemoveImageClick: (index: Int) -> Unit = {},
    onRemoveItemClick: (index: Int) -> Unit = {},
    onChangeItemType: (itemType: LostItemCategory) -> Unit = {},
    onUpdateDescription: (description: String) -> Unit = {},
    onUpdateLocation: (location: String) -> Unit = {},
    onShowDatePickerChange: (showDatePicker: Boolean) -> Unit = {},
    onDateChange: (date: LocalDate?) -> Unit = {}
) {
    val pickMultipleMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
            if (uris.isNotEmpty()) {
                uris.forEach {
                    onAddImageClick(it)
                }
            }
        }

    val imageList = articleData.images

    HorizontalDivider(thickness = 6.dp, color = KoinTheme.colors.neutral100)

    Column(
        modifier = modifier.padding(vertical = 16.dp, horizontal = 24.dp)
    ) {
        WriteArticleItemChip(
            type = lostOrFoundType,
            index = index,
            shouldShowDelete = shouldShowDelete
        ) {
            onRemoveItemClick(index)
        }

        WriteArticleUploadImage(
            type = lostOrFoundType,
            imageList = imageList,
            uploadedImageCount = imageList.size,
            onUploadImage = {
                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onRemoveImage = { index ->
                onRemoveImageClick(index)
            }
        )

        WriteArticleItemType(
            selectedChipIndex = articleData.category.id,
            itemTypeRequired = articleData.itemTypeRequired
        ) {
            onChangeItemType(LostItemCategory.entries[it])
        }

        WriteArticleItemDetail(
            type = lostOrFoundType,
            moreDescription = articleData.content ?: "",
            onMoreDescriptionChange = { onUpdateDescription(it) },
            location = articleData.foundPlace,
            locationRequired = if (lostOrFoundType == LostOrFoundType.FOUND) articleData.locationRequired else false, // Lost item doesn't require location
            onLocationChange = { onUpdateLocation(it) },
            date = articleData.foundDate,
            dateRequired = articleData.dateRequired,
            showDatePicker = showDatePicker,
            onShowDatePickerChange = onShowDatePickerChange,
            onDateChange = { onDateChange(it) }
        )
    }
}

fun handleSideEffect(
    sideEffect: LostAndFoundWriteArticleSideEffect,
    viewModel: LostAndFoundWriteArticleViewModel,
    context: Context,
    onWriteComplete: (articleId: Int) -> Unit = {}
) {
    when (sideEffect) {
        /*
        is LostAndFoundWriteArticleSideEffect.AddItem -> {}
        is LostAndFoundWriteArticleSideEffect.RemoveItem -> {}
        is LostAndFoundWriteArticleSideEffect.UpdateItemType -> {}
         */
        is LostAndFoundWriteArticleSideEffect.AddImage -> {
            if (sideEffect.tooManyImage) {
                return
            }

            val itemIndex = sideEffect.itemIndex
            val imageIndex = sideEffect.imageIndex
            val imageContextUri = sideEffect.imageUri

            val cursor = context.contentResolver.query(imageContextUri, null, null, null, null)
            cursor.use {
                if (cursor != null && cursor.moveToFirst()) {
                    val fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

                    if (fileNameIndex != -1 && fileSizeIndex != -1) {
                        val fileName = cursor.getString(fileNameIndex)
                        val fileSize = cursor.getLong(fileSizeIndex)
                        val fileType =
                            context.contentResolver.getType(imageContextUri)
                                ?: "image/${fileName.split(".").last()}"

                        viewModel.getPreSignedUrl(
                            fileSize,
                            fileType,
                            fileName,
                            imageContextUri,
                            itemIndex,
                            imageIndex
                        )
                    }
                }
            }
        }

        /*
        is LostAndFoundWriteArticleSideEffect.RemoveImage -> {}
        is LostAndFoundWriteArticleSideEffect.UpdateDescription -> {}
        is LostAndFoundWriteArticleSideEffect.UpdateLocation -> {}
        is LostAndFoundWriteArticleSideEffect.UpdateDate -> {}
         */
        is LostAndFoundWriteArticleSideEffect.LostAndFoundWriteArticle -> {
            onWriteComplete(sideEffect.articleId)
        }

        is LostAndFoundWriteArticleSideEffect.CheckAllFieldValid -> {
            var isAllFieldValid = true
            sideEffect.itemList.forEachIndexed { index, it ->
                if (it.category == LostItemCategory.NONE) {
                    viewModel.updateItemType(index, LostItemCategory.NONE)
                    isAllFieldValid = false
                }
                if (it.lostOrFoundType == LostOrFoundType.FOUND && it.foundPlace.isEmpty()) {
                    viewModel.updateLocation(index, "")
                    isAllFieldValid = false
                }
                if (it.foundDate == null) {
                    viewModel.updateDate(index, null)
                    isAllFieldValid = false
                }
            }

            if (isAllFieldValid) {
                viewModel.writeArticle()
            }
        }

        LostAndFoundWriteArticleSideEffect.FailedToUploadImage -> {
            Toast.makeText(
                context,
                context.getString(R.string.upload_image_failed),
                Toast.LENGTH_SHORT
            ).show()
        }

        LostAndFoundWriteArticleSideEffect.LostAndFoundWriteArticleFailed -> {
            Toast.makeText(context, context.getString(R.string.write_failed), Toast.LENGTH_SHORT)
                .show()
        }
    }
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}
