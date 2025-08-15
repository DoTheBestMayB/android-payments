package nextstep.payments.ui.new_card

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class NewCardViewModel : ViewModel() {

    private val _cardState = MutableStateFlow(NewCardState())
    val cardState = _cardState.asStateFlow()

    private val eventChannel = Channel<NewCardEvent> { }
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: NewCardAction) {
        when (action) {
            is NewCardAction.OnCartNumberChange -> setCardNumber(action.cardNumber)
            is NewCardAction.OnExpiredDateChange -> setExpiredDate(action.expiredDate)
            is NewCardAction.OnOwnerNameChange -> setOwnerName(action.ownerName)
            is NewCardAction.OnPasswordChange -> setPassword(action.password)
        }
    }

    private fun setCardNumber(cardNumber: TextFieldValue) {
        if (cardNumber.text.length > CARD_NUMBER_MAX_LENGTH || cardNumber.text.lastOrNull()
                ?.isDigit() == false
        ) {
            return
        }
        _cardState.update {
            if (shouldAddSignAtCardNumber(
                    it.cardNumber.text,
                    cardNumber.text
                ) && cardNumber.text.length != CARD_NUMBER_MAX_LENGTH
            ) {
                val newText = "${cardNumber.text}-"
                it.copy(
                    cardNumber = TextFieldValue(
                        text = newText,
                        selection = TextRange(newText.length)
                    )
                )
            } else {
                it.copy(cardNumber = cardNumber)
            }
        }
    }

    private fun shouldAddSignAtCardNumber(beforeText: String, afterText: String): Boolean {
        return beforeText.length % 5 == 3 && afterText.length % 5 == 4
    }


    private fun setExpiredDate(expiredDate: TextFieldValue) {
        if (expiredDate.text.length > CARD_EXPIRED_DATE_MAX_LENGTH || expiredDate.text.lastOrNull()
                ?.isDigit() == false
        ) {
            return
        }
        _cardState.update {
            if (shouldAddSignAtExpiredDate(it.expiredDate.text, expiredDate.text)) {
                val newText = "${expiredDate.text.substring(0, 2)}/${expiredDate.text[2]}"
                it.copy(
                    expiredDate = TextFieldValue(
                        text = newText,
                        selection = TextRange(newText.length)
                    )
                )
            } else {
                it.copy(expiredDate = expiredDate)
            }
        }
    }

    private fun shouldAddSignAtExpiredDate(beforeText: String, afterText: String): Boolean {
        return beforeText.length == 2 && afterText.length == 3
    }

    private fun setOwnerName(ownerName: String) {
        if (ownerName.length > CARD_OWNER_NAME_MAX_LENGTH) {
            return
        }
        _cardState.update {
            it.copy(ownerName = ownerName)
        }
    }

    private fun setPassword(password: String) {
        if (password.length > CARD_PASSWORD_MAX_LENGTH || password.lastOrNull()
                ?.isDigit() == false
        ) {
            return
        }
        _cardState.update {
            it.copy(password = password)
        }
    }

    companion object {
        private const val CARD_NUMBER_MAX_LENGTH = 19
        private const val CARD_EXPIRED_DATE_MAX_LENGTH = 5
        private const val CARD_OWNER_NAME_MAX_LENGTH = 30
        private const val CARD_PASSWORD_MAX_LENGTH = 4
    }
}
