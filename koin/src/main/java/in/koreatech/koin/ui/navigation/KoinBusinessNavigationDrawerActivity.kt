package `in`.koreatech.koin.ui.navigation

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.activity.WebViewActivity
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.ui.bus.BusActivity
import `in`.koreatech.koin.ui.dining.DiningActivity
import `in`.koreatech.koin.ui.land.LandActivity
import `in`.koreatech.koin.ui.login.LoginActivity
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.navigation.viewmodel.KoinNavigationDrawerViewModel
import `in`.koreatech.koin.ui.store.activity.StoreActivity
import `in`.koreatech.koin.ui.timetable.TimetableActivity
import `in`.koreatech.koin.ui.timetable.TimetableAnonymousActivity
import `in`.koreatech.koin.ui.userinfo.UserInfoActivity
import `in`.koreatech.koin.util.ext.*
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class KoinBusinessNavigationDrawerActivity : ActivityBase(),
    NavigationView.OnNavigationItemSelectedListener {
    protected abstract val menuState: MenuState

    val drawerLayoutId get() = R.id.business_drawer_layout

    private var pressTime = System.currentTimeMillis()
    private val koinNavigationDrawerViewModel by viewModels<KoinNavigationDrawerViewModel>()

    private val drawerLayout by lazy {
        findViewById<DrawerLayout>(drawerLayoutId)
    }

    private val leftNavigationView by lazy {
        findViewById<NavigationView>(R.id.business_left_nav_view)
    }

    private val menus by lazy {
        listOf(
            R.id.navi_item_store,
            R.id.navi_item_bus, R.id.navi_item_manual,
            R.id.navi_item_modify_store_info, R.id.navi_item_order_management,
            R.id.navi_item_sales_management, R.id.navi_item_menu_management,
            R.id.navi_item_add_store
        ).map {
            findViewById<View>(it)
        }.zip(
            listOf(
                MenuState.Store,
                MenuState.Bus,
                MenuState.Manual,
                MenuState.StoreInfo,
                MenuState.Order,
                MenuState.Sales,
                MenuState.Menu,
                MenuState.AddStore
            )
        ) { view, state ->
            state to view
        }.toMap()
    }

    private val menuTextViews by lazy {
        listOf(
            R.id.navi_item_text_view_store,
            R.id.navi_item_text_view_bus, R.id.navi_item_text_view_manual,
            R.id.navi_item_text_view_modify_store_info, R.id.navi_item_text_view_order_management,
            R.id.navi_item_text_view_sales_management, R.id.navi_item_text_view_menu_management,
            R.id.navi_item_text_view_add_store
        ).map {
            findViewById<TextView>(it).apply {
                changeMenuFont(this)
            }
        }.zip(
            listOf(
                MenuState.Store,
                MenuState.Bus,
                MenuState.Manual,
                MenuState.StoreInfo,
                MenuState.Order,
                MenuState.Sales,
                MenuState.Menu,
                MenuState.AddStore
            )
        ) { view, state ->
            state to view
        }.toMap()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        drawerLayout.setScrimColor(ContextCompat.getColor(this, R.color.black_alpha20))
        drawerLayout.addDrawerListener { _, slideOffset ->
            if (slideOffset < 0.5f) window.blueStatusBar() else window.whiteStatusBar()
        }

        menus.forEach { (state, view) ->
            view.setOnClickListener {
                koinNavigationDrawerViewModel.selectMenu(state)
            }
        }

        findViewById<View>(R.id.navi_item_myinfo).setOnClickListener {
            koinNavigationDrawerViewModel.selectMenu(MenuState.UserInfo)
        }

        val leftArrowButton = findViewById<View>(R.id.drawer_left_arrow_button)
        leftArrowButton.setOnClickListener {
            drawerLayout.closeDrawer()
        }

        val logoImageView = findViewById<View>(R.id.bcsd_logo)
        logoImageView.setOnClickListener {
            goToNavigationDeveloper()
        }

        initDrawerViewModel()
        koinNavigationDrawerViewModel.getOwnerName()
        koinNavigationDrawerViewModel.initMenu(menuState)
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
                    ToastUtil.getInstance().makeShort("뒤로가기 버튼을 한 번 더 누르면 종료됩니다.")
                } else {
                    finishAffinity()
                }
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun initDrawerViewModel() = with(koinNavigationDrawerViewModel) {
        observeLiveData(ownerName) { name ->
            val nameTextview =
                findViewById<TextView>(R.id.base_business_naviagtion_drawer_nickname_textview)
            nameTextview.text = name
        }

        observeLiveData(selectedMenu) {
            menuTextViews.forEach { (state, textView) ->
                if (menuState == state) {
                    textView.selected
                } else {
                    textView.normal
                }
            }
        }

        observeLiveData(menuEvent) { menuState ->
            when (menuState) {
                MenuState.Bus -> goToBusActivity()
                MenuState.Dining -> goToDiningActivity()
                MenuState.Land -> goToLandActivity()
                MenuState.Main -> goToMainActivity()
                MenuState.Store -> goToStoreActivity()
                MenuState.Timetable -> {
                    if (userState.value == null || userState.value?.isAnonymous == true) {
                        goToAnonymousTimeTableActivity()
                    } else {
                        goToTimetableActivty()
                    }
                }
                MenuState.UserInfo -> {
                    if (userState.value == null || userState.value?.isAnonymous == true) {
                        showLoginRequestDialog()
                    } else {
                        goToUserInfoActivity()
                    }
                }
            }
            drawerLayout.closeDrawer()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.business_left_nav_view) {
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
                ToastUtil.getInstance().makeShort("서비스예정입니다")
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
            view.typeface = Typeface.createFromAsset(assets, "fonts/notosanscjkkr_regular.otf")
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
        val intent = Intent(this, UserInfoActivity::class.java)
        startActivity(intent)
    }

    fun showLoginRequestDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("회원 전용 서비스")
            .setMessage("로그인이 필요한 서비스입니다.\n로그인 하시겠습니까?")
            .setCancelable(false)
            .setPositiveButton("확인") { dialog, _ ->
                val intent = Intent(
                    this,
                    LoginActivity::class.java
                )
                intent.putExtra("FIRST_LOGIN", false)
                startActivity(intent)
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out)
            }
            .setNegativeButton("취소") { dialog, _ ->
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

    private fun goToNavigationDeveloper() {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("title", getString(R.string.bcsd_webpage_name))
        intent.putExtra("url", "https://bcsdlab.com/")
        startActivity(intent)
    }

    fun toggleBusinessNavigationDrawer() {
        drawerLayout.toggleDrawer()
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
}