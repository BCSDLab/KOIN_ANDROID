package `in`.koreatech.koin.feature.callvan.model

interface CallvanListItemClickListener {
    fun onJoin() { /* Default no-op: override to handle join action */ }
    fun onCancelJoin() { /* Default no-op: override to handle cancel join action */ }
    fun onClose() { /* Default no-op: override to handle close action */ }
    fun onReRecruit() { /* Default no-op: override to handle re-recruit action */ }
    fun onComplete() { /* Default no-op: override to handle complete action */ }
    fun onCall() { /* Default no-op: override to handle call action */ }
    fun onChat() { /* Default no-op: override to handle chat action */ }
}
