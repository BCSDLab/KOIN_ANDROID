package `in`.koreatech.koin.ui.changepassword

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ChangePasswordPage : Parcelable {
    Verify,
    Change
}

fun ChangePasswordPage.nextPage(): ChangePasswordPage = ChangePasswordPage.entries.let { pages ->
    pages.getOrElse(ordinal + 1) { pages.last() }
}

fun ChangePasswordPage.prevPage(): ChangePasswordPage = ChangePasswordPage.entries.let { pages ->
    pages.getOrElse(ordinal - 1) { pages.first() }
}