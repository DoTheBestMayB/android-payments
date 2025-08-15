package nextstep.payments.ui.mapper

import nextstep.payments.data.card.CardEntity
import nextstep.payments.ui.common.model.Card

fun CardEntity.toUi(): Card =
    Card(
        cardNumber = cardNumber,
        expiredDate = expiredDate,
        ownerName = ownerName,
        password = password
    )