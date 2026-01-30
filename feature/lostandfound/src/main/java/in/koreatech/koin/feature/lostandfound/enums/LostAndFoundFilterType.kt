package `in`.koreatech.koin.feature.lostandfound.enums

import android.os.Parcelable
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.lostandfound.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class LostAndFoundFilterType(
    @StringRes open val stringRes: Int,
    open val value: String
) : Parcelable {
    @Parcelize
    sealed class CategoryFilterType(
        @StringRes override val stringRes: Int,
        override val value: String
    ) : LostAndFoundFilterType(stringRes, value) {
        @Parcelize
        object ALL : CategoryFilterType(stringRes = R.string.filter_list_all, value = "ALL")

        @Parcelize
        object CARD : CategoryFilterType(stringRes = R.string.filter_list_card, value = "CARD")

        @Parcelize
        object ID : CategoryFilterType(stringRes = R.string.filter_list_id_card, value = "ID")

        @Parcelize
        object WALLET : CategoryFilterType(stringRes = R.string.filter_list_wallet, value = "WALLET")

        @Parcelize
        object ELECTRONIC : CategoryFilterType(stringRes = R.string.filter_list_electronic, value = "ELECTRONICS")

        @Parcelize
        object OTHER : CategoryFilterType(stringRes = R.string.filter_list_other, value = "ETC")
    }

    @Parcelize
    sealed class LostOrFoundFilterType(
        @StringRes override val stringRes: Int,
        override val value: String
    ) : LostAndFoundFilterType(stringRes, value) {
        @Parcelize
        object ALL : LostOrFoundFilterType(stringRes = R.string.filter_list_all, value = "ALL")

        @Parcelize
        object LOST : LostOrFoundFilterType(stringRes = R.string.filter_list_lost, value = "LOST")

        @Parcelize
        object FIND : LostOrFoundFilterType(stringRes = R.string.filter_list_find, value = "FOUND")
    }

    @Parcelize
    sealed class FoundFilterType(
        @StringRes override val stringRes: Int,
        override val value: String
    ) : LostAndFoundFilterType(stringRes, value) {
        @Parcelize
        object ALL : FoundFilterType(stringRes = R.string.filter_list_all, value = "ALL")

        @Parcelize
        object FINDING : FoundFilterType(stringRes = R.string.filter_list_finding, value = "NOT_FOUND")

        @Parcelize
        object FOUND : FoundFilterType(stringRes = R.string.filter_list_found, value = "FOUND")
    }

    @Parcelize
    sealed class AuthorFilterType(
        @StringRes override val stringRes: Int,
        override val value: String
    ) : LostAndFoundFilterType(stringRes, value) {
        @Parcelize
        object ALL : AuthorFilterType(stringRes = R.string.filter_list_all, value = "ALL")

        @Parcelize
        object MY : AuthorFilterType(stringRes = R.string.filter_list_my_post, value = "MY")
    }
}
