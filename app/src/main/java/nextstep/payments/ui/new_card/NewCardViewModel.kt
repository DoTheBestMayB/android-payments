package nextstep.payments.ui.new_card

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nextstep.payments.data.card.CardEntity
import nextstep.payments.data.card.PaymentCardRepository
import nextstep.payments.ui.common.model.CreditCard
import nextstep.payments.ui.mapper.toData

// Hilt가 아닌 기본 viewModel 함수를 이용해 ViewModel을 생성할 때, savedStateHandle와 디폴트 인자를 함께 사용하면
// ViewModel factory가 SavedStateHandle만 받는 생성자를 찾지 못해 에러가 발생한다고 합니다.
// 그래서 savedStateHandle만 있는 생성자를 만들기 위해 JvmOverloads를 이용했습니다.
class NewCardViewModel @JvmOverloads constructor(
    savedStateHandle: SavedStateHandle,
    private val cardRepository: PaymentCardRepository = PaymentCardRepository,
) : ViewModel() {

    private val eventChannel = Channel<NewCardEvent> { }
    val events = eventChannel.receiveAsFlow()

    private val _cardState: MutableStateFlow<NewCardState>

    val originalState: NewCardState

    init {
        // 전달된 카드가 있다면 꺼내서 state에 저장하기
        val cardInfo = savedStateHandle.remove<CreditCard>(CARD_INFO_KEY)

        originalState = if (cardInfo != null) {
            // repository에서 비밀번호 알아내기
            val pw = cardRepository.getPassword(cardInfo.id)

            NewCardState(
                cardNumber = cardInfo.cardNumber,
                expiredDate = cardInfo.expiredDate,
                ownerName = cardInfo.ownerName,
                password = pw,
                showBottomSheet = false,
                cardType = cardInfo.company,
                id = cardInfo.id,
            )
        } else {
            NewCardState.EMPTY
        }
        _cardState = MutableStateFlow(originalState)
    }

    val cardState = _cardState.asStateFlow()

    fun onAction(action: NewCardAction) {
        when (action) {
            is NewCardAction.OnCartNumberChange -> {
                setCardNumber(action.cardNumber)
            }

            is NewCardAction.OnExpiredDateChange -> {
                setExpiredDate(action.expiredDate)
            }

            is NewCardAction.OnOwnerNameChange -> {
                setOwnerName(action.ownerName)
            }

            is NewCardAction.OnPasswordChange -> {
                setPassword(action.password)
            }

            NewCardAction.OnAddCardClick -> {
                addCard()
            }

            NewCardAction.OnBackClick -> {
                // 현재 상황에서는 이러한 로직이 불필요해 보이지만, eventChannel이 꽉차서 send에 실패할 경우 재시도할 수 있도록 trySend 대신 send를 이용했습니다.
                // 하지만 trySend 대신 send를 이용할 경우 coroutineScope이 필요합니다.
                // 현재 필요하지 않은 상황을 가정하며 이렇게 코드를 작성하는 것은 오버 엔지니어링인지, 방어적인 코드 작성인지 궁금합니다.
                viewModelScope.launch {
                    eventChannel.send(NewCardEvent.NavigateBack)
                }
            }

            is NewCardAction.OnBottomSheetCardSelect -> {
                setCardCompany(action.cardType)
            }

            NewCardAction.OnPreviewCardSelect -> {
                _cardState.update {
                    it.copy(
                        showBottomSheet = true,
                    )
                }
            }
        }
    }

    private fun setCardNumber(cardNumber: String) {
        val digits = cardNumber.filter { it.isDigit() }
        if (CardInputValidator.isCardNumberInputValid(digits)) {
            _cardState.update {
                it.copy(
                    cardNumber = digits
                )
            }
        }
    }

    private fun setExpiredDate(expiredDate: String) {
        val digitOnly = expiredDate.filter { it.isDigit() }
        if (CardInputValidator.isExpiredDateInputValid(digitOnly)) {
            _cardState.update {
                it.copy(
                    expiredDate = digitOnly,
                )
            }
        }
    }

    private fun setOwnerName(ownerName: String) {
        if (CardInputValidator.isCardOwnerNameValid(ownerName)) {
            _cardState.update {
                it.copy(ownerName = ownerName)
            }
        }
    }

    private fun setPassword(password: String) {
        if (CardInputValidator.isPasswordInputValid(password)) {
            _cardState.update {
                it.copy(password = password)
            }
        }

    }

    // isAddEnabled 조건을 통해 입력 값이 올바른지 검증하기는 했으나, 여기서 한 번 더 입력 조건이 올바른지 학인하도록 했습니다.
    private fun addCard() {
        if (CardInputValidator.isCardNumberValid(_cardState.value.cardNumber) &&
            CardInputValidator.isExpiredDateValid(_cardState.value.expiredDate) &&
            CardInputValidator.isCardOwnerNameValid(_cardState.value.ownerName) &&
            CardInputValidator.isPasswordValid(_cardState.value.password)
        ) {
            val cardEntity = CardEntity(
                id = originalState.id,
                cardNumber = _cardState.value.cardNumber,
                expiredDate = _cardState.value.expiredDate,
                ownerName = _cardState.value.ownerName,
                password = _cardState.value.password,
                company = _cardState.value.cardType.toData(),
            )
            val event = if (originalState != NewCardState.EMPTY) {
                if (cardRepository.editCard(originalState.id, cardEntity)) {
                    NewCardEvent.CardEditSuccess
                } else {
                    NewCardEvent.CardEditFail
                }
            } else {
                if (cardRepository.addCard(cardEntity)) {
                    NewCardEvent.CardAddSuccess
                } else {
                    NewCardEvent.CardAddFail
                }
            }

            viewModelScope.launch {
                eventChannel.send(event)
            }
        }
    }

    private fun setCardCompany(cardType: CardType) {
        _cardState.update {
            it.copy(
                cardType = cardType,
                showBottomSheet = false,
            )
        }
    }

    companion object {
        const val CARD_INFO_KEY = "exist_card_info"
    }
}
