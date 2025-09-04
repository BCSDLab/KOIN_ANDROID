package `in`.koreatech.koin.domain.error

sealed class KoinApiResponseCode(
    val codes: String
) : KoinErrorException() {
    /**
     * 2xx Success(성공)
     */
    data object Ok : KoinApiResponseCode("OK")
    data object Created : KoinApiResponseCode("CREATED")
    data object NoContent : KoinApiResponseCode("NO_CONTENT")

    /**
     * 400 Bad Request (잘못된 요청)
     */
    data object IllegalArgument : KoinApiResponseCode("ILLEGAL_ARGUMENT")
    data object IllegalState : KoinApiResponseCode("ILLEGAL_STATE")
    data object InvalidRequestBody : KoinApiResponseCode("INVALID_REQUEST_BODY")
    data object InvalidDateTime : KoinApiResponseCode("INVALID_DATE_TIME")
    data object InvalidGenderIndex : KoinApiResponseCode("INVALID_GENDER_INDEX")
    data object InvalidRefreshToken : KoinApiResponseCode("INVALID_REFRESH_TOKEN")
    data object InvalidDeliveryArea : KoinApiResponseCode("INVALID_DELIVERY_AREA")
    data object NotMatchedEmail : KoinApiResponseCode("NOT_MATCHED_EMAIL")
    data object NotMatchedPhoneNumber : KoinApiResponseCode("NOT_MATCHED_PHONE_NUMBER")
    data object NotMatchedPassword : KoinApiResponseCode("NOT_MATCHED_PASSWORD")
    data object NotMatchedVerificationCode : KoinApiResponseCode("NOT_MATCHED_VERIFICATION_CODE")
    data object NotMatchedRefreshToken : KoinApiResponseCode("NOT_MATCHED_REFRESH_TOKEN")
    data object NotReadableHttpMessage : KoinApiResponseCode("NOT_READABLE_HTTP_MESSAGE")
    data object UnsupportedOperation : KoinApiResponseCode("UNSUPPORTED_OPERATION")
    data object InvalidRecruitmentPeriod : KoinApiResponseCode("INVALID_RECRUITMENT_PERIOD")
    data object MustBeNullRecruitmentPeriod : KoinApiResponseCode("MUST_BE_NULL_RECRUITMENT_PERIOD")
    data object RequiredRecruitmentPeriod : KoinApiResponseCode("REQUIRED_RECRUITMENT_PERIOD")
    data object NotMatchedClubAndEvent : KoinApiResponseCode("NOT_MATCHED_CLUB_AND_EVENT")
    data object NotAllowedRecruitingSortType : KoinApiResponseCode("NOT_ALLOWED_RECRUITING_SORT_TYPE")
    data object InvalidClubEventPeriod : KoinApiResponseCode("INVALID_CLUB_EVENT_PERIOD")
    data object InvalidClubEventType : KoinApiResponseCode("INVALID_CLUB_EVENT_TYPE")
    data object ShopNotDeliverable : KoinApiResponseCode("SHOP_NOT_DELIVERABLE")
    data object ShopNotTakeoutAvailable : KoinApiResponseCode("SHOP_NOT_TAKEOUT_AVAILABLE")
    data object AddressKeywordNotProvided : KoinApiResponseCode("ADDRESS_KEYWORD_NOT_PROVIDED")
    data object AddressKeywordTooExtensive : KoinApiResponseCode("ADDRESS_KEYWORD_TOO_EXTENSIVE")
    data object AddressKeywordTooShort : KoinApiResponseCode("ADDRESS_KEYWORD_TOO_SHORT")
    data object AddressKeywordOnlyNumber : KoinApiResponseCode("ADDRESS_KEYWORD_ONLY_NUMBER")
    data object AddressKeywordTooLong : KoinApiResponseCode("ADDRESS_KEYWORD_TOO_LONG")
    data object AddressKeywordInvalidSymbols : KoinApiResponseCode("ADDRESS_KEYWORD_INVALID_SYMBOLS")
    data object AddressSearchLimitExceeded : KoinApiResponseCode("ADDRESS_SEARCH_LIMIT_EXCEEDED")
    data object DifferentShopItemInCart : KoinApiResponseCode("DIFFERENT_SHOP_ITEM_IN_CART")
    data object MenuSoldOut : KoinApiResponseCode("MENU_SOLD_OUT")
    data object ShopClosed : KoinApiResponseCode("SHOP_CLOSED")
    data object InvalidMenuInShop : KoinApiResponseCode("INVALID_MENU_IN_SHOP")
    data object InvalidOptionInGroup : KoinApiResponseCode("INVALID_OPTION_IN_GROUP")
    data object InvalidCartItemQuantity : KoinApiResponseCode("INVALID_CART_ITEM_QUANTITY")
    data object RequiredOptionGroupMissing : KoinApiResponseCode("REQUIRED_OPTION_GROUP_MISSING")
    data object MinSelectionNotMet : KoinApiResponseCode("MIN_SELECTION_NOT_MET")
    data object MaxSelectionExceeded : KoinApiResponseCode("MAX_SELECTION_EXCEEDED")
    data object OrderAmountBelowMinimum : KoinApiResponseCode("ORDER_AMOUNT_BELOW_MINIMUM")
    data object InvalidSelfChat : KoinApiResponseCode("INVALID_SELF_CHAT")
    data object InvalidWebsocketUserSession : KoinApiResponseCode("INVALID_WEBSOCKET_USER_SESSION")

    /**
     * 401 Unauthorized (인증 필요)
     */
    data object WithdrawnUser : KoinApiResponseCode("WITHDRAWN_USER")

    /**
     * 403 Forbidden (인가 필요)
     */
    data object ForbiddenUserType : KoinApiResponseCode("FORBIDDEN_USER_TYPE")
    data object ForbiddenOwner : KoinApiResponseCode("FORBIDDEN_OWNER")
    data object ForbiddenStudent : KoinApiResponseCode("FORBIDDEN_STUDENT")
    data object ForbiddenAdmin : KoinApiResponseCode("FORBIDDEN_ADMIN")
    data object ForbiddenAccount : KoinApiResponseCode("FORBIDDEN_ACCOUNT")
    data object ForbiddenVerification : KoinApiResponseCode("FORBIDDEN_VERIFICATION")
    data object ForbiddenBlockedUser : KoinApiResponseCode("FORBIDDEN_BLOCKED_USER")

    /**
     * 404 Not Found (리소스를 찾을 수 없음)
     */
    data object NotFoundUser : KoinApiResponseCode("NOT_FOUND_USER")
    data object NotFoundRefreshToken : KoinApiResponseCode("NOT_FOUND_REFRESH_TOKEN")
    data object NotFoundResetToken : KoinApiResponseCode("NOT_FOUND_RESET_TOKEN")
    data object NoHandlerFound : KoinApiResponseCode("NO_HANDLER_FOUND")
    data object NotFoundClub : KoinApiResponseCode("NOT_FOUND_CLUB")
    data object NotFoundClubRecruitment : KoinApiResponseCode("NOT_FOUND_CLUB_RECRUITMENT")
    data object NotFoundClubEvent : KoinApiResponseCode("NOT_FOUND_CLUB_EVENT")
    data object NotFoundDeliveryAddress : KoinApiResponseCode("NOT_FOUND_DELIVERY_ADDRESS")
    data object NotFoundOrderableShop : KoinApiResponseCode("NOT_FOUND_ORDERABLE_SHOP")
    data object NotFoundOrderableShopMenu : KoinApiResponseCode("NOT_FOUND_ORDERABLE_SHOP_MENU")
    data object NotFoundOrderableShopMenuPrice : KoinApiResponseCode("NOT_FOUND_ORDERABLE_SHOP_MENU_PRICE")
    data object NotFoundOrderableShopMenuOption : KoinApiResponseCode("NOT_FOUND_ORDERABLE_SHOP_MENU_OPTION")
    data object NotFoundCart : KoinApiResponseCode("NOT_FOUND_CART")
    data object NotFoundCartItem : KoinApiResponseCode("NOT_FOUND_CART_ITEM")
    data object NotFoundArticle : KoinApiResponseCode("NOT_FOUND_ARTICLE")
    data object NotFoundLostItemChatroom : KoinApiResponseCode("NOT_FOUND_LOST_ITEM_CHATROOM")

    /**
     * 409 Conflict (중복 혹은 충돌)
     */
    data object DuplicateLoginId : KoinApiResponseCode("DUPLICATE_LOGIN_ID")
    data object DuplicateNickname : KoinApiResponseCode("DUPLICATE_NICKNAME")
    data object DuplicateEmail : KoinApiResponseCode("DUPLICATE_EMAIL")
    data object DuplicatePhoneNumber : KoinApiResponseCode("DUPLICATE_PHONE_NUMBER")
    data object RequestTooFast : KoinApiResponseCode("REQUEST_TOO_FAST")
    data object OptimisticLockingFailure : KoinApiResponseCode("OPTIMISTIC_LOCKING_FAILURE")
    data object DuplicateClubRecruitment : KoinApiResponseCode("DUPLICATE_CLUB_RECRUITMENT")

    /**
     * 429 Too many Requests (요청량 초과)
     */
    data object TooManyRequestsVerification : KoinApiResponseCode("TOO_MANY_REQUESTS_VERIFICATION")

    /**
     * 500 Internal Service Error (서버 오류)
     */
    data object InternalServerError : KoinApiResponseCode("INTERNAL_SERVER_ERROR")
    data object ClientAborted : KoinApiResponseCode("CLIENT_ABORTED")
    data object ExternalApiError : KoinApiResponseCode("EXTERNAL_API_ERROR")
}
