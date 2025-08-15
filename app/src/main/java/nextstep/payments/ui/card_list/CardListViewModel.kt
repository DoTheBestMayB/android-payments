package nextstep.payments.ui.card_list

import androidx.lifecycle.ViewModel
import nextstep.payments.data.card.PaymentCardRepository

class CardListViewModel(
    private val cardRepository: PaymentCardRepository,
): ViewModel() {

    fun fetchCards() {

    }
}