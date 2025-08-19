package nextstep.payments.ui.new_card

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import nextstep.payments.R
import nextstep.payments.common.ObserveAsEvents
import nextstep.payments.ui.common.CardExpiryTransformation
import nextstep.payments.ui.common.CardNumberTransformation
import nextstep.payments.ui.components.PaymentCard
import nextstep.payments.ui.theme.PaymentsTheme

@Composable
fun NewCardScreenRoot(
    navigateToCardList: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewCardViewModel = viewModel(),
) {
    val state by viewModel.cardState.collectAsStateWithLifecycle()

    // Q. ObserveAsEvents block은 composable block이 아니기 때문에, 여기서 context와 문구를 선언한 후 사용했습니다.
    // 그런데 이렇게 작성하면 사용되는 장소와 사용하는 장소가 분리되어 적절하지 않다고 생각됩니다.
    // 어떻게 수정하는 것이 좋을지 궁금합니다.
    val context = LocalContext.current
    val cardAddFailMessage = stringResource(R.string.card_list_add_new_card_fail)

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            NewCardEvent.CardAddSuccess -> {
                navigateToCardList()
            }
            NewCardEvent.CardAddFail -> {
                Toast.makeText(context, cardAddFailMessage, Toast.LENGTH_SHORT).show()
            }
            NewCardEvent.NavigateBack -> {
                navigateToCardList()
            }
        }
    }

    val isAddEnabled by remember(state) {
        derivedStateOf {
            CardInputValidator.isCardNumberValid(state.cardNumber) &&
                    CardInputValidator.isExpiredDateValid(state.expiredDate) &&
                    CardInputValidator.isCardOwnerNameValid(state.ownerName) &&
                    CardInputValidator.isPasswordValid(state.password)
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
fun NewCardScreen(
    state: NewCardState,
    isAddEnabled: Boolean,
    onAction: (NewCardAction) -> Unit,
    modifier: Modifier = Modifier,
) {
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
        modifier = modifier
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            PaymentCard()

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = state.cardNumber,
                onValueChange = {
                    onAction(NewCardAction.OnCartNumberChange(it))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
                visualTransformation = CardNumberTransformation(),
                label = { Text(stringResource(R.string.card_number_label)) },
                placeholder = { Text(stringResource(R.string.card_number_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = state.expiredDate,
                onValueChange = {
                    onAction(NewCardAction.OnExpiredDateChange(it))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
                visualTransformation = CardExpiryTransformation(),
                label = { Text(stringResource(R.string.expired_date_label)) },
                placeholder = { Text(stringResource(R.string.expired_date_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = state.ownerName,
                onValueChange = {
                    onAction(NewCardAction.OnOwnerNameChange(it))
                },
                label = { Text(stringResource(R.string.owner_name_label)) },
                placeholder = { Text(stringResource(R.string.owner_name_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = {
                    onAction(NewCardAction.OnPasswordChange(it))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
                label = { Text(stringResource(R.string.password_label)) },
                placeholder = { Text(stringResource(R.string.password_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
            )
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
            ),
            isAddEnabled = true,
            onAction = { },
        )
    }
}
