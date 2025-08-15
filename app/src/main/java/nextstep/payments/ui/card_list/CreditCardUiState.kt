package nextstep.payments.ui.card_list

import nextstep.payments.ui.common.model.CreditCard

sealed interface CreditCardUiState {
    data object Empty : CreditCardUiState
    data class One(val creditCard: CreditCard) : CreditCardUiState
    data class Many(val creditCards: List<CreditCard>) : CreditCardUiState
}
