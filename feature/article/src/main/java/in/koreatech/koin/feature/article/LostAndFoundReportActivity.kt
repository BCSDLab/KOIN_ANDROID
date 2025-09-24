package `in`.koreatech.koin.feature.article

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.os.bundleOf
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithDarkStatusBar
import `in`.koreatech.koin.feature.article.enums.ArticleBoardType
import `in`.koreatech.koin.feature.article.ui.lostandfound.report.LostAndFoundReport

@AndroidEntryPoint
class LostAndFoundReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeWithDarkStatusBar()
        setContent {
            LostAndFoundReport(
                articleId = intent.getIntExtra("article_id", 0),
                onSuccess = {
                    startActivity(
                        Intent(this, ArticleActivity::class.java).apply {
                            putExtra(
                                ArticleActivity.Companion.BUNDLE_ARTICLE_EXTRA_KEY,
                                bundleOf(
                                    ArticleActivity.Companion.START_BOARD to ArticleBoardType.LOSTANDFOUND.id
                                )
                            )
                        }
                    )
                    finish()
                }
            )
        }
    }
}
