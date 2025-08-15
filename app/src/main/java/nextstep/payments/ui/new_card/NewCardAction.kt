package nextstep.payments.ui.new_card

sealed interface NewCardAction {
    data class OnCartNumberChange(val cardNumber: String) : NewCardAction
    data class OnExpiredDateChange(val expiredDate: String) : NewCardAction
    data class OnOwnerNameChange(val ownerName: String) : NewCardAction
    data class OnPasswordChange(val password: String) : NewCardAction
    data object OnAddCardClick : NewCardAction
    data object OnBackClick: NewCardAction
}
