package nextstep.payments.ui.new_card

sealed interface NewCardEvent {

    data object CardAddSuccess : NewCardEvent
    data object CardAddFail : NewCardEvent
    data object CardEditSuccess : NewCardEvent
    data object CardEditFail : NewCardEvent
    data object NavigateBack : NewCardEvent
}
