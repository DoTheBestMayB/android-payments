package nextstep.payments.ui.new_card

sealed interface NewCardEvent {

    data object CardAdded: NewCardEvent
}