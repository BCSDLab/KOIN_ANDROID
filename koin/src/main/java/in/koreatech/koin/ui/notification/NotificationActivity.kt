@AndroidEntryPoint
class NotificationActivity : KoinNavigationDrawerActivity() {
    override val menuState: MenuState = MenuState.Notification
    private val binding by dataBinding<ActivityNotificationBinding>(R.layout.activity_notification)

    override val screenTitle: String = "알림"
}