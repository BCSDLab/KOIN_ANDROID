package `in`.koreatech.koin.ui.navigation.state

sealed class MenuState {
    data object Main: MenuState()
    data object Setting: MenuState()
    data object LoginOrLogout: MenuState()
    data object Store: MenuState()
    data object Bus: MenuState()
    data object Dining: MenuState()
    data object Timetable: MenuState()
    data object Land: MenuState()
    data object Owner: MenuState()
    data object Article: MenuState()
    data object Contact: MenuState()
}