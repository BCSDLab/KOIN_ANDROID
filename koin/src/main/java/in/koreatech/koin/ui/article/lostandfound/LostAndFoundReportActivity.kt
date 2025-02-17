package `in`.koreatech.koin.ui.article.lostandfound

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.os.bundleOf
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithDarkStatusBar
import `in`.koreatech.koin.feature.lostandfound.ui.report.LostAndFoundReport
import `in`.koreatech.koin.ui.article.ArticleActivity
import `in`.koreatech.koin.ui.article.ArticleActivity.Companion.BUNDLE_ARTICLE_EXTRA_KEY
import `in`.koreatech.koin.ui.article.ArticleBoardType

@AndroidEntryPoint
class LostAndFoundReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithDarkStatusBar()
        setContent {
            LostAndFoundReport(
                articleId = intent.getIntExtra("article_id", 0),
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