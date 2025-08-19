package nextstep.payments.ui.new_card

sealed interface NewCardEvent {

    data object CardAddSuccess: NewCardEvent
    data object CardAddFail: NewCardEvent
    data object NavigateBack: NewCardEvent
}
