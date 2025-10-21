package nextstep.payments.ui.new_card

import java.util.UUID

data class NewCardState(
    val cardNumber: String = "",
    val expiredDate: String = "",
    val ownerName: String = "",
    val password: String = "",
    val showBottomSheet: Boolean = true,
    val cardType: CardType = CardType.NOT_SELECTED,
    val id: UUID = UUID.randomUUID(),
) {
    fun isValid(cardInputValidator: CardInputValidator): Boolean {
        return cardInputValidator.isCardNumberValid(cardNumber) &&
                cardInputValidator.isExpiredDateValid(expiredDate) &&
                cardInputValidator.isCardOwnerNameValid(ownerName) &&
                cardInputValidator.isPasswordValid(password)
    }

    fun isCardValueSame(other: NewCardState): Boolean {
        return cardNumber == other.cardNumber && expiredDate == other.expiredDate
                && ownerName == other.ownerName && password == other.password
                && cardType == other.cardType
    }

    companion object {
        val EMPTY = NewCardState()
    }
}
