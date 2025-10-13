package nextstep.payments.ui.new_card

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nextstep.payments.R
import nextstep.payments.ui.theme.PaymentsTheme

@Composable
fun CardSelectItem(
    companyImage: Painter,
    companyName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(onClick = onClick),
    ) {
        Image(
            painter = companyImage,
            contentDescription = companyName,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(37.dp)
                .clip(CircleShape),
        )
        Spacer(modifier = Modifier.height(9.dp))
        Text(
            text = companyName,
        )
    }
}

@Preview
@Composable
private fun CardSelectItemPreview() {
    PaymentsTheme {
        CardSelectItem(
            companyImage = painterResource(id = R.drawable.hyu),
            companyName = "BC카드",
            onClick = {},
        )
    }
}
