# FEATURE LostAndFound Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working on the FEATURE LOSTANDFOUND module.

## Module Overview

> **IMPORTANT**: The `feature:lostandfound` module exists as a placeholder. **All Lost & Found implementation code is located in `feature:article` module** under `ui/lostandfound/` package. Similarly, **UseCases are in `domain` module** under `usecase/article/lostandfound/`.

The Lost & Found feature manages lost and found item postings with image uploads, pagination, and reporting functionality.

## Core Responsibilities

1. **Article Listing**: Paginated list of lost/found items with filtering by type
2. **Article Creation**: Post new lost/found articles with multiple items and images
3. **Article Detail**: View detailed information about items
4. **Image Upload**: Upload multiple images via pre-signed URLs
5. **Reporting**: Report inappropriate articles with selectable reasons
6. **Hot Articles**: Display popular/trending articles

## Package Structure

```
feature/article/src/main/java/in/koreatech/koin/feature/article/ui/lostandfound/
├── LostAndFoundViewModel.kt              # Main ViewModel (user status, UI state)
├── LostAndFoundState.kt                  # Main state
├── LostAndFound.kt                       # Main Compose screen
├── component/                            # Compose components
│   ├── LostAndFoundPagination.kt
│   ├── LostAndFoundDropdownGroup.kt
│   ├── LostAndFoundKeywordGroup.kt
│   ├── LostAndFoundItem.kt
│   ├── LostAndFoundFAB.kt
│   └── LostAndFoundDialog.kt
├── detail/
│   ├── LostAndFoundDetailViewModel.kt    # Detail ViewModel
│   ├── LostAndFoundDetailState.kt
│   ├── LostAndFoundDetailSideEffect.kt
│   ├── LostAndFoundDetail.kt             # Detail Compose screen
│   └── component/
├── write/
│   ├── LostAndFoundWriteArticleViewModel.kt  # Write ViewModel
│   ├── LostAndFoundWriteArticleState.kt
│   ├── LostAndFoundWriteArticleSideEffect.kt
│   ├── LostAndFoundWriteArticle.kt
│   ├── ArticleLostAndFoundWriteLostFragment.kt
│   ├── ArticleLostAndFoundWriteFoundFragment.kt
│   └── component/
└── report/
    ├── LostAndFoundReportViewModel.kt    # Report ViewModel
    ├── LostAndFoundReportState.kt
    ├── LostAndFoundReportSideEffect.kt
    └── component/

domain/src/main/java/in/koreatech/koin/domain/usecase/article/lostandfound/
├── FetchLostAndFoundArticlePaginationUseCase.kt
├── FetchLostAndFoundArticleUseCase.kt
├── FetchSearchedLostAndFoundArticlesUseCase.kt
├── FetchHotArticlesUseCase.kt
├── UploadLostAndFoundArticleUseCase.kt
├── ReportLostAndFoundArticleUseCase.kt
└── DeleteArticleLostAndFoundUseCase.kt
```

## UseCase Reference

**CRITICAL**: All Lost & Found UseCases are under `domain.usecase.article.lostandfound` package.

| UseCase | Signature | Return Type |
|---------|-----------|-------------|
| `FetchLostAndFoundArticlePaginationUseCase` | `invoke(page: Int, limit: Int, type: String?)` | `Flow<ArticleLostAndFoundPagination>` |
| `FetchLostAndFoundArticleUseCase` | `invoke(articleId: Int)` | `Flow<ArticleLostAndFound>` |
| `FetchSearchedLostAndFoundArticlesUseCase` | `invoke(query: String, page: Int, limit: Int)` | `Flow<ArticleLostAndFoundPagination>` |
| `FetchHotArticlesUseCase` | `invoke()` | `Flow<List<ArticleHeader>>` |
| `UploadLostAndFoundArticleUseCase` | `invoke(articleLostAndFoundList: List<ArticleLostAndFoundUpload>)` | `Result<ArticleLostAndFound>` |
| `ReportLostAndFoundArticleUseCase` | `invoke(articleId: Int, articleLostAndFoundReportItem: List<ArticleLostAndFoundReportItem>)` | `Result<Unit>` |
| `DeleteArticleLostAndFoundUseCase` | `invoke(articleId: Int)` | `Result<Unit>` |

## Implementation Patterns

### Main LostAndFoundViewModel (ACTUAL)

The main ViewModel handles **user status and UI state only**, NOT pagination:

