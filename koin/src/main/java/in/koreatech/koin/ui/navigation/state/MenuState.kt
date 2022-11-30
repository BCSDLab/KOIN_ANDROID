package `in`.koreatech.koin.ui.navigation.state

sealed class MenuState {
    object Main: MenuState()
    object Store: MenuState()
    object Bus: MenuState()
    object Dining: MenuState()
    object Timetable: MenuState()
    object Land: MenuState()
    object UserInfo: MenuState()
}
