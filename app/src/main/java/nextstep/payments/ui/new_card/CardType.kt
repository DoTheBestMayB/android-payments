package nextstep.payments.ui.new_card

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import nextstep.payments.R

enum class CardType(
    val companyName: String,
    @param:DrawableRes val imageResource: Int,
    @param:ColorInt val color: Int,
) {
    NOT_SELECTED("", 0, 0xFF333333.toInt()),
    BC("BC카드", R.drawable.bc, 0xFFF04651.toInt()),
    SHINHAN("신한카드", R.drawable.shinhan, 0xFF0046FF.toInt()),
    KAKAO("카카오뱅크", R.drawable.kakao, 0xFFFFE600.toInt()),
    HYUNDAE("현대카드", R.drawable.hyu, 0xFF000000.toInt()),
    WOORI("우리카드", R.drawable.woori, 0xFF007BC8.toInt()),
    LOTTE("롯데카드", R.drawable.lotte, 0xFFED1C24.toInt()),
    HANA("하나카드", R.drawable.hana, 0xFF009490.toInt()),
    KB("국민카드", R.drawable.kb, 0xFFFFBC00.toInt())
}
