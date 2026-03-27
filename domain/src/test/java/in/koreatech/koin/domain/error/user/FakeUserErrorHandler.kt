package `in`.koreatech.koin.domain.error.user

import `in`.koreatech.koin.domain.model.error.ErrorHandler

class FakeUserErrorHandler : UserErrorHandler {
    override fun handleUserError(throwable: Throwable): ErrorHandler {
        return ErrorHandler(throwable.message ?: "Unknown error")
    }

    override fun handleGetTokenError(throwable: Throwable): ErrorHandler {
        return ErrorHandler("Failed to get token: ${throwable.message}")
    }

    override fun handleRequestPasswordResetEmailError(throwable: Throwable): ErrorHandler {
        return ErrorHandler(throwable.message ?: "Unknown error")
    }

    override fun handleGetUserInfoError(throwable: Throwable): ErrorHandler {
        return ErrorHandler(throwable.message ?: "Unknown error")
    }

    override fun handleDeleteUserError(throwable: Throwable): ErrorHandler {
        return ErrorHandler(throwable.message ?: "Unknown error")
    }

    override fun handleUsernameDuplicatedError(throwable: Throwable): ErrorHandler {
        return ErrorHandler(throwable.message ?: "Unknown error")
    }

    override fun handleUpdateUserError(throwable: Throwable): ErrorHandler {
        return ErrorHandler(throwable.message ?: "Unknown error")
    }

    override fun handleVerifyUserPasswordError(throwable: Throwable): ErrorHandler {
        return ErrorHandler(throwable.message ?: "Unknown error")
    }
}
