package nextstep.payments.data.card

object PaymentCardRepository {

    private val _cards = mutableListOf<CardEntity>()
    val cards: List<CardEntity>
        get() = _cards.toList()

    fun addCard(cardEntity: CardEntity): Boolean {
        // 카드를 등록하기 전에 이미 등록된 카드 번호인지 확인합니다.
        // LazyList에서 key를 카드번호로 설정해뒀는데, 중복된 key를 가진 데이터가 있으면 에러가 발생해서 추가했습니다.
        if (_cards.any { it.cardNumber == cardEntity.cardNumber }) {
            return false
        }
        _cards.add(cardEntity)
        return true
    }

    fun getPassword(target: CardEntity): String {
        for (card in cards) {
            // 카드 번호는 고유한 값이므로 카드 번호만 비교하도록 구현했습니다.
            if (card.cardNumber == target.cardNumber) {
                return card.password
            }
        }

        return ""
    }
}
