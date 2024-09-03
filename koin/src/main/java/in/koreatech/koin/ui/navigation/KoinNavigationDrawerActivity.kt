package `in`.koreatech.koin.ui.navigation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.BuildConfig
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.activity.WebViewActivity
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.FontManager
import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.ui.bus.BusActivity
import `in`.koreatech.koin.ui.dining.DiningActivity
import `in`.koreatech.koin.ui.land.LandActivity
import `in`.koreatech.koin.ui.login.LoginActivity
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.navigation.viewmodel.KoinNavigationDrawerViewModel
import `in`.koreatech.koin.ui.notification.NotificationActivity
import `in`.koreatech.koin.ui.store.activity.StoreActivity
import `in`.koreatech.koin.ui.timetable.TimetableActivity
import `in`.koreatech.koin.ui.timetable.TimetableAnonymousActivity
import `in`.koreatech.koin.ui.userinfo.UserInfoActivity
import `in`.koreatech.koin.util.ext.addDrawerListener
import `in`.koreatech.koin.util.ext.blueStatusBar
import `in`.koreatech.koin.util.ext.closeDrawer
import `in`.koreatech.koin.util.ext.isDrawerOpened
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.toggleDrawer
import `in`.koreatech.koin.util.ext.whiteStatusBar
import `in`.koreatech.koin.util.ext.windowWidth
import kotlinx.coroutines.launch

