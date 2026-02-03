package `in`.koreatech.koin.feature.lostandfound.ui.write

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
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.MAX_ITEM_COUNT
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.component.EditArticleDoneButton
import `in`.koreatech.koin.feature.lostandfound.component.EditArticleHeader
import `in`.koreatech.koin.feature.lostandfound.component.EditArticleItemDetail
import `in`.koreatech.koin.feature.lostandfound.component.EditArticleItemType
import `in`.koreatech.koin.feature.lostandfound.component.EditArticleUploadImage
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory.Companion.getCategoryKoreanWord
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import `in`.koreatech.koin.feature.lostandfound.ui.write.component.WriteArticleAddItemButton
import `in`.koreatech.koin.feature.lostandfound.ui.write.component.WriteArticleItemChip
import java.time.LocalDate
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LostAndFoundWriteArticle(
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
            onWriteComplete = onComplete
        )
    }
}

@Composable
fun LostAndFoundWriteArticleImpl(
    modifier: Modifier = Modifier,
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
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(WindowInsets.navigationBars)
                .imePadding(),
            containerColor = KoinTheme.colors.neutral0,
            bottomBar = {
                EditArticleDoneButton(
                    text = stringResource(id = R.string.write_done)
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

                itemsIndexed(itemList) { itemIndex, item ->
                    WriteFoundItemArticleImpl(
                        index = itemIndex,
                        shouldShowDelete = shouldShowItemRemoveButton,
                        articleData = item,
                        lostOrFoundType = item.lostOrFoundType,
                        isScrolling = isScrolling,
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
    modifier: Modifier = Modifier,
    index: Int,
    shouldShowDelete: Boolean = false,
    articleData: LostAndFoundWriteArticleItemState,
    lostOrFoundType: LostOrFoundType,
    isScrolling: Boolean,
    onAddImageClick: (uri: Uri) -> Unit = {},
    onRemoveImageClick: (index: Int) -> Unit = {},
    onRemoveItemClick: (index: Int) -> Unit = {},
    onChangeItemType: (itemType: LostItemCategory) -> Unit = {},
    onUpdateDescription: (description: String) -> Unit = {},
    onUpdateLocation: (location: String) -> Unit = {},
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

        EditArticleUploadImage(
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

        EditArticleItemType(
            selectedChipIndex = articleData.category.id,
            itemTypeRequired = articleData.itemTypeRequired
        ) {
            onChangeItemType(LostItemCategory.entries[it])
        }

        EditArticleItemDetail(
            type = lostOrFoundType,
            moreDescription = articleData.content ?: "",
            onMoreDescriptionChange = { onUpdateDescription(it) },
            location = articleData.foundPlace,
            locationRequired = if (lostOrFoundType == LostOrFoundType.FOUND) articleData.locationRequired else false, // Lost item doesn't require location
            onLocationChange = { onUpdateLocation(it) },
            date = articleData.foundDate,
            dateRequired = articleData.dateRequired,
            shouldCollapse = isScrolling,
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
