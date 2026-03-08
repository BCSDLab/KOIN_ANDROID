package `in`.koreatech.koin.feature.callvan.ui.list.model

interface CallvanListItemClickListener {
    fun onJoin() {}
    fun onCancelJoin() {}
    fun onClose() {}
    fun onReRecruit() {}
    fun onComplete() {}
    fun onCall() {}
    fun onChat() {}
}