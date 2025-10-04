@file:OptIn(ExperimentalMaterial3Api::class)

package nextstep.payments.ui.new_card

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import nextstep.payments.R
import nextstep.payments.common.ObserveAsEvents
import nextstep.payments.ui.common.CardExpiryTransformation
import nextstep.payments.ui.common.CardNumberTransformation
import nextstep.payments.ui.common.model.CreditCard
import nextstep.payments.ui.components.PaymentCard
import nextstep.payments.ui.theme.PaymentsTheme

@Composable
fun NewCardScreenRoot(
    navigateToCardList: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewCardViewModel = viewModel(),
) {
    val state by viewModel.cardState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            NewCardEvent.CardAddSuccess -> {
                navigateToCardList()
            }

            NewCardEvent.CardAddFail -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.card_list_add_new_card_fail),
                    Toast.LENGTH_SHORT
                ).show()
            }

            NewCardEvent.NavigateBack -> {
                navigateToCardList()
            }
        }
    }

    val isAddEnabled by remember {
        derivedStateOf {
            state.isValid(CardInputValidator)
        }
    }

    NewCardScreen(
        state = state,
        isAddEnabled = isAddEnabled,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@Composable
internal fun NewCardScreen(
    state: NewCardState,
    isAddEnabled: Boolean,
    onAction: (NewCardAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardNumberTransformation = remember {
        CardNumberTransformation()
    }
    val cardExpiryTransformation = remember {
        CardExpiryTransformation()
    }

    Scaffold(
        topBar = {
            NewCardTopBar(
                onBackClick = {
                    onAction(NewCardAction.OnBackClick)
                },
                onSaveClick = {
                    onAction(NewCardAction.OnAddCardClick)
                },
                isAddEnabled = isAddEnabled,
            )
        },
        containerColor = Color.White,
        modifier = modifier
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            PaymentCard(
                cardInfo = CreditCard(
                    company = state.cardType
                ),
                modifier = Modifier
                    .clickable {
                        onAction(NewCardAction.OnPreviewCardSelect)
                    }
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = state.cardNumber,
                onValueChange = {
                    onAction(NewCardAction.OnCartNumberChange(it))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                visualTransformation = cardNumberTransformation,
                label = { Text(stringResource(R.string.card_number_label)) },
                placeholder = { Text(stringResource(R.string.card_number_placeholder)) },
                modifier = Modifier
                    .semantics {
                        contentDescription = "카드 번호 입력"
                    }
                    .fillMaxWidth(),

                )

            OutlinedTextField(
                value = state.expiredDate,
                onValueChange = {
                    onAction(NewCardAction.OnExpiredDateChange(it))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                visualTransformation = cardExpiryTransformation,
                label = { Text(stringResource(R.string.expired_date_label)) },
                placeholder = { Text(stringResource(R.string.expired_date_placeholder)) },
                modifier = Modifier
                    .semantics {
                        contentDescription = "만료일 입력"
                    }
                    .fillMaxWidth(),
            )

            OutlinedTextField(
                value = state.ownerName,
                onValueChange = {
                    onAction(NewCardAction.OnOwnerNameChange(it))
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                label = { Text(stringResource(R.string.owner_name_label)) },
                placeholder = { Text(stringResource(R.string.owner_name_placeholder)) },
                modifier = Modifier
                    .semantics {
                        contentDescription = "카드 소유자 이름 입력"
                    }
                    .fillMaxWidth(),
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = {
                    onAction(NewCardAction.OnPasswordChange(it))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                label = { Text(stringResource(R.string.password_label)) },
                placeholder = { Text(stringResource(R.string.password_placeholder)) },
                modifier = Modifier
                    .semantics {
                        contentDescription = "카드 비밀번호 입력"
                    }
                    .fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
            )
        }
    }

    if (state.showBottomSheet) {
        CardSelectSheet(
            onCardSelect = {
                onAction(NewCardAction.OnBottomSheetCardSelect(it))
            }
        )
    }
}

@Composable
private fun CardSelectSheet(
    onCardSelect: (CardType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { },
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        dragHandle = null,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,
            shouldDismissOnClickOutside = false,
        ),
        containerColor = Color.White,
        modifier = modifier,
    ) {
        FlowRow(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 4,
        ) {
            for (card in CardType.entries) {
                if (card == CardType.NOT_SELECTED) {
                    continue
                }
                CardSelectItem(
                    companyImage = painterResource(id = card.imageResource),
                    companyName = card.companyName,
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            onCardSelect(card)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun NewCardScreenPreview() {
    PaymentsTheme {
        NewCardScreen(
            state = NewCardState(
                cardNumber = "1111111111111111",
                expiredDate = "0000",
                ownerName = "홍길동",
                password = "0000",
                showBottomSheet = false,
            ),
            isAddEnabled = true,
            onAction = { },
        )
    }
}
