@file:OptIn(ExperimentalMaterial3Api::class)

package nextstep.payments.ui.card_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nextstep.payments.R
import nextstep.payments.ui.common.model.CreditCard
import nextstep.payments.ui.components.PaymentCard
import nextstep.payments.ui.components.PaymentCardAdd
import nextstep.payments.ui.theme.PaymentsTheme

@Composable
fun CardListScreenRoot(
    viewModel: CardListViewModel,
    navigateToNewCard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    CardListScreen(
        state = state.value,
        navigateToNewCard = navigateToNewCard,
        modifier = modifier,
    )
}

@Composable
fun CardListScreen(
    state: CardListState,
    navigateToNewCard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.card_list_title))
                },
                actions = {
                    if (state.cards is CreditCardUiState.Many) {
                        Text(
                            text = stringResource(R.string.card_list_add_new_card_top_bar),
                            fontWeight = FontWeight.W700,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable {
                                    navigateToNewCard()
                                },
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            /**
             * 각 상황에 따른 UI를 미리 분리해 두는 것이 요구사항이 변경되었을 때 더 대응하기 쉽다고 생각해 when 절을 사용했습니다.
             * Compound Component 패턴을 적용해야 할지 고민해봤는데, 적용해야 하는 이유와 어떻게 적용할지를 떠올리지 못했습니다.
             * https://wisemuji.medium.com/jetpack-compose-ui-%EC%A1%B0%ED%95%A9-composition-%ED%95%98%EA%B8%B0-%EC%8B%AC%ED%99%94-33910e8f09df
             */
            when (val cards = state.cards) {
                CreditCardUiState.Empty -> {
                    Text(
                        text = stringResource(R.string.card_list_add_new_card),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(vertical = 32.dp)
                    )
                    PaymentCardAdd(
                        modifier = Modifier.clickable {
                            navigateToNewCard()
                        }
                    )
                }

                is CreditCardUiState.Many -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                    ) {
                        items(
                            items = cards.creditCards,
                            key = {
                                it.cardNumber
                            }
                        ) {
                            PaymentCard(
                                cardInfo = it,
                            )
                        }
                    }
                }

                is CreditCardUiState.One -> {
                    PaymentCard(
                        cardInfo = cards.creditCard
                    )
                    Spacer(modifier = Modifier.padding(bottom = 32.dp))
                    PaymentCardAdd(
                        modifier = Modifier.clickable {
                            navigateToNewCard()
                        }
                    )
                }
            }

        }
    }
}

public class CardListScreenPreviewParameterProvider : PreviewParameterProvider<CardListState> {
    override val values: Sequence<CardListState>
        get() = sequenceOf(
            CardListState(
                cards = CreditCardUiState.Empty,
            ),
            CardListState(
                cards = CreditCardUiState.One(
                    creditCard = CreditCard(
                        cardNumber = "1111111111111111",
                        expiredDate = "0000",
                        ownerName = "홍길동",
                        password = "0000",
                    )
                )
            ),
            CardListState(
                cards = CreditCardUiState.Many(
                    creditCards = listOf(
                        CreditCard(
                            cardNumber = "4111-1111-1111-1111",
                            expiredDate = "12/27",
                            ownerName = "Kim Minsoo",
                            password = "1234"
                        ),
                        CreditCard(
                            cardNumber = "5500-0000-0000-0004",
                            expiredDate = "07/26",
                            ownerName = "Lee Hana",
                            password = "5678"
                        ),
                        CreditCard(
                            cardNumber = "3400-000000-00009",
                            expiredDate = "03/28",
                            ownerName = "Park Jiwon",
                            password = "9876"
                        )
                    )
                )
            )
        )
}

@Preview
@Composable
private fun CardListScreenPreview(
    @PreviewParameter(provider = CardListScreenPreviewParameterProvider::class) state: CardListState,
) {
    PaymentsTheme {
        CardListScreen(
            state = state,
            navigateToNewCard = {},
        )
    }
}
