# FEATURE Article Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the FEATURE ARTICLE module of the KOIN_ANDROID repository.

## Module Overview

The `feature:article` module provides the **article and announcement system** for the KOIN app, including university notices, keyword-based notifications, search functionality, and the lost & found feature. It uses a hybrid architecture with legacy XML-based fragments and modern Compose components.

### Architecture Position
```
┌─────────────────────────────────────────────────────────────┐
│                    koin (App Module)                        │
└─────────────────────────────────────────────────────────────┘
                            ↓ depends on
┌─────────────────────────────────────────────────────────────┐
│                  feature:article                            │
│    (Notices, Keywords, Search, Lost & Found)                │
└─────────────────────────────────────────────────────────────┘
                            ↓ depends on
┌─────────────────────────────────────────────────────────────┐
│     domain    │    core:designsystem    │    core:analytics │
└─────────────────────────────────────────────────────────────┘
```

## Core Responsibilities

1. **Article List**: Display categorized university announcements
2. **Article Detail**: Show full article content with attachments
3. **Keyword Management**: User keyword subscriptions for notifications
4. **Article Search**: Full-text search with history
5. **Lost & Found**: Lost/found item posting and browsing
6. **Hot Articles**: Popular article recommendations
7. **Deep Linking**: Handle notification and external links

## Package Structure

```
feature/article/src/main/java/in/koreatech/koin/feature/article/
├── ArticleActivity.kt                    # Main activity with navigation
├── LostAndFoundReportActivity.kt         # Report feature activity
├── Constant.kt                           # Module constants
├── component/                            # Reusable Compose components
│   ├── Dropdown.kt                       # Dropdown selector
│   ├── HotArticle.kt                     # Hot article card
│   ├── ItemTypeChip.kt                   # Item type chip
│   ├── KeywordChipGroup.kt               # Keyword chips
│   ├── LoadingDialog.kt                  # Loading indicator
│   └── LostItemTypeChip.kt               # Lost item category chip
├── enums/
│   └── ArticleBoardType.kt               # Board type enumeration
├── model/
│   ├── ArticleHeaderState.kt             # Article header UI state
│   ├── ArticleState.kt                   # Full article UI state
│   └── ArticleToolbarState.kt            # Toolbar configuration state
├── ui/
│   ├── article/
│   │   ├── adapter/                      # RecyclerView adapters
│   │   │   ├── ArticleAdapter.kt
│   │   │   ├── HotArticleAdapter.kt
│   │   │   └── RecentSearchedHistoryAdapter.kt
│   │   └── detail/
│   │       ├── ArticleDetailFragment.kt   # Article detail screen
│   │       └── ArticleDetailViewModel.kt  # Detail ViewModel
│   ├── keyword/
│   │   ├── ArticleKeywordFragment.kt     # Keyword management
│   │   └── ArticleKeywordViewModel.kt
│   ├── list/
│   │   ├── ArticleListFragment.kt        # Article list screen
│   │   └── ArticleListViewModel.kt
│   ├── lostandfound/
│   │   ├── component/                    # Lost & found Compose components
│   │   ├── detail/
│   │   │   ├── ArticleLostAndFoundDetailFragment.kt
│   │   │   └── ArticleLostAndFoundDetailViewModel.kt
│   │   ├── report/
│   │   │   └── LostAndFoundReportScreen.kt
│   │   └── write/
│   │       ├── ArticleLostAndFoundWriteLostFragment.kt
│   │       └── ArticleLostAndFoundWriteFoundFragment.kt
│   └── search/
│       ├── ArticleSearchFragment.kt      # Search screen
│       └── ArticleSearchViewModel.kt
└── util/
    ├── ContextExtensions.kt              # Context utilities
    ├── HtmlView.kt                       # HTML rendering
    ├── KoreanDateUtil.kt                 # Korean date formatting
    ├── ModifierUtil.kt                   # Compose modifier utilities
    ├── ParsingExtensions.kt              # String parsing
    └── TextExtensions.kt                 # Text utilities
```

## Implementation Patterns

### Activity with Navigation Pattern

**MUST** use single activity with Navigation Component:

```kotlin
@AndroidEntryPoint
class ArticleActivity : ActivityBase() {
    private val binding by dataBinding<ActivityArticleBinding>()
    private lateinit var navController: NavController
    override val screenTitle: String = "공지사항"

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val navHostFragment =
            supportFragmentManager.findFragmentById(
                R.id.nav_host_article_fragment
            ) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, dest, _ ->
            when (dest.id) {
                R.id.articleListFragment -> setToolbar(ArticleToolbarState.ARTICLE_LIST)
                R.id.articleDetailFragment -> setToolbar(ArticleToolbarState.ARTICLE_DETAIL)
                R.id.articleSearchFragment -> setToolbar(ArticleToolbarState.ARTICLE_SEARCH)
                // ... other destinations
            }
        }
    }
}
```

