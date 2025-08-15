package nextstep.payments.ui.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * 카드 만료일 입력("MMYY")을 "MM/YY" 포맷으로 변경해줍니다.
 * filter에 입력하는 text가 카드 만료일 포맷에 맞는지 여부는 ViewModel에서 처리합니다.
 */
class CardExpiryTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text.filter { it.isDigit() }
        val out = if (raw.length <= 2) {
            raw
        } else {
            raw.substring(0, 2) + "/" + raw.substring(2)
        }

        val offset = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return if (raw.length <= 2 || offset <= 2) {
                    offset
                } else {
                    offset + 1
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return if (raw.length <= 2) {
                    offset.coerceAtMost(raw.length)
                } else if (offset <= 2) {
                    offset
                } else {
                    (offset - 1).coerceAtMost(raw.length)
                }
            }
        }

        return TransformedText(AnnotatedString(out), offset)
    }
}