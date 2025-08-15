package nextstep.payments.ui.new_card

data class NewCardState(
    val cardNumber: String = "",
    val expiredDate: String = "",
    val ownerName: String = "",
    val password: String = "",
)