**Rules**:
- **MUST** use `@AndroidEntryPoint` for Hilt injection
- **MUST** use Navigation Component for fragment navigation
- **MUST** handle deep links from notifications
- **SHOULD** update toolbar based on destination

### AssistedInject ViewModel Pattern

**MUST** use AssistedInject for ViewModels with runtime parameters:

```kotlin
class ArticleDetailViewModel @AssistedInject constructor(
    @Assisted("articleId") articleId: Int,
    @Assisted("navigatedBoardId") val navigatedBoardId: Int,
    private val articleRepository: ArticleRepository
) : BaseViewModel() {
    
    val article: StateFlow<ArticleState> = articleRepository.fetchArticle(articleId, navigatedBoardId)
        .onStart { _isLoading.value = true }
        .map { it.toArticleState() }
        .onEach { _isLoading.value = false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ArticleState.EMPTY
        )

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("articleId") articleId: Int,
            @Assisted("navigatedBoardId") navigatedBoardId: Int
        ): ArticleDetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            article: Int,
            boardId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(article, boardId) as T
                }
            }
        }
    }
}
```

**Rules**:
- **MUST** use `@AssistedInject constructor` for runtime parameters
- **MUST** use `@Assisted("name")` to differentiate same-type parameters
- **MUST** create `@AssistedFactory` interface
- **MUST** provide companion factory method for Fragment use

### Fragment with AssistedInject ViewModel

**MUST** initialize ViewModel with assisted factory:

```kotlin
@AndroidEntryPoint
class ArticleDetailFragment : BaseFragment() {
    
    @Inject
    lateinit var viewModelFactory: ArticleDetailViewModel.Factory
    
    private val viewModel: ArticleDetailViewModel by viewModels {
        ArticleDetailViewModel.provideFactory(
            viewModelFactory,
            arguments?.getInt(ARTICLE_ID) ?: 0,
            arguments?.getInt(NAVIGATED_BOARD_ID) ?: ArticleBoardType.ALL.id
        )
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.article.collect { article ->
                // Update UI
            }
        }
    }
    
    companion object {
        const val ARTICLE_ID = "article_id"
        const val NAVIGATED_BOARD_ID = "navigated_board_id"
    }
}
```

### Board Type Enumeration Pattern

**MUST** use enum for board types with correct IDs and link types:

```kotlin
// ArticleBoardType.kt - Actual implementation
enum class ArticleBoardType(
    val id: Int,
    @StringRes val koreanName: Int,
    @StringRes val simpleKoreanName: Int,
    val linkType: LinkType,
    val exposedInAll: Boolean = true
) {
    ALL(4, R.string.article_all, R.string.article_all, LinkType.NONE),
    LOSTANDFOUND(14, R.string.article_lost_and_found, R.string.article_lost_and_found, LinkType.NONE),
    NORMAL(5, R.string.article_normal, R.string.article_normal_simple, LinkType.ARTICLE),
    SCHOLARSHIP(6, R.string.article_scholarship, R.string.article_scholarship_simple, LinkType.ARTICLE),
    SCHOOL(7, R.string.article_school, R.string.article_school_simple, LinkType.ARTICLE),
    RECRUIT(8, R.string.article_recruit, R.string.article_recruit_simple, LinkType.STEMS),
    IPP(12, R.string.article_ipp, R.string.article_ipp_simple, LinkType.PORTAL),
    STUDENT(13, R.string.article_student, R.string.article_student_simple, LinkType.PORTAL, false),
    KOIN(9, R.string.article_koin, R.string.article_koin, LinkType.NONE, false);

    companion object {
        fun fromId(id: Int): ArticleBoardType =
            entries.find { it.id == id } ?: ALL
    }
}

// LinkType enum - determines how article links are opened
enum class LinkType {
    NONE,      // Internal articles (no external link)
    ARTICLE,   // Opens in external browser via article URL
    PORTAL,    // Opens in KOREATECH portal
    STEMS      // Opens in STEMS (recruitment system)
}
```

**Board Type Reference Table**:

| Type | ID | Link Type | Exposed in ALL | Description |
|------|-----|-----------|----------------|-------------|
| ALL | 4 | NONE | - | Aggregated view of all boards |
| LOSTANDFOUND | 14 | NONE | Yes | Lost & Found posts |
| NORMAL | 5 | ARTICLE | Yes | General announcements |
| SCHOLARSHIP | 6 | ARTICLE | Yes | Scholarship notices |
| SCHOOL | 7 | ARTICLE | Yes | School-wide notices |
| RECRUIT | 8 | STEMS | Yes | Job/internship postings |
| IPP | 12 | PORTAL | Yes | IPP program notices |
| STUDENT | 13 | PORTAL | No | Student council notices |
| KOIN | 9 | NONE | No | KOIN service notices |

