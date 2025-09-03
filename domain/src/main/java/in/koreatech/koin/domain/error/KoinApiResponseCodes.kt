package `in`.koreatech.koin.domain.error

sealed class KoinApiResponseCodes(
    codes : String
) : KoinErrorException() {
    /**
     * 2xx Success(성공)
     */
    object Ok : KoinApiResponseCodes("OK")
    object Created : KoinApiResponseCodes("CREATED")
    object NoContent : KoinApiResponseCodes("NO_CONTENT")

    /**
     * 400 Bad Request (잘못된 요청)
     */
    object IllegalArgument : KoinApiResponseCodes("ILLEGAL_ARGUMENT")
    object IllegalState : KoinApiResponseCodes("ILLEGAL_STATE")
    object InvalidRequestBody : KoinApiResponseCodes("INVALID_REQUEST_BODY")
    object InvalidDateTime : KoinApiResponseCodes("INVALID_DATE_TIME")
    object InvalidGenderIndex : KoinApiResponseCodes("INVALID_GENDER_INDEX")
    object InvalidRefreshToken : KoinApiResponseCodes("INVALID_REFRESH_TOKEN")
    object InvalidDeliveryArea : KoinApiResponseCodes("INVALID_DELIVERY_AREA")
    object NotMatchedEmail : KoinApiResponseCodes("NOT_MATCHED_EMAIL")
    object NotMatchedPhoneNumber : KoinApiResponseCodes("NOT_MATCHED_PHONE_NUMBER")
    object NotMatchedPassword : KoinApiResponseCodes("NOT_MATCHED_PASSWORD")
    object NotMatchedVerificationCode : KoinApiResponseCodes("NOT_MATCHED_VERIFICATION_CODE")
    object NotMatchedRefreshToken : KoinApiResponseCodes("NOT_MATCHED_REFRESH_TOKEN")
    object NotReadableHttpMessage : KoinApiResponseCodes("NOT_READABLE_HTTP_MESSAGE")
    object UnSupportedOperation : KoinApiResponseCodes("UNSUPPORTED_OPERATION")
    object InvalidRecruitmentPeriod : KoinApiResponseCodes("INVALID_RECRUITMENT_PERIOD")
    object MustBeNullRecruitmentPeriod : KoinApiResponseCodes("MUST_BE_NULL_RECRUITMENT_PERIOD")
    object RequiredRecruitmentPeriod : KoinApiResponseCodes("REQUIRED_RECRUITMENT_PERIOD")
    object NotMatchedClubAndEvent : KoinApiResponseCodes("NOT_MATCHED_CLUB_AND_EVENT")
    object NotAllowedRecruitingSortType : KoinApiResponseCodes("NOT_ALLOWED_RECRUITING_SORT_TYPE")
    object InvalidClubEventPeriod : KoinApiResponseCodes("INVALID_CLUB_EVENT_PERIOD")
    object InvalidClubEventType : KoinApiResponseCodes("INVALID_CLUB_EVENT_TYPE")
    object ShopNotDeliverable : KoinApiResponseCodes("SHOP_NOT_DELIVERABLE")
    object ShopNotTakeoutAvailable : KoinApiResponseCodes("SHOP_NOT_TAKEOUT_AVAILABLE")
    object AddressKeywordNotProvided : KoinApiResponseCodes("ADDRESS_KEYWORD_NOT_PROVIDED")
    object AddressKeywordTooExtensive : KoinApiResponseCodes("ADDRESS_KEYWORD_TOO_EXTENSIVE")
    object AddressKeywordTooShort : KoinApiResponseCodes("ADDRESS_KEYWORD_TOO_SHORT")
    object AddressKeywordOnlyNumber : KoinApiResponseCodes("ADDRESS_KEYWORD_ONLY_NUMBER")
    object AddressKeywordTooLong : KoinApiResponseCodes("ADDRESS_KEYWORD_TOO_LONG")
    object AddressKeywordInvalidSymbols : KoinApiResponseCodes("ADDRESS_KEYWORD_INVALID_SYMBOLS")
    object AddressSearchLimitExceeded : KoinApiResponseCodes("ADDRESS_SEARCH_LIMIT_EXCEEDED")
    object DifferentShopItemInCart : KoinApiResponseCodes("DIFFERENT_SHOP_ITEM_IN_CART")
    object MenuSoldout : KoinApiResponseCodes("MENU_SOLD_OUT")
    object ShopClosed : KoinApiResponseCodes("SHOP_CLOSED")
    object InvalidMenuInShop : KoinApiResponseCodes("INVALID_MENU_IN_SHOP")
    object InvalidOptionInGroup : KoinApiResponseCodes("INVALID_OPTION_IN_GROUP")
    object InvalidCartItemQuantity : KoinApiResponseCodes("INVALID_CART_ITEM_QUANTITY")
    object RequiredOptionGroupMissing : KoinApiResponseCodes("REQUIRED_OPTION_GROUP_MISSING")
    object MinSelectionNotMet : KoinApiResponseCodes("MIN_SELECTION_NOT_MET")
    object MaxSelectionExceeded : KoinApiResponseCodes("MAX_SELECTION_EXCEEDED")
    object OrderAmountBelowMinimun : KoinApiResponseCodes("ORDER_AMOUNT_BELOW_MINIMUM")
    object InvalidSelfChat : KoinApiResponseCodes("INVALID_SELF_CHAT")
    object InvalidWebsocketUserSession : KoinApiResponseCodes("INVALID_WEBSOCKET_USER_SESSION")

    /**
     * 401 Unauthorized (인증 필요)
     */
    object WithdrawnUser : KoinApiResponseCodes("WITHDRAWN_USER")

    /**
     * 403 Forbidden (인가 필요)
     */
    object ForbiddenUserType : KoinApiResponseCodes("FORBIDDEN_USER_TYPE")
    object ForbiddenOwner : KoinApiResponseCodes("FORBIDDEN_OWNER")
    object ForbiddenStudent : KoinApiResponseCodes("FORBIDDEN_STUDENT")
    object ForbiddenAdmin : KoinApiResponseCodes("FORBIDDEN_ADMIN")
    object ForbiddenAccount : KoinApiResponseCodes("FORBIDDEN_ACCOUNT")
    object ForbiddenVerification : KoinApiResponseCodes("FORBIDDEN_VERIFICATION")
    object ForbiddenBlockedUser : KoinApiResponseCodes("FORBIDDEN_BLOCKED_USER")

    /**
     * 404 Not Found (리소스를 찾을 수 없음)
     */
    object NotFoundUser : KoinApiResponseCodes("NOT_FOUND_USER")
    object NotFoundRefreshToken : KoinApiResponseCodes("NOT_FOUND_REFRESH_TOKEN")
    object NotFountResetToken : KoinApiResponseCodes("NOT_FOUND_RESET_TOKEN")
    object NoHandlerFound : KoinApiResponseCodes("NO_HANDLER_FOUND")
    object NotFoundClub : KoinApiResponseCodes("NOT_FOUND_CLUB")
    object NotFoundClubRecruitment : KoinApiResponseCodes("NOT_FOUND_CLUB_RECRUITMENT")
    object NotFoundClubEvent : KoinApiResponseCodes("NOT_FOUND_CLUB_EVENT")
    object NotFoundDeliveryAddress : KoinApiResponseCodes("NOT_FOUND_DELIVERY_ADDRESS")
    object NotFoundOrderableShop : KoinApiResponseCodes("NOT_FOUND_ORDERABLE_SHOP")
    object NotFoundOrderableShopMenu : KoinApiResponseCodes("NOT_FOUND_ORDERABLE_SHOP_MENU")
    object NotFoundOrderableShopMenuPrice : KoinApiResponseCodes("NOT_FOUND_ORDERABLE_SHOP_MENU_PRICE")
    object NotFoundOrderableShopMenuOption : KoinApiResponseCodes("NOT_FOUND_ORDERABLE_SHOP_MENU_OPTION")
    object NotFoundCart : KoinApiResponseCodes("NOT_FOUND_CART")
    object NotFoundCartItem : KoinApiResponseCodes("NOT_FOUND_CART_ITEM")
    object NotfoundArticle : KoinApiResponseCodes("NOT_FOUND_ARTICLE")
    object NotfoundLostItemChatroom : KoinApiResponseCodes("NOT_FOUND_LOST_ITEM_CHATROOM")

    /**
     * 409 Conflict (중복 혹은 충돌)
     */
    object DuplicateLoginId : KoinApiResponseCodes("DUPLICATE_LOGIN_ID")
    object DuplicateNickname : KoinApiResponseCodes("DUPLICATE_NICKNAME")
    object DuplicateEmail : KoinApiResponseCodes("DUPLICATE_EMAIL")
    object DuplicatePhoneNumber : KoinApiResponseCodes("DUPLICATE_PHONE_NUMBER")
    object RequestTooFast : KoinApiResponseCodes("REQUEST_TOO_FAST")
    object OptimisticLockingFailure : KoinApiResponseCodes("OPTIMISTIC_LOCKING_FAILURE")
    object DuplicateClubRecruitment : KoinApiResponseCodes("DUPLICATE_CLUB_RECRUITMENT")

    /**
     * 429 Too many Requests (요청량 초과)
     */
    object TooManyRequestsVerification : KoinApiResponseCodes("TOO_MANY_REQUESTS_VERIFICATION")

    /**
     * 500 Internal Service Error (서버 오류)
     */
    object InternalServerError : KoinApiResponseCodes("INTERNAL_SERVER_ERROR")
    object ClientAborted : KoinApiResponseCodes("CLIENT_ABORTED")
    object ExternalApiError : KoinApiResponseCodes("EXTERNAL_API_ERROR")

}
