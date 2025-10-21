package nextstep.payments.ui.card_list

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import nextstep.payments.ui.common.model.CreditCard
import org.junit.Rule
import org.junit.Test

class CardListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `등록된_카드가_없으면_카드_등록_안내_문구를_보여준다`() {
        composeTestRule.setContent {
            CardListScreen(
                state = CardListState(
                    cards = CreditCardUiState.Empty
                ),
                navigateToNewCard = {},
                navigateToEditCard = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("새로운 카드 등록 안내 문구")
            .assertExists()
    }

    @Test
    fun `등록된_카드가_1개_있으면_카드_등록_안내_문구를_보여주지_않는다`() {
        composeTestRule.setContent {
            CardListScreen(
                state = CardListState(
                    cards = CreditCardUiState.One(
                        creditCard = CreditCard(
                            cardNumber = "1111222233334444",
                            expiredDate = "1212",
                            ownerName = "홍길동",
                        )
                    )
                ),
                navigateToNewCard = {},
                navigateToEditCard = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("새로운 카드 등록 안내 문구")
            .assertDoesNotExist()
    }

    @Test
    fun `등록된_카드가_2개_이상_있으면_카드_추가_버튼을_Top_app_bar_에_둔다`() {
        composeTestRule.setContent {
            CardListScreen(
                state = CardListState(
                    cards = CreditCardUiState.Many(
                        creditCards = listOf(
                            CreditCard(
                                cardNumber = "1111222233334444",
                                expiredDate = "1212",
                                ownerName = "홍길동",
                            ),
                            CreditCard(
                                cardNumber = "1111222233334445",
                                expiredDate = "1212",
                                ownerName = "홍길동",
                            )
                        )
                    )
                ),
                navigateToNewCard = {},
                navigateToEditCard = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("TopAppBar 카드 추가 버튼")
            .assertExists()
    }
}
