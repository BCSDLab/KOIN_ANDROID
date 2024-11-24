package `in`.koreatech.koin.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.abtest.Experiment
import `in`.koreatech.koin.core.abtest.ExperimentGroup
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.article.articleNotiContent
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.model.store.StoreCategories
import `in`.koreatech.koin.domain.repository.ArticleRepository
import `in`.koreatech.koin.domain.usecase.bus.timer.GetBusTimerUseCase
import `in`.koreatech.koin.domain.usecase.dining.GetDiningUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreCategoriesUseCase
import `in`.koreatech.koin.domain.usecase.user.ABTestUseCase
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.ui.main.state.ArticleMainState
import `in`.koreatech.koin.ui.main.state.toContent
import `in`.koreatech.koin.ui.main.state.toNoti
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getBusTimerUseCase: GetBusTimerUseCase,
    private val busErrorHandler: BusErrorHandler,
    private val getDiningUseCase: GetDiningUseCase,
    private val getStoreCategoriesUseCase: GetStoreCategoriesUseCase,
    private val abTestUseCase: ABTestUseCase,
    private val articleRepository: ArticleRepository
) : BaseViewModel() {
    private val _variableName = MutableLiveData<String>()
    val variableName: LiveData<String> get() = _variableName
    private val _busNode =
        MutableLiveData<Pair<BusNode, BusNode>>(BusNode.Koreatech to BusNode.Terminal)

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

    val articleMain: StateFlow<List<ArticleMainState>> = combine(
        articleNoti, hotArticles
    ) { noti, article ->
        listOf(noti) + article
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _selectedPosition = MutableLiveData(0)
    val selectedPosition: LiveData<Int> get() = _selectedPosition
    private val _diningData = MutableLiveData<List<Dining>>(listOf())
    val diningData: LiveData<List<Dining>> get() = _diningData
    private val _selectedType = MutableLiveData(DiningUtil.getCurrentType())
    val selectedType: LiveData<DiningType> get() = _selectedType

    private val _storeCategories = MutableLiveData<List<StoreCategories>>(emptyList())
    val storeCategories: LiveData<List<StoreCategories>> get() = _storeCategories

    val bannerABTestExperimentGroup = flow {
        abTestUseCase(Experiment.MAIN_ARTICLE_KEYWORD_BANNER.experimentTitle).onSuccess {
            emit(it)
            when (it) {
                ExperimentGroup.MAIN_BANNER_NEW -> {
                    EventLogger.logCustomEvent(
                        "AB_TEST",
                        "a/b test 로깅(키워드관리 진입 배너)",
                        AnalyticsConstant.Label.CAMPUS_NOTICE_1,
                        "진입점O"
                    )
                }

                ExperimentGroup.MAIN_BANNER_ORIGINAL -> {
                    EventLogger.logCustomEvent(
                        "AB_TEST",
                        "a/b test 로깅(키워드관리 진입 배너)",
                        AnalyticsConstant.Label.CAMPUS_NOTICE_1,
                        "진입점X"
                    )
                }
            }
        }.onFailure {
            emit(Experiment.MAIN_ARTICLE_KEYWORD_BANNER.experimentGroups.first())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Experiment.MAIN_ARTICLE_KEYWORD_BANNER.experimentGroups.first()
    )

    val diningABTestExperimentGroup = flow {
        abTestUseCase(Experiment.MAIN_DINING_SEE_MORE.experimentTitle).onSuccess {
            emit(it)
            when (it) {
                ExperimentGroup.MAIN_DINING_NEW -> {
                    EventLogger.logClickEvent(
                        EventAction.CAMPUS,
                        AnalyticsConstant.Label.CAMPUS_DINING_1,
                        "더보기O"
                    )
                }

                ExperimentGroup.MAIN_DINING_ORIGINAL -> {
                    EventLogger.logClickEvent(
                        EventAction.CAMPUS,
                        AnalyticsConstant.Label.CAMPUS_DINING_1,
                        "더보기X"
                    )
                }
            }
        }.onFailure {
            emit(Experiment.MAIN_DINING_SEE_MORE.experimentGroups.first())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Experiment.MAIN_DINING_SEE_MORE.experimentGroups.first()
    )

    init {
        updateDining()
        getStoreCategories()
    }

    fun postABTestAssign(title: String) = viewModelScope.launchWithLoading {
        abTestUseCase(title).onSuccess {
            _variableName.value = it
        }
    }

    val busTimer = liveData {
        _busNode.asFlow()
            .distinctUntilChanged()
            .collectLatest { (departure, arrival) ->
                _isLoading.value = false
                try {
                    if (departure != arrival) {
                        getBusTimerUseCase(departure, arrival)
                            .conflate()
                            .collect { result ->
                                emit(result)
                            }
                    }

                } catch (_: CancellationException) {
                } catch (e: Exception) {
                    _errorToast.value = busErrorHandler.handleGetBusRemainTimeError(e).message
                }
            }
    }

    fun checkKeywordNotiContent() {
        viewModelScope.launchWithLoading {
            articleRepository.saveKeywordNotiIndex()
        }
    }

    fun switchBusNode() {
        _busNode.value = _busNode.value?.let { (departure, arrival) ->
            arrival to departure
        } ?: (BusNode.Koreatech to BusNode.Terminal)
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
                    _diningData.value = it
                    _selectedPosition.value = 0
                    _isLoading.value = false
                }
                .onFailure {
                    _diningData.value = listOf()
                    _isLoading.value = false
                }
        }
    }

    fun getStoreCategories() {
        viewModelScope.launchWithLoading {
            _storeCategories.value = getStoreCategoriesUseCase()
        }
    }

    companion object {
        private const val HOT_ARTICLE_COUNT = 4
    }
}