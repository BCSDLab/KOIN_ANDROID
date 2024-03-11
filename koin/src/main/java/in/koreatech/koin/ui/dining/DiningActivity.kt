package `in`.koreatech.koin.ui.dining

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.databinding.DiningActivityMainBinding
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.ext.toColorForHtml
import `in`.koreatech.koin.domain.util.ext.toUnderlineForHtml
import `in`.koreatech.koin.ui.dining.adapter.DiningRecyclerViewAdapter
import `in`.koreatech.koin.ui.dining.viewmodel.DiningViewModel
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiningActivity : KoinNavigationDrawerActivity(),
    SwipeRefreshLayout.OnRefreshListener {
    override val menuState: MenuState = MenuState.Dining
    lateinit var binding: DiningActivityMainBinding
    private val diningViewModel by viewModels<DiningViewModel>()
    private val diningAdapter = DiningRecyclerViewAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.dining_activity_main)
        init()
        with(diningViewModel) {
            updateDiningData()
            selectedDate.observe(this@DiningActivity) {
                binding.diningDateTextView.text = it
            }
            diningData.observe(this@DiningActivity) {
                setSwipeRefreshingFalse()
                updateRecyclerData()
            }
            isLoading.observe(this@DiningActivity) {
                if (it) {
                    showProgressDialog(R.string.loading)
                    //NavigationDrawer Refactoring 할 시 변경
                } else {
                    hideProgressDialog()
                    //NavigationDrawer Refactoring 할 시 변경
                }
            }
            isDataLoaded.observe(this@DiningActivity) {
                setSwipeRefreshingFalse()
                if (!it) {
                    updateRecyclerData()
                    Toast.makeText(
                        this@DiningActivity,
                        R.string.error_network,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            isDateError.observe(this@DiningActivity) {
                if(it) {
                    Toast.makeText(this@DiningActivity, R.string.dining_no_more_data_load, Toast.LENGTH_SHORT).show()
                    dateErrorInit()
                }
            }
            when (selectedType) {
                is DiningType.Breakfast -> setTextSelected(binding.diningBreakfastButton)
                is DiningType.Lunch -> setTextSelected(binding.diningLunchButton)
                is DiningType.Dinner -> setTextSelected(binding.diningDinnerButton)
            }
        }


    }

    private fun init() {
        with(binding.diningRecyclerview) {
            adapter = diningAdapter
            layoutManager = LinearLayoutManager(this@DiningActivity)
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
        }

        binding.diningSwiperefreshlayout.setOnRefreshListener(this)

        binding.diningBreakfastButton.setOnClickListener {
            diningViewModel.selectedType = DiningType.Breakfast
            updateRecyclerData()
            setTextSelected(it as TextView)
            with(binding.diningLunchButton) {
                text = getString(R.string.dining_lunch)
            }
            with(binding.diningDinnerButton) {
                text = getString(R.string.dining_dinner)
            }
        }

        binding.diningLunchButton.setOnClickListener {
            diningViewModel.selectedType = DiningType.Lunch
            updateRecyclerData()
            setTextSelected(it as TextView)
            with(binding.diningBreakfastButton) {
                text = getString(R.string.dining_breakfast)
            }
            with(binding.diningDinnerButton) {
                text = getString(R.string.dining_dinner)
            }
        }

        binding.diningDinnerButton.setOnClickListener {
            diningViewModel.selectedType = DiningType.Dinner
            updateRecyclerData()
            setTextSelected(it as TextView)
            with(binding.diningBreakfastButton) {
                text = getString(R.string.dining_breakfast)
            }
            with(binding.diningLunchButton) {
                text = getString(R.string.dining_lunch)
            }
        }

        binding.diningBeforeDateButton.setOnClickListener {
            onPreviousDay()
        }

        binding.diningNextDateButton.setOnClickListener {
            onNextDay()
        }

        binding.koinBaseAppBarDark.setOnClickListener {
            when (it.id) {
                AppBarBase.getLeftButtonId() -> callDrawerItem(R.id.navi_item_home)
                AppBarBase.getRightButtonId() -> toggleNavigationDrawer()
            }
        }
    }

    override fun onRefresh() {
        diningViewModel.updateDiningData()
    }

    private fun onPreviousDay() {
        diningViewModel.getPreviousDayDiningData()
    }

    private fun onNextDay() {
        diningViewModel.getNextDayDiningData()
    }

    private fun updateRecyclerData() {
        with(diningViewModel.diningData.value) {
            if ((this == null) || isEmpty()) {
                diningAdapter.setData(listOf())
                binding.diningViewEmpty.emptyDiningListFrameLayout.visibility = View.VISIBLE
            } else {
                with(DiningUtil.typeFiltering(this, diningViewModel.selectedType)) {
                    if (isEmpty()) {
                        binding.diningViewEmpty.emptyDiningListFrameLayout.visibility = View.VISIBLE
                    } else {
                        binding.diningViewEmpty.emptyDiningListFrameLayout.visibility = View.GONE
                    }
                    diningAdapter.setData(this)
                }
            }
        }
    }

    private fun setTextSelected(view: TextView) {
        val color = "#" + Integer.toHexString(
            ContextCompat.getColor(this, R.color.colorAccent))

        view.text = if (Build.VERSION_CODES.N > Build.VERSION.SDK_INT) {
            Html.fromHtml(view.text.toString().toColorForHtml(color)
                .toUnderlineForHtml())
        } else {
            Html.fromHtml(
                view.text.toString().toColorForHtml(color)
                    .toUnderlineForHtml(),
                Html.FROM_HTML_MODE_LEGACY
            )
        }
    }

    private fun setSwipeRefreshingFalse() {
        with(binding.diningSwiperefreshlayout) {
            if (isRefreshing) isRefreshing = false
        }
    }
}