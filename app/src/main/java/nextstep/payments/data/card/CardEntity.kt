package nextstep.payments.data.card

data class CardEntity(
    val cardNumber: String = "",
    val expiredDate: String = "",
    val ownerName: String = "",
    val password: String = "",
    val company: CardTypeEntity = CardTypeEntity.NOT_SELECTED,
)
