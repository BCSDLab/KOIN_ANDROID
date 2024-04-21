package `in`.koreatech.koin.ui.navigation.state

sealed class MenuState {
    data object Main: MenuState()
    data object Store: MenuState()
    data object Bus: MenuState()
    data object Dining: MenuState()
    data object Timetable: MenuState()
    data object Land: MenuState()
    data object Owner: MenuState()
    data object UserInfo: MenuState()
    data object Notification: MenuState()
}