@AndroidEntryPoint
abstract class KoinNavigationDrawerActivity : ActivityBase(),
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
            R.id.navi_item_notification,
            R.id.navi_item_myinfo,
            R.id.navi_item_login_or_logout,
            R.id.navi_item_store,
            R.id.navi_item_bus, R.id.navi_item_dining,
            R.id.navi_item_timetable, R.id.navi_item_land,
            R.id.navi_item_owner
        ).map {
            findViewById<View>(it)
        }.zip(
            listOf(
                MenuState.Notification,
                MenuState.UserInfo,
                MenuState.LoginOrLogout,
                MenuState.Store,
                MenuState.Bus,
                MenuState.Dining,
                MenuState.Timetable,
                MenuState.Land,
                MenuState.Owner
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
    private val loginOrLogoutTextView by lazy {
        menus.get(MenuState.LoginOrLogout) as TextView? ?: findViewById(R.id.navi_item_login_or_logout)
    }


    private val requestMainPermissionLauncher = registerForActivityResult(
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
                EventLogger.logClickEvent(
                    AnalyticsConstant.Domain.USER,
                    AnalyticsConstant.Label.USER_ONLY_OK,
                    getString(R.string.user_only_ok)
                )
            }
            .setNegativeButton(getString(R.string.navigation_cancel)) { dialog, _ ->
                dialog.cancel()
            }.create()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        drawerLayout.setScrimColor(ContextCompat.getColor(this, R.color.black_alpha20))
        drawerLayout.addDrawerListener { _, slideOffset ->
            if (slideOffset < 0.5f) window.blueStatusBar() else window.whiteStatusBar()
        }

        menus.forEach { (state, view) ->
            view.setOnClickListener {
                when (state) {
                    MenuState.Owner -> {
                        Intent(this, WebViewActivity::class.java).apply {
                            putExtra(
                                "url",
                                if (BuildConfig.IS_DEBUG) URLConstant.OWNER_URL_STAGE
                                else URLConstant.OWNER_URL_PRODUCTION
                            )
                        }.run(::startActivity)
                    }

                    else -> {
                        koinNavigationDrawerViewModel.selectMenu(state)
                        when (state) {
                            MenuState.Store -> {
                                EventLogger.logClickEvent(
                                    AnalyticsConstant.Domain.BUSINESS,
                                    AnalyticsConstant.Label.HAMBURGER,
                                    getString(R.string.nearby_stores)
                                )
                            }

                            MenuState.Bus -> {
                                EventLogger.logClickEvent(
                                    AnalyticsConstant.Domain.CAMPUS,
                                    AnalyticsConstant.Label.HAMBURGER_BUS,
                                    getString(R.string.bus)
                                )
                            }

                            MenuState.Dining -> {
                                EventLogger.logClickEvent(
                                    AnalyticsConstant.Domain.CAMPUS,
                                    AnalyticsConstant.Label.HAMBURGER_DINING,
                                    getString(R.string.navigation_item_dining)
                                )
                            }

                            MenuState.Land -> {
                                EventLogger.logClickEvent(
                                    AnalyticsConstant.Domain.BUSINESS,
                                    AnalyticsConstant.Label.HAMBURGER,
                                    getString(R.string.navigation_item_real_estate)
                                )
                            }

                            MenuState.UserInfo -> {
                                if (koinNavigationDrawerViewModel.userInfoFlow.value.isAnonymous) {
                                    EventLogger.logClickEvent(
                                        AnalyticsConstant.Domain.USER,
                                        AnalyticsConstant.Label.HAMBURGER_MY_INFO_WITHOUT_LOGIN,
                                        getString(R.string.navigation_item_my_info)
                                    )
                                } else {
                                    EventLogger.logClickEvent(
                                        AnalyticsConstant.Domain.USER,
                                        AnalyticsConstant.Label.HAMBURGER_MY_INFO_WITH_LOGIN,
                                        getString(R.string.navigation_item_my_info)
                                    )
                                }
                            }

                            MenuState.LoginOrLogout -> {}

                            else -> Unit
                        }
                    }
                }
            }
        }

        val leftArrowButton = findViewById<View>(R.id.drawer_left_arrow_button)
        leftArrowButton.setOnClickListener { //왼쪽화살표  클릭리스너 등록
            drawerLayout.closeDrawer()
        }

        initDrawerViewModel()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        leftNavigationView.layoutParams =
            leftNavigationView.layoutParams.apply { width = windowWidth }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpened()) {
            drawerLayout.closeDrawer()
        } else {
            if (menuState == MenuState.Main) {
                if (System.currentTimeMillis() > pressTime + 2000) {
                    pressTime = System.currentTimeMillis()
                    ToastUtil.getInstance().makeShort(getString(R.string.press_again_to_exit))
                } else {
                    finishAffinity()
                }
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun initDrawerViewModel() = with(koinNavigationDrawerViewModel) {
        observeLiveData(menuEvent) { menuState ->
            when (menuState) {
                MenuState.Bus -> goToBusActivity()
                MenuState.Dining -> goToDiningActivity()
                MenuState.Land -> goToLandActivity()
                MenuState.Main -> goToMainActivity()
                MenuState.Store -> goToStoreActivity()
                MenuState.Timetable -> {
                    if (userInfoFlow.value.isAnonymous) {
                        goToAnonymousTimeTableActivity()
                    } else {
                        goToTimetableActivty()
                    }
                }

                MenuState.UserInfo -> {
                    if (userInfoFlow.value.isAnonymous) {
                        loginAlertDialog.show()
                    } else {
                        goToUserInfoActivity()
                    }
                }

                MenuState.Notification -> {
                    if (userInfoFlow.value.isAnonymous) {
                        loginAlertDialog.show()
                    } else {
                        goToNotificationActivity()
                    }
                }

                MenuState.LoginOrLogout -> {
                    if (userInfoFlow.value.isStudent) {
                        logout()
                    }
                    goToLoginActivity()
                }

                else -> Unit
            }
            drawerLayout.closeDrawer()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userInfoFlow.collect { user ->
                    when (user) {
                        User.Anonymous -> {
                            nameTextView.visibility = View.GONE
                            helloMessageTextView.text = getString(R.string.navigation_hello_message_anonymous)
                            loginOrLogoutTextView.text = getString(R.string.navigation_item_login)
                        }

                        is User.Student -> {
                            nameTextView.text = user.name
                            nameTextView.visibility = View.VISIBLE
                            helloMessageTextView.text = getString(R.string.navigation_hello_message)
                            loginOrLogoutTextView.text = getString(R.string.navigation_item_logout)

                            when (menuState) {
                                MenuState.Main, MenuState.Notification -> {
                                    if (!checkMainPermission()) requestMainPermissionLauncher.launch(MAIN_REQUIRED_PERMISSION)
                                    koinNavigationDrawerViewModel.updateDeviceToken()
                                }

                                else -> Unit
                            }
                        }
                    }
                }
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

            R.id.navi_item_bus -> {
                koinNavigationDrawerViewModel.selectMenu(MenuState.Bus)
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
        } else if (itemId == R.id.navi_item_bus) {
            goToBusActivity(bundle)
        }
    }

    private fun changeMenuFont(view: View) {
        if (view is TextView) {
            view.typeface = FontManager.getTypeface(this, FontManager.KoinFontType.PRETENDARD_REGULAR)
        }
    }

    private fun goToMainActivity() {
        goToActivityFinish(Intent(this, MainActivity::class.java))
    }

    private fun goToStoreActivity() {
        if (menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, StoreActivity::class.java))
        } else {
            startActivity(Intent(this, StoreActivity::class.java))
        }
    }

    private fun goToDiningActivity() {
        if (menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, DiningActivity::class.java))
        } else {
            startActivity(Intent(this, DiningActivity::class.java))
        }
    }

    private fun goToBusActivity() {
        if (menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, BusActivity::class.java))
        } else {
            startActivity(Intent(this, BusActivity::class.java))
        }
    }

    private fun goToStoreActivity(bundle: Bundle?) {
        val intent = Intent(this, StoreActivity::class.java)
        intent.putExtras(bundle!!)

        if (menuState != MenuState.Main) {
            goToActivityFinish(intent)
        } else {
            startActivity(intent)
        }
    }

    private fun goToBusActivity(bundle: Bundle?) {
        val intent = Intent(this, BusActivity::class.java)
        intent.putExtras(bundle!!)

        if (menuState != MenuState.Main) {
            goToActivityFinish(intent)
        } else {
            startActivity(intent)
        }
    }

    private fun goToTimetableActivty() {
        if (menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, TimetableActivity::class.java))
        } else {
            startActivity(Intent(this, TimetableActivity::class.java))
        }
    }

    private fun goToLandActivity() {
        if (menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, LandActivity::class.java))
        } else {
            startActivity(Intent(this, LandActivity::class.java))
        }
    }

    private fun goToAnonymousTimeTableActivity() {
        if (menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, TimetableAnonymousActivity::class.java))
        } else {
            startActivity(Intent(this, TimetableAnonymousActivity::class.java))
        }
    }

    /**
     * right navigation drawer 서비스 메뉴 호출
     */
    private fun goToUserInfoActivity() {
        Intent(this, UserInfoActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun goToNotificationActivity() {
        Intent(this, NotificationActivity::class.java).apply {
            startActivity(this)
        }
    }

    private fun goToActivityFinish(intent: Intent) {
        startActivity(intent)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out)
        finish()
    }

    private fun goToNavigationDeveloper() {
        Intent(this, WebViewActivity::class.java).apply {
            putExtra("title", getString(R.string.bcsd_webpage_name))
            putExtra("url", "https://bcsdlab.com/")
            startActivity(this)
        }
    }

    private fun goToLoginActivity() {
        Intent(
            this,
            LoginActivity::class.java
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

    val TextView.selected: TextView
        get() {
            val s = text.toString()
            val styledText = HtmlCompat.fromHtml(
                "<font color='#f7941e'>$s</font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            setText(styledText, TextView.BufferType.SPANNABLE) //#f7941e
            return this
        }

    val TextView.normal: TextView
        get() {
            text = text.toString()
            return this
        }

    companion object {
        private val MAIN_REQUIRED_PERMISSION = mutableListOf<String>().apply {
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