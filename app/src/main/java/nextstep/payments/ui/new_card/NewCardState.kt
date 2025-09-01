package nextstep.payments.ui.new_card

data class NewCardState(
    val cardNumber: String = "",
    val expiredDate: String = "",
    val ownerName: String = "",
    val password: String = "",
) {
    fun isValid(cardInputValidator: CardInputValidator): Boolean {
        return cardInputValidator.isCardNumberValid(cardNumber) &&
                cardInputValidator.isExpiredDateValid(expiredDate) &&
                cardInputValidator.isCardOwnerNameValid(ownerName) &&
                cardInputValidator.isPasswordValid(password)
    }
}
