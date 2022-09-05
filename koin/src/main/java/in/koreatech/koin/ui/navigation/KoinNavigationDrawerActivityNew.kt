package `in`.koreatech.koin.ui.navigation

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.activity.WebViewActivity
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.ui.bus.BusActivity
import `in`.koreatech.koin.ui.dining.DiningActivity
import `in`.koreatech.koin.ui.land.LandActivity
import `in`.koreatech.koin.ui.login.LoginActivity
import `in`.koreatech.koin.ui.main.MainActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.navigation.viewmodel.KoinNavigationDrawerViewModel
import `in`.koreatech.koin.ui.store.StoreActivity
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
abstract class KoinNavigationDrawerActivityNew : ActivityBase(),
    NavigationView.OnNavigationItemSelectedListener {
    protected abstract val menuState: MenuState
    private var pressTime = System.currentTimeMillis()
    private val koinNavigationDrawerViewModel by viewModels<KoinNavigationDrawerViewModel>()

    private val drawerLayout by lazy {
        findViewById<DrawerLayout>(R.id.drawer_layout)
    }

    private val leftNavigationView by lazy {
        findViewById<NavigationView>(R.id.left_nav_view)
    }

    private val menus by lazy {
        listOf(
            R.id.navi_item_store,
            R.id.navi_item_bus, R.id.navi_item_dining,
            R.id.navi_item_timetable, R.id.navi_item_land
        ).map {
            findViewById<View>(it)
        }.zip(
            listOf(
                MenuState.Store,
                MenuState.Bus,
                MenuState.Dining,
                MenuState.Timetable,
                MenuState.Land
            )
        ) { view, state ->
            state to view
        }.toMap()
    }

    private val menuTextViews by lazy {
        listOf(
            R.id.navi_item_store_textview,
            R.id.navi_item_bus_textview, R.id.navi_item_dining_textview,
            R.id.navi_item_timetable_textview, R.id.navi_item_land_textview
        ).map {
            findViewById<TextView>(it).apply {
                changeMenuFont(this)
            }
        }.zip(
            listOf(
                MenuState.Store,
                MenuState.Bus,
                MenuState.Dining,
                MenuState.Timetable,
                MenuState.Land
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
            koinNavigationDrawerViewModel.selectMenu(MenuState.MyInfo)
        }

        val leftArrowButton = findViewById<View>(R.id.drawer_left_arrow_button)
        leftArrowButton.setOnClickListener { //왼쪽화살표  클릭리스너 등록
            drawerLayout.closeDrawer()
        }

        val logoImageView = findViewById<View>(R.id.bcsd_logo)
        logoImageView.setOnClickListener {
            goToNavigationDeveloper()
        }

        initViewModel()
        koinNavigationDrawerViewModel.getUser()
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

    private fun initViewModel() = with(koinNavigationDrawerViewModel) {
        observeLiveData(userState) { (user, _) ->
            val nameTextview = findViewById<TextView>(R.id.base_naviagtion_drawer_nickname_textview)
            if (isAnonymous) {
                nameTextview.text = "익명"
            } else {
                nameTextview.text = user!!.name
            }
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
                MenuState.Timetable -> if (isAnonymous) {
                    goToAnonymousTimeTableActivity()
                } else {
                    goToTimetableActivty()
                }
                MenuState.MyInfo -> if (isAnonymous) {
                    showLoginRequestDialog()
                } else {
                    goToUserInfoActivity()
                }

            }
            drawerLayout.closeDrawer()
        }

        observeLiveData(logoutEvent) {
            finishAffinity()
            startActivity(Intent(this@KoinNavigationDrawerActivityNew, LoginActivity::class.java))
        }

        observeLiveData(logoutError) {
            ToastUtil.getInstance().makeShort(it)
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
        if(menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, StoreActivity::class.java))
        } else {
            startActivity(Intent(this, StoreActivity::class.java))
        }
    }

    private fun goToDiningActivity() {
        if(menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, DiningActivity::class.java))
        } else {
            startActivity(Intent(this, DiningActivity::class.java))
        }
    }

    private fun goToBusActivity() {
        if(menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, BusActivity::class.java))
        } else {
            startActivity(Intent(this, BusActivity::class.java))
        }
    }

    private fun goToStoreActivity(bundle: Bundle?) {
        val intent = Intent(this, StoreActivity::class.java)
        intent.putExtras(bundle!!)

        if(menuState != MenuState.Main) {
            goToActivityFinish(intent)
        } else {
            startActivity(intent)
        }
    }

    private fun goToBusActivity(bundle: Bundle?) {
        val intent = Intent(this, BusActivity::class.java)
        intent.putExtras(bundle!!)

        if(menuState != MenuState.Main) {
            goToActivityFinish(intent)
        } else {
            startActivity(intent)
        }
    }

    private fun goToTimetableActivty() {
        if(menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, TimetableActivity::class.java))
        } else {
            startActivity(Intent(this, TimetableActivity::class.java))
        }
    }

    private fun goToLandActivity() {
        if(menuState != MenuState.Main) {
            goToActivityFinish(Intent(this, LandActivity::class.java))
        } else {
            startActivity(Intent(this, LandActivity::class.java))
        }
    }

    private fun goToAnonymousTimeTableActivity() {
        if(menuState != MenuState.Main) {
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

    fun onClickNavigationLogout() {
        koinNavigationDrawerViewModel.logout()
    }

    private fun goToActivityFinish(intent: Intent) {
        startActivity(intent)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out)
        finish()
    }

    private fun goToNavigationDeveloper() {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("title", "BCSD 홈페이지")
        intent.putExtra("url", "https://bcsdlab.com/")
        startActivity(intent)
    }

    fun toggleNavigationDrawer() {
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