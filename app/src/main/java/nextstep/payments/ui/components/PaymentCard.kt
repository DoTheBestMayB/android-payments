package nextstep.payments.ui.components

import android.R.attr.end
import android.R.attr.top
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import nextstep.payments.ui.common.model.CreditCard
import nextstep.payments.ui.theme.PaymentsTheme

/**
 * 카드 chip 하단 8dp에 카드 번호를 추가해야 한다는 요구 조건을 보고 ConstraintLayout을 떠올렸습니다.
 * ConstraintLayout 없이 Box, Row, Column 만을 이용해서 구현하는 방법을 AI에게 물어봤으나, 하드코딩된 padding 방법만 알려줬습니다.
 * Custom Layout으로 구현해보려고 관련 강의를 봤는데, ConstraintLayout보다 더 복잡해지는 방법이란 생각이 들었습니다.
 * 그래서 ConstraintLayout을 이용해 구현했습니다.
 */
@Composable
fun PaymentCard(
    modifier: Modifier = Modifier,
    cardInfo: CreditCard = CreditCard(),
) {
    ConstraintLayout(
        modifier = modifier
            .shadow(8.dp)
            .size(width = 208.dp, height = 124.dp)
            .background(
                color = Color(0xFF333333),
                shape = RoundedCornerShape(5.dp),
            )
    ) {
        val (chip, cardNumber, expiredDate, ownerName) = createRefs()

        Box(
            modifier = Modifier
                .constrainAs(chip) {
                    start.linkTo(parent.start, margin = 14.dp)
                    centerVerticallyTo(parent, 0.45f)
                }
                .size(width = 40.dp, height = 26.dp)
                .background(
                    color = Color(0xFFCBBA64),
                    shape = RoundedCornerShape(4.dp),
                )
        )

        Text(
            text = cardInfo.cardNumber.maskCardNumber(),
            modifier = Modifier
                .constrainAs(cardNumber) {
                    start.linkTo(parent.start, 14.dp)
                    end.linkTo(parent.end, 14.dp)
                    top.linkTo(chip.bottom, margin = 8.dp)
                    width = Dimension.fillToConstraints // Composable width가 제약에 맞게 늘어나도록 설정
                },
            color = Color.White,
            fontWeight = FontWeight.W500,
            maxLines = 1,
            autoSize = TextAutoSize.StepBased() // Material3 version 1.4에 추가된 autoSize 사용. ref: https://stackoverflow.com/a/79704166/11722881
        )
    }
}

private fun String.maskCardNumber(): String {
    val groups = chunked(4)

    return groups.mapIndexed { index, group ->
        if (index < 2) {
            group
        } else {
            "*".repeat(group.length)
        }
    }.joinToString(" - ")
}

@Preview
@Composable
private fun PaymentCardPreview() {
    PaymentsTheme {
        PaymentCard(
            cardInfo = CreditCard(
                cardNumber = "1111111111111111",
                expiredDate = "1111",
                ownerName = "홍길동",
                password = "1234",
            )
        )
    }
}
