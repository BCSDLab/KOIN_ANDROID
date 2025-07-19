package `in`.koreatech.koin.ui.error

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import `in`.koreatech.koin.R
import `in`.koreatech.koin.databinding.ActivityInspectionBinding
import `in`.koreatech.koin.ui.main.activity.MainActivity

class InspectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInspectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInspectionBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inspection)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnGotoMain.setOnClickListener {
            val intent = Intent(this@InspectionActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
