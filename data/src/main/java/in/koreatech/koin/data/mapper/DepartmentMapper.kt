package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.department.DepartmentCategoryContactsResponse
import `in`.koreatech.koin.data.response.department.DepartmentContactResponse
import `in`.koreatech.koin.data.response.department.DepartmentContactsByCategoryResponse
import `in`.koreatech.koin.data.response.department.DepartmentContactsResponse
import `in`.koreatech.koin.data.response.department.DepartmentResponse
import `in`.koreatech.koin.domain.model.department.Department
import `in`.koreatech.koin.domain.model.department.DepartmentCategoryContacts
import `in`.koreatech.koin.domain.model.department.DepartmentContact
import `in`.koreatech.koin.domain.model.department.DepartmentContacts
import `in`.koreatech.koin.domain.model.department.DepartmentContactsByCategory
import java.time.LocalDateTime

fun DepartmentContactResponse.toDepartmentContact() = DepartmentContact(
    task = task,
    phoneNumber = phoneNumber
)

fun DepartmentResponse.toDepartment() = Department(
    name = name,
    isSingleContact = isSingleContact,
    contacts = contacts.map { it.toDepartmentContact() }
)

fun DepartmentCategoryContactsResponse.toDepartmentCategoryContacts() = DepartmentCategoryContacts(
    category = category,
    categoryName = categoryName,
    departments = departments.map { it.toDepartment() }
)

fun DepartmentContactsResponse.toDepartmentContacts() = DepartmentContacts(
    updatedAt = LocalDateTime.parse(updatedAt),
    categories = categories.map { it.toDepartmentCategoryContacts() }
)

fun DepartmentContactsByCategoryResponse.toDepartmentContactsByCategory() = DepartmentContactsByCategory(
    updatedAt = LocalDateTime.parse(updatedAt),
    categoryContacts = DepartmentCategoryContacts(
        category = category,
        categoryName = categoryName,
        departments = departments.map { it.toDepartment() }
    )
)