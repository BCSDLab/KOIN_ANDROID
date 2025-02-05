package `in`.koreatech.koin.ui.article.lostandfound

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.os.bundleOf
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.feature.lostandfound.ui.report.LostAndFoundReport
import `in`.koreatech.koin.ui.article.ArticleActivity
import `in`.koreatech.koin.ui.article.ArticleActivity.Companion.BUNDLE_ARTICLE_EXTRA_KEY
import `in`.koreatech.koin.ui.article.ArticleBoardType

@AndroidEntryPoint
class LostAndFoundReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        setContent {
            LostAndFoundReport(
                articleId = intent.getIntExtra("articleId", 0),
                onSuccess = {
                    startActivity(Intent(this, ArticleActivity::class.java).apply {
                        putExtra(
                            BUNDLE_ARTICLE_EXTRA_KEY, bundleOf(
                                ArticleActivity.START_BOARD to ArticleBoardType.LOSTANDFOUND.id
                            )
                        )
                    })
                    finish()
                })
        }
    }
}