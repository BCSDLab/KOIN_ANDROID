package `in`.koreatech.koin.ui.navigation.state

sealed class MenuState {
    data object Main : MenuState()

    data object Chat : MenuState()

    data object Setting : MenuState()

    data object LoginOrLogout : MenuState()

    data object Store : MenuState()

    data object BusTimetable : MenuState()

    data object BusSearch : MenuState()

    data object Dining : MenuState()

    data object OperatingInfo : MenuState()

    data object Timetable : MenuState()

    data object Land : MenuState()

    data object Owner : MenuState()

    data object Article : MenuState()

    data object Contact : MenuState()

    data object BenefitStore : MenuState()
}
