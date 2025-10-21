package nextstep.payments.data.card

import java.util.UUID

data class CardEntity(
    val id: UUID = UUID.randomUUID(),
    val cardNumber: String = "",
    val expiredDate: String = "",
    val ownerName: String = "",
    val password: String = "",
    val company: CardTypeEntity = CardTypeEntity.NOT_SELECTED,
)
