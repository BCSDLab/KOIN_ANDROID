package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.FakeUserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateStudentUserInfoUseCaseTest {

    private lateinit var userRepository: FakeUserRepository

    private val baseStudent = User.Student(
        id = 1,
        loginId = "student01",
        email = "student@koreatech.ac.kr",
        anonymousNickname = "익명",
        gender = Gender.Man,
        major = "컴퓨터공학부",
        name = "홍길동",
        nickname = "코인유저",
        phoneNumber = "010-1234-5678",
        studentNumber = "2023136001",
        userType = "STUDENT"
    )

    @Before
    fun setUp() {
        userRepository = FakeUserRepository()
    }

    @Test
    fun `정상적인 필드로 학생 정보를 업데이트한다`() = runTest {
        val result = UpdateStudentUserInfoUseCase(userRepository)(
            beforeUser = baseStudent,
            email = "new@koreatech.ac.kr",
            name = "이순신",
            nickname = "새닉네임",
            gender = Gender.Man,
            phoneNumber = "010-9999-8888",
            studentNumber = "2023130001",
            major = "기계공학부"
        )

        assertTrue(result.isSuccess)
        val updated = userRepository.getUpdatedUser() as User.Student
        assertEquals("new@koreatech.ac.kr", updated.email)
        assertEquals("이순신", updated.name)
        assertEquals("새닉네임", updated.nickname)
        assertEquals("010-9999-8888", updated.phoneNumber)
        assertEquals("2023130001", updated.studentNumber)
        assertEquals("기계공학부", updated.major)
    }

    @Test
    fun `이메일이 빈 문자열이면 null로 저장된다`() = runTest {
        UpdateStudentUserInfoUseCase(userRepository)(
            beforeUser = baseStudent,
            email = "",
            name = "홍길동",
            nickname = "코인유저",
            gender = Gender.Man,
            phoneNumber = "010-1234-5678",
            studentNumber = "2023136001",
            major = "컴퓨터공학부"
        )

        val updated = userRepository.getUpdatedUser() as User.Student
        assertNull(updated.email)
    }

    @Test
    fun `닉네임이 빈 문자열이면 null로 저장된다`() = runTest {
        UpdateStudentUserInfoUseCase(userRepository)(
            beforeUser = baseStudent,
            email = "student@koreatech.ac.kr",
            name = "홍길동",
            nickname = "",
            gender = Gender.Man,
            phoneNumber = "010-1234-5678",
            studentNumber = "2023136001",
            major = "컴퓨터공학부"
        )

        val updated = userRepository.getUpdatedUser() as User.Student
        assertNull(updated.nickname)
    }

    @Test
    fun `이메일과 닉네임 모두 빈 문자열이면 둘 다 null로 저장된다`() = runTest {
        UpdateStudentUserInfoUseCase(userRepository)(
            beforeUser = baseStudent,
            email = "",
            name = "홍길동",
            nickname = "",
            gender = Gender.Man,
            phoneNumber = "010-1234-5678",
            studentNumber = "2023136001",
            major = "컴퓨터공학부"
        )

        val updated = userRepository.getUpdatedUser() as User.Student
        assertNull(updated.email)
        assertNull(updated.nickname)
    }

    @Test
    fun `성별을 여성으로 업데이트할 수 있다`() = runTest {
        UpdateStudentUserInfoUseCase(userRepository)(
            beforeUser = baseStudent,
            email = "student@koreatech.ac.kr",
            name = "홍길동",
            nickname = "코인유저",
            gender = Gender.Woman,
            phoneNumber = "010-1234-5678",
            studentNumber = "2023136001",
            major = "컴퓨터공학부"
        )

        val updated = userRepository.getUpdatedUser() as User.Student
        assertEquals(Gender.Woman, updated.gender)
    }

    @Test
    fun `변경하지 않은 필드는 기존 값을 유지한다`() = runTest {
        UpdateStudentUserInfoUseCase(userRepository)(
            beforeUser = baseStudent,
            email = baseStudent.email ?: "",
            name = baseStudent.name ?: "",
            nickname = baseStudent.nickname ?: "",
            gender = baseStudent.gender,
            phoneNumber = baseStudent.phoneNumber ?: "",
            studentNumber = baseStudent.studentNumber ?: "",
            major = baseStudent.major ?: ""
        )

        val updated = userRepository.getUpdatedUser() as User.Student
        assertEquals(baseStudent.loginId, updated.loginId)
        assertEquals(baseStudent.anonymousNickname, updated.anonymousNickname)
        assertEquals(baseStudent.userType, updated.userType)
        assertEquals(baseStudent.id, updated.id)
    }
}
