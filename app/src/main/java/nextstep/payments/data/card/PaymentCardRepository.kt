package nextstep.payments.data.card

object PaymentCardRepository {

    private val _cards = mutableListOf<CardEntity>()
    val cards: List<CardEntity>
        get() = _cards.toList()

    fun addCard(cardEntity: CardEntity) {
        _cards.add(cardEntity)
    }
}