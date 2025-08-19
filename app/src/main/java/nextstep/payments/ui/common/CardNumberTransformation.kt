package nextstep.payments.ui.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CardNumberTransformation(
    private val groupSize: Int = 4,
    private val sep: Char = '-',
) : VisualTransformation {

    /**
     * ViewModel에서 digit만 filtering 했으나, 여기서 한 번 더 필터링 했습니다.
     */
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text.filter { it.isDigit() }
        val out = buildString {
            raw.forEachIndexed { i, c ->
                if (i != 0 && i % groupSize == 0) {
                    append(sep)
                }
                append(c)
            }
        }
        // OffsetMapping : cursor 위치를 계산하는 클래스
        val offset = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val sepCount = offset / groupSize
                return (offset + sepCount).coerceAtMost(out.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val sepCount = offset / (groupSize + 1)
                return (offset - sepCount).coerceIn(0, raw.length)
            }
        }

        return TransformedText(AnnotatedString(out), offset)
    }
}
