package nextstep.payments.ui.common.model

/**
 * 이 클래스가 common/model 하위에 있는데, 더 적절한 위치가 어디일지 궁금합니다.
 */
data class CreditCard(
    val cardNumber: String = "",
    val expiredDate: String = "",
    val ownerName: String = "",
    val password: String = "",
)
