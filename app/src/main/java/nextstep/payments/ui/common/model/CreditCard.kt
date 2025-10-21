package nextstep.payments.ui.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import nextstep.payments.ui.new_card.CardType
import java.util.UUID
import kotlin.text.filter

@Parcelize
data class CreditCard(
    val id: UUID = UUID.randomUUID(),
    val cardNumber: String = "",
    val expiredDate: String = "",
    val ownerName: String = "",
    val company: CardType = CardType.NOT_SELECTED,
): Parcelable {
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
