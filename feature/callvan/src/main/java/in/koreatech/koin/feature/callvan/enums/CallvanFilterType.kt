package `in`.koreatech.koin.feature.callvan.enums

import android.os.Parcelable
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.callvan.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CallvanFilterType(
    @StringRes open val stringRes: Int,
    open val value: String
) : Parcelable {
    @Parcelize
    sealed class DestinationFilterType(
        @StringRes override val stringRes: Int,
        override val value: String
    ) : CallvanFilterType(stringRes, value) {
        @Parcelize
        object ALL : DestinationFilterType(stringRes = R.string.filter_list_all, value = "ALL")

        @Parcelize
        object SCHOOL : DestinationFilterType(stringRes = R.string.filter_list_school, value = "SCHOOL")

        @Parcelize
        object TERMINAL : DestinationFilterType(stringRes = R.string.filter_list_terminal, value = "TERMINAL")

        @Parcelize
        object DOWNTOWN : DestinationFilterType(stringRes = R.string.filter_list_downtown, value = "DOWNTOWN")

        @Parcelize
        object SINGYERI : DestinationFilterType(stringRes = R.string.filter_list_singyeri, value = "SINGYERI")

        @Parcelize
        object OCHANG : DestinationFilterType(stringRes = R.string.filter_list_ochang, value = "OCHANG")

        @Parcelize
        object CHEONAN : DestinationFilterType(stringRes = R.string.filter_list_cheonan, value = "CHEONAN")

        @Parcelize
        object ASAN : DestinationFilterType(stringRes = R.string.filter_list_asan, value = "ASAN")
    }

    @Parcelize
    sealed class OriginFilterType(
        @StringRes override val stringRes: Int,
        override val value: String
    ) : CallvanFilterType(stringRes, value) {
        @Parcelize
        object ALL : OriginFilterType(stringRes = R.string.filter_list_all, value = "ALL")

        @Parcelize
        object SCHOOL : OriginFilterType(stringRes = R.string.filter_list_school, value = "SCHOOL")

        @Parcelize
        object TERMINAL : OriginFilterType(stringRes = R.string.filter_list_terminal, value = "TERMINAL")

        @Parcelize
        object DOWNTOWN : OriginFilterType(stringRes = R.string.filter_list_downtown, value = "DOWNTOWN")

        @Parcelize
        object SINGYERI : OriginFilterType(stringRes = R.string.filter_list_singyeri, value = "SINGYERI")

        @Parcelize
        object OCHANG : OriginFilterType(stringRes = R.string.filter_list_ochang, value = "OCHANG")

        @Parcelize
        object CHEONAN : OriginFilterType(stringRes = R.string.filter_list_cheonan, value = "CHEONAN")

        @Parcelize
        object ASAN : OriginFilterType(stringRes = R.string.filter_list_asan, value = "ASAN")
    }

    @Parcelize
    sealed class RecruitmentStatusType(
        @StringRes override val stringRes: Int,
        override val value: String
    ) : CallvanFilterType(stringRes, value) {
        @Parcelize
        object ALL : RecruitmentStatusType(stringRes = R.string.filter_list_all, value = "ALL")

        @Parcelize
        object RECRUITING : RecruitmentStatusType(stringRes = R.string.filter_list_recruiting, value = "RECRUITING")

        @Parcelize
        object COMPLETED : RecruitmentStatusType(stringRes = R.string.filter_list_completed, value = "COMPLETED")
    }

    @Parcelize
    sealed class SortOrderType(
        @StringRes override val stringRes: Int,
        override val value: String
    ) : CallvanFilterType(stringRes, value) {
        @Parcelize
        object LATEST : SortOrderType(stringRes = R.string.filter_list_latest, value = "LATEST")

        @Parcelize
        object DEPARTURE : SortOrderType(stringRes = R.string.filter_list_departure, value = "DEPARTURE")
    }
}
