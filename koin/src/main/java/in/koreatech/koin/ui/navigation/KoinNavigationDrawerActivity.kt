package `in`.koreatech.koin.ui.navigation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.bus.BusSearchActivity
import `in`.koreatech.bus.BusTimetableActivity
import `in`.koreatech.koin.BuildConfig
import `in`.koreatech.koin.R
import `in`.koreatech.koin.constant.URL
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventCategory
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.blueStatusBar
import `in`.koreatech.koin.core.util.whiteStatusBar
import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.feature.chat.ui.list.ChatListActivity
import `in`.koreatech.koin.feature.club.ui.ClubActivity
import `in`.koreatech.koin.feature.dining.ui.DiningActivity
import `in`.koreatech.koin.feature.user.ui.signin.SignInActivity
import `in`.koreatech.koin.feature.user.ui.signup.SignUpActivity
import `in`.koreatech.koin.ui.article.ArticleActivity
import `in`.koreatech.koin.ui.land.LandActivity
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.navigation.viewmodel.KoinNavigationDrawerViewModel
import `in`.koreatech.koin.ui.operating.OperatingInfoActivity
import `in`.koreatech.koin.ui.setting.SettingActivity
import `in`.koreatech.koin.ui.store.activity.StoreActivity
import `in`.koreatech.koin.util.ext.addDrawerListener
import `in`.koreatech.koin.util.ext.closeDrawer
import `in`.koreatech.koin.util.ext.isDrawerOpened
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.toggleDrawer
import `in`.koreatech.koin.util.ext.windowWidth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
abstract class KoinNavigationDrawerActivity :
    ActivityBase(),
    NavigationView.OnNavigationItemSelectedListener {
    protected abstract val menuState: MenuState

    val drawerLayoutId get() = R.id.drawer_layout

    private var pressTime = System.currentTimeMillis()
    private val koinNavigationDrawerViewModel by viewModels<KoinNavigationDrawerViewModel>()

    private val drawerLayout by lazy {
        findViewById<DrawerLayout>(drawerLayoutId)
    }

    private val leftNavigationView by lazy {
        findViewById<NavigationView>(R.id.left_nav_view)
    }

    private val menus by lazy {
        listOf(
            R.id.navi_item_chat,
            R.id.navi_item_setting,
            R.id.navi_item_sign_up,
            R.id.navi_item_login_or_logout,
            R.id.navi_item_store,
            R.id.navi_item_bus_timetable,
            R.id.navi_item_bus_search,
            R.id.navi_item_dining,
            R.id.navi_item_operating_information,
            R.id.navi_item_timetable,
            R.id.navi_item_club,
            R.id.navi_item_land,
            R.id.navi_item_owner,
            R.id.navi_item_article,
            R.id.navi_item_contact
        ).map {
            findViewById<View>(it)
        }.zip(
            listOf(
                MenuState.Chat,
                MenuState.Setting,
                MenuState.SignUp,
                MenuState.LoginOrLogout,
                MenuState.Store,
                MenuState.BusTimetable,
                MenuState.BusSearch,
                MenuState.Dining,
                MenuState.OperatingInfo,
                MenuState.Timetable,
                MenuState.Club,
                MenuState.Land,
                MenuState.Owner,
                MenuState.Article,
                MenuState.Contact
            )
        ) { view, state ->
            state to view
        }.toMap()
    }

    private val nameTextView by lazy {
        findViewById<TextView>(R.id.navi_user_nickname)
    }
    private val helloMessageTextView by lazy {
        findViewById<TextView>(R.id.navi_hello_message)
    }
    private val signUpTextView by lazy {
        findViewById<TextView>(R.id.navi_item_sign_up)
    }
    private val loginOrLogoutTextView by lazy {
        menus.get(MenuState.LoginOrLogout) as TextView?
            ?: findViewById(R.id.navi_item_login_or_logout)
    }
    private val unReadMessageCountTextView by lazy {
        findViewById<TextView>(R.id.navi_item_chat_badge)
    }
    private val chatMenuIcon by lazy {
        findViewById<AppCompatImageView>(R.id.navi_item_chat)
    }

    private val requestMainPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permission ->
            var permissionGranted = true
            permission.entries.forEach {
                if (it.key in MAIN_REQUIRED_PERMISSION && it.value == false) {
                    permissionGranted = false
                }
            }

            if (!permissionGranted) {
                // handle permission granted
            } else {
                // handle permission denied
            }
        }

    private val loginAlertDialog: AlertDialog by lazy {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.user_only))
            .setMessage(getString(R.string.login_request))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.navigation_ok)) { dialog, _ ->
                dialog.dismiss()
                goToLoginActivity()
            }
            .setNegativeButton(getString(R.string.navigation_cancel)) { dialog, _ ->
                dialog.cancel()
            }.create()
    }

    override val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpened()) {
                    drawerLayout.closeDrawer()
                } else {
                    if (menuState == MenuState.Main) {
                        if (System.currentTimeMillis() > pressTime + 2000) {
                            pressTime = System.currentTimeMillis()
                            ToastUtil.getInstance().makeShort(
                                getString(R.string.press_again_to_exit)
                            )
                        } else {
                            finishAffinity()
                        }
                    } else {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        drawerLayout.setScrimColor(ContextCompat.getColor(this, R.color.black_alpha20))
        drawerLayout.addDrawerListener { _, slideOffset ->
            if (slideOffset < 0.5f) window.blueStatusBar() else window.whiteStatusBar()
            if (koinNavigationDrawerViewModel.userInfoFlow.value.isStudent) {
                if (slideOffset == 1f) koinNavigationDrawerViewModel.getUnreadMessageCount()
            }
        }

        menus.forEach { (state, view) ->
            view.setOnClickListener {
                when (state) {
                    MenuState.Owner -> {
                        val intent =
                            Intent(
                                Intent.ACTION_VIEW,
                                if (BuildConfig.IS_DEBUG) {
                                    Uri.parse(
                                        URLConstant.OWNER_URL_STAGE
                                    )
                                } else {
                                    Uri.parse(URLConstant.OWNER_URL_PRODUCTION)
                                }
                            )
                        startActivity(intent)
                    }

                    else -> {
                        koinNavigationDrawerViewModel.selectMenu(state)
                        when (state) {
                            MenuState.Store -> {
                                EventLogger.logClickEvent(
                                    EventAction.BUSINESS,
                                    AnalyticsConstant.Label.HAMBURGER_SHOP,
                                    getString(R.string.nearby_stores)
                                )
                            }

                            MenuState.Dining -> {
                                EventLogger.logClickEvent(
                                    EventAction.CAMPUS,
                                    AnalyticsConstant.Label.HAMBURGER_DINING,
                                    getString(R.string.navigation_item_dining)
                                )
                            }

                            MenuState.Land -> {
                                EventLogger.logClickEvent(
                                    EventAction.BUSINESS,
                                    AnalyticsConstant.Label.HAMBURGER,
                                    getString(R.string.navigation_item_real_estate)
                                )
                            }

                            MenuState.OperatingInfo -> {
                                EventLogger.logClickEvent(
                                    EventAction.CAMPUS,
                                    AnalyticsConstant.Label.HAMBURGER,
                                    getString(
                                        R.string.navigation_item_koreatech_operating_information
                                    )
                                )
                            }

                            MenuState.LoginOrLogout -> {
                                if (koinNavigationDrawerViewModel.userInfoFlow.value.isStudent || koinNavigationDrawerViewModel.userInfoFlow.value.isGeneral) {
                                    EventLogger.logClickEvent(
                                        EventAction.USER,
                                        AnalyticsConstant.Label.HAMBURGER,
                                        getString(R.string.navigation_item_logout)
                                    )
                                } else {
                                    EventLogger.logClickEvent(
                                        EventAction.USER,
                                        AnalyticsConstant.Label.HAMBURGER,
                                        "로그인 시도"
                                    )
                                }
                            }

                            MenuState.Article -> {
                                EventLogger.logClickEvent(
                                    EventAction.CAMPUS,
                                    AnalyticsConstant.Label.HAMBURGER,
                                    getString(R.string.navigation_item_article)
                                )
                            }

                            MenuState.BusTimetable -> {
                                EventLogger.logCampusClickEvent(
                                    AnalyticsConstant.Label.HAMBURGER,
                                    "버스 시간표"
                                )
                            }

                            MenuState.BusSearch -> {
                                EventLogger.logCampusClickEvent(
                                    AnalyticsConstant.Label.HAMBURGER,
                                    "교통편 조회하기"
                                )
                            }

                            MenuState.Club -> {
                                EventLogger.logCampusClickEvent(
                                    AnalyticsConstant.Label.HAMBURGER,
                                    "동아리"
                                )
                            }

                            MenuState.SignUp -> {
                                EventLogger.logSessionEvent(
                                    action = EventAction.USER,
                                    category = EventCategory.CLICK,
                                    label = AnalyticsConstant.Label.HAMBURGER,
                                    value = "회원가입 시작",
                                    customSessionId = koinNavigationDrawerViewModel.getSignUpSessionId()
                                )
                            }

                            else -> Unit
                        }
                    }
                }
            }
        }

        val leftArrowButton = findViewById<View>(R.id.drawer_left_arrow_button)
        leftArrowButton.setOnClickListener { // 왼쪽화살표  클릭리스너 등록
            drawerLayout.closeDrawer()
        }

        initDrawerViewModel()
    }

    override fun onResume() {
        super.onResume()
        if (drawerLayout.isDrawerOpened(GravityCompat.END)) {
            window.whiteStatusBar()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        ViewCompat.setOnApplyWindowInsetsListener(leftNavigationView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(
                top = systemBars.top,
                bottom = systemBars.bottom
            )
            WindowInsetsCompat.CONSUMED
        }

        leftNavigationView.layoutParams =
            leftNavigationView.layoutParams.apply { width = windowWidth }
    }

    private fun initDrawerViewModel() = with(koinNavigationDrawerViewModel) {
        observeLiveData(menuEvent) { menuState ->
            when (menuState) {
                MenuState.BusTimetable -> goToBusTimetableActivity()
                MenuState.BusSearch -> goToBusSearchActivity()
                MenuState.Dining -> goToDiningActivity()
                MenuState.OperatingInfo -> goToOperatingInfoActivity()
                MenuState.Land -> goToLandActivity()
                MenuState.Main -> goToMainActivity()
                MenuState.Store -> goToStoreActivity()
                MenuState.Chat -> goToChatActivity()
                MenuState.SignUp -> goToSignUpActivity()
                MenuState.Setting -> {
                    goToSettingActivity()
                    return@observeLiveData
                }

                MenuState.LoginOrLogout -> {
                    if (userInfoFlow.value.isStudent || userInfoFlow.value.isGeneral) {
                        logout()
                    }
                    goToLoginActivity()
                }

                MenuState.Timetable -> {
                    goToTimetableActivity()
                }

                MenuState.Club -> {
                    goToClubActivity()
                }

                MenuState.LoginOrLogout -> {
                    if (userInfoFlow.value.isStudent || userInfoFlow.value.isGeneral) {
                        logout()
                    }
                    goToLoginActivity()
                }

                MenuState.Article -> goToArticleActivity()

                MenuState.Contact -> {
                    goToContactWebActivity()
                }

                else -> Unit
            }
            drawerLayout.closeDrawer()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userInfoFlow.collect { user ->
                        when (user) {
                            User.Anonymous -> {
                                nameTextView.visibility = View.GONE
                                helloMessageTextView.text =
                                    getString(R.string.navigation_hello_message_anonymous)
                                loginOrLogoutTextView.text = getString(R.string.navigation_item_login)
                                signUpTextView.visibility = View.VISIBLE
                                chatMenuIcon.visibility = View.GONE
                                unReadMessageCountTextView.visibility = View.GONE
                            }

                            is User.Student -> {
                                nameTextView.text =
                                    if (user.nickname?.isNotEmpty() == true) {
                                        user.nickname!!
                                    } else if (user.name?.isNotEmpty() == true) {
                                        user.name!!
                                    } else {
                                        "회원"
                                    }
                                nameTextView.visibility = View.VISIBLE
                                helloMessageTextView.text = getString(R.string.navigation_hello_message)
                                loginOrLogoutTextView.text = getString(R.string.navigation_item_logout)
                                signUpTextView.visibility = View.GONE
                                chatMenuIcon.visibility = View.VISIBLE
                                koinNavigationDrawerViewModel.getUnreadMessageCount()

                                when (menuState) {
                                    MenuState.Main -> {
                                        if (!checkMainPermission()) {
                                            requestMainPermissionLauncher.launch(
                                                MAIN_REQUIRED_PERMISSION
                                            )
                                        }
                                        koinNavigationDrawerViewModel.updateDeviceToken()
                                    }

                                    else -> Unit
                                }
                            }

                            is User.General -> {
                                nameTextView.text =
                                    if (user.nickname?.isNotEmpty() == true) {
                                        user.nickname!!
                                    } else if (user.name.isNotEmpty()) {
                                        user.name
                                    } else {
                                        "회원"
                                    }
                                nameTextView.visibility = View.VISIBLE
                                helloMessageTextView.text = getString(R.string.navigation_hello_message)
                                loginOrLogoutTextView.text = getString(R.string.navigation_item_logout)
                                signUpTextView.visibility = View.GONE
                                chatMenuIcon.visibility = View.VISIBLE
                                koinNavigationDrawerViewModel.getUnreadMessageCount()

                                when (menuState) {
                                    MenuState.Main -> {
                                        if (!checkMainPermission()) {
                                            requestMainPermissionLauncher.launch(
                                                MAIN_REQUIRED_PERMISSION
                                            )
                                        }
                                        koinNavigationDrawerViewModel.updateDeviceToken()
                                    }

                                    else -> Unit
                                }
                            }
                        }
                    }
                }
                launch {
                    unReadMessageCount.collectLatest {
                        if (it > 0) {
                            unReadMessageCountTextView.visibility = View.VISIBLE
                            unReadMessageCountTextView.text = "$it"
                        } else {
                            unReadMessageCountTextView.visibility = View.GONE
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getStoreSprintEnabled()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.left_nav_view) {
            drawerLayout.closeDrawer(GravityCompat.END)
            return true
        }

        drawerLayout.closeDrawer(GravityCompat.END)
        return true
    }

    open fun callDrawerItem(itemId: Int) {
        when (itemId) {
            R.id.navi_item_home -> {
                koinNavigationDrawerViewModel.selectMenu(MenuState.Main)
            }

            R.id.navi_item_store -> {
                koinNavigationDrawerViewModel.selectMenu(MenuState.Store)
            }

            R.id.navi_item_dining -> {
                koinNavigationDrawerViewModel.selectMenu(MenuState.Dining)
            }

            R.id.navi_item_operating_information -> {
                koinNavigationDrawerViewModel.selectMenu(MenuState.OperatingInfo)
            }

            R.id.navi_item_land -> {
                koinNavigationDrawerViewModel.selectMenu(MenuState.Land)
            }

            else -> {
                ToastUtil.getInstance().makeShort(getString(R.string.to_be_opened))
            }
        }
    }

    open fun callDrawerItem(itemId: Int, bundle: Bundle?) {
        if (itemId == R.id.navi_item_store) {
            goToStoreActivity(bundle)
        }
    }

    private fun goToMainActivity() {
        goToActivityFinish(Intent(this, MainActivity::class.java))
    }

    private fun goToStoreActivity(bundle: Bundle? = bundleOf()) {
        val intent = Intent(
            this,
            if (koinNavigationDrawerViewModel.isStoreSprintEnabled.value == false) `in`.koreatech.koin.feature.store.StoreActivity::class.java else StoreActivity::class.java
        )
        intent.putExtras(bundle!!)

        if (menuState != MenuState.Main) {
            goToActivityFinish(intent)
        } else {
            startActivity(intent)
        }
    }

    private fun goToBusTimetableActivity() {
        if (menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, BusTimetableActivity::class.java))
        } else {
            startActivity(Intent(this, BusTimetableActivity::class.java))
        }
    }

    private fun goToBusSearchActivity() {
        if (menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, BusSearchActivity::class.java))
        } else {
            startActivity(Intent(this, BusSearchActivity::class.java))
        }
    }

    private fun goToDiningActivity() {
        if (menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, DiningActivity::class.java))
        } else {
            startActivity(Intent(this, DiningActivity::class.java))
        }
    }

    private fun goToOperatingInfoActivity() {
        if (menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, OperatingInfoActivity::class.java))
        } else {
            startActivity(Intent(this, OperatingInfoActivity::class.java))
        }
    }

    private fun goToTimetableActivity() {
        if (menuState != MenuState.Main) {
            goToActivityFinish(
                Intent(this, `in`.koreatech.koin.ui.timetablev2.TimetableActivity::class.java)
            )
        } else {
            val intent =
                Intent(
                    this,
                    `in`.koreatech.koin.ui.timetablev2.TimetableActivity::class.java
                ).apply {
                    if (koinNavigationDrawerViewModel.userInfoFlow.value.isAnonymous) {
                        putExtra("isAnonymous", true)
                    } else {
                        putExtra("isAnonymous", false)
                    }
                }
            EventLogger.logClickEvent(
                action = EventAction.USER,
                label = "hamburger",
                value = "시간표"
            )
            startActivity(intent)
        }
    }

    private fun goToClubActivity() {
        if (menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, ClubActivity::class.java))
        } else {
            startActivity(Intent(this, ClubActivity::class.java))
        }
    }

    private fun goToArticleActivity() {
        val intent = Intent(this, ArticleActivity::class.java)

        if (menuState != MenuState.Main) {
            goToActivityFinish(intent)
        } else {
            startActivity(intent)
        }
    }

    private fun goToLandActivity() {
        if (menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, LandActivity::class.java))
        } else {
            startActivity(Intent(this, LandActivity::class.java))
        }
    }

    /**
     * right navigation drawer 서비스 메뉴 호출
     */
    private fun goToSettingActivity() {
        Intent(this, SettingActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun goToSignUpActivity() {
        Intent(this, SignUpActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun goToChatActivity() {
        EventLogger.logCampusClickEvent(
            AnalyticsConstant.Label.CHAT.HAMBURGER,
            "쪽지"
        )
        Intent(this, ChatListActivity::class.java).apply {
            startActivity(this)
        }
    }

    fun showLoginRequestDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.user_only))
            .setMessage(getString(R.string.login_request))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.navigation_ok)) { dialog, _ ->
                val intent =
                    Intent(
                        this,
                        SignInActivity::class.java
                    )
                intent.putExtra("FIRST_LOGIN", false)
                startActivity(intent)
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out)
            }
            .setNegativeButton(getString(R.string.navigation_cancel)) { dialog, _ ->
                dialog.cancel()
            }
        val dialog = builder.create() // 알림창 객체 생성
        dialog.show() // 알림창 띄우기
    }

    private fun goToActivityFinish(intent: Intent) {
        startActivity(intent)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out)
        finish()
    }

    private fun goToContactWebActivity() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL.KOIN_ASK_FORM)))
    }

    private fun goToLoginActivity() {
        Intent(
            this,
            SignInActivity::class.java
        ).apply {
            startActivity(this)
        }
    }

    fun toggleNavigationDrawer() {
        drawerLayout.toggleDrawer()
    }

    private fun checkMainPermission() = MAIN_REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val MAIN_REQUIRED_PERMISSION =
            mutableListOf<String>().apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.POST_NOTIFICATIONS)
                }
            }.toTypedArray()
    }

    override fun onDestroy() {
        loginAlertDialog.dismiss()
        super.onDestroy()
    }
}
