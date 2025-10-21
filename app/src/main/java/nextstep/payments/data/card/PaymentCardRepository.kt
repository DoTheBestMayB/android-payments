package nextstep.payments.data.card

import java.util.UUID

object PaymentCardRepository {

    private val cardById = linkedMapOf<UUID, CardEntity>()
    val cards: List<CardEntity>
        get() = cardById.values.toList()

    private val idByCardNumber = hashMapOf<String, UUID>()

    /**
     * 전달된 uuid가 이미 사용 중이라면 사용 중이지 않은 uuid로 변경해서 등록합니다.
     */
    fun addCard(card: CardEntity): Boolean {
        // 카드를 등록하기 전에 이미 등록된 카드 번호인지 확인합니다.
        if (card.cardNumber in idByCardNumber) {
            return false
        }
        var id = card.id
        while (id in cardById) {
            id = UUID.randomUUID()
        }

        cardById[id] = card.copy(id = id)
        idByCardNumber[card.cardNumber] = id
        return true
    }

    fun editCard(id: UUID, newCard: CardEntity): Boolean {
        val oldCard = cardById[id] ?: return false

        if (newCard.cardNumber != oldCard.cardNumber) {
            if (newCard.cardNumber in idByCardNumber) {
                return false
            }
            idByCardNumber.remove(oldCard.cardNumber)
            idByCardNumber[newCard.cardNumber] = id
        }
        cardById[id] = newCard.copy(id = id)
        return true
    }

    fun getPassword(id: UUID): String {
        return cardById[id]?.password ?: ""
    }

}
