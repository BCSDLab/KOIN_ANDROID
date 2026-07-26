package `in`.koreatech.koin.feature.department.state

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.department.Department
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class DepartmentState(
    val name: String,
    val isSingleContact: Boolean,
    val tasks: ImmutableList<DepartmentTaskState>
) {
    val singlePhoneNumber: String? get() = tasks.firstOrNull()?.phoneNumber
}

@Immutable
data class DepartmentTaskState(
    val name: String,
    val phoneNumber: String
)

fun Department.toDepartmentState() =
    DepartmentState(
        name = name,
        isSingleContact = isSingleContact,
        tasks = contacts.map { DepartmentTaskState(name = it.task, phoneNumber = it.phoneNumber) }.toImmutableList()
    )
