package nextstep.payments.ui.common.model

import nextstep.payments.ui.new_card.CardType
import kotlin.text.filter

/**
 * 이 클래스가 common/model 하위에 있는데, 더 적절한 위치가 어디일지 궁금합니다.
 */
data class CreditCard(
    val cardNumber: String = "",
    val expiredDate: String = "",
    val ownerName: String = "",
    val company: CardType = CardType.NOT_SELECTED,
) {
    fun formatExpiredDate(): String {
        val groups = expiredDate.filter { it.isDigit() }.chunked(2)
        return groups.joinToString(" / ")
    }

    fun maskedCardNumber(unmaskedGroups: Int): String {
        val groups = cardNumber.filter { it.isDigit() }.chunked(4)

        return groups.mapIndexed { index, group ->
            if (index < unmaskedGroups) {
                group
            } else {
                "*".repeat(group.length)
            }
        }.joinToString(" - ")
    }
}
