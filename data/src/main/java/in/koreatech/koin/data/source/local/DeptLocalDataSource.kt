package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.R
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeptLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun getDeptFromDeptCode(deptCode: String) = context.getString(
        when (deptCode) {
            "20" -> R.string.dept_mechanical_engineering
            "80" -> R.string.dept_industrial_management
            "35", "36" -> R.string.dept_computer_science_and_engineering
            "40" -> R.string.dept_mechatronics_engineering
            "85" -> R.string.dept_employment_service
            "51" -> R.string.dept_industrial_design
            "74" -> R.string.dept_energy_meterials_chemical_engineering
            "61" -> R.string.dept_electrical_electronics_and_communication_engineering
            "72" -> R.string.dept_architectural_engineering
            else -> throw IllegalArgumentException()
        }
    )
}