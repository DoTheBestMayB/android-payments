@file:OptIn(ExperimentalTestApi::class)

package nextstep.payments.ui.new_card

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextInputSelection
import androidx.compose.ui.test.pressKey
import androidx.compose.ui.text.TextRange
import org.junit.Rule
import org.junit.Test

class NewCardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun `카드_번호_포맷팅`() {
        composeTestRule.setContent {
            NewCardScreenRoot(
                navigateToCardList = {},
            )
        }

        val node = composeTestRule
            .onNodeWithContentDescription("카드 번호 입력", useUnmergedTree = true)

        node.performTextInput("1111222233334444")

        node.assertTextEquals("1111-2222-3333-4444")
    }

    @Test
    fun `만료일_포맷팅`() {
        composeTestRule.setContent {
            NewCardScreenRoot(
                navigateToCardList = {},
            )
        }

        val node = composeTestRule
            .onNodeWithContentDescription("만료일 입력", useUnmergedTree = true)

        node.performTextInput("1111")

        node.assertTextEquals("11/11")
    }

    @Test
    fun `카드_번호를_입력하지_않으면_카드_추가_버튼이_비활성화된다`() {
        composeTestRule.setContent {
            NewCardScreenRoot(
                navigateToCardList = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("만료일 입력")
            .performTextInput("1111")

        composeTestRule
            .onNodeWithContentDescription("카드 소유자 이름 입력")
            .performTextInput("홍길동")

        composeTestRule
            .onNodeWithContentDescription("카드 비밀번호 입력")
            .performTextInput("1111")

        composeTestRule
            .onNodeWithContentDescription("카드 추가 버튼")
            .assertIsNotEnabled()
    }

    @Test
    fun `카드_번호를_모두_입력하지_않으면_카드_추가_버튼이_비활성화된다`() {
        composeTestRule.setContent {
            NewCardScreenRoot(
                navigateToCardList = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("카드 번호 입력")
            .performTextInput("111122223333444")

        composeTestRule
            .onNodeWithContentDescription("만료일 입력")
            .performTextInput("1111")

        composeTestRule
            .onNodeWithContentDescription("카드 소유자 이름 입력")
            .performTextInput("홍길동")

        composeTestRule
            .onNodeWithContentDescription("카드 비밀번호 입력")
            .performTextInput("1111")

        composeTestRule
            .onNodeWithContentDescription("카드 추가 버튼")
            .assertIsNotEnabled()
    }

    @Test
    fun `만료일을_입력하지_않으면_카드_추가_버튼이_비활성화된다`() {
        composeTestRule.setContent {
            NewCardScreenRoot(
                navigateToCardList = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("카드 번호 입력")
            .performTextInput("1111222233334444")

        composeTestRule
            .onNodeWithContentDescription("카드 소유자 이름 입력")
            .performTextInput("홍길동")

        composeTestRule
            .onNodeWithContentDescription("카드 비밀번호 입력")
            .performTextInput("1111")

        composeTestRule
            .onNodeWithContentDescription("카드 추가 버튼")
            .assertIsNotEnabled()
    }

    @Test
    fun `만료일을_모두_입력하지_않으면_카드_추가_버튼이_비활성화된다`() {
        composeTestRule.setContent {
            NewCardScreenRoot(
                navigateToCardList = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("카드 번호 입력")
            .performTextInput("1111222233334444")

        composeTestRule
            .onNodeWithContentDescription("만료일 입력")
            .performTextInput("111")

        composeTestRule
            .onNodeWithContentDescription("카드 소유자 이름 입력")
            .performTextInput("홍길동")

        composeTestRule
            .onNodeWithContentDescription("카드 비밀번호 입력")
            .performTextInput("1111")

        composeTestRule
            .onNodeWithContentDescription("카드 추가 버튼")
            .assertIsNotEnabled()
    }

    @Test
    fun `만료일을_잘못_입력하면_카드_추가_버튼이_비활성화된다`() {
        composeTestRule.setContent {
            NewCardScreenRoot(
                navigateToCardList = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("카드 번호 입력")
            .performTextInput("1111222233334444")

        composeTestRule
            .onNodeWithContentDescription("만료일 입력")
            .performTextInput("1777")

        composeTestRule
            .onNodeWithContentDescription("카드 소유자 이름 입력")
            .performTextInput("홍길동")

        composeTestRule
            .onNodeWithContentDescription("카드 비밀번호 입력")
            .performTextInput("1111")

        composeTestRule
            .onNodeWithContentDescription("카드 추가 버튼")
            .assertIsNotEnabled()
    }

    @Test
    fun `카드_소유자_이름을_입력하지_않아도_카드_추가_버튼이_활성화된다`() {
        composeTestRule.setContent {
            NewCardScreenRoot(
                navigateToCardList = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("카드 번호 입력")
            .performTextInput("1111222233334444")

        composeTestRule
            .onNodeWithContentDescription("만료일 입력")
            .performTextInput("1212")

        composeTestRule
            .onNodeWithContentDescription("카드 비밀번호 입력")
            .performTextInput("1111")

        composeTestRule
            .onNodeWithContentDescription("카드 추가 버튼")
            .assertIsEnabled()
    }

    @Test
    fun `카드_비밀번호를_입력하지_않으면_카드_추가_버튼이_비활성화된다`() {
        composeTestRule.setContent {
            NewCardScreenRoot(
                navigateToCardList = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("카드 번호 입력")
            .performTextInput("1111222233334444")

        composeTestRule
            .onNodeWithContentDescription("만료일 입력")
            .performTextInput("1212")

        composeTestRule
            .onNodeWithContentDescription("카드 소유자 이름 입력")
            .performTextInput("홍길동")

        composeTestRule
            .onNodeWithContentDescription("카드 추가 버튼")
            .assertIsNotEnabled()
    }

    @Test
    fun `카드_비밀번호를_모두_입력하지_않으면_카드_추가_버튼이_비활성화된다`() {
        composeTestRule.setContent {
            NewCardScreenRoot(
                navigateToCardList = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("카드 번호 입력")
            .performTextInput("1111222233334444")

        composeTestRule
            .onNodeWithContentDescription("만료일 입력")
            .performTextInput("1212")

        composeTestRule
            .onNodeWithContentDescription("카드 소유자 이름 입력")
            .performTextInput("홍길동")

        composeTestRule
            .onNodeWithContentDescription("카드 비밀번호 입력")
            .performTextInput("111")

        composeTestRule
            .onNodeWithContentDescription("카드 추가 버튼")
            .assertIsNotEnabled()
    }

    @Test
    fun `카드_만료일을_포맷에_맞게_입력하지_않으면_입력이_안된다`() {
        composeTestRule.setContent {
            NewCardScreenRoot(
                navigateToCardList = {},
            )
        }

        // useUnmergedTree를 true로 설정하지 않으면 `assertTextEquals`로 값을 검증할 수 없음
        val node = composeTestRule
            .onNodeWithContentDescription(label = "만료일 입력", useUnmergedTree = true)

        // ViewModel에서 입력된 값이 포맷에 맞는지 검증하고, 일치하지 않으면 반영하지 않고 있습니다.
        // 1999를 입력하면 포맷에 맞지 않아 1 또한 입력되지 않기 때문에, 실제로 입력하는 것처럼 개별적으로 입력해줬습니다.
        node.performClick()
        node.performTextInput("1")
        node.performTextInput("9")
        node.performTextInput("9")
        node.performTextInput("9")

        node.assertTextEquals("1")
    }

    @Test
    fun `카드_만료일에서_값을_수정할때_포맷에_맞지_않으면_무시한다`() {
        composeTestRule.setContent {
            NewCardScreenRoot(
                navigateToCardList = {},
            )
        }

        val node = composeTestRule
            .onNodeWithContentDescription(label = "만료일 입력", useUnmergedTree = true)

        node.performClick()
        node.performTextInput("1")
        node.performTextInput("1")
        node.performTextInput("7")
        node.performTextInput("9")

        // 커서를 7 앞으로 옮깁니다.
        node.performTextInputSelection(TextRange(2))

        // https://stackoverflow.com/a/78985901/11722881
        node.performKeyInput {
            pressKey(Key.Backspace)
        }

        node.assertTextEquals("11/79")
    }

    @Test
    fun `카드_만료일에서_값을_수정할때_포맷에_맞으면_반영한다`() {
        composeTestRule.setContent {
            NewCardScreenRoot(
                navigateToCardList = {},
            )
        }

        val node = composeTestRule
            .onNodeWithContentDescription(label = "만료일 입력", useUnmergedTree = true)

        node.performClick()
        node.performTextInput("1")
        node.performTextInput("1")
        node.performTextInput("2")
        node.performTextInput("9")

        // 커서를 2 앞으로 옮깁니다.
        node.performTextInputSelection(TextRange(2))

        // https://stackoverflow.com/a/78985901/11722881
        node.performKeyInput {
            pressKey(Key.Backspace)
        }

        node.assertTextEquals("12/9")
    }

    @Test
    fun `모두_포맷에_맞게_입력하면_카드_추가_버튼이_활성화된다`() {
        composeTestRule.setContent {
            NewCardScreenRoot(
                navigateToCardList = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("카드 번호 입력")
            .performTextInput("1111222233334444")

        composeTestRule
            .onNodeWithContentDescription("만료일 입력")
            .performTextInput("1212")

        composeTestRule
            .onNodeWithContentDescription("카드 소유자 이름 입력")
            .performTextInput("홍길동")

        composeTestRule
            .onNodeWithContentDescription("카드 비밀번호 입력")
            .performTextInput("1111")

        composeTestRule
            .onNodeWithContentDescription("카드 추가 버튼")
            .assertIsEnabled()
    }

}
