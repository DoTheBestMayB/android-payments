package nextstep.payments.ui.card_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import nextstep.payments.data.card.PaymentCardRepository
import nextstep.payments.ui.mapper.toUi

class CardListViewModel(
    private val cardRepository: PaymentCardRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CardListState())
    val state = _state.asStateFlow()

    fun fetchCards() {
        _state.update {
            val cards = cardRepository.cards.map { card ->
                card.toUi()
            }
            it.copy(
                cards = if (cards.isEmpty()) {
                    CreditCardUiState.Empty
                } else if (cards.size == 1) {
                    CreditCardUiState.One(cards.first())
                } else {
                    CreditCardUiState.Many(cards)
                }
            )
        }
    }

    companion object {
        /**
         * https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories
         */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = PaymentCardRepository

                CardListViewModel(repository)
            }
        }
    }
}
