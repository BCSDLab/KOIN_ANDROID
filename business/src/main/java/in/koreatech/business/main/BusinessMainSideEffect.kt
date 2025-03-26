package `in`.koreatech.business.main

sealed class BusinessMainSideEffect {
    object NetWorkError : BusinessMainSideEffect()
}
