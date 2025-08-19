package nextstep.payments.ui.mapper

import nextstep.payments.data.card.CardEntity
import nextstep.payments.ui.common.model.CreditCard

fun CardEntity.toUi(): CreditCard =
    CreditCard(
        cardNumber = cardNumber,
        expiredDate = expiredDate,
        ownerName = ownerName,
    )
