package `in`.koreatech.koin.ui.scheme

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import androidx.core.content.getSystemService
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.core.navigation.NavigatorType
import `in`.koreatech.koin.core.navigation.SchemeType
import `in`.koreatech.koin.core.navigation.utils.EXTRA_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_NAV_TYPE
import `in`.koreatech.koin.core.navigation.utils.EXTRA_TYPE
import `in`.koreatech.koin.core.navigation.utils.EXTRA_URL
import `in`.koreatech.koin.core.navigation.utils.toHost
import `in`.koreatech.koin.databinding.ActivitySchemeBinding
import `in`.koreatech.koin.ui.main.activity.MainActivity
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SchemeActivity : ActivityBase() {
    @Inject
    lateinit var navigator: Navigator

    companion object {
        private const val screenTitle = "스킴화면"
    }

    override val screenTitle: String
        get() = SchemeActivity.screenTitle
    private lateinit var binding: ActivitySchemeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchemeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleIntent()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent()
    }

    private fun handleIntent() {
        intent.getStringExtra(EXTRA_URL).let { url ->
            val tasks = getSystemService<ActivityManager>()?.appTasks?.map {
                /**
                 * class Name : in.koreatech.koin.ui.main.activity.MainActivity
                 */
                it.taskInfo.baseActivity?.className?.equals(MainActivity::class.qualifiedName) == true
            }

            val navigatorType = tasks?.any { it }.let {
                if (it == true) {
                    NavigatorType.DETAIL
                } else {
                    NavigatorType.MAIN
                }
            }
            Timber.e("navigatorType : $navigatorType")
            Timber.e("url : ${url}")
            Timber.e("url to host : ${url?.toHost()}")

            /**
             * koin://shop
             * url?.toHost = shop
             */
            when (navigatorType) {
                NavigatorType.MAIN -> {
                    val intent = navigator.navigateToSplash(
                        context = this,
                        targetId = Pair(EXTRA_ID, ""),
                        type = Pair(EXTRA_TYPE, url?.toHost()),
                        navType = Pair(EXTRA_NAV_TYPE, NavigatorType.MAIN.type)
                    )
                    navigateToActivity(intent)
                }

                NavigatorType.DETAIL -> {
                    when (val host = url?.toHost()) {
                        SchemeType.SHOP.type -> {
                            val intent = navigator.navigateToShop(
                                context = this,
                                targetId = Pair(EXTRA_ID, ""),
                                type = Pair(EXTRA_TYPE, host)
                            )
                            navigateToActivity(intent)
                        }

                        SchemeType.DINING.type -> {
                            val intent = navigator.navigateToDinging(
                                context = this,
                                targetId = Pair(EXTRA_ID, ""),
                                type = Pair(EXTRA_TYPE, host)
                            )
                            navigateToActivity(intent)
                        }

                        SchemeType.ARTICLE.type -> {
                            val intent = navigator.navigateToArticle(
                                context = this,
                                targetId = Pair(
                                    EXTRA_ID,
                                    url.substringAfterLast("=").toLongOrNull()
                                ),
                                type = Pair(EXTRA_TYPE, host)
                            )
                            navigateToActivity(intent)
                        }

                        else -> {
                            val intent = navigator.navigateToMain(context = this)
                            navigateToActivity(intent)
                        }
                    }
                }

                else -> {
                    val intent = navigator.navigateToMain(context = this)
                    navigateToActivity(intent)
                }
            }
        }
    }

    private fun navigateToActivity(intent: Intent) {
        startActivity(intent)
        finish()
    }
}