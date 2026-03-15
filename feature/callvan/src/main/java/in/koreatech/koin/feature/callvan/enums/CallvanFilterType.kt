package `in`.koreatech.koin.feature.callvan.enums

import android.os.Parcelable
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.callvan.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CallvanFilterType(
    @StringRes open val stringRes: Int,
    open val value: String?
) : Parcelable {

    @Parcelize
    sealed class DeparturesFilterType(
        @StringRes override val stringRes: Int,
        override val value: String?
    ) : CallvanFilterType(stringRes, value) {
        @Parcelize
        object All : DeparturesFilterType(R.string.filter_list_all, null)

        @Parcelize
        object FrontGate : DeparturesFilterType(R.string.filter_list_front_gate, "FRONT_GATE")

        @Parcelize
        object BackGate : DeparturesFilterType(R.string.filter_list_back_gate, "BACK_GATE")

        @Parcelize
        object TennisCourt : DeparturesFilterType(R.string.filter_list_tennis_court, "TENNIS_COURT")

        @Parcelize
        object DormitoryMain : DeparturesFilterType(R.string.filter_list_dormitory_main, "DORMITORY_MAIN")

        @Parcelize
        object DormitorySub : DeparturesFilterType(R.string.filter_list_dormitory_sub, "DORMITORY_SUB")

        @Parcelize
        object Terminal : DeparturesFilterType(R.string.filter_list_terminal, "TERMINAL")

        @Parcelize
        object Station : DeparturesFilterType(R.string.filter_list_station, "STATION")

        @Parcelize
        object AsanStation : DeparturesFilterType(R.string.filter_list_asan_station, "ASAN_STATION")
    }

    @Parcelize
    sealed class ArrivalsFilterType(
        @StringRes override val stringRes: Int,
        override val value: String?
    ) : CallvanFilterType(stringRes, value) {
        @Parcelize
        object All : ArrivalsFilterType(R.string.filter_list_all, null)

        @Parcelize
        object FrontGate : ArrivalsFilterType(R.string.filter_list_front_gate, "FRONT_GATE")

        @Parcelize
        object BackGate : ArrivalsFilterType(R.string.filter_list_back_gate, "BACK_GATE")

        @Parcelize
        object TennisCourt : ArrivalsFilterType(R.string.filter_list_tennis_court, "TENNIS_COURT")

        @Parcelize
        object DormitoryMain : ArrivalsFilterType(R.string.filter_list_dormitory_main, "DORMITORY_MAIN")

        @Parcelize
        object DormitorySub : ArrivalsFilterType(R.string.filter_list_dormitory_sub, "DORMITORY_SUB")

        @Parcelize
        object Terminal : ArrivalsFilterType(R.string.filter_list_terminal, "TERMINAL")

        @Parcelize
        object Station : ArrivalsFilterType(R.string.filter_list_station, "STATION")

        @Parcelize
        object AsanStation : ArrivalsFilterType(R.string.filter_list_asan_station, "ASAN_STATION")
    }

    @Parcelize
    sealed class StatusesType(
        @StringRes override val stringRes: Int,
        override val value: String?
    ) : CallvanFilterType(stringRes, value) {
        @Parcelize
        object All : StatusesType(R.string.filter_list_all, null)

        @Parcelize
        object Recruiting : StatusesType(R.string.filter_list_recruiting, "RECRUITING")

        @Parcelize
        object Closed : StatusesType(R.string.filter_list_closed, "CLOSED")

        @Parcelize
        object Completed : StatusesType(R.string.filter_list_completed, "COMPLETED")
    }

    @Parcelize
    sealed class SortType(
        @StringRes override val stringRes: Int,
        override val value: String?
    ) : CallvanFilterType(stringRes, value) {
        @Parcelize
        object LatestDesc : SortType(R.string.filter_list_latest_desc, "LATEST_DESC")

        @Parcelize
        object LatestAsc : SortType(R.string.filter_list_latest_asc, "LATEST_ASC")

        @Parcelize
        object DepartureDesc : SortType(R.string.filter_list_departure_desc, "DEPARTURE_DESC")

        @Parcelize
        object DepartureAsc : SortType(R.string.filter_list_departure_asc, "DEPARTURE_ASC")
    }
}
