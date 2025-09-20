package nextstep.payments.ui.new_card

import androidx.annotation.DrawableRes
import nextstep.payments.R

enum class CardType(
    val companyName: String,
    @get:DrawableRes val imageResource: Int,
) {
    NOT_SELECTED("", 0), BC("BC카드", R.drawable.bc),
    SHINHAN("신한카드", R.drawable.shinhan),
    KAKAO("카카오뱅크", R.drawable.kakao), HYUNDAE("현대카드", R.drawable.hyu),
    WOORI("우리카드", R.drawable.woori), LOTTE("롯데카드", R.drawable.lotte),
    HANA("하나카드", R.drawable.hana), KB("국민카드", R.drawable.kb)
}
