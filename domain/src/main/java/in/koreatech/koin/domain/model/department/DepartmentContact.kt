package `in`.koreatech.koin.domain.model.department

import java.time.LocalDateTime

data class DepartmentContact(
    val task: String,
    val phoneNumber: String
)

data class Department(
    val name: String,
    val isSingleContact: Boolean,
    val contacts: List<DepartmentContact>
) {
    val singlePhoneNumber: String? get() = contacts.firstOrNull()?.phoneNumber
}

data class DepartmentCategoryContacts(
    val category: String,
    val categoryName: String,
    val departments: List<Department>
)

data class DepartmentContacts(
    val updatedAt: LocalDateTime,
    val categories: List<DepartmentCategoryContacts>
)

data class DepartmentContactsByCategory(
    val updatedAt: LocalDateTime,
    val categoryContacts: DepartmentCategoryContacts
)
