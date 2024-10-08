package `in`.koreatech.koin.ui.term

import `in`.koreatech.koin.domain.model.term.Term


sealed class TermState {
    data object Init : TermState()
    data class Success(val term: Term) : TermState()
    data class Failure(val message: String) : TermState()
}