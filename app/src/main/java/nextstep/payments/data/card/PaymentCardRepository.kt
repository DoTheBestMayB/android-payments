package nextstep.payments.data.card

object PaymentCardRepository {

    // key : CardNumber
    private val _cards = linkedMapOf<String, CardEntity>()
    val cards: List<CardEntity>
        get() = _cards.toList().map { it.second }

    fun addCard(cardEntity: CardEntity): Boolean {
        // 카드를 등록하기 전에 이미 등록된 카드 번호인지 확인합니다.
        // LazyList에서 key를 카드번호로 설정해뒀는데, 중복된 key를 가진 데이터가 있으면 에러가 발생해서 추가했습니다.
        if (cardEntity.cardNumber in _cards) {
            return false
        }
        _cards[cardEntity.cardNumber] = cardEntity
        return true
    }

    fun editCard(originalCard: CardEntity, newCard: CardEntity): Boolean {
        if (newCard.cardNumber in _cards) {
            return false
        }
        _cards.remove(originalCard.cardNumber)
        _cards[newCard.cardNumber] = newCard
        return true
    }

    fun getPassword(target: CardEntity): String {
        // 카드 번호는 고유한 값이므로 카드 번호만 비교하도록 구현했습니다.
        return _cards[target.cardNumber]?.password ?: ""
    }
}