**Usage examples**:
```kotlin
// Get board type from API response ID
val boardType = ArticleBoardType.fromId(response.boardId)

// Check if article opens externally
if (boardType.linkType != LinkType.NONE) {
    openExternalBrowser(article.url)
} else {
    navigateToDetailScreen(article.id)
}

// Filter boards shown in "ALL" tab
val visibleBoards = ArticleBoardType.entries.filter { it.exposedInAll }
```

**Rules**:
- **MUST** use correct IDs matching the backend API
- **MUST** check `linkType` before navigating to article detail
- **MUST** use `entries` instead of deprecated `values()` for iteration
- **NEVER** assume sequential IDs (they are non-contiguous: 4, 5, 6, 7, 8, 9, 12, 13, 14)

### UI State Pattern

**MUST** create dedicated state classes:

```kotlin
data class ArticleHeaderState(
    val id: Int,
    val board: ArticleBoardType,
    val title: String,
    val author: String,
    val viewCount: Int,
    val registeredAt: String,
    val updatedAt: String
)

data class ArticleState(
    val header: ArticleHeaderState,
    val content: String,
    val prevArticleId: Int?,
    val nextArticleId: Int?,
    val attachments: List<AttachmentState>,
    val url: String
) {
    companion object {
        val EMPTY = ArticleState(
            header = ArticleHeaderState(
                id = 0,
                board = ArticleBoardType.ALL,
                title = "",
                author = "",
                viewCount = 0,
                registeredAt = "",
                updatedAt = ""
            ),
            content = "",
            prevArticleId = null,
            nextArticleId = null,
            attachments = emptyList(),
            url = ""
        )
    }
}
```

### Mapper Extension Functions

**MUST** use extension functions for domain-to-UI mapping:

```kotlin
// ArticleHeaderState.kt
fun ArticleHeader.toArticleHeaderState(): ArticleHeaderState = ArticleHeaderState(
    id = id,
    board = ArticleBoardType.fromId(boardId),
    title = title,
    author = author,
    viewCount = viewCount,
    registeredAt = registeredAt.formatToKoreanDate(),
    updatedAt = updatedAt.formatToKoreanDate()
)

// ArticleState.kt
fun Article.toArticleState(): ArticleState = ArticleState(
    header = header.toArticleHeaderState(),
    content = content,
    prevArticleId = prevArticleId,
    nextArticleId = nextArticleId,
    attachments = attachments.map { it.toAttachmentState() },
    url = url
)
```

### Compose Component Pattern (Lost & Found)

**MUST** follow design system patterns for new components:

```kotlin
@Composable
fun LostItemTypeChip(
    type: LostItemType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = koinColors()
    
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = stringResource(type.labelRes),
                style = koinTypography().labelMedium
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = colors.primary,
            selectedLabelColor = colors.background
        ),
        modifier = modifier
    )
}

@Preview
@Composable
private fun LostItemTypeChipPreview() {
    RebrandKoinTheme {
        LostItemTypeChip(
            type = LostItemType.LOST,
            isSelected = true,
            onClick = {}
        )
    }
}
```

### Korean Date Formatting

**MUST** use consistent Korean date formatting:

```kotlin
// KoreanDateUtil.kt
object KoreanDateUtil {
    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREA)
    private val outputDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
    private val outputDateTimeFormat = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA)
    
    fun String.formatToKoreanDate(): String {
        return try {
            val date = inputFormat.parse(this)
            outputDateFormat.format(date!!)
        } catch (e: Exception) {
            this
        }
    }
    
    fun String.formatToKoreanDateTime(): String {
        return try {
            val date = inputFormat.parse(this)
            outputDateTimeFormat.format(date!!)
        } catch (e: Exception) {
            this
        }
    }
}
```

### Deep Link Handling Pattern

**MUST** handle notification deep links:

```kotlin
// In ArticleActivity
private fun navigateToDetailFragment() {
    val uri = intent.data
    val link = uri?.getQueryParameter("fragment")

    when (link) {
        "article_keyword" -> {
            setNavigationGraph()
            navController.popBackStack()
            navController.navigate(R.id.articleKeywordFragment)
        }
        "article_detail" -> {
            val articleId = uri.getQueryParameter("article_id")?.toIntOrNull() ?: 0
            val boardId = uri.getQueryParameter("board_id")?.toIntOrNull() ?: 0
            setNavigationGraph()
            navController.popBackStack()
            navController.navigate(
                R.id.articleDetailFragment,
                bundleOf(
                    ARTICLE_ID to articleId,
                    NAVIGATED_BOARD_ID to boardId
                )
            )
        }
        "article_lost_and_found" -> {
            setNavigationGraph(ArticleBoardType.LOSTANDFOUND.id)
        }
        null -> {
            val bundle = intent.getBundleExtra(BUNDLE_ARTICLE_EXTRA_KEY)
            bundle?.getInt(START_BOARD)?.let {
                setNavigationGraph(it)
            } ?: setNavigationGraph()
        }
    }
}
```

