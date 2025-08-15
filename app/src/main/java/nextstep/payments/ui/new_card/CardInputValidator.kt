package nextstep.payments.ui.new_card


object CardInputValidator {
    const val CARD_TEXT_NUMBER_MAX_LENGTH = 19
    const val CARD_EXPIRED_DATE_TEXT_MAX_LENGTH = 5
    const val CARD_OWNER_NAME_MAX_LENGTH = 30
    const val CARD_PASSWORD_MAX_LENGTH = 4

    fun isCardNumberValid(cardNumber: String): Boolean {
        return cardNumber.length == CARD_TEXT_NUMBER_MAX_LENGTH && cardNumber.all { it.isDigit() || it == '-' }
    }

    fun isExpiredDateValid(expiredDate: String): Boolean {
        return expiredDate.length == CARD_EXPIRED_DATE_TEXT_MAX_LENGTH && expiredDate.all { it.isDigit() || it == '/' }
    }

    fun isCardOwnerNameValid(cardOwnerName: String): Boolean {
        return cardOwnerName.length <= CARD_OWNER_NAME_MAX_LENGTH
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length == CARD_PASSWORD_MAX_LENGTH && password.all { it.isDigit() }
    }
}