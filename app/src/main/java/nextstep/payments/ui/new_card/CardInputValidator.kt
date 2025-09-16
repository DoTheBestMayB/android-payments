package nextstep.payments.ui.new_card

object CardInputValidator {
    private const val CARD_NUMBER_MAX_LENGTH = 16
    private const val CARD_EXPIRED_DATE_MAX_LENGTH = 4
    private const val CARD_OWNER_NAME_MAX_LENGTH = 30
    private const val CARD_PASSWORD_MAX_LENGTH = 4
    private val FIRST_MONTH_DIGIT_RANGE = '0'..'1'
    private val MONTH_RANGE = 1..12

    fun isCardNumberValid(cardNumber: String): Boolean {
        return cardNumber.length == CARD_NUMBER_MAX_LENGTH && cardNumber.all { it.isDigit() }
    }

    /**
     * @param cardNumber 숫자만 입력해주세요.
     */
    fun isCardNumberInputValid(cardNumber: String): Boolean {
        return cardNumber.length <= CARD_NUMBER_MAX_LENGTH
    }

    fun isExpiredDateValid(expiredDate: String): Boolean {
        return expiredDate.length == CARD_EXPIRED_DATE_MAX_LENGTH && expiredDate.all { it.isDigit() }
    }

    /**
     * @param expiredDate 숫자만 입력해주세요. ex) 0930
     */
    fun isExpiredDateInputValid(expiredDate: String): Boolean {
        // 최대 길이를 초과한 입력은 무시
        if (expiredDate.length > CARD_EXPIRED_DATE_MAX_LENGTH) {
            return false
        }

        if (expiredDate.length >= 2) {
            val mm = expiredDate.substring(0, 2).toInt()
            if (mm !in MONTH_RANGE) {
                return false
            }
        } else if (expiredDate.length == 1) {
            val first = expiredDate[0]
            if (first !in FIRST_MONTH_DIGIT_RANGE) {
                return false
            }
        }
        return true
    }

    fun isCardOwnerNameValid(cardOwnerName: String): Boolean {
        return cardOwnerName.length <= CARD_OWNER_NAME_MAX_LENGTH
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length == CARD_PASSWORD_MAX_LENGTH && password.all { it.isDigit() }
    }

    fun isPasswordInputValid(password: String): Boolean {
        return password.length <= CARD_PASSWORD_MAX_LENGTH && password.all { it.isDigit() }
    }
}