```kotlin
@HiltViewModel
class LostAndFoundViewModel @Inject constructor(
    private val getUserStatusUseCase: GetUserStatusUseCase
) : ViewModel(), ContainerHost<LostAndFoundState, Unit> {
    override val container = container<LostAndFoundState, Unit>(LostAndFoundState())

    init {
        getUserType()
    }

    fun getUserType() = intent {
        getUserStatusUseCase().collectLatest { user ->
            when (user) {
                is User.Student -> reduce {
                    state.copy(isAnonymous = false, userType = user.userType)
                }
                is User.General -> reduce {
                    state.copy(isAnonymous = false, userType = user.userType)
                }
                User.Anonymous -> reduce {
                    state.copy(isAnonymous = true)
                }
            }
        }
    }

    fun setShowLoginRequestDialog(showDialog: Boolean) = intent {
        reduce { state.copy(showLoginRequestDialog = showDialog) }
    }

    fun setFabDialogExpanded(isExpanded: Boolean) = intent {
        reduce { state.copy(isFabDialogExpanded = isExpanded) }
    }

    fun setDropdownExpanded(isExpanded: Boolean) = intent {
        reduce { state.copy(isDropdownExpanded = isExpanded) }
    }
}

@Parcelize
data class LostAndFoundState(
    val isLoading: Boolean = false,
    val showLoginRequestDialog: Boolean = false,
    val isFabDialogExpanded: Boolean = false,
    val isDropdownExpanded: Boolean = false,
    val isAnonymous: Boolean = false,
    val userType: String = ""
) : Parcelable
```

### Write Article ViewModel (ACTUAL)

The write ViewModel handles multi-item article creation with image uploads:

```kotlin
@HiltViewModel
class LostAndFoundWriteArticleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val uploadLostAndFoundArticleUseCase: UploadLostAndFoundArticleUseCase,
    private val getLostAndFoundPreSignedUrlUseCase: GetLostAndFoundPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase
) : ViewModel(), ContainerHost<LostAndFoundWriteArticleState, LostAndFoundWriteArticleSideEffect> {
    
    override val container = container<LostAndFoundWriteArticleState, LostAndFoundWriteArticleSideEffect>(
        LostAndFoundWriteArticleState(),
        savedStateHandle
    ) {
        val rawLostOrFoundType = savedStateHandle.get<String>(LOST_OR_FOUND_TYPE)
        val lostOrFoundType = LostOrFoundType.entries.find { it.name == rawLostOrFoundType } ?: LostOrFoundType.FOUND
        setLostOrFoundType(lostOrFoundType)
        addItem(LostAndFoundWriteArticleItemState(lostOrFoundType = lostOrFoundType))
    }

    // Write article - takes list of ArticleLostAndFoundUpload
    fun writeArticle() = viewModelScope.launch {
        intent {
            uploadLostAndFoundArticleUseCase(
                state.itemList.map { it.toArticleLostAndFoundUpload() }
            ).onSuccess {
                postSideEffect(LostAndFoundWriteArticleSideEffect.LostAndFoundWriteArticle(it.id))
            }.onFailure {
                postSideEffect(LostAndFoundWriteArticleSideEffect.LostAndFoundWriteArticleFailed)
            }
        }
    }

    // Image upload with pre-signed URL
    fun getPreSignedUrl(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: Uri,
        itemIndex: Int,
        imageIndex: Int
    ) = viewModelScope.launch {
        getLostAndFoundPreSignedUrlUseCase(fileSize, fileType, fileName)
            .onSuccess { (fileUrl, preSignedUrl) ->
                uploadImage(preSignedUrl, fileUrl, fileType, fileSize, imageUri, itemIndex, imageIndex)
            }
            .onFailure {
                intent { postSideEffect(LostAndFoundWriteArticleSideEffect.FailedToUploadImage) }
            }
    }

    companion object {
        const val LOST_OR_FOUND_TYPE = "lost_or_found_type"
    }
}

sealed class LostAndFoundWriteArticleSideEffect {
    data class LostAndFoundWriteArticle(val articleId: Int) : LostAndFoundWriteArticleSideEffect()
    data object LostAndFoundWriteArticleFailed : LostAndFoundWriteArticleSideEffect()
    data object FailedToUploadImage : LostAndFoundWriteArticleSideEffect()
    data class AddImage(
        val itemIndex: Int,
        val imageIndex: Int,
        val imageUri: Uri,
        val isMaxImageCountExceeded: Boolean
    ) : LostAndFoundWriteArticleSideEffect()
    data class CheckAllFieldValid(val items: List<LostAndFoundWriteArticleItemState>) : LostAndFoundWriteArticleSideEffect()
}
```

### Report ViewModel (ACTUAL)

The report ViewModel handles article reporting with **list of report items**:

