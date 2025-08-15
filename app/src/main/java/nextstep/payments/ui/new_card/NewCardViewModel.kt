package nextstep.payments.ui.new_card

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
            NewCardAction.OnAddCardClick -> addCard()
            NewCardAction.OnBackClick -> {
                // 현재 상황에서는 이러한 로직이 불필요해 보이지만, eventChannel이 꽉차서 send에 실패할 경우 재시도할 수 있도록 trySend 대신 send를 이용했습니다.
                // 하지만 trySend 대신 send를 이용할 경우 coroutineScope이 필요합니다.
                // 현재 필요하지 않은 상황을 가정하며 이렇게 코드를 작성하는 것은 오버 엔지니어링인지, 방어적인 코드 작성인지 궁금합니다.
                viewModelScope.launch {
                    eventChannel.send(NewCardEvent.NavigateBack)
                }
            }
        }
    }

    private fun setCardNumber(cardNumber: TextFieldValue) {
        if (cardNumber.text.length > CardInputValidator.CARD_NUMBER_MAX_LENGTH || cardNumber.text.lastOrNull()
                ?.isDigit() == false
        ) {
            return
        }
        _cardState.update {
            if (shouldAddSignAtCardNumber(
                    it.cardNumber.text,
                    cardNumber.text
                ) && cardNumber.text.length != CardInputValidator.CARD_NUMBER_MAX_LENGTH
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
        val digits = expiredDate.text.filter { it.isDigit() }.take(4)

        if (digits.length >= 2) {
            val mm = digits.substring(0, 2).toInt()
            if (mm !in MONTH_RANGE) {
                return
            }
        } else if (digits.length == 1) {
            val first = digits[0]
            if (first !in FIRST_MONTH_DIGIT_RANGE) {
                return
            }
        }

        val formatted = when {
            digits.length <= 2 -> digits
            else -> digits.substring(0, 2) + "/" + digits.substring(2)
        }

        _cardState.update {
            it.copy(
                expiredDate = TextFieldValue(
                    text = formatted,
                    selection = TextRange(formatted.length)
                )
            )
        }
    }

    private fun shouldAddSignAtExpiredDate(beforeText: String, afterText: String): Boolean {
        return beforeText.length == 2 && afterText.length == 3
    }

    private fun setOwnerName(ownerName: String) {
        if (CardInputValidator.isCardOwnerNameValid(ownerName)) {
            _cardState.update {
                it.copy(ownerName = ownerName)
            }
        }
    }

    private fun setPassword(password: String) {
        if (password.length > CardInputValidator.CARD_PASSWORD_MAX_LENGTH
            || password.lastOrNull()?.isDigit() == false
        ) {
            return
        }
        _cardState.update {
            it.copy(password = password)
        }
    }

    // isAddEnabled 조건을 통해 입력 값이 올바른지 검증하기는 했으나, 여기서 한 번 더 입력 조건이 올바른지 학인하도록 했습니다.
    private fun addCard() {
        if (CardInputValidator.isCardNumberValid(_cardState.value.cardNumber.text) &&
            CardInputValidator.isExpiredDateValid(_cardState.value.expiredDate.text) &&
            CardInputValidator.isCardOwnerNameValid(_cardState.value.ownerName) &&
            CardInputValidator.isPasswordValid(_cardState.value.password)
        ) {
            // TODO : 카드 추가하기

            viewModelScope.launch {
                eventChannel.send(NewCardEvent.CardAdded)
            }
        }
    }

    companion object {
        private val FIRST_MONTH_DIGIT_RANGE = '0'..'1'
        private val MONTH_RANGE = 1..12
    }
}
