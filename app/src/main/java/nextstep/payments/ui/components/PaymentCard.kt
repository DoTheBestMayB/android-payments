package nextstep.payments.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import nextstep.payments.ui.common.model.CreditCard
import nextstep.payments.ui.new_card.CardType
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
                color = Color(cardInfo.company.color),
                shape = RoundedCornerShape(5.dp),
            )
    ) {
        val (chip, cardNumber, expiredDate, ownerName, companyName) = createRefs()

        Box(
            modifier = Modifier
                .constrainAs(chip) {
                    centerHorizontallyTo(parent, 0.1197f)
                    centerVerticallyTo(parent, 0.3562f)
                }
                .size(width = 40.dp, height = 26.dp)
                .background(
                    color = Color(0xFFCBBA64),
                    shape = RoundedCornerShape(4.dp),
                )
        )

        Text(
            text = cardInfo.company.companyName,
            color = Color.White,
            modifier = Modifier
                .constrainAs(companyName) {
                    start.linkTo(chip.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(chip.top)
                },
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
        )

        Text(
            text = cardInfo.maskedCardNumber(2),
            modifier = Modifier
                .constrainAs(cardNumber) {
                    start.linkTo(chip.start)
                    end.linkTo(parent.end, 14.dp)
                    top.linkTo(chip.bottom, margin = 8.dp)

                    width = Dimension.fillToConstraints // Composable width가 제약에 맞게 늘어나도록 설정
                },
            color = Color.White,
            fontWeight = FontWeight.W500,
            maxLines = 1,
            autoSize = TextAutoSize.StepBased() // Material3 version 1.4에 추가된 autoSize 사용. ref: https://stackoverflow.com/a/79704166/11722881
        )
        Text(
            text = cardInfo.ownerName,
            modifier = Modifier
                .constrainAs(ownerName) {
                    start.linkTo(chip.start)
                    end.linkTo(expiredDate.start)
                    top.linkTo(cardNumber.bottom)
                    bottom.linkTo(
                        parent.bottom,
                        8.dp
                    ) // 이렇게 설정할 경우, CardNumber Text Composable과 일부가 겹치지만, 대안이 떠오르지 않네요.

                    width = Dimension.fillToConstraints
                },
            color = Color.White,
            fontWeight = FontWeight.W500,
            autoSize = TextAutoSize.StepBased(maxFontSize = 12.sp),
            maxLines = 1,
            textAlign = TextAlign.Start,
        )

        Text(
            text = cardInfo.formatExpiredDate(),
            modifier = Modifier
                .constrainAs(expiredDate) {
                    start.linkTo(ownerName.end)
                    end.linkTo(parent.end, margin = 14.dp)
                    top.linkTo(ownerName.top)
                    bottom.linkTo(parent.bottom, 8.dp)

                    width = Dimension.wrapContent
                    height = Dimension.fillToConstraints
                },
            color = Color.White,
            fontWeight = FontWeight.W500,
            maxLines = 1,
            autoSize = TextAutoSize.StepBased(maxFontSize = 12.sp),
            textAlign = TextAlign.End,
        )
    }
}

@Preview
@Composable
private fun PaymentCardPreview() {
    PaymentsTheme {
        PaymentCard(
            cardInfo = CreditCard(
                cardNumber = "1111111111111111",
                expiredDate = "1111",
                ownerName = "Elon Reeve Musk Long Long Long",
                company = CardType.BC
            )
        )
    }
}