```kotlin
@HiltViewModel
class LostAndFoundReportViewModel @Inject constructor(
    private val reportLostAndFoundArticleUseCase: ReportLostAndFoundArticleUseCase
) : ViewModel(), ContainerHost<LostAndFoundReportState, LostAndFoundReportSideEffect> {
    
    override val container = container<LostAndFoundReportState, LostAndFoundReportSideEffect>(LostAndFoundReportState())

    fun setReportReason(reportReason: ReportReason) = intent {
        if (lostAndFoundReportReasonList.indexOf(reportReason) in state.selectedReason) {
            removeReportReason(reportReason)
        } else {
            addReportReason(reportReason)
        }
    }

    fun setReportReasonDescription(reportReasonDescription: String) = blockingIntent {
        reduce { state.copy(reportReasonDescription = reportReasonDescription) }
    }

    // Report article - takes articleId and List<ArticleLostAndFoundReportItem>
    fun reportArticle(articleId: Int) = viewModelScope.launch {
        val reportReasonList = mutableListOf<ArticleLostAndFoundReportItem>()
        intent {
            state.selectedReason.forEach {
                if (lostAndFoundReportReasonList[it] == ReportReason.OTHER) {
                    reportReasonList.add(
                        ArticleLostAndFoundReportItem(
                            lostAndFoundReportReasonList[it].title, 
                            state.reportReasonDescription
                        )
                    )
                } else {
                    reportReasonList.add(
                        ArticleLostAndFoundReportItem(
                            lostAndFoundReportReasonList[it].title,
                            lostAndFoundReportReasonList[it].description
                        )
                    )
                }
            }
            reportLostAndFoundArticleUseCase(articleId, reportReasonList)
                .onSuccess { postSideEffect(LostAndFoundReportSideEffect.ReportSuccess) }
                .onFailure { postSideEffect(LostAndFoundReportSideEffect.ReportFailure(it.message ?: "")) }
        }
    }
}

sealed class LostAndFoundReportSideEffect {
    data object ReportSuccess : LostAndFoundReportSideEffect()
    data class ReportFailure(val message: String) : LostAndFoundReportSideEffect()
}
```

### Working with Flow-based UseCases

For pagination and article fetching, **use Flow** with `collectLatest`:

```kotlin
// CORRECT: Flow-based pagination UseCase
fun loadArticles(page: Int, limit: Int, type: String?) = intent {
    reduce { state.copy(isLoading = true) }
    
    fetchLostAndFoundArticlePaginationUseCase(page, limit, type)
        .collectLatest { result ->
            reduce {
                state.copy(
                    isLoading = false,
                    articles = result.articles,
                    totalCount = result.totalCount
                )
            }
        }
}

// CORRECT: Flow-based single article fetch
fun loadArticleDetail(articleId: Int) = intent {
    fetchLostAndFoundArticleUseCase(articleId)
        .collectLatest { article ->
            reduce { state.copy(article = article) }
        }
}
```

### Enumerations

Use actual enums from `feature/article/enums/`:

```kotlin
// LostOrFoundType.kt
enum class LostOrFoundType {
    LOST, FOUND
}

// LostItemCategory.kt
enum class LostItemCategory {
    NONE, ELECTRONICS, WALLET, CARD, CLOTHES, ACCESSORIES, OTHER
}

// ReportReason.kt
enum class ReportReason(val title: String, val description: String) {
    SPAM("스팸", "광고성 게시글"),
    INAPPROPRIATE("부적절", "부적절한 내용"),
    DUPLICATE("중복", "중복 게시글"),
    OTHER("기타", "")
}
```

## Critical Rules

1. **Module Location**: Lost & Found code is in `feature:article`, NOT `feature:lostandfound`

2. **UseCase Returns**: 
   - Pagination/Fetch UseCases return `Flow<T>` - use `collectLatest`
   - Upload/Report/Delete UseCases return `Result<T>` - use `onSuccess/onFailure`

3. **Upload UseCase**: Takes `List<ArticleLostAndFoundUpload>`, NOT individual params

4. **Report UseCase**: Takes `List<ArticleLostAndFoundReportItem>`, NOT a single reason string

5. **Pagination UseCase**: Has 3 params: `page`, `limit`, `type` (nullable String for filtering)

6. **Image Upload**: **MUST** use pre-signed URL flow (get URL → upload file → store returned URL)

7. **Image Limit**: **MUST** enforce max images per article (defined in `IMAGE_MAX_COUNT`)

8. **State Persistence**: Write ViewModel uses `SavedStateHandle` for process death recovery

## Build Commands

```bash
# Build article module (contains lost and found)
./gradlew :feature:article:build
./gradlew :feature:article:test

# This module is a placeholder
./gradlew :feature:lostandfound:build
./gradlew :feature:lostandfound:test
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on FEATURE LOSTANDFOUND module
