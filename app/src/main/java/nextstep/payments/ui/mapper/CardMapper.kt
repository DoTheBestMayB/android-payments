package nextstep.payments.ui.mapper

import nextstep.payments.data.card.CardEntity
import nextstep.payments.data.card.CardTypeEntity
import nextstep.payments.ui.common.model.CreditCard
import nextstep.payments.ui.new_card.CardType

fun CardEntity.toUi(): CreditCard =
    CreditCard(
        cardNumber = cardNumber,
        expiredDate = expiredDate,
        ownerName = ownerName,
        company = company.toUi(),
    )

fun CardType.toData(): CardTypeEntity {
    return when (this) {
        CardType.NOT_SELECTED -> CardTypeEntity.NOT_SELECTED
        CardType.BC -> CardTypeEntity.BC
        CardType.SHINHAN -> CardTypeEntity.SHINHAN
        CardType.KAKAO -> CardTypeEntity.KAKAO
        CardType.HYUNDAE -> CardTypeEntity.HYUNDAE
        CardType.WOORI -> CardTypeEntity.WOORI
        CardType.LOTTE -> CardTypeEntity.LOTTE
        CardType.HANA -> CardTypeEntity.HANA
        CardType.KB -> CardTypeEntity.KB
    }
}

fun CardTypeEntity.toUi(): CardType {
    return when(this) {
        CardTypeEntity.NOT_SELECTED -> CardType.NOT_SELECTED
        CardTypeEntity.BC -> CardType.BC
        CardTypeEntity.SHINHAN -> CardType.SHINHAN
        CardTypeEntity.KAKAO -> CardType.KAKAO
        CardTypeEntity.HYUNDAE -> CardType.HYUNDAE
        CardTypeEntity.WOORI -> CardType.WOORI
        CardTypeEntity.LOTTE -> CardType.LOTTE
        CardTypeEntity.HANA -> CardType.HANA
        CardTypeEntity.KB -> CardType.KB
    }
}

fun CreditCard.toEntity(): CardEntity {
    return CardEntity(
        cardNumber = cardNumber,
        expiredDate = expiredDate,
        ownerName = ownerName,
        password = "",
        company = company.toData(),
    )
}
