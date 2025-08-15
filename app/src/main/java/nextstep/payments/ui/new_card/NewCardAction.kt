package nextstep.payments.ui.new_card

import androidx.compose.ui.text.input.TextFieldValue

sealed interface NewCardAction {
    data class OnCartNumberChange(val cardNumber: TextFieldValue) : NewCardAction
    data class OnExpiredDateChange(val expiredDate: TextFieldValue) : NewCardAction
    data class OnOwnerNameChange(val ownerName: String) : NewCardAction
    data class OnPasswordChange(val password: String) : NewCardAction
    data object OnAddCardClick : NewCardAction
    data object OnBackClick: NewCardAction
}