### RecyclerView Adapter Pattern

**MUST** use ListAdapter for efficient updates:

```kotlin
class ArticleAdapter(
    private val onItemClick: (ArticleHeaderState) -> Unit
) : ListAdapter<ArticleHeaderState, ArticleAdapter.ViewHolder>(DiffCallback) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ViewHolder(
        private val binding: ItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(item: ArticleHeaderState) {
            binding.apply {
                tvTitle.text = item.title
                tvAuthor.text = item.author
                tvDate.text = item.registeredAt
                root.setOnClickListener { onItemClick(item) }
            }
        }
    }
    
    companion object DiffCallback : DiffUtil.ItemCallback<ArticleHeaderState>() {
        override fun areItemsTheSame(oldItem: ArticleHeaderState, newItem: ArticleHeaderState) =
            oldItem.id == newItem.id
        
        override fun areContentsTheSame(oldItem: ArticleHeaderState, newItem: ArticleHeaderState) =
            oldItem == newItem
    }
}
```

### HTML Content Rendering

**MUST** handle HTML content safely:

```kotlin
// HtmlView.kt
@Composable
fun HtmlView(
    html: String,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = { textView ->
            textView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(html)
            }
        }
    )
}
```

## Navigation Graph Structure

```xml
<!-- nav_graph_article.xml -->
<navigation
    android:id="@+id/nav_graph_article"
    app:startDestination="@id/articleListFragment">
    
    <fragment
        android:id="@+id/articleListFragment"
        android:name="...ArticleListFragment">
        <action
            android:id="@+id/action_articleListFragment_to_articleDetailFragment"
            app:destination="@id/articleDetailFragment" />
        <action
            android:id="@+id/action_articleListFragment_to_articleSearchFragment"
            app:destination="@id/articleSearchFragment" />
    </fragment>
    
    <fragment
        android:id="@+id/articleDetailFragment"
        android:name="...ArticleDetailFragment">
        <argument
            android:name="article_id"
            app:argType="integer" />
        <argument
            android:name="navigated_board_id"
            app:argType="integer"
            android:defaultValue="0" />
    </fragment>
    
    <!-- More fragments... -->
</navigation>
```

## Import Organization

```kotlin
// 1. Android/AndroidX imports
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

// 2. Compose imports (for Compose-based screens)
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

// 3. Dagger/Hilt imports
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.AndroidEntryPoint

// 4. Internal imports
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.fragment.BaseFragment
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.feature.article.databinding.FragmentArticleDetailBinding
import `in`.koreatech.koin.feature.article.enums.ArticleBoardType
import `in`.koreatech.koin.feature.article.model.ArticleState

// 5. Kotlinx imports
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
```

## Critical Rules

These rules are **non-negotiable**:

1. **AssistedInject**: **MUST** use AssistedInject for ViewModels with runtime parameters (article IDs, board IDs).

2. **Navigation Component**: **MUST** use Navigation Component for all fragment transitions.

3. **State Mapping**: **MUST** use extension functions to map domain models to UI states.

4. **Deep Links**: **MUST** handle notification deep links properly for keyword alerts.

5. **HTML Safety**: **MUST** sanitize HTML content before rendering.

6. **ListAdapter**: **MUST** use ListAdapter with DiffUtil for RecyclerView.

7. **Edge-to-Edge**: **MUST** enable edge-to-edge display with proper inset handling.

8. **Analytics**: **MUST** log user interactions for article views, searches, and keyword actions.

## Migration Notes

This module is in a **hybrid state**:
- Legacy XML fragments for article list, detail, search, keyword screens
- Compose components for Lost & Found feature
- New features **SHOULD** use Compose
- Existing screens **MAY** be migrated gradually

## Build Commands

```bash
# Build article module
./gradlew :feature:article:build

# Run article tests
./gradlew :feature:article:test

# Check ktlint for article
./gradlew :feature:article:ktlintCheck
```

## Testing Guidelines

### ViewModel Testing

```kotlin
@Test
fun `article detail loads correctly`() = runTest {
    val repository = mockk<ArticleRepository>()
    coEvery { repository.fetchArticle(1, 0) } returns flowOf(testArticle)
    
    val viewModel = ArticleDetailViewModel.Factory(repository)
        .create(articleId = 1, navigatedBoardId = 0)
    
    viewModel.article.test {
        val result = awaitItem()
        assertThat(result.header.id).isEqualTo(1)
        cancelAndIgnoreRemainingEvents()
    }
}
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on FEATURE ARTICLE module  
**Maintainers**: BCSD Android Track
