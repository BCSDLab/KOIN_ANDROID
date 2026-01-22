package `in`.koreatech.koin.feature.lostandfound.enums

import android.os.Parcelable
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.lostandfound.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class LostAndFoundFilterType : Parcelable {
    abstract val stringRes: Int
    abstract val value: String

    @Parcelize
    object ALL : LostAndFoundFilterType() {
        override val stringRes: Int = R.string.filter_list_all
        override val value: String = "ALL"
    }

    @Parcelize
    sealed class CategoryFilterType(
        @StringRes override val stringRes: Int,
        override val value: String
    ) : LostAndFoundFilterType() {
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
    ) : LostAndFoundFilterType() {
        @Parcelize
        object LOST : CategoryFilterType(stringRes = R.string.filter_list_lost, value = "LOST")

        @Parcelize
        object FIND : CategoryFilterType(stringRes = R.string.filter_list_find, value = "FOUND")
    }

    @Parcelize
    sealed class FoundFilterType(
        @StringRes override val stringRes: Int,
        override val value: String
    ) : LostAndFoundFilterType() {
        @Parcelize
        object FINDING : CategoryFilterType(stringRes = R.string.filter_list_finding, value = "NOT_FOUND")

        @Parcelize
        object FOUND : CategoryFilterType(stringRes = R.string.filter_list_found, value = "FOUND")
    }

    @Parcelize
    sealed class AuthorFilterType(
        @StringRes override val stringRes: Int,
        override val value: String
    ) : LostAndFoundFilterType() {
        @Parcelize
        object MY : CategoryFilterType(stringRes = R.string.filter_list_my_post, value = "MY")
    }
}
