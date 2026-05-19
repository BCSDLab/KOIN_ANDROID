package `in`.koreatech.koin.ui.main.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.article.articleNotiContent
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.model.store.StoreCategories
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchArticleLostAndFoundStatsUseCase
import `in`.koreatech.koin.domain.usecase.banner.CheckBannerRefusalUseCase
import `in`.koreatech.koin.domain.usecase.dining.GetDiningUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreCategoriesUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.domain.util.ext.arrange
import `in`.koreatech.koin.domain.util.ext.typeFilter
import `in`.koreatech.koin.ui.main.state.ArticleMainState
import `in`.koreatech.koin.ui.main.state.LostAndFoundEntryState
import `in`.koreatech.koin.ui.main.state.toContent
import `in`.koreatech.koin.ui.main.state.toLostAndFoundEntryState
import `in`.koreatech.koin.ui.main.state.toNoti
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getDiningUseCase: GetDiningUseCase,
    private val getStoreCategoriesUseCase: GetStoreCategoriesUseCase,
    private val checkBannerRefusalUseCase: CheckBannerRefusalUseCase,
    private val articleRepository: ArticleRepository,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val fetchArticleLostAndFoundStatsUseCase: FetchArticleLostAndFoundStatsUseCase
) : BaseViewModel() {
    val hotArticles: StateFlow<List<ArticleMainState.Content>> =
        articleRepository.fetchHotArticleHeaders()
            .map {
                it.take(HOT_ARTICLE_COUNT).map { article -> article.toContent() }
            }.catch {
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    val articleNoti: StateFlow<ArticleMainState.Noti> =
        articleRepository.fetchKeywordNotiIndex()
            .map { articleNotiContent[it].toNoti() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = articleNotiContent.first().toNoti()
            )

    val articleMain: StateFlow<List<ArticleMainState>> =
        combine(
            articleNoti,
            hotArticles
        ) { noti, articles ->
            listOf(noti) + articles
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _selectedPosition = MutableStateFlow(0)
    val selectedPosition: StateFlow<Int> get() = _selectedPosition
    private val _diningData = MutableStateFlow<List<Dining>>(listOf())
    val diningData: StateFlow<List<Dining>> get() = _diningData
    private val _selectedType = MutableStateFlow(DiningUtil.getCurrentType())
    val selectedType: StateFlow<DiningType> get() = _selectedType

    private val _userState: StateFlow<User> = getUserStatusUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = User.Anonymous
    )

    private val _storeCategories = MutableStateFlow<List<StoreCategories>>(emptyList())
    val storeCategories: StateFlow<List<StoreCategories>> get() = _storeCategories

    private val _isBannerRefusal = MutableStateFlow<Boolean?>(null)
    val isBannerRefusal: StateFlow<Boolean?> get() = _isBannerRefusal

    init {
        checkBannerRefusal()
        updateDining()
        getLostAndFoundState()
    }

    fun checkKeywordNotiContent() {
        viewModelScope.launchWithLoading {
            articleRepository.saveKeywordNotiIndex().launchIn(viewModelScope)
        }
    }

    fun setDiningType(diningType: DiningType) {
        _selectedType.value = diningType
    }

    fun setSelectedPosition(position: Int) {
        _selectedPosition.value = position
    }

    fun updateDining() {
        viewModelScope.launchWithLoading {
            getDiningUseCase(TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate()))
                .onSuccess {
                    if (it.isNotEmpty()) {
                        _selectedType.value = DiningUtil.getCurrentType()
                    }
                    _diningData.value = it.typeFilter(DiningUtil.getCurrentType()).arrange()
                    _selectedPosition.value = 0
                    _isLoading.value = false
                }
                .onFailure {
                    _diningData.value = listOf()
                    _isLoading.value = false
                }
        }
    }

    fun getStoreCategoriesWithBenefit(storeCategory: StoreCategories) = viewModelScope.launchWithLoading {
        val categoryList = getStoreCategoriesUseCase().drop(1).toMutableList()
        categoryList.add(0, storeCategory)
        _storeCategories.value = categoryList
    }

    fun getStoreCategories() = viewModelScope.launchWithLoading {
        _storeCategories.value = getStoreCategoriesUseCase()
    }

    private fun checkBannerRefusal() {
        viewModelScope.launchWithLoading {
            checkBannerRefusalUseCase().let {
                _isBannerRefusal.value = it
            }
        }
    }

    private val _lostAndFoundEntryState = MutableStateFlow<LostAndFoundEntryState?>(null)
    val lostAndFoundEntryState: StateFlow<LostAndFoundEntryState?> get() = _lostAndFoundEntryState

    private fun getLostAndFoundState() {
        viewModelScope.launchWithLoading {
            fetchArticleLostAndFoundStatsUseCase().onSuccess { stats ->
                _lostAndFoundEntryState.value = stats.toLostAndFoundEntryState()
            }
        }
    }

    companion object {
        private const val HOT_ARTICLE_COUNT = 4
    }
}
