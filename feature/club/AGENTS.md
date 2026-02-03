# FEATURE Club Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working on the FEATURE CLUB module.

## Module Overview

The `feature:club` module manages student club information, member recruitment, Q&A, and club likes.

## Core Responsibilities

1. **Club Discovery**: Browse and search clubs
2. **Club Details**: View detailed club information
3. **Club Creation/Editing**: CRUD operations for club management
4. **Recruitment System**: Manage club recruitment status
5. **Q&A System**: Ask and answer questions about clubs
6. **Like System**: Like/unlike clubs (separate UseCases for like and cancel)
7. **Event System**: Club events with subscription

## Key Patterns

### Club Detail ViewModel (Orbit MVI)

```kotlin
@HiltViewModel
class ClubDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val getClubDetailsUseCase: GetClubDetailsUseCase,
    private val getClubQnasUseCase: GetClubQnasUseCase,
    private val deleteClubQnaUseCase: DeleteClubQnaUseCase,
    private val cancelClubLikeUseCase: CancelClubLikeUseCase,
    private val postClubQnaUseCase: PostClubQnaUseCase,
    private val setClubEmpowermentUseCase: SetClubEmpowermentUseCase,
    private val setClubLikeUseCase: SetClubLikeUseCase,
    private val getClubRecruitmentUseCase: GetClubRecruitmentUseCase,
    private val deleteClubRecruitmentUseCase: DeleteClubRecruitmentUseCase,
    private val getClubEventsUseCase: GetClubEventsUseCase,
    private val deleteClubEventUseCase: DeleteClubEventUseCase,
    private val subscribeClubRecruitmentUseCase: SubscribeClubRecruitmentUseCase,
    private val unsubscribeClubRecruitmentUseCase: UnsubscribeClubRecruitmentUseCase,
    private val subscribeClubEventUseCase: SubscribeClubEventUseCase,
    private val unsubscribeClubEventUseCase: UnsubscribeClubEventUseCase
) : ViewModel(), ContainerHost<ClubDetailState, ClubDetailSideEffect> {
    
    override val container = container<ClubDetailState, ClubDetailSideEffect>(
        initialState = ClubDetailState(),
        savedStateHandle = savedStateHandle
    ) {
        val clubId = savedStateHandle.get<Int>(CLUB_ID)
        checkNotNull(clubId)
        intent {
            reduce { state.copy(clubId = clubId) }
        }
    }

    private val userInfoFlow: StateFlow<User> =
        getUserStatusUseCase()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), User.Anonymous)
    
    // Like/Unlike uses TWO SEPARATE UseCases
    fun changeClubLike() = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        state.clubDetails?.let {
            if (it.isLiked) {
                // CANCEL like - uses cancelClubLikeUseCase
                cancelClubLikeUseCase(clubId = state.clubId).onSuccess { _ ->
                    // Analytics logging
                }.onFailure { e ->
                    when (e) {
                        is KoinClubException.UnauthorizedException -> 
                            postSideEffect(ClubDetailSideEffect.UnauthorizedError)
                        is KoinClubException.AlreadyNotLikedException -> 
                            postSideEffect(ClubDetailSideEffect.AlreadyNotLikedError)
                        else -> postSideEffect(ClubDetailSideEffect.UnknownError)
                    }
                }
            } else {
                // SET like - uses setClubLikeUseCase (SINGLE PARAM: clubId only)
                setClubLikeUseCase(clubId = state.clubId).onSuccess { _ ->
                    // Analytics logging
                }.onFailure { e ->
                    when (e) {
                        is KoinClubException.UnauthorizedException -> 
                            postSideEffect(ClubDetailSideEffect.UnauthorizedError)
                        is KoinClubException.AlreadyLikedException -> 
                            postSideEffect(ClubDetailSideEffect.AlreadyLikedError)
                        else -> postSideEffect(ClubDetailSideEffect.UnknownError)
                    }
                }
            }
        }
        loadClubDetails()
        dismissLoginDialog()
    }
    
    // Q&A uses THREE parameters: clubId, parentId (nullable for root question), content
    fun addClubQna(parentId: Int?, content: String) = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        if (content.isBlank()) {
            reduce {
                state.copy(
                    isLoading = false,
                    textFieldErrorMessageResId = R.string.detail_error_empty_text_field
                )
            }
            return@intent
        }
        state.clubDetails?.let {
            postClubQnaUseCase(
                clubId = state.clubId,
                parentId = parentId,  // null for new question, Int for reply
                content = content
            ).onFailure { e ->
                when (e) {
                    is KoinClubException.UnauthorizedException -> 
                        postSideEffect(ClubDetailSideEffect.UnauthorizedError)
                    is KoinClubException.ClubNotFoundException -> 
                        postSideEffect(ClubDetailSideEffect.ClubNotFoundError)
                    is KoinClubException.NotClubManagerException -> 
                        postSideEffect(ClubDetailSideEffect.NotClubManagerError)
                    else -> postSideEffect(ClubDetailSideEffect.UnknownError)
                }
            }
        }
        loadClubQnas()
        dismissAddQnaDialog()
    }
}
```

### UseCase Signatures (CRITICAL)

**SetClubLikeUseCase** - Takes 1 parameter (clubId only):
```kotlin
class SetClubLikeUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(clubId: Int): Result<Unit>
}
```

**CancelClubLikeUseCase** - Separate UseCase for unliking:
```kotlin
class CancelClubLikeUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(clubId: Int): Result<Unit>
}
```

**PostClubQnaUseCase** - Takes 3 parameters:
```kotlin
class PostClubQnaUseCase @Inject constructor(
    private val clubRepository: ClubRepository
) {
    suspend operator fun invoke(
        clubId: Int,
        parentId: Int?,  // null for new question, non-null for reply/answer
        content: String
    ): Result<Unit>
}
```

### Critical Rules

1. **Like System**: Uses TWO separate UseCases:
   - `setClubLikeUseCase(clubId)` - for liking (1 param)
   - `cancelClubLikeUseCase(clubId)` - for unliking (1 param)
   - Do NOT pass boolean toggle to setClubLikeUseCase
2. **Q&A System**: `postClubQnaUseCase(clubId, parentId, content)` - 3 params
   - `parentId = null` for new root questions
   - `parentId = Int` for replies/answers
3. **Loading Guard**: Always check `if (state.isLoading) return@intent` before operations
4. **Error Handling**: Map `KoinClubException` subtypes to specific side effects
5. **Data Refresh**: Call `loadClubDetails()` or `loadClubQnas()` after mutations
6. **SavedStateHandle**: Store clubId from navigation in container initialization

### Domain Exceptions

```kotlin
sealed class KoinClubException : Exception() {
    class ClubNotFoundException : KoinClubException()
    class UnauthorizedException : KoinClubException()
    class AlreadyLikedException : KoinClubException()
    class AlreadyNotLikedException : KoinClubException()
    class NotClubManagerException : KoinClubException()
    class QnaNotFoundException : KoinClubException()
    class DeletePermissionDeniedException : KoinClubException()
    class ClubRecruitNotFoundException : KoinClubException()
    class ClubEventNotFoundException : KoinClubException()
    class WrongInputDataException : KoinClubException()
    class AlreadyManagerException : KoinClubException()
    class LoginIdNotFoundException : KoinClubException()
}
```

## Build Commands

```bash
./gradlew :feature:club:build
./gradlew :feature:club:test
```

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on FEATURE CLUB module
