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
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
internal fun CardListScreen(
    state: CardListState,
    navigateToNewCard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.White,
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
                                .semantics {
                                    contentDescription = "TopAppBar 카드 추가 버튼"
                                }
                                .clickable {
                                    navigateToNewCard()
                                },
                        )
                    }
                },
                colors = topAppBarColors(
                    containerColor = Color.White,
                )
            )
        }
    ) { innerPadding ->
        val paymentCardAdd = remember {
            movableContentOf {
                PaymentCardAdd(
                    modifier = Modifier.clickable {
                        navigateToNewCard()
                    }
                )
            }
        }

        when (val cards = state.cards) {
            CreditCardUiState.Empty -> {
                EmptyCardScreen(
                    modifier = Modifier.padding(innerPadding),
                    newCardAddContent = paymentCardAdd,
                )
            }

            is CreditCardUiState.Many -> {
                ManyCardScreen(
                    state = cards,
                    modifier = Modifier.padding(innerPadding),
                )
            }

            is CreditCardUiState.One -> {
                OneCardScreen(
                    state = cards,
                    newCardAddContent = paymentCardAdd,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}

@Composable
private fun EmptyCardScreen(
    modifier: Modifier = Modifier,
    newCardAddContent: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize(),
    ) {
        Text(
            text = stringResource(R.string.card_list_add_new_card),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .semantics {
                    contentDescription = "새로운 카드 등록 안내 문구"
                }
                .padding(vertical = 32.dp)
        )
        newCardAddContent()
    }
}

@Composable
private fun ManyCardScreen(
    state: CreditCardUiState.Many,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        items(
            items = state.creditCards,
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

@Composable
fun OneCardScreen(
    state: CreditCardUiState.One,
    newCardAddContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize(),
    ) {
        PaymentCard(
            cardInfo = state.creditCard
        )
        Spacer(modifier = Modifier.padding(bottom = 32.dp))
        newCardAddContent()
    }
}

private class CardListScreenPreviewParameterProvider : PreviewParameterProvider<CardListState> {
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
                        ),
                        CreditCard(
                            cardNumber = "5500-0000-0000-0004",
                            expiredDate = "07/26",
                            ownerName = "Lee Hana",
                        ),
                        CreditCard(
                            cardNumber = "3400-000000-00009",
                            expiredDate = "03/28",
                            ownerName = "Park Jiwon",
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
